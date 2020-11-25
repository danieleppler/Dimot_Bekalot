package com.example.dimot_bekalot.structures;

public class Costumer_Details_Patient extends Costumer_Details {
    private String first_name;
    private String second_name;
    private int age;

    public Costumer_Details_Patient(String client_id, String userName, String email,
                                    String phone_number, String password, Address address) {
        super(userName, email, phone_number, password, address);
        this.first_name = first_name;
        this.second_name = second_name;
        this.age = age;
    }


    public String getFirst_name() { return first_name; }
    public String getSecond_name() { return second_name; }
    public int getAge() { return age; }

    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public void setSecond_name(String second_name) { this.second_name = second_name; }
}
