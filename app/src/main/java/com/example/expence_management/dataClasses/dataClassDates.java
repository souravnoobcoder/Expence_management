package com.example.expence_management.dataClasses;

public class dataClassDates {

    private long pDate;
    private long uDate;

    public dataClassDates(long pDate, long uDate) {
        this.pDate = pDate;
        this.uDate = uDate;
    }

    public long getpDate() {
        return pDate;
    }
    public long getuDate() {
        return uDate;
    }
}
