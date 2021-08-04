package com.example.expense_management.dataClasses;

import java.util.List;

public class dataClassDescription {
    private long date;
    private List<String> moneyDescriptionList;

    public dataClassDescription(long date, List<String> moneyDescriptionList) {
        this.date = date;
        this.moneyDescriptionList = moneyDescriptionList;
    }

    public long getDate() {
        return date;
    }

    public List<String> getMoneyDescriptionList() {
        return moneyDescriptionList;
    }
}
