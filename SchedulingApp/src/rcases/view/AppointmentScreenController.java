/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import rcases.SchedulingApp;
import rcases.model.Appointment;

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
    private TableColumn<Appointment, ?> startApptColumn;

    @FXML
    private TableColumn<Appointment, ?> endApptColumn;
    
     @FXML
    private TableColumn<Appointment, ?> titleApptColumn;

    @FXML
    private TableColumn<Appointment, ?> typeApptColumn;

    @FXML
    private TableColumn<Appointment, ?> customerApptColumn;

    @FXML
    private TableColumn<Appointment, ?> consultantApptColumn;

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
    }

    @FXML
    void handleDeleteAppt(ActionEvent event) {

    }

    @FXML
    void handleEditAppt(ActionEvent event) {

    }

    @FXML
    void handleNewAppt(ActionEvent event) {

    }
    
    
}
