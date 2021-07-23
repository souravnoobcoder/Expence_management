package com.example.expence_management.dataClasses;

import java.util.List;

public class dataClassMoney {
        private String date;
        private List<Integer> moneyList;
        private int money;

    public dataClassMoney(String date, List<Integer> moneyList, int money) {
        this.date = date;
        this.moneyList = moneyList;
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public List<Integer> getMoneyList() {
        return moneyList;
    }

    public int getMoney() {
        return money;
    }
}
