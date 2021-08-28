package com.example.expense_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense_management.Database.DataItems;
import com.example.expense_management.Database.DataViewModel;
import com.example.expense_management.Database.myDatabase;
import com.example.expense_management.RecyclerViewAdapters.detailed_adapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.expense_management.dataClasses.psfs.CHECK;
import static com.example.expense_management.dataClasses.psfs.DATA_ID;
import static com.example.expense_management.dataClasses.psfs.DETAIL_DATE;
import static com.example.expense_management.dataClasses.psfs.DETAIL_GROSS_MONEY_GOT;
import static com.example.expense_management.dataClasses.psfs.DETAIL_GROSS_MONEY_PAID;
import static com.example.expense_management.dataClasses.psfs.DETAIL_MONEY_EXPENSE;
import static com.example.expense_management.dataClasses.psfs.DETAIL_MONEY_EXPENSE_PURPOSE;
import static com.example.expense_management.dataClasses.psfs.DETAIL_MONEY_GOT;
import static com.example.expense_management.dataClasses.psfs.DETAIL_MONEY_GOT_PURPOSE;
import static com.example.expense_management.dataClasses.psfs.OUR_DATE;
import static com.example.expense_management.dataClasses.psfs.makeDate;
import static com.example.expense_management.dataClasses.psfs.setForAdapterIntent;
import static com.example.expense_management.dataClasses.psfs.setGrossMoney;


public class detailed_data extends AppCompatActivity {

