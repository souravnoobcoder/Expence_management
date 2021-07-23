package com.example.expence_management.dataClasses;

import java.util.List;

public class dataClassDescription {
    private String date;
    private List<String> moneyDescriptionList;

    public dataClassDescription(String date, List<String> moneyDescriptionList) {
        this.date = date;
        this.moneyDescriptionList = moneyDescriptionList;
    }

    public String getDate() {
        return date;
    }

    public List<String> getMoneyDescriptionList() {
        return moneyDescriptionList;
    }
}
