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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rcases.DBConnection;
import rcases.SchedulingApp;
import rcases.model.Appointment;
import rcases.model.AppointmentReport;
import rcases.model.Customer;
import rcases.model.User;

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
    private TableView<Appointment> schedTableView;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> startSchedColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endSchedColumn;
    
    @FXML
    private TableColumn<Appointment, String> titleSchedColumn;

    @FXML
    private TableColumn<Appointment, String> typeSchedColumn;

    @FXML
    private TableColumn<Appointment, Customer> customerSchedColumn;

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
    private ObservableList<Appointment> schedule;
    private ObservableList<PieChart.Data> pieChartData;
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId newzid = ZoneId.systemDefault();
    private User currentUser;
    
    public ReportsController() {
        
    }
    
    public void setReports(SchedulingApp mainApp, User currentUser) {
        this.mainApp = mainApp;
        this.currentUser = currentUser;
        
        tabPane.getSelectionModel().select(schedTab);
        
        populateApptTypeList();
        populateCustPie();
        populateSchedule();
        
        
        
        startSchedColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endSchedColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        titleSchedColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeSchedColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        customerSchedColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("Month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        typeAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }
    
    
    
    private void populateApptTypeList() {
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
        //System.out.println(apptList);
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
                
        //System.out.println(locationMap);
	pieChartData = FXCollections.observableArrayList();
	locationMap.forEach((key, value) -> pieChartData.add(new PieChart.Data(key, value)));
        //System.out.println(pieChartData);
        pieChart.setData(pieChartData);
    }
    
    private void populateSchedule() {
      
        schedule = FXCollections.observableArrayList();
        
        
        try{
            
            
        PreparedStatement pst = DBConnection.getConn().prepareStatement(
        "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.description, "
                + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                + "FROM appointment, customer "
                + "WHERE appointment.customerId = customer.customerId AND appointment.`start` >= CURRENT_DATE AND appointment.createdBy = ?"
                + "ORDER BY `start`");
            pst.setString(1, currentUser.getUsername());
            ResultSet rs = pst.executeQuery();
           
            
            while (rs.next()) {
                
                String tAppointmentId = rs.getString("appointment.appointmentId");
                Timestamp tsStart = rs.getTimestamp("appointment.start");
                ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);

                Timestamp tsEnd = rs.getTimestamp("appointment.end");
                ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);

                String tTitle = rs.getString("appointment.title");
                
                String tType = rs.getString("appointment.description");
                
                Customer tCustomer = new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName"));
                
                String tUser = rs.getString("appointment.createdBy");
                      
                schedule.add(new Appointment(tAppointmentId, newLocalStart.format(timeDTF), newLocalEnd.format(timeDTF), tTitle, tType, tCustomer, tUser));
                

            }
            
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }
        schedTableView.getItems().setAll(schedule);
    }

    
}