    long dateId =-1;
    FloatingActionButton button;
    RecyclerView gainRecycle,expenseRecycle;
    TextView da,pa,go;
    String date,grossGot,grossPaid,cheek;
    List<String> gotDescription,paidDescription;
    List<Integer> got,paid;
    List<Long> dates;
    detailed_adapter forGainAdapter, forExpenseAdapter;
    private DataItems dataItems,items;
    boolean adapterCheck=false,aBoolean=false;
    private int deletePosition=-1;
    Intent forEditIntent;
    AlertDialog.Builder dialogBuilder,dateDailogBuilder;
    AlertDialog dialog,dateDailog;
    private DataViewModel model;
    private MaterialDatePicker<Long> changeDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.detailed_data);

        model=ViewModelProviders.of(this).get(DataViewModel.class);
        button=findViewById(R.id.add_while_watching);
        da=findViewById(R.id.date_of_detail);
        pa=findViewById(R.id.paid_amount);
        go=findViewById(R.id.gain_amount);
        gainRecycle=findViewById(R.id.gain_recyclerView);
        expenseRecycle=findViewById(R.id.expense_recyclerView);
        Intent intent=getIntent();

        MaterialDatePicker.Builder<Long> builder= MaterialDatePicker.Builder.datePicker();
        changeDatePicker= builder.setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        changeDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dates=myDatabase.getDbINSTANCE(detailed_data.this).Dao().getAllDate();
                    }
                }).start();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myDatabase.DELETE_INSTANCE();
                int get= Arrays.binarySearch(dates.toArray(),selection);
                if (get<0){
                    model.updateDate(dateId,selection);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent dataIntent=new Intent(detailed_data.this,detailed_data.class);
                    dataIntent.putExtra(DATA_ID,selection);
                    dataIntent.putExtra(CHECK,"yes");
                    startActivity(dataIntent);
                }else {
                 changeDateAlert(selection);
                }

            }
        });
        cheek=intent.getStringExtra(CHECK);
        if (cheek.equals("yes")){
           button.setVisibility(View.VISIBLE);
            dateId =intent.getLongExtra(DATA_ID,-1);
            System.out.println("yes its"+dateId);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dataItems= myDatabase.getDbINSTANCE(detailed_data.this).Dao().getRoww(dateId);
                }
            }).start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myDatabase.DELETE_INSTANCE();
            initializeLists();
            date=makeDate(dataItems.getDate());
            grossGot=String.valueOf(dataItems.getGrossMoneyGot());
            grossPaid=String.valueOf(dataItems.getGrossMoneyExpense());
            paid.addAll(dataItems.getMoneyExpense());
            got.addAll(dataItems.getMoneyGot());
            paidDescription.addAll(dataItems.getMoneyExpensePurposes());
            gotDescription.addAll(dataItems.getMoneyGotPurposes());

        }else {
            date=intent.getStringExtra(DETAIL_DATE);
            grossGot=intent.getStringExtra(DETAIL_GROSS_MONEY_GOT);
            grossPaid=intent.getStringExtra(DETAIL_GROSS_MONEY_PAID);
            got=intent.getIntegerArrayListExtra(DETAIL_MONEY_GOT);
            paid=intent.getIntegerArrayListExtra(DETAIL_MONEY_EXPENSE);
            gotDescription=intent.getStringArrayListExtra(DETAIL_MONEY_GOT_PURPOSE);
            paidDescription=intent.getStringArrayListExtra(DETAIL_MONEY_EXPENSE_PURPOSE);
            aBoolean=true;
        }
        da.setText(date);
        go.setText("Got : "+grossGot);
        pa.setText("Paid : "+grossPaid);
            forExpenseAdapter =new detailed_adapter(paid,paidDescription,cheek,false);
            forGainAdapter =new detailed_adapter(got,gotDescription,cheek,true);

            gainRecycle.setLayoutManager(new LinearLayoutManager(this));
            expenseRecycle.setLayoutManager(new LinearLayoutManager(this));
            gainRecycle.setAdapter(forGainAdapter);
            expenseRecycle.setAdapter(forExpenseAdapter);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent addNew=new Intent(detailed_data.this,editHandler.class);
                addNew.putExtra(DATA_ID, dateId);
                    addNew.putExtra(OUR_DATE,date);
                    addNew.putExtra(CHECK,true);
                    startActivity(addNew);
                    finish();
                }
            });
            forGainAdapter.setOnItemLongClickListener(new detailed_adapter.onItemLongClickListener() {
                @Override
                public void onItemLongClicked( String integer, String string,int listPosition) {
                    deletePosition=listPosition;
                    Pair<Intent,Boolean> intentBooleanPair;
                 intentBooleanPair=setForAdapterIntent(true,integer,string,listPosition,
                         dateId,date,detailed_data.this);
                    forEditIntent=intentBooleanPair.first;
                    adapterCheck=intentBooleanPair.second;
                    makeAlertDailogbBox(integer,string);
                    dialog.show();
                }
            });
            forExpenseAdapter.setOnItemLongClickListener(new detailed_adapter.onItemLongClickListener() {
                @Override
                public void onItemLongClicked( String integer, String string, int listPosition) {
                    deletePosition=listPosition;
                    Pair<Intent,Boolean> intentBooleanPair;
                   intentBooleanPair= setForAdapterIntent(false,integer,string,
                           listPosition,dateId,date,detailed_data.this);
                   forEditIntent=intentBooleanPair.first;
                   adapterCheck=intentBooleanPair.second;
                    makeAlertDailogbBox(integer,string);
                    dialog.show();
                }
            });
            if (!aBoolean){
                da.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        changeDatePicker.show(getSupportFragmentManager(),null);
                        return false;
                    }
                });
            }
    }
    void initializeLists(){
        paid=new ArrayList<>();
        got=new ArrayList<>();
        paidDescription=new ArrayList<>();
        gotDescription=new ArrayList<>();
    }


    @Override
    public void onBackPressed() {
        if (aBoolean) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(detailed_data.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }

    }
    void makeAlertDailogbBox(String money,String description){
        this.dialogBuilder =new AlertDialog.Builder(detailed_data.this);
        dialogBuilder. setTitle( "Money : "+money+"\n Description : "+ description)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(forEditIntent);
                        finish();
                    }
                }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                items= myDatabase.getDbINSTANCE(detailed_data.this).Dao().getRoww(dateId);
                            }
                        }).start();
                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        myDatabase.DELETE_INSTANCE();
                        if (adapterCheck){
                            items.getMoneyGot().remove(deletePosition);
                            items.setGrossMoneyGot(setGrossMoney(items.getMoneyGot()));
                            items.getMoneyGotPurposes().remove(deletePosition);
                        }else {
                            items.getMoneyExpense().remove(deletePosition);
                            items.setGrossMoneyExpense(setGrossMoney(items.getMoneyExpense()));
                            items.getMoneyExpensePurposes().remove(deletePosition);
                        }
                        model.update(items);
                    }
                }).start();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent dataIntent=new Intent(detailed_data.this,detailed_data.class);
                dataIntent.putExtra(DATA_ID,dateId);
                dataIntent.putExtra(CHECK,"yes");
                startActivity(dataIntent);
                finish();
            }
        });
        dialog=dialogBuilder.create();
    }
    void changeDateAlert(long date){
        this.dateDailogBuilder=new AlertDialog.Builder(detailed_data.this);
        dateDailogBuilder.setTitle(makeDate(date)+" Already exists ")
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeDatePicker.show(getSupportFragmentManager(),null);
                    }
                });
        dateDailog=dateDailogBuilder.create();
        dateDailog.show();
    }
}