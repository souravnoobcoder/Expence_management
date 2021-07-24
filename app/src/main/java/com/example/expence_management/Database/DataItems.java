

package com.example.expence_management.Database;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import java.util.List;

@Entity(tableName = "expenses")
public class DataItems {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;

    private int grossMoneyExpense;

    private int grossMoneyGot;

    private List<Integer> moneyExpense;

    private List<Integer> moneyGot;

    private List<String> moneyGotPurposes;

    private List<String> moneyExpensePurposes;

    public DataItems(){

    }
    @Ignore
    public DataItems(String date, int grossMoneyExpense, int grossMoneyGot,
                     List<Integer> moneyExpense, List<Integer> moneyGot,
                     List<String> moneyGotPurposes, List<String> moneyExpensePurposes) {
        this.date = date;
        this.grossMoneyExpense = grossMoneyExpense;
        this.grossMoneyGot = grossMoneyGot;
        this.moneyExpense = moneyExpense;
        this.moneyGot = moneyGot;
        this.moneyGotPurposes = moneyGotPurposes;
        this.moneyExpensePurposes = moneyExpensePurposes;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGrossMoneyExpense() {
        return grossMoneyExpense;
    }

    public void setGrossMoneyExpense(int grossMoneyExpense) {
        this.grossMoneyExpense = grossMoneyExpense;
    }

    public int getGrossMoneyGot() {
        return grossMoneyGot;
    }

    public void setGrossMoneyGot(int grossMoneyGot) {
        this.grossMoneyGot = grossMoneyGot;
    }

    public List<Integer> getMoneyExpense() {
        return moneyExpense;
    }

    public void setMoneyExpense(List<Integer> moneyExpense) {
        this.moneyExpense = moneyExpense;
    }

    public List<Integer> getMoneyGot() {
        return moneyGot;
    }

    public void setMoneyGot(List<Integer> moneyGot) {
        this.moneyGot = moneyGot;
    }

    public List<String> getMoneyGotPurposes() {
        return moneyGotPurposes;
    }

    public void setMoneyGotPurposes(List<String> moneyGotPurposes) {
        this.moneyGotPurposes = moneyGotPurposes;
    }

    public List<String> getMoneyExpensePurposes() {
        return moneyExpensePurposes;
    }

    public void setMoneyExpensePurposes(List<String> moneyExpensePurposes) {
        this.moneyExpensePurposes = moneyExpensePurposes;
    }
}
