/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.model;

import java.util.Date;

/**
 *
 * @author rcases
 */
public class Appointment {
    
    private int appointmentId;
    private int customerId;
    private String title;
    private String description;
    private String contact;
    private Date start;
    private Date end;
    
    public Appointment() {
    }

    public Appointment(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Appointment(int appointmentId, int customerId, String title, String description, String contact, Date start, Date end) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.contact = contact;
        this.start = start;
        this.end = end;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
    
    @Override
    public String toString() {
        return "model.Appointment[ appointmentId=" + appointmentId + " ]";
    }
}
