package com.example.expense_management.dataClasses;

import java.util.List;

public class dataClassMoney {
        private long date;
        private List<Integer> moneyList;
        private int money;

    public dataClassMoney(long date, List<Integer> moneyList, int money) {
        this.date = date;
        this.moneyList = moneyList;
        this.money = money;
    }

    public long getDate() {
        return date;
    }

    public List<Integer> getMoneyList() {
        return moneyList;
    }
}
