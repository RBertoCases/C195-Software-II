/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases;

import rcases.util.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rcases.model.Appointment;
import rcases.model.User;
import rcases.view.LoginScreenController;
import rcases.view.CustomerScreenController;
import rcases.view.AppointmentScreenController;
import rcases.view.MenuController;
import rcases.view.AppointmentEditScreenController;
import rcases.view.ReportsController;

/**
 *
 * @author rcases
 */
public class SchedulingApp extends Application {
    
    private Stage primaryStage;
    private BorderPane menu;
    private AnchorPane loginScreen;
    private AnchorPane customerScreen;
    private AnchorPane appointmentScreen;
    private AnchorPane custReportScreen;
    private TabPane tabPane;
    Locale locale = Locale.getDefault();
    private static Connection connection;

    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Scheduling Application - RCases");
        showLoginScreen();
        //showMenu(null);
        //showCustomerScreen();
        //showAppointmentScreen();
        
        
    }
    
    public static void main(String[] args) {
        //Locale.setDefault(new Locale("fr", "FR"));
        //System.out.println(Locale.getDefault()); 
        DBConnection.init();
        connection = DBConnection.getConn();
        launch(args);
        DBConnection.closeConn();
    }
    
    /**
     * Initializes the root layout.
     * @param currentUser
     */
    public void showMenu(User currentUser) {
        
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SchedulingApp.class.getResource("/rcases/view/Menu.fxml"));
            menu = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(menu);
            primaryStage.setScene(scene);
            // Give the controller access to the main app.
            MenuController controller = loader.getController();
            controller.setMenu(this, currentUser);
            
            primaryStage.show();
        } catch (IOException e) {
            e.getCause().printStackTrace();
        }
    }
    
    public void showLoginScreen() {
        try {
            // Load Login Screen.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SchedulingApp.class.getResource("/rcases/view/LoginScreen.fxml"));
            AnchorPane loginScreen = (AnchorPane) loader.load();
            
            // Give the controller access to the main app.
            LoginScreenController controller = loader.getController();
            controller.setLogin(this);            
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(loginScreen);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void showCustomerScreen(User currentUser) {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SchedulingApp.class.getResource("/rcases/view/CustomerScreen.fxml"));
            AnchorPane customerScreen = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            menu.setCenter(customerScreen);

            // Give the controller access to the main app.
            CustomerScreenController controller = loader.getController();
            controller.setCustomerScreen(this, currentUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showAppointmentScreen(User currentUser) {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SchedulingApp.class.getResource("/rcases/view/AppointmentScreen.fxml"));
            AnchorPane appointmentScreen = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            menu.setCenter(appointmentScreen);

            // Give the controller access to the main app.
            AppointmentScreenController controller = loader.getController();
            controller.setAppointmentScreen(this, currentUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean showNewApptScreen(User currentUser) {
    try {
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SchedulingApp.class.getResource("/rcases/view/NewApptScreen.fxml"));
        AnchorPane newApptScreen = (AnchorPane) loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("New Appointment");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(newApptScreen);
        dialogStage.setScene(scene);

        // Set the person into the controller.
        AppointmentEditScreenController controller = loader.getController();
        controller.setDialogStage(dialogStage, currentUser);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();

        return controller.isOkClicked();
        } catch (IOException e) {
        e.printStackTrace();
        return false;
        }
    }
    
    public boolean showEditApptScreen(Appointment appointment, User currentUser) {
    try {
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SchedulingApp.class.getResource("/rcases/view/NewApptScreen.fxml"));
        AnchorPane editApptScreen = (AnchorPane) loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Appointment");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(editApptScreen);
        dialogStage.setScene(scene);

        // Set the person into the controller.
        AppointmentEditScreenController controller = loader.getController();
        controller.setDialogStage(dialogStage, currentUser);
        controller.setAppointment(appointment);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();

        return controller.isOkClicked();
        } catch (IOException e) {
        e.printStackTrace();
        return false;
        }
    }
    
    public void showReports(User currentUser) {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SchedulingApp.class.getResource("/rcases/view/Reports.fxml"));
            TabPane tabPane = (TabPane) loader.load();

            // Set person overview into the center of root layout.
            menu.setCenter(tabPane);

            // Give the controller access to the main app.
            ReportsController controller = loader.getController();
            controller.setReports(this, currentUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
