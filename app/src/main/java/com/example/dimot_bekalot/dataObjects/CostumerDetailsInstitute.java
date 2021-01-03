package com.example.dimot_bekalot.dataObjects;

import java.io.Serializable;

/**
 * This class represent Costumer Details of Institute
 */
public class CostumerDetailsInstitute extends Costumer_Details implements Serializable {

    private String institute_name;
    private String instituteID;

    public CostumerDetailsInstitute(){super();}

    public CostumerDetailsInstitute(String email, String phone_number, String password,
                                    Address address, LockedAccount lockedAccount,
                                    String institute_name, String instituteID) {
        super(email, phone_number, password, address, lockedAccount);
        this.institute_name = institute_name;
        this.instituteID = instituteID;
    }


    public String getInstitute_name() {
        return institute_name;
    }

    public String getInstituteID() {
        return instituteID;
    }

    public void setInstitute_name(String institute_name) {
        this.institute_name = institute_name;
    }

    public void setInstituteID(String instituteID) {
        this.instituteID = instituteID;
    }
}
