package com.example.expence_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.expence_management.Database.DataViewModel;
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

        model = ViewModelProviders.of(this).get(DataViewModel.class);

        Intent intent = getIntent();
        String positionString=intent.getStringExtra(detailed_data.LIST_POSITION);
        int position = Integer.parseInt(positionString);
        upDate = intent.getStringExtra(detailed_data.OUR_DATE);
        String updateMoney = intent.getStringExtra(detailed_data.UPDATE_MONEY);
        String updateMoneyDescription = intent.getStringExtra(detailed_data.UPDATE_MONEY_DESCRIPTION);
        List<Integer> updateMoneyList = intent.getIntegerArrayListExtra(detailed_data.UPDATE_MONEY_LIST);
        List<String> updateMoneyDescriptionList = intent.getStringArrayListExtra(detailed_data.UPDATE_MONEY_DESCRIPTION_LIST);

        date.setText(upDate);
        money.setText(updateMoney);
        description.setText(updateMoneyDescription);

        Integer updatedMoney = Integer.parseInt(money.getText().toString());
        String updatedDescription = description.getText().toString();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.save_edit_data) {
                    setNewData(position, updatedMoney, updatedDescription, updateMoneyList, updateMoneyDescriptionList);
                    return true;
                }
                return false;
            }
        });
    }

    private void setNewData(int position, Integer money,
                            String description, List<Integer> moneyList, List<String> descriptionList) {
        List<Integer> mList=new ArrayList<>();
        List<String> dList=new ArrayList<>();
        mList.clear();
        dList.clear();
        mList.addAll(moneyList);
        dList.addAll(descriptionList);
        mList.set(position, money);
        dList.set(position, description);
        int grossMoney = setGrossMoney(moneyList);
        if (gain.isChecked()) {
            model.updateMoneyGotList(upDate, moneyList, grossMoney);
            model.updateMoneyGotDescriptionList(upDate, descriptionList);
        } else if (paid.isChecked()) {
            model.updateMoneyExpenseList(upDate, moneyList, grossMoney);
            model.updateMoneyExpenseDescriptionList(upDate, descriptionList);
        } else {
            Toast.makeText(editHandler.this, "Select Paid or Gain", Toast.LENGTH_LONG).show();
        }
    }

    private int setGrossMoney(List<Integer> money) {
        int i, gross = 0, value;
        if (money.isEmpty())
            return 0;
        for (i = 0; i < money.size(); i++) {
            value = Integer.parseInt(money.get(i).toString());
            gross += value;
        }
        return gross;
    }
}