package com.example.dimot_bekalot.dataObjects;

import androidx.annotation.Nullable;

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

    public String getID() {
        return ID;
    }

    public String getPassword() {
        return password;
    }

    /**
     * This method will help to check users at the login Activity
     * @param obj
     * @return
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Login_Input_Data) {
            Login_Input_Data login_input_data = (Login_Input_Data) obj;
            if ((this.ID.equals(login_input_data.getID())) && (this.password.equals(login_input_data.getPassword()))) {
                return true;
            }
        }
        return false;
    }
}
