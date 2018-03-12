package rcases.view;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import rcases.util.DBConnection;
import rcases.SchedulingApp;
import rcases.model.User;


public class MenuController {
    
    @FXML
    private MenuItem logoutUser;
    
    private SchedulingApp mainApp;
    private User currentUser;
    
    public MenuController() {
        
    }
    
    public void setMenu(SchedulingApp mainApp, User currentUser) {
	this.mainApp = mainApp;
        this.currentUser = currentUser;
        
        logoutUser.setText("Logout: " + currentUser.getUsername());
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
    void handleMenuReports(ActionEvent event) {
        mainApp.showReports(currentUser);

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
    void handleMenuClose(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Close");
            alert.setHeaderText("Are you sure you want close the program?");
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent((ButtonType response) -> {
                Platform.exit();
                System.exit(0);
                }
            );
            
    }    

}
