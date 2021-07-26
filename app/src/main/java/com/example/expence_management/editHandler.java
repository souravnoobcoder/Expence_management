package com.example.expence_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.expence_management.Database.DataItems;
import com.example.expence_management.Database.DataViewModel;
import com.example.expence_management.Database.myDatabase;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import static com.example.expence_management.MainActivity.CHECK;
import static com.example.expence_management.MainActivity.DATA_ID;
import static com.example.expence_management.detailed_data.LOOK;
import static com.example.expence_management.detailed_data.OUR_DATE;

public class  editHandler extends AppCompatActivity {
    int position=-1;
    TextView date;
    MaterialRadioButton gain, paid;
    TextInputEditText money, description;
    MaterialToolbar toolbar;
    DataViewModel model;
    String upDate,updateMoney,updateMoneyDescription;
    long dateId =-1;
    DataItems d;
    boolean see=false,look=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            position=intent.getIntExtra(detailed_data.LIST_POSITION,-1);
            upDate = intent.getStringExtra(OUR_DATE);
            dateId = intent.getLongExtra(MainActivity.DATA_ID,-1);
            updateMoney = intent.getStringExtra(detailed_data.UPDATE_MONEY);
             updateMoneyDescription = intent.getStringExtra(detailed_data.UPDATE_MONEY_DESCRIPTION);
             if (look)
                 gain.setChecked(true);
             else
                 paid.setChecked(true);
        }else {
           dateId= intent.getLongExtra(DATA_ID,-1);
           upDate= intent.getStringExtra(OUR_DATE);
        }


        date.setText(upDate);
        money.setText(updateMoney);
        description.setText(updateMoneyDescription);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(editHandler.this,detailed_data.class);
//                startActivity(intent);
//            }
//        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.save_edit_data) {
                    int updatedMoney = Integer.parseInt(money.getText().toString());
                    String updatedDescription = description.getText().toString();
                    if (position==-1)
                        makeToast("Sorry for you effort");
                    if (see)
                        addNew(updatedMoney,updatedDescription);
                        else
                    setNewData(position, updatedMoney, updatedDescription);
                    return true;
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
                Thread.sleep(200);
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
    }

    private int setGrossMoney(List<Integer> money) {
        int i, gross = 0, value;
        if (money==null)
            return 0;
        for (i = 0; i < money.size(); i++) {
            value = money.get(i);
            gross += value;
        }
        return gross;
    }

    private void makeToast(String message){
        Toast.makeText(editHandler.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        finish();

    }
}