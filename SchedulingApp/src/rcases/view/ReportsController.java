/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import rcases.DBConnection;
import rcases.SchedulingApp;
import rcases.model.Appointment;
import rcases.model.AppointmentReport;
import rcases.model.Customer;

/**
 * FXML Controller class
 *
 * @author rcases
 */
public class ReportsController {
    
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab schedTab;

    @FXML
    private TableView<?> schedTableView;

    @FXML
    private TableColumn<?, ?> consultantColumn;

    @FXML
    private Tab apptTab;

    @FXML
    private TableView<AppointmentReport> apptTableView;

    @FXML
    private TableColumn<AppointmentReport, String> monthColumn;

    @FXML
    private TableColumn<AppointmentReport, String> typeColumn;

    @FXML
    private TableColumn<AppointmentReport, String> typeAmount;

    @FXML
    private Tab custTab;
    
    @FXML
    private PieChart pieChart;

    /*@FXML
    private TableView<?> custTableView;
    
    @FXML
    private TableColumn<?, ?> locationColumn;
    
    @FXML
    private TableColumn<?, ?> custColumn;
    
    @FXML
    private TableColumn<?, ?> custAmountColumn;*/
    
    private SchedulingApp mainApp;
    private ObservableList<AppointmentReport> apptList;
    private ObservableList<PieChart.Data> pieChartData;
    
    public ReportsController() {
        
    }
    
    public void setReports(SchedulingApp mainApp) {
        this.mainApp = mainApp;
        tabPane.getSelectionModel().select(apptTab);
        populateApptList();
        populateCustPie();
        
        
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("Month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        typeAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }
    
    
    
    private void populateApptList() {
        apptList = FXCollections.observableArrayList();
        
        try{
            
            
        PreparedStatement statement = DBConnection.getConn().prepareStatement(
            "SELECT MONTHNAME(`start`) AS \"Month\", description AS \"Type\", COUNT(*) as \"Amount\" "
            + "FROM appointment "
            + "GROUP BY MONTHNAME(`start`), description");
            ResultSet rs = statement.executeQuery();
           
            
            while (rs.next()) {
                
                String month = rs.getString("Month");
                
                String type = rs.getString("Type");

                String amount = rs.getString("Amount");
                      
                apptList.add(new AppointmentReport(month, type, amount));
                
                

            }
            
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }
        System.out.println(apptList);
        apptTableView.getItems().setAll(apptList);
    }
    
    private void populateCustPie() {
        Map<String, Double> locationMap = new HashMap<>();

            try { PreparedStatement pst = DBConnection.getConn().prepareStatement(
                  "SELECT city.city, COUNT(city) "
                + "FROM customer, address, city "
                + "WHERE customer.addressId = address.addressId "
                + "AND address.cityId = city.cityId "
                + "GROUP BY city"); 
                ResultSet rs = pst.executeQuery();


                while (rs.next()) {
                        String city = rs.getString("city");
                        double count = rs.getInt("COUNT(city)");
                        locationMap.put(city, count);
                }

            } catch (SQLException sqe) {
                System.out.println("Check your SQL");
                sqe.printStackTrace();
            } catch (Exception e) {
                System.out.println("Something besides the SQL went wrong.");
                e.printStackTrace();
            }     
                
        System.out.println(locationMap);
	pieChartData = FXCollections.observableArrayList();
	locationMap.forEach((key, value) -> pieChartData.add(new PieChart.Data(key, value)));
        System.out.println(pieChartData);
        pieChart.setData(pieChartData);
    }

    
}
