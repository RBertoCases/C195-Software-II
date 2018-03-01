/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.view;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import rcases.DBConnection;
import rcases.model.Appointment;
import rcases.model.City;
import rcases.model.Customer;

/**
 * FXML Controller class
 *
 * @author rcases
 */
public class NewApptScreenController implements Initializable {

    @FXML
    private Label apptLabel;

    @FXML
    private TextField titleField;
    
    @FXML
    private TextField customerField;

    @FXML
    private ComboBox<String> startComboBox;

    @FXML
    private ComboBox<String> endComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button apptSaveButton;

    @FXML
    private Button apptCancelButton;

    @FXML
    private TableView<Customer> customerSelectTableView;

    @FXML
    private TableColumn<Customer, String> customerNameApptColumn;

    @FXML
    private TextField customerSearchField;


    private Stage dialogStage;
    private boolean okClicked = false;
    private ZoneId zid = ZoneId.systemDefault();
    
    private ObservableList<Customer> masterData = FXCollections.observableArrayList();
    private ObservableList<String> startTimes = FXCollections.observableArrayList();
    private ObservableList<String> endTimes = FXCollections.observableArrayList();
    private DateTimeFormatter shortTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleNewSave(ActionEvent event) {
        saveAppt();
        dialogStage.close();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dialogStage.close();
        } else {
            alert.close();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        populateTypeList();
        customerNameApptColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        masterData = populateCustomerList();
        
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Customer> filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        customerSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare  name of every customer with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getCustomerName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Customer> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(customerSelectTableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        customerSelectTableView.setItems(sortedData);
        customerSelectTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue)->customerField.setText(newValue.getCustomerName()));
        
        
	LocalTime time = LocalTime.of(8, 0);
	do {
		startTimes.add(time.format(shortTime));
		endTimes.add(time.format(shortTime));
		time = time.plusMinutes(15);
	} while(!time.equals(LocalTime.of(17, 15)));
		startTimes.remove(startTimes.size() - 1);
		endTimes.remove(0);
        
        datePicker.setValue(LocalDate.now());

        startComboBox.setItems(startTimes);
	endComboBox.setItems(endTimes);
	startComboBox.getSelectionModel().select(LocalTime.of(8, 0).format(shortTime));
	endComboBox.getSelectionModel().select(LocalTime.of(8, 15).format(shortTime));
        
    }

    public void setAppointment(Appointment appointment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void saveAppt() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm"); 
	String txtStartTime = "2017-03-29 12:00";
	LocalDateTime ldtStart = LocalDateTime.parse(txtStartTime, df);
                
        ZonedDateTime zdtStart = ldtStart.atZone(zid);
        //System.out.println("Local Time: " + zdtStart);
	ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
	//System.out.println("Zoned time: " + utcStart);
	ldtStart = utcStart.toLocalDateTime();
	System.out.println("Zoned time with zone stripped:" + ldtStart);
	//Create Timestamp values from Instants to update database
	Timestamp startsqlts = Timestamp.valueOf(ldtStart); //this value can be inserted into database
	//System.out.println("Timestamp to be inserted: " +startsqlts);
        
        try {

                PreparedStatement pst = DBConnection.getConn().prepareStatement("INSERT INTO appointment "
                + "(customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
                pst.setString(1, customerSelectTableView.getSelectionModel().getSelectedItem().getCustomerId());
                pst.setString(2, titleField.getText());
                pst.setString(3, typeComboBox.getValue());
                pst.setString(4, "");
                pst.setString(5, "");
                pst.setString(6, "");
                pst.setString(7, startsqlts.toString());
                pst.setString(8, LocalDateTime.now().toString());
                pst.setString(9, LocalDateTime.now().toString());
                pst.setString(10, "test");
                pst.setString(11, LocalDateTime.now().toString());
                pst.setString(12, "test");
                int result = pst.executeUpdate();
                if (result == 1) {//one row was affected; namely the one that was inserted!
                    System.out.println("YAY! New Appointment Save");
                } else {
                    System.out.println("BOO! New Appointment Save");
                }
            } catch (SQLException ex) {
            ex.printStackTrace();
            }
        
        //INSERT INTO U04Esb.appointment (customerId, title, description, location, contact, url, `start`, `end`, createDate, createdBy, lastUpdate, lastUpdateBy) 
	//VALUES (14, 'test14', 'test', ' ', '', ' ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', DEFAULT, 'test')
    }

    private void populateTypeList() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.addAll("Consultation", "New Account", "Follow Up", "Close Account");
        typeComboBox.setItems(typeList);
    }
    
    protected ObservableList<Customer> populateCustomerList() {
      
        String tCustomerId;
        String tCustomerName;
        
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try(
            
            
        PreparedStatement statement = DBConnection.getConn().prepareStatement(
        "SELECT customer.customerId, customer.customerName " +
        "FROM customer, address, city, country " +
        "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");
            ResultSet rs = statement.executeQuery();){
           
            
            while (rs.next()) {
                tCustomerId = rs.getString("customer.customerId");

                tCustomerName = rs.getString("customer.customerName");

                customerList.add(new Customer(tCustomerId, tCustomerName));

            }
          
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }

         
        return customerList;

    }
    
}
