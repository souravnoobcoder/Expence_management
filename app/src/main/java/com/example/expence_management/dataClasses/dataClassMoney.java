package com.example.expence_management.dataClasses;

import java.util.List;

public class dataClassMoney {
        private int id;
        private List<Integer> moneyList;
        private int money;

    public dataClassMoney(int id, List<Integer> moneyList, int money) {
        this.id = id;
        this.moneyList = moneyList;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getMoneyList() {
        return moneyList;
    }

    public int getMoney() {
        return money;
    }
}
