package com.example.expence_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.expence_management.Database.myDatabase;

import com.example.expence_management.Database.DataItems;
import com.example.expence_management.Database.DataViewModel;
import com.example.expence_management.RecyclerViewAdapters.detailed_adapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.expence_management.MainActivity.CHECK;


public class detailed_data extends AppCompatActivity {

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
    private DataItems dataItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_data);

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
            dateId =intent.getLongExtra(MainActivity.DATA_ID,-1);
            System.out.println("yes its"+dateId);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dataItems= myDatabase.getDbINSTANCE(detailed_data.this).Dao().getRoww(dateId);
                }
            }).start();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myDatabase.DELETE_INSTANCE();
//            DataViewModel model= ViewModelProviders.of(detailed_data.this).get(DataViewModel.class);
//            model.getRow(dateId).observe(detailed_data.this, new Observer<DataItems>() {
//                @Override
//                public void onChanged(DataItems items) {
////                    da.setText(makeDate(items.getDate()));
////                    go.setText("Got "+items.getGrossMoneyGot());
////                    pa.setText("Pain "+items.getGrossMoneyExpense());
////                    forExpenseAdapter=new detailed_adapter(items.getMoneyExpense(),items.getMoneyExpensePurposes(),cheek);
//                   // forGainAdapter=new detailed_adapter(items.getMoneyGot(),items.getMoneyGotPurposes(),cheek);
//                }
//            });
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
                addNew.putExtra(MainActivity.DATA_ID, dateId);
                    addNew.putExtra(OUR_DATE,date);
                    addNew.putExtra(CHECK,true);
                    startActivity(addNew);
                }
            });
            forGainAdapter.setOnItemLongClickListener(new detailed_adapter.onItemLongClickListener() {
                @Override
                public void onItemLongClicked( String integer, String string,int listPosition) {
                    Intent forEditIntent=new Intent(detailed_data.this,editHandler.class);
                    forEditIntent.putExtra(MainActivity.DATA_ID, dateId);
                    forEditIntent.putExtra(UPDATE_MONEY,integer);
                    forEditIntent.putExtra(UPDATE_MONEY_DESCRIPTION,string);
                    forEditIntent.putExtra(OUR_DATE,date);
                    forEditIntent.putExtra(LIST_POSITION,listPosition);
                    forEditIntent.putExtra(CHECK,false);
                   detailed_data.this.startActivity(forEditIntent);
                }
            });
            forExpenseAdapter.setOnItemLongClickListener(new detailed_adapter.onItemLongClickListener() {
                @Override
                public void onItemLongClicked( String integer, String string, int listPosition) {
                    Intent forEditIntent=new Intent(detailed_data.this,editHandler.class);
                    forEditIntent.putExtra(MainActivity.DATA_ID, dateId);
                    forEditIntent.putExtra(UPDATE_MONEY,integer);
                    forEditIntent.putExtra(UPDATE_MONEY_DESCRIPTION,string);
                    forEditIntent.putExtra(OUR_DATE,date);
                    forEditIntent.putExtra(LIST_POSITION,listPosition);
                    detailed_data.this.startActivity(forEditIntent);
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
}