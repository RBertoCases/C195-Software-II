/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.model;

/**
 *
 * @author rcases
 */
public class Appointment {
    
    private String appointmentId;
    private Customer customer;
    private String title;
    private String description;
    private String start;
    private String end;
    private String user;
    
    public Appointment() {
    }

    public Appointment(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    /*public Appointment(String appointmentId, Customer customer, String title, String description, String start, String end) {
    this.appointmentId = appointmentId;
    this.customer = customer;
    this.title = title;
    this.description = description;
    this.start = start;
    this.end = end;
    }*/
    
    public Appointment(String appointmentId, String start, String end, String title, String description, Customer customer, String user) {
        this.appointmentId = appointmentId;    
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.customer = customer;
        this.user = user;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
    
    @Override
    public String toString() {
    return "ID: " + this.appointmentId +
            " Start: " + this.start +
            " End: " + this.end + 
            " Title: " + this.title +
            " Type: " + this.description +
            " Customer: " + this.customer.getCustomerName() +
            " Consultant: " + this.user + ".\n" ;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
