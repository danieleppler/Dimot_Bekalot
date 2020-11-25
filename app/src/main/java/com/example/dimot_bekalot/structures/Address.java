package com.example.dimot_bekalot.structures;
/**
 * This JAVA Class represent a Address Details of a costumer
 * aka Imaging Institutes and patients
 */
public class Address {
    private String City_Name;
    private String Street_Name;
    private String House_Number;

    public Address(String city_Name, String street_Name, String house_Number) {
        City_Name = city_Name;
        Street_Name = street_Name;
        House_Number = house_Number;
    }

    public String getCity_Name() { return City_Name; }
    public String getStreet_Name() { return Street_Name; }
    public String getHouse_Number() { return House_Number; }

}
