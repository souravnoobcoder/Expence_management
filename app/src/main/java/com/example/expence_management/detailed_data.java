package com.example.expence_management;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.expence_management.Database.myDatabase;

import com.example.expence_management.Database.DataItems;
import com.example.expence_management.Database.DataViewModel;
import com.example.expence_management.RecyclerViewAdapters.detailed_adapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.expence_management.MainActivity.CHECK;
import static com.example.expence_management.MainActivity.DATA_ID;
import static com.example.expence_management.MainActivity.DATE_KEY;
import static com.example.expence_management.MainActivity.makeDate;


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
    public static final String UPDATE_MONEY="OK";
    public static final String UPDATE_MONEY_DESCRIPTION="hello";
    public static final String OUR_DATE="date";
    public static final String LIST_POSITION="position";
    public static final String LOOK="look";
    private DataItems dataItems,items;
    boolean adapterCheck=false;
    private int deletePosition=-1;
    Intent forEditIntent;
    AlertDialog.Builder dialogBuilder,dateDailogBuilder;
    AlertDialog dialog,dateDailog;
    private DataViewModel model;
    private MaterialDatePicker<Long> changeDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        da.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                changeDatePicker.show(getSupportFragmentManager(),null);
                return false;
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
            date=intent.getStringExtra(MainActivity.DETAIL_DATE);
            grossGot=intent.getStringExtra(MainActivity.DETAIL_GROSS_MONEY_GOT);
            grossPaid=intent.getStringExtra(MainActivity.DETAIL_GROSS_MONEY_PAID);
            got=intent.getIntegerArrayListExtra(MainActivity.DETAIL_MONEY_GOT);
            paid=intent.getIntegerArrayListExtra(MainActivity.DETAIL_MONEY_EXPENSE);
            gotDescription=intent.getStringArrayListExtra(MainActivity.DETAIL_MONEY_GOT_PURPOSE);
            paidDescription=intent.getStringArrayListExtra(MainActivity.DETAIL_MONEY_EXPENSE_PURPOSE);

        }
        da.setText(date);
        go.setText("Got:"+grossGot);
        pa.setText("Paid:"+grossPaid);
            forExpenseAdapter =new detailed_adapter(paid,paidDescription,cheek);
            forGainAdapter =new detailed_adapter(got,gotDescription,cheek);

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
                    setForAdapterIntent(true,integer,string,listPosition);
                    makeAlertDailogbBox(integer,string);
                    dialog.show();
                }
            });
            forExpenseAdapter.setOnItemLongClickListener(new detailed_adapter.onItemLongClickListener() {
                @Override
                public void onItemLongClicked( String integer, String string, int listPosition) {
                    setForAdapterIntent(false,integer,string,listPosition);
                    makeAlertDailogbBox(integer,string);
                    dialog.show();
                }
            });

    }
    void initializeLists(){
        paid=new ArrayList<>();
        got=new ArrayList<>();
        paidDescription=new ArrayList<>();
        gotDescription=new ArrayList<>();
    }
    void setForAdapterIntent(boolean adapterCheck,String integer, String string, int listPosition){
        this.deletePosition=listPosition;
        if (adapterCheck){
         forEditIntent=new Intent(detailed_data.this,editHandler.class);
            forEditIntent.putExtra(DATA_ID, dateId);
            forEditIntent.putExtra(UPDATE_MONEY,integer);
            forEditIntent.putExtra(UPDATE_MONEY_DESCRIPTION,string);
            forEditIntent.putExtra(OUR_DATE,date);
            forEditIntent.putExtra(LIST_POSITION,listPosition);
            forEditIntent.putExtra(CHECK,false);
            forEditIntent.putExtra(LOOK,true);
            this.adapterCheck=true;
        }else {
            forEditIntent=new Intent(detailed_data.this,editHandler.class);
            forEditIntent.putExtra(DATA_ID, dateId);
            forEditIntent.putExtra(UPDATE_MONEY,integer);
            forEditIntent.putExtra(UPDATE_MONEY_DESCRIPTION,string);
            forEditIntent.putExtra(OUR_DATE,date);
            forEditIntent.putExtra(CHECK,false);
            forEditIntent.putExtra(LIST_POSITION,listPosition);
            forEditIntent.putExtra(LOOK,false);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(detailed_data.this,MainActivity.class);
        startActivity(intent);
    }
    void makeAlertDailogbBox(String money,String description){
        this.dialogBuilder =new AlertDialog.Builder(detailed_data.this);
        dialogBuilder. setTitle( "Money : "+money+"\n Description : "+ description)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(forEditIntent);
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
                            items.setGrossMoneyGot(editHandler.setGrossMoney(items.getMoneyGot()));
                            items.getMoneyGotPurposes().remove(deletePosition);
                        }else {
                            items.getMoneyExpense().remove(deletePosition);
                            items.setGrossMoneyExpense(editHandler.setGrossMoney(items.getMoneyExpense()));
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