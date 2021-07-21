package com.example.expence_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.expence_management.Database.DataItems;
import com.example.expence_management.Database.DataViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


public class AddingToDatabase extends AppCompatActivity {
    private TextInputEditText amount,detail;
    private MaterialToolbar bar;
    private TextView date;
    private RadioButton gain,paid;
    private FloatingActionButton addingMore;
    private DataViewModel model;
    private List<Integer> moneyExpense,moneyGot;
    private List<String> mEPurpose,mGPurpose;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_input);

        amount=findViewById(R.id.money);
        detail=findViewById(R.id.money_explain);
        addingMore=findViewById(R.id.add_more_subData);
        gain=findViewById(R.id.gain);
        paid=findViewById(R.id.paid);
        bar=findViewById(R.id.input_toolbar);
        date=findViewById(R.id.date_of_input);

        model= ViewModelProviders.of(this).get(DataViewModel.class);
        moneyExpense= new ArrayList<>();
        moneyGot=new ArrayList<>();
        mEPurpose=new ArrayList<>();
        mGPurpose=new ArrayList<>();
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddingToDatabase.this,MainActivity.class);
                startActivity(intent);
            }
        });
        Intent intent=getIntent();
        String inputDate=intent.getStringExtra(MainActivity.DATE_KEY);
        date.setText(inputDate);
        // Menu clickListener
        bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.preview:

                        return true;
                    case R.id.save_data:
                        setInsert(inputDate,moneyExpense,moneyGot,mGPurpose,mEPurpose);
                        return true;
                    default:
                        return false;
                }
            }
        });

     String amountUsed=amount.getText().toString();
     String amountPurpose=detail.getText().toString();

     addingMore.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if (gain.isSelected()){
                    settingToGain(amountUsed,amountPurpose);
             }else if (paid.isSelected()){
                        settingToExpense(amountUsed,amountPurpose);
             }else{
                    makeToast("please select Gain or Paid");
             }
             makeEditTextNullAgain();
         }
     });
    }
    private void settingToGain(String amountGain,String gainAmountPurpose){
        moneyGot.add(Integer.parseInt(amountGain));
        mGPurpose.add(gainAmountPurpose);
    }
    private void settingToExpense(String amountExpand,String expendAmountPurpose){
        moneyExpense.add(Integer.parseInt(amountExpand));
        mEPurpose.add(expendAmountPurpose);
    }
    private void makeToast(String message){
        Toast.makeText(AddingToDatabase.this, message, Toast.LENGTH_SHORT).show();
    }
    private void makeEditTextNullAgain(){
        amount.setText(null);
        detail.setText(null);
        gain.setSelected(false);
        paid.setSelected(false);
    }
    private int setGrossMoney(List<Integer> money){
        int i=0,gross=0;
        if (money.isEmpty())
            return 0;
        while (!money.isEmpty()){
            gross+=money.get(i);
            i++;
        }
        return gross;
    }
    private void setInsert(String date,List<Integer> moneyExpense, List<Integer> moneyGot
    ,List<String> moneyGotPurpose,List<String> moneyExpensePurpose){
       int grossExpense= setGrossMoney(moneyExpense);
       int grossGot=setGrossMoney(moneyGot);
        DataItems myData=new DataItems(date,grossExpense,grossGot,
                moneyExpense,moneyGot,moneyGotPurpose,moneyExpensePurpose);
       model.insertData(myData);
    }
}
