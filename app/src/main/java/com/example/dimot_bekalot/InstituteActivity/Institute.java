package com.example.dimot_bekalot.InstituteActivity;

import java.io.Serializable;

public class Institute implements Serializable {
    private String _name, _location;

    Institute(){}
    Institute(String name, String location){
        this._name = name;
        this._location = location;
    }

    String getName(){
        return this._name;
    }
    String getLocation(){
        return this._location;
    }
}
