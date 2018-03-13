package rcases.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import rcases.util.DBConnection;
import rcases.SchedulingApp;
import rcases.model.City;
import rcases.model.Customer;
import rcases.model.User;

public class CustomerScreenController {

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;
    
    @FXML
    private TextField customerIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private ComboBox<City> cityComboBox;

    @FXML
    private TextField address2Field;

    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField countryField;
    
    @FXML
    private ButtonBar newEditDeleteButtonBar;
    
    @FXML
    private ButtonBar saveCancelButtonBar;
    
    private SchedulingApp mainApp;
    private boolean editClicked = false;
    private Stage dialogStage;
    private User currentUser;
    
    public CustomerScreenController() {
    }
    
    @FXML
    void handleNewCustomer(ActionEvent event) {
        editClicked = false;
        enableCustomerFields();
        saveCancelButtonBar.setDisable(false);
        customerTable.setDisable(true);
        clearCustomerDetails();
        customerIdField.setText("Auto-Generated");
        newEditDeleteButtonBar.setDisable(true);        
    }
    
    @FXML
    void handleEditCustomer(ActionEvent event) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer != null) {
            editClicked = true;
            enableCustomerFields();
            saveCancelButtonBar.setDisable(false);
            customerTable.setDisable(true);
            newEditDeleteButtonBar.setDisable(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer selected");
            alert.setContentText("Please select a Customer in the Table");
            alert.showAndWait();
        }
        
        
    }

    @FXML
    void handleDeleteCustomer(ActionEvent event) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete " + selectedCustomer.getCustomerName() + "?");
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> {
                deleteCustomer(selectedCustomer);
                mainApp.showCustomerScreen(currentUser);
                }
            );
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer selected for Deletion");
            alert.setContentText("Please select a Customer in the Table to delete");
            alert.showAndWait();
        }
        
    }
   
    @FXML
    void handleSaveCustomer(ActionEvent event) {
        if (validateCustomer()){
            saveCancelButtonBar.setDisable(true);
            customerTable.setDisable(false);
            if (editClicked == true) {
                //edits Customer record
                updateCustomer();
            } else if (editClicked == false){
                //inserts new customer record
                saveCustomer();
        }
        mainApp.showCustomerScreen(currentUser);
        } 
    }
    
    @FXML
    void handleCancelCustomer(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        alert.showAndWait()
        .filter(response -> response == ButtonType.OK)
        .ifPresent(response -> {
            saveCancelButtonBar.setDisable(true);
            customerTable.setDisable(false);
            clearCustomerDetails();
            newEditDeleteButtonBar.setDisable(false);
            editClicked = false;
            }
        );
    }
    
    /**
     * Initializes Customer Screen
     * @param mainApp
     * @param currentUser 
     */
    public void setCustomerScreen(SchedulingApp mainApp, User currentUser) {
	this.mainApp = mainApp;
        this.currentUser = currentUser;
        
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        disableCustomerFields();
        
        populateCityList();
        
        cityComboBox.setConverter(new StringConverter<City>() {

        @Override
        public String toString(City object) {
        return object.getCity();
        }     

        @Override
        public City fromString(String string) {
        return cityComboBox.getItems().stream().filter(ap -> 
            ap.getCity().equals(string)).findFirst().orElse(null);
        }
        });
        
        cityComboBox.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null)
                showCountry(newval.toString());
        });
        
        customerTable.getItems().setAll(populateCustomerList());          
        customerTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue)->showCustomerDetails(newValue));
        
    }
         
    /**
     * Sets fields from selectedCustomer
     * @param selectedCustomer 
     */
    @FXML
    private void showCustomerDetails(Customer selectedCustomer) {
     
        customerIdField.setText(selectedCustomer.getCustomerId());
        nameField.setText(selectedCustomer.getCustomerName());
        addressField.setText(selectedCustomer.getAddress());
        address2Field.setText(selectedCustomer.getAddress2());
        cityComboBox.setValue(selectedCustomer.getCity());
        countryField.setText(selectedCustomer.getCountry());
        postalCodeField.setText(selectedCustomer.getPostalCode());
        phoneField.setText(selectedCustomer.getPhone());

    }
    
    //disables editing to not allow entry prior to clicking New or Edit
    private void disableCustomerFields() {
        
        nameField.setEditable(false);
        addressField.setEditable(false);
        address2Field.setEditable(false);
        postalCodeField.setEditable(false);
        phoneField.setEditable(false);
    }
    
    //enables editing after New or Edit clicked
    private void enableCustomerFields() {
        
        nameField.setEditable(true);
        addressField.setEditable(true);
        address2Field.setEditable(true);
        postalCodeField.setEditable(true);
        phoneField.setEditable(true);
    }
    
    //clears details listed in fields
    @FXML 
    private void clearCustomerDetails() {
     
        customerIdField.clear();
        nameField.clear();
        addressField.clear();
        address2Field.clear();
        countryField.clear();
        postalCodeField.clear();
        phoneField.clear();

    }
    
    /**
     * Populates customerList for CustomerTable
     * @return customerList
     */
    protected List<Customer> populateCustomerList() {
      
        String tCustomerId;
        String tCustomerName;
        String tAddress;
        String tAddress2;
        City tCity;
        String tCountry;
        String tPostalCode;
        String tPhone;
        
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try(
            
            
        PreparedStatement statement = DBConnection.getConn().prepareStatement(
        "SELECT customer.customerId, customer.customerName, address.address, address.address2, address.postalCode, city.cityId, city.city, country.country, address.phone " +
        "FROM customer, address, city, country " +
        "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");
            ResultSet rs = statement.executeQuery();){
           
            
            while (rs.next()) {
                tCustomerId = rs.getString("customer.customerId");

                tCustomerName = rs.getString("customer.customerName");

                tAddress = rs.getString("address.address");
                
                tAddress2 = rs.getString("address.address2");
                
                tCity = new City(rs.getInt("city.cityId"), rs.getString("city.city"));
                
                tCountry = rs.getString("country.country");
                
                tPostalCode = rs.getString("address.postalCode");
                
                tPhone = rs.getString("address.phone");

                customerList.add(new Customer(tCustomerId, tCustomerName, tAddress, tAddress2, tCity, tCountry, tPostalCode, tPhone));

            }
          
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }

         
        return customerList;

    }
    
    /**
     * populates cities list and sets to cityComboBox
     */
    protected void populateCityList() {
    
    
    ObservableList<City> cities = FXCollections.observableArrayList();
    
    try(

    PreparedStatement statement = DBConnection.getConn().prepareStatement("SELECT cityId, city FROM city LIMIT 100;");
    ResultSet rs = statement.executeQuery();){
    
    while (rs.next()) {
        //tCityId = rs.getInt("city.cityId");     //.add(new City(tCity));
        //tCityName = rs.getString("city.city");
        cities.add(new City(rs.getInt("city.cityId"),rs.getString("city.city")));
    }
    
    
    } catch (SQLException sqe) {
    System.out.println("Check your SQL");
    sqe.printStackTrace();
    } catch (Exception e) {
    System.out.println("Something besides the SQL went wrong.");
    }
    
    //System.out.println(cities);
    cityComboBox.setItems(cities);
    
    }
    
    /**
     * Sets Country based on citySelection
     * Cities and Countries hard coded in to database
     * with no way for user to create their own
     * on the assumption that this business only has customers in
     * cities their offices are located
     * @param citySelection 
     */
    @FXML
    private void showCountry(String citySelection) {
        if (citySelection.equals("London")) {
            countryField.setText("England");
        } else if (citySelection.equals("Phoenix") || citySelection.equals("New York")) {
            countryField.setText("United States");
        }
    }
    
    /**
     * Inserts new customer record
     */
    private void saveCustomer() {

            try {

                PreparedStatement ps = DBConnection.getConn().prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                        + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)",Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, addressField.getText());
                ps.setString(2, address2Field.getText());
                ps.setInt(3, cityComboBox.getValue().getCityId());
                ps.setString(4, postalCodeField.getText());
                ps.setString(5, phoneField.getText());
                ps.setString(6, currentUser.getUsername());
                ps.setString(7, currentUser.getUsername());
                boolean res = ps.execute();
                int newAddressId = -1;
                ResultSet rs = ps.getGeneratedKeys();
                
                if(rs.next()){
                    newAddressId = rs.getInt(1);
                    //System.out.println("Generated AddressId: "+ newAddressId);
                }
            
            
                PreparedStatement psc = DBConnection.getConn().prepareStatement("INSERT INTO customer "
                + "(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");
            
                psc.setString(1, nameField.getText());
                psc.setInt(2, newAddressId);
                psc.setInt(3, 1);
                //psc.setString(4, LocalDateTime.now().toString());
                psc.setString(4, currentUser.getUsername());
                //psc.setString(6, LocalDateTime.now().toString());
                psc.setString(5, currentUser.getUsername());
                int result = psc.executeUpdate();
                if (result == 1) {//one row was affected; namely the one that was inserted!
                    System.out.println("YAY! Customer");
                } else {
                    System.out.println("BOO! Customer");
                }
            } catch (SQLException ex) {
            ex.printStackTrace();
            }
    }
    
    /**
     * Deletes selected customer from table
     * @param customer 
     */
    private void deleteCustomer(Customer customer) {
        
        try{           
            PreparedStatement pst = DBConnection.getConn().prepareStatement("DELETE customer.*, address.* from customer, address WHERE customer.customerId = ? AND customer.addressId = address.addressId");
            pst.setString(1, customer.getCustomerId()); 
            pst.executeUpdate();   
                
        } catch(SQLException e){
            e.printStackTrace();
        }       
    }

    /**
     * Updates Customer records
     */
    private void updateCustomer() {
        try {

                PreparedStatement ps = DBConnection.getConn().prepareStatement("UPDATE address, customer, city, country "
                        + "SET address = ?, address2 = ?, address.cityId = ?, postalCode = ?, phone = ?, address.lastUpdate = CURRENT_TIMESTAMP, address.lastUpdateBy = ? "
                        + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");

                ps.setString(1, addressField.getText());
                ps.setString(2, address2Field.getText());
                ps.setInt(3, cityComboBox.getValue().getCityId());
                ps.setString(4, postalCodeField.getText());
                ps.setString(5, phoneField.getText());
                ps.setString(6, currentUser.getUsername());
                ps.setString(7, customerIdField.getText());
                
                int result = ps.executeUpdate();
                if (result == 1) {//one row was affected; namely the one that was inserted!
                    System.out.println("YAY! Address Update");
                } else {
                    System.out.println("BOO! Address Update");
                }
                
            
            
                PreparedStatement psc = DBConnection.getConn().prepareStatement("UPDATE customer, address, city "
                + "SET customerName = ?, customer.lastUpdate = CURRENT_TIMESTAMP, customer.lastUpdateBy = ? "
                + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId");
            
                psc.setString(1, nameField.getText());
                psc.setString(2, currentUser.getUsername());
                psc.setString(3, customerIdField.getText());
                int results = psc.executeUpdate();
                if (results == 1) {//one row was affected; namely the one that was inserted!
                    System.out.println("YAY! Customer Update");
                } else {
                    System.out.println("BOO! Customer Update");
                }
            } catch (SQLException ex) {
            ex.printStackTrace();
            }
    }
    
    /**
     * Validates Customer details to ensure no non-existant or invalid data
     * @return true if no errors
     */
    private boolean validateCustomer() {
        String name = nameField.getText();
        String address = addressField.getText();
        City city = cityComboBox.getValue();
        String country = countryField.getText();
        String zip = postalCodeField.getText();
        String phone = phoneField.getText();
        
        String errorMessage = "";
        //first checks to see if inputs are null
        if (name == null || name.length() == 0) {
            errorMessage += "Please enter the Customer's name.\n"; 
        }
        if (address == null || address.length() == 0) {
            errorMessage += "Please enter an address.\n";  
        } 
        if (city == null) {
            errorMessage += "Please Select a City.\n"; 
        } 
        if (country == null || country.length() == 0) {
            errorMessage += "No valid Country. Country set by City.\n"; 
        }         
        if (zip == null || zip.length() == 0) {
            errorMessage += "Please enter the Postal Code.\n"; 
        } else if (zip.length() > 10 || zip.length() < 5){
            errorMessage += "Please enter a valid Postal Code.\n";
        }
        if (phone == null || phone.length() == 0) {
            errorMessage += "Please enter a Phone Number (including Area Code)."; 
        } else if (phone.length() < 10 || phone.length() > 15 ){
            errorMessage += "Please enter a valid phone number (including Area Code).\n";
        }        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid Customer fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

}
