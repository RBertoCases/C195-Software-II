/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rcases.model.Appointment;
import rcases.view.LoginScreenController;
import rcases.view.CustomerScreenController;
import rcases.view.AppointmentScreenController;
import rcases.view.MenuController;
import rcases.view.NewApptScreenController;

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
    Locale locale = Locale.getDefault();
    private static Connection connection;

    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Scheduling Application - RCases");
        //showLoginScreen();
        showMenu();
        showCustomerScreen();
        //showAppointmentScreen();
        
    }
    
    /**
     * Initializes the root layout.
     */
    public void showMenu() {
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
            controller.setMenu(this);
            
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
    
    
    public void showCustomerScreen() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SchedulingApp.class.getResource("/rcases/view/CustomerScreen.fxml"));
            AnchorPane customerScreen = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            menu.setCenter(customerScreen);

            // Give the controller access to the main app.
            CustomerScreenController controller = loader.getController();
            controller.setCustomerScreen(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) {
        //Locale.setDefault(new Locale("fr", "FR"));
        //System.out.println(Locale.getDefault()); 
        DBConnection.init();
        connection = DBConnection.getConn();
        System.out.println(LocalDate.now());
        System.out.println(LocalTime.now());
        System.out.println(LocalDateTime.now());
        System.out.println(ZonedDateTime.now());
        launch(args);
        DBConnection.closeConn();
    }

    public void showAppointmentScreen() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SchedulingApp.class.getResource("/rcases/view/AppointmentScreen.fxml"));
            AnchorPane appointmentScreen = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            menu.setCenter(appointmentScreen);

            // Give the controller access to the main app.
            AppointmentScreenController controller = loader.getController();
            controller.setAppointmentScreen(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean showNewApptScreen() {
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
        NewApptScreenController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();

        return controller.isOkClicked();
        } catch (IOException e) {
        e.printStackTrace();
        return false;
        }
    }
    
    public boolean showEditApptScreen(Appointment appointment) {
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
        NewApptScreenController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setAppointment(appointment);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();

        return controller.isOkClicked();
        } catch (IOException e) {
        e.printStackTrace();
        return false;
        }
    }

    public void showScheduleReportScreen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void showAppointmentTypesByMonthScreen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
