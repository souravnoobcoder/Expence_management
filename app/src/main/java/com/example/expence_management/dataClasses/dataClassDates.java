package com.example.expence_management.dataClasses;

public class dataClassDates {
    private String uDate;
    private int id;

    public dataClassDates(String uDate, int id) {
        this.uDate = uDate;
        this.id = id;
    }

    public String getuDate() {
        return uDate;
    }

    public void setuDate(String uDate) {
        this.uDate = uDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
