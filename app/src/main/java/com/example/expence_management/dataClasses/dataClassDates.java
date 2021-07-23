package com.example.expence_management.dataClasses;

public class dataClassDates {
    private String uDate;
    private String date;

    public dataClassDates(String uDate, String date) {
        this.uDate = uDate;
        this.date = date;
    }
    public String getuDate() {
        return uDate;
    }

    public String getDate() {
        return date;
    }


}
