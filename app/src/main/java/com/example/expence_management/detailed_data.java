package com.example.expence_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.expence_management.MainActivity.CHECK;
import static com.example.expence_management.MainActivity.DATA_ID;


public class detailed_data extends AppCompatActivity {

    MaterialToolbar detailedViewToolbar;
    long dateId =-1;
    FloatingActionButton button;
    RecyclerView gainRecycle,expenseRecycle;
    TextView da,pa,go;
    String date,grossGot,grossPaid,cheek;
    List<String> gotDescription,paidDescription;
    List<Integer> got,paid;
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
    private DataViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_data);

        model=ViewModelProviders.of(this).get(DataViewModel.class);
        detailedViewToolbar=findViewById(R.id.detailed_toolBar);
        button=findViewById(R.id.add_while_watching);
        da=findViewById(R.id.date_of_detail);
        pa=findViewById(R.id.paid_amount);
        go=findViewById(R.id.gain_amount);
        gainRecycle=findViewById(R.id.gain_recyclerView);
        expenseRecycle=findViewById(R.id.expense_recyclerView);
        Intent intent=getIntent();


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
                    if (detailedViewToolbar.getVisibility()==View.GONE)
                        detailedViewToolbar.setVisibility(View.VISIBLE);
                    else detailedViewToolbar.setVisibility(View.GONE);
                }
            });
            forExpenseAdapter.setOnItemLongClickListener(new detailed_adapter.onItemLongClickListener() {
                @Override
                public void onItemLongClicked( String integer, String string, int listPosition) {
                    setForAdapterIntent(false,integer,string,listPosition);
                    if (detailedViewToolbar.getVisibility()==View.GONE)
                    detailedViewToolbar.setVisibility(View.VISIBLE);
                    else detailedViewToolbar.setVisibility(View.GONE);
                }
            });
            detailedViewToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId()==R.id.edit_detailed){
                            startActivity(forEditIntent);
                        return true;
                    }else if (item.getItemId()==R.id.delete_detailed){

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
                                    Thread.sleep(200);
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
                        Intent dataIntent=new Intent(detailed_data.this,detailed_data.class);
                        dataIntent.putExtra(DATA_ID,dateId);
                        dataIntent.putExtra(CHECK,"yes");
                        startActivity(dataIntent);
                        return true;
                    }
                    return false;
                }
            });
    }
    String makeDate(long l){
        return DateFormat.format("dd/MM/yy",new Date(l)).toString();
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
}