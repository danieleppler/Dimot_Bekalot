package com.example.dimot_bekalot.dataObjects;

public class TreatmentQueue {
    private MyDate _date;
    private long _idPatient;
    private String _type, _nameInstitute, _city;

    public TreatmentQueue(MyDate date, long id, String type, String name, String city){
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

    public long getIdPatient() {
        return _idPatient;
    }

    public void setIdPatient(long _idPatient) {
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
