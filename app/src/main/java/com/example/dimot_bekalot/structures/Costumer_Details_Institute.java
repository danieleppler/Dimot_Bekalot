package com.example.dimot_bekalot.structures;

public class Costumer_Details_Institute extends Costumer_Details {

    private String institute_name;

    public Costumer_Details_Institute(String client_id, String userName,
                                      String email, String phone_number,
                                      String password, Address address) {
        super(userName, email, phone_number, password, address);
    }
}
