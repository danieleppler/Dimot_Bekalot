package com.example.dimot_bekalot.dataObjects;

import android.location.Address;

import java.util.Date;

/**
 * This class represent Queue Details
 */

public class Queue_details {
    private String queue_id;
    private String Institute;
    private java.util.Date Date;
    private String time;
    private String Patient_id_attending;

    public Queue_details(){;};

    public Queue_details(String queue_id, String Institute,
                                    Date Date, String time, String Patient_id_attending) {
        this.queue_id = queue_id;
        this.Institute = Institute;
        this.Date = Date;
        this.time = time;
        this.Patient_id_attending=Patient_id_attending;
    }

    public void setQueue_id(String queue_id) {
        this.queue_id = queue_id;
    }

    public void setInstitute(String institute) {
        Institute = institute;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPatient_id_attending(String patient_id_attending) {
        Patient_id_attending = patient_id_attending;
    }

    public String getQueue_id() {
        return queue_id;
    }

    public String getInstitute() {
        return Institute;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public String getTime() {
        return time;
    }

    public String getPatient_id_attending() {
        return Patient_id_attending;
    }
}
