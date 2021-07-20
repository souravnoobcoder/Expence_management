package com.example.expence_management.Database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "expenses")
public class DataItems {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date date;

    private int moneyExpense;

    private int moneyGot;

    private String moneyGotPurpose;

    private String moneyExpensePurpose;

    @Ignore
    public DataItems(int id, Date date, int moneyExpense, int moneyGot, String moneyGotPurpose, String moneyExpensePurpose) {
        this.id = id;
        this.date = date;
        this.moneyExpense = moneyExpense;
        this.moneyGot = moneyGot;
        this.moneyGotPurpose = moneyGotPurpose;
        this.moneyExpensePurpose = moneyExpensePurpose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMoneyExpense() {
        return moneyExpense;
    }

    public void setMoneyExpense(int moneyExpense) {
        this.moneyExpense = moneyExpense;
    }

    public int getMoneyGot() {
        return moneyGot;
    }

    public void setMoneyGot(int moneyGot) {
        this.moneyGot = moneyGot;
    }

    public String getMoneyGotPurpose() {
        return moneyGotPurpose;
    }

    public void setMoneyGotPurpose(String moneyGotPurpose) {
        this.moneyGotPurpose = moneyGotPurpose;
    }

    public String getMoneyExpensePurpose() {
        return moneyExpensePurpose;
    }

    public void setMoneyExpensePurpose(String moneyExpensePurpose) {
        this.moneyExpensePurpose = moneyExpensePurpose;
    }
}
