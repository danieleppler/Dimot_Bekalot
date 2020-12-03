package com.example.dimot_bekalot.dataObjects;

class MyDate {
    private int _day, _month, _year;
    private int _hour, _minute;


    MyDate(int day, int month, int year, int hour, int minute){
        this._day = day;
        this._month = month;
        this._year = year;
        this._hour = hour;
        this._minute = minute;
    }

    public int getDay() {
        return _day;
    }

    public int getMonth() {
        return _month;
    }

    public int getYear() {
        return _year;
    }

    public int getHour() {
        return _hour;
    }

    public int getMinute() {
        return _minute;
    }

    public void setDay(int day) {
        if(day > 0 && day < 29)
            this._day = day;
        else if(day == 29){
            if(this.getMonth() != 2){this._day = day;}
            else{
                if(this.getYear() % 4 == 0 && this.getYear() % 100 == 0){this._day = day;}
            }
        }
        else if(day == 30){
            if(this.getMonth() != 2){this._day = day;}
        }
        else if(day == 31){
            if(this.getMonth() == 1 || this.getMonth() == 3 || this.getMonth() == 5 ||
                    this.getMonth() == 7 || this.getMonth() == 8 || this.getMonth() == 10 ||
                    this.getMonth() == 12) {
                this._day = day;
            }
            else{
                System.out.println("error");
            }
        }
    }

    public void setMonth(int month) {
        if(month > 0 && month <= 12)
            this._month = month;
        else
            System.out.println("error");
    }

    public void setYear(int year) {
        this._year = year;
    }

    public void setHour(int hour) {
        if(hour >= 0 && hour <= 23)
            this._hour = hour;
        else
            System.out.println("error");
    }

    public void setMinute(int minute) {
        if(minute >= 0 && minute <= 59)
            this._minute = minute;
        else
            System.out.println("error");
    }
}

public class TreatmentQueue {
    private MyDate _date;
    private long _idPatient;
    private String _type, _nameInstitute, _city;

    TreatmentQueue(MyDate date, long id, String type, String name, String city){
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
