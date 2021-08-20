package com.example.expense_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.expense_management.Database.DataItems;
import com.example.expense_management.Database.DataViewModel;
import com.example.expense_management.Database.myDatabase;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;


import static com.example.expense_management.dataClasses.psfs.CHECK;
import static com.example.expense_management.dataClasses.psfs.DATA_ID;
import static com.example.expense_management.dataClasses.psfs.LIST_POSITION;
import static com.example.expense_management.dataClasses.psfs.LOOK;
import static com.example.expense_management.dataClasses.psfs.OUR_DATE;
import static com.example.expense_management.dataClasses.psfs.UPDATE_MONEY;
import static com.example.expense_management.dataClasses.psfs.UPDATE_MONEY_DESCRIPTION;
import static com.example.expense_management.dataClasses.psfs.setGrossMoney;


public class  editHandler extends AppCompatActivity {
    int position=-1;
    TextView date;
    RadioButton gain, paid;
    TextInputEditText money, description;
    MaterialToolbar toolbar;
    DataViewModel model;
    String upDate,updateMoney,updateMoneyDescription;
    long dateId =-1;
    DataItems d;
    boolean see=false,look=false,aBoolean=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_handler);

        toolbar = findViewById(R.id.update_toolbar);
        date = findViewById(R.id.date_of_update);
        gain = findViewById(R.id.update_gain);
        paid = findViewById(R.id.update_paid);
        money = findViewById(R.id.update_money);
        description = findViewById(R.id.update_money_explain);

        model = ViewModelProviders.of(editHandler.this).get(DataViewModel.class);

        Intent intent = getIntent();
        see=intent.getBooleanExtra(CHECK,false);
        if (!see){
            look=intent.getBooleanExtra(LOOK,false);
            position=intent.getIntExtra(LIST_POSITION,-1);
            upDate = intent.getStringExtra(OUR_DATE);
            dateId = intent.getLongExtra(DATA_ID,-1);
            updateMoney = intent.getStringExtra(UPDATE_MONEY);
             updateMoneyDescription = intent.getStringExtra(UPDATE_MONEY_DESCRIPTION);
             if (look){
                 gain.setChecked(true);
                 paid.setVisibility(View.GONE);
             }
             else{

                paid.setChecked(true);
                gain.setVisibility(View.GONE);
            }

        }else {
            aBoolean=true;
           dateId= intent.getLongExtra(DATA_ID,-1);
           upDate= intent.getStringExtra(OUR_DATE);
        }


        date.setText(upDate);
        money.setText(updateMoney);
        description.setText(updateMoneyDescription);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(editHandler.this,detailed_data.class);
                intent.putExtra(CHECK,"yes");
                intent.putExtra(DATA_ID,dateId);
                startActivity(intent);
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.save_edit_data) {
                    int updatedMoney = Integer.parseInt(money.getText().toString());
                    String updatedDescription = description.getText().toString();
                    if (see){
                        addNew(updatedMoney,updatedDescription);
                        return true;
                    } else if (position==-1)
                        makeToast("Sorry for you effort");
                    else {
                            setNewData(position, updatedMoney, updatedDescription);
                        return true;
                    }
                }
                return false;
            }
        });
        if (see){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    d=  myDatabase.getDbINSTANCE(editHandler.this).Dao().getRoww(dateId);
                }
            }).start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myDatabase.DELETE_INSTANCE();
        } else {
            model.getRow(dateId).observe(editHandler.this, new Observer<DataItems>() {
                @Override
                public void onChanged(DataItems items) {
                    d=items;
                }
            });
        }
        }

    void setNewData(int position, int money,
                            String description) {

        if (gain.isChecked()) {
            d.getMoneyGot().set(position,money);
            d.getMoneyGotPurposes().set(position,description);
            d.setGrossMoneyGot(setGrossMoney(d.getMoneyGot()));
           model.update(d);
           makeToast(money+" "+description+" Updated");
        } else if (paid.isChecked()) {
            d.getMoneyExpense().set(position,money);
            d.getMoneyExpensePurposes().set(position,description);
            d.setGrossMoneyExpense(setGrossMoney(d.getMoneyExpense()));
            model.update(d);
            makeToast(money+" "+description+" Updated");
        } else {
            makeToast(""+position);
        }

    }
    void addNew(int money,String description){
        if (gain.isChecked()){
            d.getMoneyGot().add(money);
            d.getMoneyGotPurposes().add(description);
            d.setGrossMoneyGot(setGrossMoney(d.getMoneyGot()));
            makeToast(money+" "+description+" Added");
            model.update(d);
        }else if (paid.isChecked()){
            d.getMoneyExpense().add(money);
            d.getMoneyExpensePurposes().add(description);
            d.setGrossMoneyExpense(setGrossMoney(d.getMoneyExpense()));
            model.update(d);
            makeToast(money+" "+description+" Added");
        }else makeToast("Select Paid Or gain");
        if (aBoolean)
        makeNullAgain();

    }

    private void makeToast(String message){
        Toast.makeText(editHandler.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(editHandler.this,detailed_data.class);
        intent.putExtra(CHECK,"yes");
        intent.putExtra(DATA_ID,dateId);
        startActivity(intent);
        this.finish();
    }
    void makeNullAgain(){
        money.setText(null);
        description.setText(null);
        gain.setChecked(false);
        paid.setChecked(false);
    }
}