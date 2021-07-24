package com.example.expence_management.dataClasses;

import java.util.List;

public class dataClassDescription {
    private int id;
    private List<String> moneyDescriptionList;

    public dataClassDescription(int id, List<String> moneyDescriptionList) {
        this.id = id;
        this.moneyDescriptionList = moneyDescriptionList;
    }

    public int getId() {
        return id;
    }

    public List<String> getMoneyDescriptionList() {
        return moneyDescriptionList;
    }
}
