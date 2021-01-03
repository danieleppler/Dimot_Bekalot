package com.example.dimot_bekalot.dataObjects;

public class MyDate {
    private String _day, _month, _year;
    private String _hour, _minute;


    public MyDate(String day, String month, String year, String hour, String minute) {
        this._day = day;
        this._month = month;
        this._year = year;
        this._hour = hour;
        this._minute = minute;
    }

    public String getDay() {
        return _day;
    }

    public String getMonth() {
        return _month;
    }

    public String getYear() {
        return _year;
    }

    public int getIntYear() { return Integer.parseInt(_year); }

    public String getHour() {
        return _hour;
    }

    public String getMinute() {
        return _minute;
    }

    public void setDay(String day) {
        int dayFromString = Integer.parseInt(day);
        if (dayFromString > 0 && dayFromString < 29)
            this._day = day;
        else if (day == "29") {
            if (this.getMonth() != "2") {
                this._day = day;
            } else {
                if (this.getIntYear() % 4 == 0 && this.getIntYear() % 100 == 0) {
                    this._day = day;
                }
            }
        } else if (day == "30") {
            if (this.getMonth() != "2") {
                this._day = day;
            }
        } else if (day == "31") {
            if (this.getMonth() == "1" || this.getMonth() == "3" || this.getMonth() == "5" ||
                    this.getMonth() == "7" || this.getMonth() == "8" || this.getMonth() == "10" ||
                    this.getMonth() == "12") {
                this._day = day;
            } else {
                System.out.println("error");
            }
        }
    }

    public void setMonth(String month) {
        int monthFromString = Integer.parseInt(month);
        if (monthFromString > 0 && monthFromString <= 12)
            this._month = month;
        else
            System.out.println("error");
    }

    public void setYear(String year) {
        this._year = year;
    }

    public void setHour(String hour) {
        int hourFromString = Integer.parseInt(hour);
        if (hourFromString >= 0 && hourFromString <= 23)
            this._hour = hour;
        else
            System.out.println("error");
    }

    public void setMinute(String minute) {
        int minuteFromString = Integer.parseInt(minute);
        if (minuteFromString >= 0 && minuteFromString <= 59)
            this._minute = minute;
        else
            System.out.println("error");
    }
    public String toString()
    {
        return _day+"."+_month+"."+_year+"   "+_hour+":"+_minute;
    }
}
