/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases;

import java.io.IOException;
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
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Scheduling Application");

        showLoginScreen();
    }
    
    public void showLoginScreen() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SchedulingApp.class.getResource("/rcases/view/Login.fxml"));
            loginScreen = (AnchorPane) loader.load();   

            // Give the controller access to the main app.
            LoginController controller = loader.getController();
            
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
    
    /*@Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/rcases/view/Login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
