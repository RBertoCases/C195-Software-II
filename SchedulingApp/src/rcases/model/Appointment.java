/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.model;

import java.time.LocalDateTime;

/**
 *
 * @author rcases
 */
public class Appointment {
    
    private int appointmentID;
    private String title;
    private String description;
    private String contact;
    private LocalDateTime start;
    private LocalDateTime end;

    public Appointment(int appointmentID, String title, String description, String contact, LocalDateTime start, LocalDateTime end) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.contact = contact;
        this.start = start;
        this.end = end;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
