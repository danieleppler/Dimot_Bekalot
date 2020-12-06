package com.example.dimot_bekalot.dataObjects;

import java.io.Serializable;

/**
 * This JAVA Class represent a Address Details of a costumer
 * aka Imaging Institutes and patients
 */
public class Address implements Serializable {
    private String city_Name;
    private String street_Name;
    private String house_Number;

    public Address(){}

    public Address(String city_Name, String street_Name, String house_Number) {
        city_Name = city_Name;
        street_Name = street_Name;
        house_Number = house_Number;
    }

    public String getCity_Name() { return city_Name; }
    public String getStreet_Name() { return street_Name; }
    public String getHouse_Number() {return house_Number; }
}
