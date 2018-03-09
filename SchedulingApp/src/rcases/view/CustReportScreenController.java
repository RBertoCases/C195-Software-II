/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.view;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import rcases.DBConnection;
import rcases.SchedulingApp;

/**
 * FXML Controller class
 *
 * @author rcases
 */
public class CustReportScreenController {
    
    @FXML
    private PieChart pieChart;
    
    private SchedulingApp mainApp;
    
    private ObservableList<PieChart.Data> pieChartData;
    

    public CustReportScreenController() {
        
        }

    public void setCustReportScreen(SchedulingApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public static Map<String, Double> customerLocation() {
		Map<String, Double> locationMap = new HashMap<>();

		String sql = "SELECT city, COUNT(city) AS customerCount "
				+ "FROM customer AS cu, address AS ad, city AS ci "
				+ "WHERE cu.addressId = ad.addressId "
				+ "AND ad.cityId = ci.cityId "
				+ "GROUP BY city";

		try { PreparedStatement pst = DBConnection.getConn().prepareStatement("SELECT city, COUNT(city) AS customerCount "
				+ "FROM customer AS cu, address AS ad, city AS ci "
				+ "WHERE cu.addressId = ad.addressId "
				+ "AND ad.cityId = ci.cityId "
				+ "GROUP BY city"); 
                    ResultSet rs = pst.executeQuery();
                    

			while (rs.next()) {
				String city = rs.getString("city");
				double count = rs.getInt("customerCount");
				locationMap.put(city, count);
			}

		} catch (SQLException sqe) {
                    System.out.println("Check your SQL");
                    sqe.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Something besides the SQL went wrong.");
                    e.printStackTrace();
                }
		return locationMap;
	}
    
    /**
     *
     * @throws InvocationTargetException
     */
    public void setData() throws InvocationTargetException {
        Map<String, Double> locationMap = new HashMap<>();

		String sql = "SELECT city, COUNT(city) AS customerCount "
				+ "FROM customer AS cu, address AS ad, city AS ci "
				+ "WHERE cu.addressId = ad.addressId "
				+ "AND ad.cityId = ci.cityId "
				+ "GROUP BY city";

		try { PreparedStatement pst = DBConnection.getConn().prepareStatement("SELECT city, COUNT(city) AS customerCount "
				+ "FROM customer AS cu, address AS ad, city AS ci "
				+ "WHERE cu.addressId = ad.addressId "
				+ "AND ad.cityId = ci.cityId "
				+ "GROUP BY city"); 
                    ResultSet rs = pst.executeQuery();
                    

			while (rs.next()) {
				String city = rs.getString("city");
				double count = rs.getInt("customerCount");
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
