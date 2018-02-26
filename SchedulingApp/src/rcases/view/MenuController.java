package rcases.view;

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
    void handleScheduleReport(ActionEvent event) {
    mainApp.showScheduleReportScreen();
    }
    
    @FXML
    void handleAppointmentTypesByMonth(ActionEvent event) {
    mainApp.showAppointmentTypesByMonthScreen();
    }
    
    @FXML
    void handleMenuReport3(ActionEvent event) {
        Platform.exit();
    }

}
