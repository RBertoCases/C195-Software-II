/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases;

import java.io.IOException;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import rcases.view.LoginController;

/**
 *
 * @author rcases
 */
public class SchedulingApp extends Application {
    
    private Stage primaryStage;
    private AnchorPane loginScreen;
    Locale locale = Locale.getDefault();

    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Scheduling Application - RCases");

        showLoginScreen();
    }
    
    public void showLoginScreen() {
        try {
            // Load Login Screen.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SchedulingApp.class.getResource("/rcases/view/Login.fxml"));
            loginScreen = (AnchorPane) loader.load();   

            // Give the controller access to the main app.
            LoginController controller = loader.getController();
            System.out.println(locale);
            Locale localeFr = new Locale("fr");
            Locale.setDefault(localeFr);               // change the default
            System.out.println(Locale.getDefault()); // fr
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(loginScreen);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
