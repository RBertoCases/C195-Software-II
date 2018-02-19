/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.model;

/**
 *
 * @author rober
 */
public class City {
    private int cityId;
    private String city;
    
    public City(String city){
        this.city = city;
    }

    public City(int cityID, String city) {
        this.cityId = cityID;
        this.city = city;
    }

    public int getCityID() {
        return cityId;
    }

    public void setCityID(int cityID) {
        this.cityId = cityID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    @Override
    public String toString() {
        return city;
    }
    
}
