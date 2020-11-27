package com.example.dimot_bekalot.dataObjects;

import java.io.Serializable;

/**
 * This class represent an object contains ID and password that the user
 * write at the login input
 */
public class Login_Input_Data implements Serializable {

    private String ID;
    private String password;

    public Login_Input_Data(String ID, String password) {
        this.ID = ID;
        this.password = password;
    }

    public String getID() { return ID; }
    public String getPassword() { return password; }
}
