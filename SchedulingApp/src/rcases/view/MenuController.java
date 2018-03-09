package rcases.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import rcases.DBConnection;
import rcases.SchedulingApp;
import rcases.model.User;


public class MenuController {
    
    @FXML
    private MenuItem logoutUser;
    
    private SchedulingApp mainApp;
    private User currentUser;
    
    public void setMenu(SchedulingApp mainApp, User currentUser) {
	this.mainApp = mainApp;
        this.currentUser = currentUser;
        
        logoutUser.setText("Logout: " + currentUser.getUsername());
    }
    
    public MenuController() {
    }
    

    @FXML
    void handleMenuAppointments(ActionEvent event) {
        mainApp.showAppointmentScreen(currentUser);
    }

    @FXML
    void handleMenuCustomers(ActionEvent event) {
        mainApp.showCustomerScreen(currentUser);
    }
    
    @FXML
    void handleMenuLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Logout");
            alert.setHeaderText("Are you sure you want logout?");
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> mainApp.showLoginScreen());
    }

    @FXML
    void handleScheduleReport(ActionEvent event) {
        mainApp.showScheduleReportScreen();
    }
    
    @FXML
    void handleAppointmentTypesByMonth(ActionEvent event) {
        mainApp.showApptTypeReportScreen();
    }
    
    @FXML
    void handleCustReport(ActionEvent event) {
        mainApp.showCustReportScreen();
    }
    
    

}
