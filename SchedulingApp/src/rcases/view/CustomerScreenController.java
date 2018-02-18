package rcases.view;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import rcases.DBConnection;
import rcases.SchedulingApp;
import rcases.model.Customer;

public class CustomerScreenController implements Initializable {

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
    
    private SchedulingApp mainApp;
    
    public CustomerScreenController() {
    }

    @FXML
    void handleDeleteCustomer(ActionEvent event) {

    }

    @FXML
    void handleEditCustomer(ActionEvent event) {

    }

    @FXML
    void handleNewCustomer(ActionEvent event) {

    }
    
    public void setCustomerScreen(SchedulingApp mainApp) {
	this.mainApp = mainApp;
        cityChoiceBox.getItems().addAll("Phoenix","New York","London");
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
         
        customerTable.getItems().setAll(parseCustomerList()); /* takes the list from the parseCustomerList() 
        method, and addes the rows to the TableView */
         
        customerTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue)->showCustomerDetails(newValue));
        
    }    
    
    @FXML /* port information over to the form */
    private void showCustomerDetails(Customer selectedCustomer) {
     
        phoneField.setText(selectedCustomer.getCustomerId());
        nameField.setText(selectedCustomer.getCustomerName());
        addressField.setText(selectedCustomer.getAddress());
        postalCodeField.setText(selectedCustomer.getZip());

    }
    
    private List<Customer> parseCustomerList() {
      
        String tId;
        String tName;
        String tAddress;
        String tZip;
        ArrayList<Customer> custList = new ArrayList();
        try(
            
            
        PreparedStatement statement = DBConnection.getConn().prepareStatement("SELECT customer.customerId, customer.customerName, "
                + "address.address, address.postalCode FROM customer, address WHERE customer.addressId = address.addressId;");
            ResultSet rs = statement.executeQuery();){
           
            
            while (rs.next()) {
                tId = rs.getString("customer.customerId");

                tName = rs.getString("customer.customerName");

                tAddress = rs.getString("address.address");
                
                tZip = rs.getString("address.postalCode");

                custList.add(new Customer(tId, tName, tAddress, tZip));

            }
          
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }

         
        return custList;

    }

}
