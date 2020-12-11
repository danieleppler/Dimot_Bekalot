package com.example.dimot_bekalot.dataObjects;

import java.io.Serializable;

/**
 * This class
 */
public class LockedAccount implements Serializable {
    private String isLocked;
    private String logintry;

    public LockedAccount (){ }

    public LockedAccount(String isLocked, String logintry) {
        this.isLocked = isLocked;
        this.logintry = logintry;
    }

    public String isLocked() {
        return isLocked;
    }

    public String getLogintry() {
        return logintry;
    }

    public void setLocked(String locked) {
        isLocked = locked;
    }

    public void setLogintry(String logintry) {
        this.logintry = logintry;
    }
}
