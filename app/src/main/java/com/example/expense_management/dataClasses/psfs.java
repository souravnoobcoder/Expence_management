package com.example.expense_management.dataClasses;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;


import androidx.core.util.Pair;

import com.example.expense_management.Database.myDatabase;
import com.example.expense_management.R;
import com.example.expense_management.detailed_data;
import com.example.expense_management.editHandler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    public static int checkDate(long date, Context context){
        final List<Long>[] dates = new List[]{null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                dates[0] =myDatabase.getDbINSTANCE(context).Dao().getAllDate();;
            }
        }).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myDatabase.DELETE_INSTANCE();
        return Arrays.binarySearch(dates[0].toArray(), date);
    }
    public static Pair<Intent,Boolean> setForAdapterIntent(boolean adapterCheck, String integer, String string, int listPosition,
                               long dateId, String date,Context context){
        Intent forEditIntent;
        boolean adap=false;
        if (adapterCheck){
            forEditIntent=new Intent(context, editHandler.class);
            forEditIntent.putExtra(DATA_ID, dateId);
            forEditIntent.putExtra(UPDATE_MONEY,integer);
            forEditIntent.putExtra(UPDATE_MONEY_DESCRIPTION,string);
            forEditIntent.putExtra(OUR_DATE,date);
            forEditIntent.putExtra(LIST_POSITION,listPosition);
            forEditIntent.putExtra(CHECK,false);
            forEditIntent.putExtra(LOOK,true);
            adap=true;
        }else {
            forEditIntent=new Intent(context,editHandler.class);
            forEditIntent.putExtra(DATA_ID, dateId);
            forEditIntent.putExtra(UPDATE_MONEY,integer);
            forEditIntent.putExtra(UPDATE_MONEY_DESCRIPTION,string);
            forEditIntent.putExtra(OUR_DATE,date);
            forEditIntent.putExtra(CHECK,false);
            forEditIntent.putExtra(LIST_POSITION,listPosition);
            forEditIntent.putExtra(LOOK,false);
        }
        return Pair.create(forEditIntent,adap);
    }
}
