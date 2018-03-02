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
import java.time.LocalDateTime;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TableColumn<Appointment, LocalDateTime> startApptColumn;

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
        
        ApptTableView.getItems().setAll(populateAppointmentList());
        
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
      
        LocalDateTime tStart;
        LocalDateTime tEnd;
        String tTitle;
        String tType;
        Customer tCustomer;        
        String tUser;        
        
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try(
            
            
        PreparedStatement statement = DBConnection.getConn().prepareStatement(
        "SELECT appointment.customerId, appointment.title, appointment.description, "
                + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                + "FROM appointment, customer "
                + "WHERE appointment.customerId = customer.customerId;");
            ResultSet rs = statement.executeQuery();){
           
            
            while (rs.next()) {
                tStart = rs.getTimestamp("appointment.start").toLocalDateTime();

                tEnd = rs.getTimestamp("appointment.end").toLocalDateTime();

                tTitle = rs.getString("appointment.title");
                
                tType = rs.getString("appointment.description");
                
                tCustomer = new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName"));
                
                tUser = rs.getString("appointment.createdBy");
                
                //System.out.println(rs.getString("appointment.createdBy"));
                
                appointmentList.add(new Appointment(tStart, tEnd, tTitle, tType, tCustomer, tUser));
                
                

            }
          
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }

        return appointmentList;

    }
    
}
