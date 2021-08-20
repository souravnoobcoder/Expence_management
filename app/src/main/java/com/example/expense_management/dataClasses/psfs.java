package com.example.expense_management.dataClasses;

import android.text.format.DateFormat;


import java.util.Date;
import java.util.List;

public class psfs {
    public static final String UPDATE_MONEY="OK";
    public static final String UPDATE_MONEY_DESCRIPTION="hello";
    public static final String OUR_DATE="date";
    public static final String LIST_POSITION="position";
    public static final String LOOK="look";
    public static final String DETAIL_DATE = "da";
    public static final String DETAIL_GROSS_MONEY_GOT = "wildDog";
    public static final String DETAIL_GROSS_MONEY_PAID = "leopard";
    public static final String DETAIL_MONEY_EXPENSE = "cheetah";
    public static final String DETAIL_MONEY_GOT = "lion";
    public static final String DETAIL_MONEY_EXPENSE_PURPOSE = "hyena";
    public static final String DETAIL_MONEY_GOT_PURPOSE = "dragonLizard";
    public static final String DATE_KEY = "selected date";
    public static final String CHECK = "check";
    public static final String DATA_ID = "idkd";

    public static int setGrossMoney(List<Integer> money) {
        int i, gross = 0, value;
        if (money==null)
            return 0;
        for (i = 0; i < money.size(); i++) {
            value = money.get(i);
            gross += value;
        }
        return gross;
    }

    public static String makeDate(long l) {
        return DateFormat.format("dd/MM/yy", new Date(l)).toString();
    }
}
