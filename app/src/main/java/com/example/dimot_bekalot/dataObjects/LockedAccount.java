package com.example.dimot_bekalot.dataObjects;

import java.io.Serializable;

/**
 * This class
 */
public class LockedAccount implements Serializable {
    private boolean isLocked;
    private int logintry;

    public LockedAccount (){ }

    public LockedAccount(boolean isLocked, int logintry) {
        this.isLocked = isLocked;
        this.logintry = logintry;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public int getLogintry() {
        return logintry;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void setLogintry(int logintry) {
        this.logintry = logintry;
    }
}
