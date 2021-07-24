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
import com.example.expence_management.RecyclerViewAdapters.mainRecycleAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class editHandler extends AppCompatActivity {

    TextView date;
    MaterialRadioButton gain, paid;
    TextInputEditText money, description;
    MaterialToolbar toolbar;
    DataViewModel model;
    String upDate;
    int id=-1,mainPosition=-1;
    DataItems d;
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
       int position=intent.getIntExtra(detailed_data.LIST_POSITION,-1);
        upDate = intent.getStringExtra(detailed_data.OUR_DATE);
        id= intent.getIntExtra(MainActivity.DATA_ID,-1);
       // mainPosition=intent.getIntExtra(MainActivity.POSITION,-1);
        String updateMoney = intent.getStringExtra(detailed_data.UPDATE_MONEY);
        String updateMoneyDescription = intent.getStringExtra(detailed_data.UPDATE_MONEY_DESCRIPTION);
        List<Integer> updateMoneyList = intent.getIntegerArrayListExtra(detailed_data.UPDATE_MONEY_LIST);
        List<String> updateMoneyDescriptionList = intent.getStringArrayListExtra(detailed_data.UPDATE_MONEY_DESCRIPTION_LIST);

        date.setText(upDate);
        money.setText(updateMoney);
        description.setText(updateMoneyDescription);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.save_edit_data) {
                    int updatedMoney = Integer.parseInt(money.getText().toString());
                    String updatedDescription = description.getText().toString();
                    if (position==-1)
                        makeToast("Sorry for you effort");
                    setNewData(position, updatedMoney, updatedDescription);
                    return true;
                }
                return false;
            }
        });
        model.getRow(id).observe(this, new Observer<DataItems>() {
            @Override
            public void onChanged(DataItems items) {
                d=items;
            }

        });
    }


    void setNewData(int position, int money,
                            String description) {
        int pos=position;
        int mon=money;
        String des=description;
        List<Integer> mList=new ArrayList<>();
        List<String> dList=new ArrayList<>();
        mList.clear();
        dList.clear();
        if (gain.isChecked()) {
            mList.addAll(d.getMoneyGot());
            dList.addAll(d.getMoneyGotPurposes());
            mList.set(pos, money);
            dList.set(pos, description);
            int grossMoney = setGrossMoney(mList);
           d.setMoneyGot(mList);
           d.setMoneyGotPurposes(dList);
           d.setGrossMoneyGot(grossMoney);
           model.update(d);
           makeToast(mList.toString());
        } else if (paid.isChecked()) {
            mList.addAll(d.getMoneyExpense());
            dList.addAll(d.getMoneyExpensePurposes());
            mList.set(pos, money);
            dList.set(pos, description);
            int grossMoney = setGrossMoney(mList);
            d.setMoneyExpense(mList);
            d.setMoneyExpensePurposes(dList);
            d.setGrossMoneyExpense(grossMoney);
            model.update(d);
            makeToast(""+pos);
        } else {
            makeToast(pos+" Position");
        }
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

    public void onclickDate(View view) {
    }
    private void makeToast(String message){
        Toast.makeText(editHandler.this, message, Toast.LENGTH_LONG).show();
    }
}