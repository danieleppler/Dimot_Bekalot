package com.example.dimot_bekalot.dataObjects;
/**
 * This class represent Costumer Details of Patient
 */
public class Costumer_Details_Patient extends Costumer_Details {
    private String first_name;
    private String second_name;
    private String age;
    private String patientID;

    public Costumer_Details_Patient(){
        super();
    }

    public Costumer_Details_Patient(String email, String phone_number,
                                    String password, Address address, String first_name,
                                    String second_name, String age, String patientID) {
        super(email, phone_number, password, address);
        this.first_name = first_name;
        this.second_name = second_name;
        this.age = age;
        this.patientID = patientID;
    }

    public String getFirst_name() { return first_name; }
    public String getSecond_name() { return second_name; }
    public String getAge() { return age; }
    public String getID() { return patientID; }

    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public void setSecond_name(String second_name) { this.second_name = second_name; }
}