package rcases.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import rcases.DBConnection;
import rcases.SchedulingApp;
import rcases.model.Customer;

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
    private ChoiceBox cityChoiceBox;

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
    
    public CustomerScreenController() {
    }
    
    @FXML
    void handleNewCustomer(ActionEvent event) {
        saveCancelButtonBar.setDisable(false);
        customerTable.setDisable(true);
        clearCustomerDetails();
        customerIdField.setText("Auto-Generated ID");
        newEditDeleteButtonBar.setDisable(true);
        
        
    }
    
    @FXML
    void handleEditCustomer(ActionEvent event) {
        saveCancelButtonBar.setDisable(false);
        customerTable.setDisable(true);
        newEditDeleteButtonBar.setDisable(true);
    }

    @FXML
    void handleDeleteCustomer(ActionEvent event) {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        if (customer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete " + customer.getCustomerName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                deleteCustomer(customer);
                mainApp.showCustomerScreen();
            } else {
                alert.close();
            }
        }

    }
   
    @FXML
    void handleSaveCustomer(ActionEvent event) {
        saveCancelButtonBar.setDisable(true);
        customerTable.setDisable(false);
        saveCustomer();
        mainApp.showCustomerScreen();

    }
    
    @FXML
    void handleCancelCustomer(ActionEvent event) {
        saveCancelButtonBar.setDisable(true);
        customerTable.setDisable(false);
        clearCustomerDetails();
        newEditDeleteButtonBar.setDisable(false);
    }
    
    public void setCustomerScreen(SchedulingApp mainApp) {
	this.mainApp = mainApp;
        
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        cityChoiceBox.getItems().addAll(populateCityChoiceBox());
        cityChoiceBox.valueProperty().set("Please Select:");
        cityChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showCountry(newValue.toString());});
        
        
        customerTable.getItems().setAll(populateCustomerList()); /* takes the list from the populateCustomerList() 
        method, and addes the rows to the TableView */         
        customerTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue)->showCustomerDetails(newValue));
        
    }
         
    
    @FXML /* port information over to the form */
    private void showCustomerDetails(Customer selectedCustomer) {
     
        customerIdField.setText(selectedCustomer.getCustomerId());
        nameField.setText(selectedCustomer.getCustomerName());
        addressField.setText(selectedCustomer.getAddress());
        address2Field.setText(selectedCustomer.getAddress2());
        cityChoiceBox.setValue(selectedCustomer.getCity());
        countryField.setText(selectedCustomer.getCountry());
        postalCodeField.setText(selectedCustomer.getPostalCode());
        phoneField.setText(selectedCustomer.getPhone());

    }
    
    //private void disableCustomerFields ()
    
    @FXML /* port information over to the form */
    private void clearCustomerDetails() {
     
        customerIdField.clear();
        nameField.clear();
        addressField.clear();
        address2Field.clear();
        cityChoiceBox.valueProperty().set("Please Select:");
        countryField.clear();
        postalCodeField.clear();
        phoneField.clear();

    }
    
    private List<Customer> populateCustomerList() {
      
        String tCustomerId;
        String tCustomerName;
        String tAddress;
        String tAddress2;
        String tCity;
        String tCountry;
        String tPostalCode;
        String tPhone;
        
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try(
            
            
        PreparedStatement statement = DBConnection.getConn().prepareStatement(
        "SELECT customer.customerId, customer.customerName, address.address, address.address2, address.postalCode, city.city, country.country, address.phone " +
        "FROM customer, address, city, country " +
        "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");
            ResultSet rs = statement.executeQuery();){
           
            
            while (rs.next()) {
                tCustomerId = rs.getString("customer.customerId");

                tCustomerName = rs.getString("customer.customerName");

                tAddress = rs.getString("address.address");
                
                tAddress2 = rs.getString("address.address2");
                
                tCity = rs.getString("city.city");
                
                tCountry = rs.getString("country.country");
                
                tPostalCode = rs.getString("address.postalCode");
                
                tPhone = rs.getString("address.phone");

                customerList.add(new Customer(tCustomerId, tCustomerName, tAddress, tAddress2, tCity.toString(), tCountry, tPostalCode, tPhone));

            }
          
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }

         
        return customerList;

    }

    private List<String> populateCityChoiceBox() {
        String defaultCity = "Please Select:";
        String tCity;
        ObservableList<String> cityList = FXCollections.observableArrayList();
        cityList.add(defaultCity);
        
        try(
            
            
        PreparedStatement statement = DBConnection.getConn().prepareStatement("SELECT city FROM city LIMIT 100;");
            ResultSet rs = statement.executeQuery();){
           
            
            while (rs.next()) {
                tCity = rs.getString("city.city");

                cityList.add(tCity);      //.add(new City(tCity));

            }
          
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }

        System.out.println(cityList); 
        return cityList;
    }
    
    
    @FXML
    private void showCountry(String citySelection) {
        if (citySelection.equals("London")) {
            countryField.setText("England");
        } else if (citySelection.equals("Phoenix") || citySelection.equals("New York")) {
            countryField.setText("United States");
        }
    }

    private void saveCustomer() {

            try {

                PreparedStatement ps = DBConnection.getConn().prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, addressField.getText());
                ps.setString(2, address2Field.getText());
                ps.setInt(3, 1);
                ps.setString(4, postalCodeField.getText());
                ps.setString(5, phoneField.getText());
                ps.setString(6, LocalDateTime.now().toString());
                ps.setString(7, "test");
                ps.setString(8, LocalDateTime.now().toString());
                ps.setString(9, "test");
                boolean res = ps.execute();
                int newAddressId = -1;
                ResultSet rs = ps.getGeneratedKeys();
                
                if(rs.next()){
                newAddressId = rs.getInt(1);
                System.out.println("Generated AddressId: "+ newAddressId);
                }
            
            
                PreparedStatement psc = DBConnection.getConn().prepareStatement("INSERT INTO customer "
                + "(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            
                psc.setString(1, nameField.getText());
                psc.setInt(2, newAddressId);
                psc.setInt(3, 1);
                psc.setString(4, LocalDateTime.now().toString());
                psc.setString(5, "test");
                psc.setString(6, LocalDateTime.now().toString());
                psc.setString(7, "test");
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

    private void deleteCustomer(Customer customer) {
        
        try{           
            PreparedStatement pst = DBConnection.getConn().prepareStatement("DELETE customer.*, address.* from customer, address WHERE customer.customerId = ? AND customer.addressId = address.addressId");
            pst.setString(1, customer.getCustomerId()); 
            pst.executeUpdate();   
                
        } catch(SQLException e){
            e.printStackTrace();
        }       
    }
}
