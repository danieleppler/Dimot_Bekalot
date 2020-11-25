package com.example.dimot_bekalot.structures;
/**
 * This JAVA Class represent a Costumer Details
 * aka Imaging Institutes and patients
 */
public class Costumer_Details {
    private String userName;
    private String email;
    private String phone_number;
    private String password;
    private Address address;

    public Costumer_Details( String userName,
                            String email, String phone_number,String password,
                           Address address) {
        this.userName = userName;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.address = address;
    }

    public String getUserName() { return userName; }
    public String getEmail() { return email; }
    public String getPhone_number() { return phone_number; }
    public Address getAddress() { return address; }
    public String getPassword() {return password; }

    public void setUserName(String userName) { this.userName = userName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone_number(String phone_number) { this.phone_number = phone_number; }
    public void setPassword(String password) { this.password = password; }
    public void setAddress(Address address) {
        this.address = address;
    }
}
