package rcases.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import rcases.DBConnection;
import rcases.SchedulingApp;
import rcases.model.Customer;
import rcases.model.City;

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
    private ButtonBar saveCancelButtonBar;
    
    private SchedulingApp mainApp;
    
    public CustomerScreenController() {
    }
    
    @FXML
    void handleNewCustomer(ActionEvent event) {
        saveCancelButtonBar.setDisable(false);
        customerTable.setDisable(true);
        clearCustomerDetails();
        
        
    }
    
    @FXML
    void handleEditCustomer(ActionEvent event) {
        saveCancelButtonBar.setDisable(false);
        customerTable.setDisable(true);
    }

    @FXML
    void handleDeleteCustomer(ActionEvent event) {

    }
   
    @FXML
    void handleSaveCustomer(ActionEvent event) {
        saveCancelButtonBar.setDisable(true);
        customerTable.setDisable(false);

    }
    
    @FXML
    void handleCancelCustomer(ActionEvent event) {
        saveCancelButtonBar.setDisable(true);
        customerTable.setDisable(false);
        clearCustomerDetails();
    }
    
    public void setCustomerScreen(SchedulingApp mainApp) {
	this.mainApp = mainApp;
        
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        cityChoiceBox.getItems().addAll(populateCityChoiceBox());
        cityChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showCountry(newValue.toString());
        cityChoiceBox.valueProperty().set("Please Select:");
});
        customerTable.getItems().setAll(populateCustomerList()); /* takes the list from the parseCustomerList() 
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

}
