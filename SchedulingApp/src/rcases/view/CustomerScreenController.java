package rcases.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import rcases.SchedulingApp;

public class CustomerScreenController {

    @FXML
    private TableView<?> CustomerTable;

    @FXML
    private TableColumn<?, ?> firstNameColumn;

    @FXML
    private TableColumn<?, ?> lastNameColumn;

    @FXML
    private TextField countryField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField streetField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField postalCodeField;

    @FXML
    void handleDeleteCustomer(ActionEvent event) {

    }

    @FXML
    void handleEditCustomer(ActionEvent event) {

    }

    @FXML
    void handleNewCustomer(ActionEvent event) {

    }

}
