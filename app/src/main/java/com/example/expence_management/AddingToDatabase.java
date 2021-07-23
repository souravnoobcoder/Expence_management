package com.example.expence_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import static com.example.expence_management.MainActivity.CHECK;
import static com.example.expence_management.MainActivity.DETAIL_GROSS_MONEY_PAID;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                        Intent i=new Intent(AddingToDatabase.this,detailed_data.class);
                        int exp=setGrossMoney(moneyExpense);
                        int go=setGrossMoney(moneyGot);
                        i.putExtra(MainActivity.DETAIL_DATE,inputDate);
                        i.putExtra(DETAIL_GROSS_MONEY_PAID,String.valueOf(exp));
                        i.putExtra(MainActivity.DETAIL_GROSS_MONEY_GOT,String.valueOf(go));
                        i.putIntegerArrayListExtra(MainActivity.DETAIL_MONEY_EXPENSE, (ArrayList<Integer>) moneyExpense);
                        i.putIntegerArrayListExtra(MainActivity.DETAIL_MONEY_GOT, (ArrayList<Integer>) moneyGot);
                        i.putStringArrayListExtra(MainActivity.DETAIL_MONEY_EXPENSE_PURPOSE, (ArrayList<String>) mEPurpose);
                        i.putStringArrayListExtra(MainActivity.DETAIL_MONEY_GOT_PURPOSE, (ArrayList<String>) mGPurpose);
                        i.putExtra(CHECK,"no");
                        startActivity(i);
                        return true;
                    case R.id.save_data:
                        setInsert(inputDate,moneyExpense,moneyGot,mGPurpose,mEPurpose);
                        makeToast("Your data is saved");
                        makeAllListNull();
                        return true;
                    default:
                        return false;
                }
            }
        });
     addingMore.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String amountUsed=amount.getText().toString();
             String amountPurpose=detail.getText().toString();
             if (gain.isChecked()){
                    settingToGain(amountUsed,amountPurpose);
             }else if (paid.isChecked()){
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
        gain.setChecked(false);
        paid.setChecked(true);
    }
    private int setGrossMoney(List<Integer> money){
        int i,gross=0,value;
        if (money.isEmpty())
            return 0;
       for (i=0;i<money.size();i++){
           value =Integer.parseInt(money.get(i).toString());
           gross+=value;
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
   private void makeAllListNull(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                moneyGot.clear();
                moneyExpense.clear();
                mEPurpose.clear();
                mGPurpose.clear();
            }
        }).start();

      }
}
