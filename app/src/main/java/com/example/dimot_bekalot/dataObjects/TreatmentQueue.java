package com.example.dimot_bekalot.dataObjects;


public class TreatmentQueue {
    public MyDate _date;
    private String _type, _nameInstitute, _city, _idPatient;

    public TreatmentQueue(){}


    public TreatmentQueue(MyDate date, String id, String type, String name, String city){

        this._date = date;
        this._idPatient = id;
        this._type = type;
        this._nameInstitute = name;
        this._city = city;
    }

    public MyDate getDate() {
        return _date;
    }

    public void setDate(MyDate _date) {
        this._date = _date;
    }

    public String getIdPatient() {
        return _idPatient;
    }

    public void setIdPatient(String _idPatient) {
        this._idPatient = _idPatient;
    }

    public String getType() {
        return _type;
    }

    public void setType(String _type) {
        this._type = _type;
    }

    public String getNameInstitute() {
        return _nameInstitute;
    }

    public void setNameInstitute(String _nameInstitute) {
        this._nameInstitute = _nameInstitute;
    }

    public String getCity() {
        return _city;
    }

    public void setCity(String _city) {
        this._city = _city;
    }

}
