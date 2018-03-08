package rcases.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import rcases.SchedulingApp;
import rcases.model.User;


public class MenuController {
    
    @FXML
    private MenuItem logoutUser;
    
    private SchedulingApp mainApp;
    private User user;
    
    public void setMenu(SchedulingApp mainApp, User currentUser) {
	this.mainApp = mainApp;
        
        String username = currentUser.getUsername();
        logoutUser.setText("Logout: " + username);
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
    void handleMenuLogout(ActionEvent event) {
        mainApp.showLoginScreen();
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
