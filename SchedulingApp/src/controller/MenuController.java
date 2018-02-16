package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import rcases.SchedulingApp;


public class MenuController {
    
    private SchedulingApp mainApp;
    
    public void setMenu(SchedulingApp mainApp) {
	this.mainApp = mainApp;
    }
    
    public MenuController() {
    }

    @FXML
    void handleMenuAppointments(ActionEvent event) {
        mainApp.showAppointmentScreen();
    }

    @FXML
    void handleMenuCustomers(ActionEvent event) {
        mainApp.showCustomerScreen();
    }

    @FXML
    void handleMenuReport1(ActionEvent event) {

    }

    @FXML
    void handleMenuReport2(ActionEvent event) {

    }

    @FXML
    void handleMenuReport3(ActionEvent event) {
        Platform.exit();
    }

}
