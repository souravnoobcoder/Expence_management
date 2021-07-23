package com.example.expence_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.expence_management.RecyclerViewAdapters.detailed_adapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class detailed_data extends AppCompatActivity {

    FloatingActionButton button;
    RecyclerView gainRecycle,expenseRecycle;
    TextView da,pa,go;
    String date,grossGot,grossPaid,cheek;
    List<String> gotDescription,paidDescription;
    List<Integer> got,paid;
    detailed_adapter forGain,forExpense;

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

        cheek=intent.getStringExtra(MainActivity.CHECK);
        if (cheek.equals("yes")){
           button.setVisibility(View.VISIBLE);
        }
            date=intent.getStringExtra(MainActivity.DETAIL_DATE);
            grossGot=intent.getStringExtra(MainActivity.DETAIL_GROSS_MONEY_GOT);
            grossPaid=intent.getStringExtra(MainActivity.DETAIL_GROSS_MONEY_PAID);
            got=intent.getIntegerArrayListExtra(MainActivity.DETAIL_MONEY_GOT);
            paid=intent.getIntegerArrayListExtra(MainActivity.DETAIL_MONEY_EXPENSE);
            gotDescription=intent.getStringArrayListExtra(MainActivity.DETAIL_MONEY_GOT_PURPOSE);
            paidDescription=intent.getStringArrayListExtra(MainActivity.DETAIL_MONEY_EXPENSE_PURPOSE);

            da.setText(date);
            go.setText("Got:"+grossGot);
            pa.setText("Paid:"+grossPaid);

            forExpense=new detailed_adapter(paid,paidDescription);
            forGain=new detailed_adapter(got,gotDescription);

            gainRecycle.setLayoutManager(new LinearLayoutManager(this));
            expenseRecycle.setLayoutManager(new LinearLayoutManager(this));
            gainRecycle.setAdapter(forGain);
            expenseRecycle.setAdapter(forExpense);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

    }
}