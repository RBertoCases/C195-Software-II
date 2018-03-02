/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.view;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import rcases.DBConnection;
import rcases.SchedulingApp;
import rcases.model.Appointment;
import rcases.model.Customer;

/**
 * FXML Controller class
 *
 * @author rcases
 */
public class AppointmentScreenController {
    
    private SchedulingApp mainApp;
    
    @FXML
    private TableView<Appointment> ApptTableView;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> startApptColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endApptColumn;
    
    @FXML
    private TableColumn<Appointment, String> titleApptColumn;

    @FXML
    private TableColumn<Appointment, String> typeApptColumn;

    @FXML
    private TableColumn<Appointment, Customer> customerApptColumn;

    @FXML
    private TableColumn<Appointment, String> consultantApptColumn;

    @FXML
    private RadioButton weekRadioButton;

    @FXML
    private RadioButton monthRadioButton;
    
    @FXML
    private ToggleGroup apptToggleGroup;
    
    private final DateTimeFormatter shortTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private ZoneId newzid = ZoneId.systemDefault();
    ObservableList<Appointment> apptList;
        
    public void setAppointmentScreen(SchedulingApp mainApp) {
	this.mainApp = mainApp;
        apptToggleGroup = new ToggleGroup();
        this.weekRadioButton.setToggleGroup(apptToggleGroup);
        this.monthRadioButton.setToggleGroup(apptToggleGroup);
        
        startApptColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endApptColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        titleApptColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeApptColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        customerApptColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        consultantApptColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        
       apptList = FXCollections.observableArrayList();
       ApptTableView.getItems().setAll(populateAppointmentList());
        
        
    }
    
    @FXML
    void handleApptMonth(ActionEvent event) {
        
        LocalDate now = LocalDate.now();
        System.out.println(now);
        LocalDate nowPlus7 = now.plusMonths(1);
        FilteredList<Appointment> filteredData = new FilteredList<>(apptList);
        filteredData.setPredicate(row -> {

            LocalDate rowDate = LocalDate.parse(row.getStart(), shortTime);

            return rowDate.equals(now) && rowDate.isBefore(nowPlus7);
        });
        ApptTableView.setItems(filteredData);

    }

    @FXML
    void handleApptWeek(ActionEvent event) {
        
        LocalDate now = LocalDate.now();
        System.out.println(now);
        LocalDate nowPlus7 = now.plusDays(7);
        FilteredList<Appointment> filteredData = new FilteredList<>(apptList);
        filteredData.setPredicate(row -> {

            LocalDate rowDate = LocalDate.parse(row.getStart(), shortTime);

            return rowDate.equals(now) && rowDate.isBefore(nowPlus7);
        });
        ApptTableView.setItems(filteredData);
    }

    @FXML
    void handleDeleteAppt(ActionEvent event) {

    }

    @FXML
    void handleEditAppt(ActionEvent event) {

    }

    @FXML
    void handleNewAppt(ActionEvent event) throws IOException{
        boolean okClicked = mainApp.showNewApptScreen();
    }
    
    protected List<Appointment> populateAppointmentList() {
      
        String tTitle;
        String tType;
        Customer tCustomer;        
        String tUser;        
        
        //ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try(
            
            
        PreparedStatement statement = DBConnection.getConn().prepareStatement(
        "SELECT appointment.customerId, appointment.title, appointment.description, "
                + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                + "FROM appointment, customer "
                + "WHERE appointment.customerId = customer.customerId;");
            ResultSet rs = statement.executeQuery();){
           
            
            while (rs.next()) {
                Timestamp tsStart = rs.getTimestamp("appointment.start");
                ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);

                Timestamp tsEnd = rs.getTimestamp("appointment.end");
                ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);

                tTitle = rs.getString("appointment.title");
                
                tType = rs.getString("appointment.description");
                
                tCustomer = new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName"));
                
                tUser = rs.getString("appointment.createdBy");
                
                //System.out.println(rs.getString("appointment.createdBy"));
                
                apptList.add(new Appointment(newLocalStart.format(shortTime), newLocalEnd.format(shortTime), tTitle, tType, tCustomer, tUser));
                
                

            }
          
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }

        return apptList;

    }
    
}
