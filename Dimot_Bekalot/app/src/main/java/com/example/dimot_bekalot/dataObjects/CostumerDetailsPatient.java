package com.example.dimot_bekalot.dataObjects;

import java.io.Serializable;

/**
 * This class represent Costumer Details of Patient
 */
public class CostumerDetailsPatient extends Costumer_Details implements Serializable {
    private String first_name;
    private String second_name;
    private String age;
    private String patientID;
    private String Uid;//unique id created by GOOGLE



    public CostumerDetailsPatient(){
        super();
    }

    public CostumerDetailsPatient(String email, String phone_number, String password, Address address,
                                  LockedAccount lockedAccount, String first_name,
                                  String second_name, String age, String patientID) {
        super(email, phone_number, password, address, lockedAccount);
        this.first_name = first_name;
        this.second_name = second_name;
        this.age = age;
        this.patientID = patientID;
        this.Uid =null;
    }


    public String getFirst_name() {
        return first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public String getAge() {
        return age;
    }

    public String getPatientID() {
        return patientID;
    }

    public String GetUid(){return this.Uid;}

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }
}