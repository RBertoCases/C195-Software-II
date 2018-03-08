/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import rcases.SchedulingApp;
import rcases.model.Appointment;
import rcases.model.Customer;
import rcases.model.User;

/**
 * FXML Controller class
 *
 * @author rcases
 */
public class NewApptScreenController {

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
    private SchedulingApp mainApp;
    private boolean okClicked = false;
    private final ZoneId zid = ZoneId.systemDefault();
    private Appointment selectedAppt;
    private final ZoneId newzid = ZoneId.systemDefault();
    private User currentUser;
    
    private ObservableList<Customer> masterData = FXCollections.observableArrayList();
    private final ObservableList<String> startTimes = FXCollections.observableArrayList();
    private final ObservableList<String> endTimes = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    ObservableList<Appointment> apptTimeList;
    
    
      
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
        if (validateAppointment()){
            if (isOkClicked()) {
                System.out.println(isOkClicked() + " before calling saveappt()");
                updateAppt();            
            } else {
                saveAppt();
            }
            dialogStage.close();
        }
        
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        alert.showAndWait()
        .filter(response -> response == ButtonType.OK)
        .ifPresent(response -> dialogStage.close());
        
    }

    public void setDialogStage(Stage dialogStage, User currentUser) {
        this.dialogStage = dialogStage;
        this.currentUser = currentUser;
        
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
		startTimes.add(time.format(timeDTF));
		endTimes.add(time.format(timeDTF));
		time = time.plusMinutes(15);
	} while(!time.equals(LocalTime.of(17, 15)));
		startTimes.remove(startTimes.size() - 1);
		endTimes.remove(0);
        
        datePicker.setValue(LocalDate.now());

        startComboBox.setItems(startTimes);
	endComboBox.setItems(endTimes);
	startComboBox.getSelectionModel().select(LocalTime.of(8, 0).format(timeDTF));
	endComboBox.getSelectionModel().select(LocalTime.of(8, 15).format(timeDTF));
        System.out.println(currentUser.getUsername());
        
    }

    public void setAppointment(Appointment appointment) {
        
        System.out.println(okClicked);
        okClicked = true;
        System.out.println(okClicked);
        selectedAppt = appointment;
        
        String start = appointment.getStart();
        
        LocalDateTime startLDT = LocalDateTime.parse(start, dateDTF);
        String end = appointment.getEnd();
        LocalDateTime endLDT = LocalDateTime.parse(end, dateDTF);        
        
        apptLabel.setText("Edit Appointment");
        titleField.setText(appointment.getTitle());
        typeComboBox.setValue(appointment.getDescription());
        customerSelectTableView.getSelectionModel().select(appointment.getCustomer());
        datePicker.setValue(LocalDate.parse(appointment.getStart(), dateDTF));
        startComboBox.getSelectionModel().select(startLDT.toLocalTime().format(timeDTF));
        endComboBox.getSelectionModel().select(endLDT.toLocalTime().format(timeDTF));
        
        
    }

    private void saveAppt() {
  
        LocalDate localDate = datePicker.getValue();
	LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), timeDTF);
	LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        
        LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime);

        ZonedDateTime startUTC = startDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));            
	
	Timestamp startsqlts = Timestamp.valueOf(startUTC.toLocalDateTime()); //this value can be inserted into database
        Timestamp endsqlts = Timestamp.valueOf(endUTC.toLocalDateTime()); //this value can be inserted into database        
        
        try {

                PreparedStatement pst = DBConnection.getConn().prepareStatement("INSERT INTO appointment "
                + "(customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");
            
                pst.setString(1, customerSelectTableView.getSelectionModel().getSelectedItem().getCustomerId());
                pst.setString(2, titleField.getText());
                pst.setString(3, typeComboBox.getValue());
                pst.setString(4, "");
                pst.setString(5, "");
                pst.setString(6, "");
                pst.setTimestamp(7, startsqlts);
                pst.setTimestamp(8, endsqlts);
                //pst.setTimestamp(9, TIMESTAMP);
                pst.setString(9, currentUser.getUsername());
                //pst.setString(11, LocalDateTime.now().toString());
                pst.setString(10, currentUser.getUsername());
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
    
    private void updateAppt() {
        
        LocalDate localDate = datePicker.getValue();
	LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), timeDTF);
	LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        
        LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime);

        ZonedDateTime startUTC = startDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));            
	
	Timestamp startsqlts = Timestamp.valueOf(startUTC.toLocalDateTime()); //this value can be inserted into database
        Timestamp endsqlts = Timestamp.valueOf(endUTC.toLocalDateTime()); //this value can be inserted into database        
        
        try {

                PreparedStatement pst = DBConnection.getConn().prepareStatement("UPDATE appointment "
                        + "SET customerId = ?, title = ?, description = ?, start = ?, end = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? "
                        + "WHERE appointmentId = ?");
            
                pst.setString(1, customerSelectTableView.getSelectionModel().getSelectedItem().getCustomerId());
                pst.setString(2, titleField.getText());
                pst.setString(3, typeComboBox.getValue());
                pst.setTimestamp(4, startsqlts);
                pst.setTimestamp(5, endsqlts);
                pst.setString(6, currentUser.getUsername());
                pst.setString(7, selectedAppt.getAppointmentId());
                int result = pst.executeUpdate();
                if (result == 1) {//one row was affected; namely the one that was inserted!
                    System.out.println("YAY! Update Appointment Save");
                } else {
                    System.out.println("BOO! Update Appointment Save");
                }
            } catch (SQLException ex) {
            ex.printStackTrace();
            }
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

    private boolean validateAppointment() {
        String title = titleField.getText();
        String type = typeComboBox.getValue();
        Customer customer = customerSelectTableView.getSelectionModel().getSelectedItem();
        LocalDate localDate = datePicker.getValue();
	LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), timeDTF);
	LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        
        LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime);

        ZonedDateTime startUTC = startDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));            
	        
        String errorMessage = "";
        //first checks to see if inputs are null
        if (title == null || title.length() == 0) {
            errorMessage += "Please enter an Appointment title.\n"; 
        }
        if (type == null || type.length() == 0) {
            errorMessage += "Please select an Appointment type.\n";  
        } 
        if (customer == null) {
            errorMessage += "Please Select a Customer.\n"; 
        } 
        if (startUTC == null) {
            errorMessage += "Please select a Start time"; 
        }         
        if (endUTC == null) {
            errorMessage += "Please select an End time.\n"; 
            } else if (endUTC.equals(startUTC) || endUTC.isBefore(startUTC)){
                errorMessage += "End time must be after Start time.\n";
            } else try {
                if (hasApptConflict(startUTC, endUTC)){
                    errorMessage += "Appointment times conflict with an existing appointment of yours. Please select a new time.\n";
                }
        } catch (SQLException ex) {
            Logger.getLogger(NewApptScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid Appointment fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    private boolean hasApptConflict(ZonedDateTime newStart, ZonedDateTime newEnd) throws SQLException {
        String apptID;
        if (isOkClicked()) {
            apptID = selectedAppt.getAppointmentId();
        } else {
            apptID = "0";
        }
        System.out.println("ApptID: " + apptID);
        
        try{
            
            
        PreparedStatement pst = DBConnection.getConn().prepareStatement(
        "SELECT * FROM appointment "
	+ "WHERE (? BETWEEN start AND end OR ? BETWEEN start AND end OR ? < start AND ? > end) "
	+ "AND (createdBy = ? AND appointmentID != ?)");
        pst.setTimestamp(1, Timestamp.valueOf(newStart.toLocalDateTime()));
	pst.setTimestamp(2, Timestamp.valueOf(newEnd.toLocalDateTime()));
        pst.setTimestamp(3, Timestamp.valueOf(newStart.toLocalDateTime()));
	pst.setTimestamp(4, Timestamp.valueOf(newEnd.toLocalDateTime()));
        pst.setString(5, currentUser.getUsername());
        pst.setString(6, apptID);
        ResultSet rs = pst.executeQuery();
           
        if(rs.next()) {
            return true;
	}
            
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
            e.printStackTrace();
        }
        return false;
    }
    
}
