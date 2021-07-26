package com.example.expence_management;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expence_management.Database.DataItems;
import com.example.expence_management.Database.DataViewModel;
import com.example.expence_management.Database.myDatabase;
import com.example.expence_management.RecyclerViewAdapters.mainRecycleAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    public static final String DETAIL_DATE="da";
    public static final String DETAIL_GROSS_MONEY_GOT="wildDog";
    public static final String DETAIL_GROSS_MONEY_PAID="leopard";
    public static final String DETAIL_MONEY_EXPENSE="cheetah";
    public static final String DETAIL_MONEY_GOT="lion";
    public static final String DETAIL_MONEY_EXPENSE_PURPOSE="hyena";
    public static final String DETAIL_MONEY_GOT_PURPOSE="dragonLizard";
    public static final String DATE_KEY="selected date";
    public static final String MY_KEY="key";
    public static final String CHECK="check";
    public static final String DATA_ID="idkd";
    public static final String POSITION="position";
    private MaterialDatePicker<Long> materialDatePicker,datePickerForSearch;
    private MaterialDatePicker<Pair<Long,Long>> forMultiDates;
    private mainRecycleAdapter adapter;
    DataItems items;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.tool_bar);

        RecyclerView mainListRecyclerView = findViewById(R.id.main_list_recycleView);
        mainListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new mainRecycleAdapter();
        mainListRecyclerView.setAdapter(adapter);

        FloatingActionButton addingData = findViewById(R.id.adding_element);
       MaterialDatePicker.Builder<Long> builder= MaterialDatePicker.Builder.datePicker();
      materialDatePicker= builder.setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        DataViewModel viewModel = ViewModelProviders.of(this).get(DataViewModel.class);
    viewModel.getAllDataDescending().observe(this, new Observer<List<DataItems>>() {
        @Override
        public void onChanged(List<DataItems> dataItems) {
            MainActivity.this.setDataItemsList(dataItems);
        }
    });
      datePickerForSearch=builder.setTitleText("Search by date")
              .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
              .build();
      MaterialDatePicker.Builder<Pair<Long, Long>> multiBuilder=MaterialDatePicker.Builder.dateRangePicker();
      forMultiDates=multiBuilder.setTitleText("Select multi dates")
              .build();

      addingData.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              materialDatePicker.show(MainActivity.this.getSupportFragmentManager(), "Yes");
          }
      });
     materialDatePicker.addOnPositiveButtonClickListener(selection -> {
         Intent intent=new Intent(MainActivity.this,AddingToDatabase.class);
         intent.putExtra(DATE_KEY,selection);
         startActivity(intent);
     });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search_by_date:
                        datePickerForSearch.show(getSupportFragmentManager(),"ust");
                        return true;
                    case R.id.search_for_multi_days:
                        forMultiDates.show(getSupportFragmentManager(),"get");
                        return true;
                    default:
                        return false;
                }
            }
        });
        adapter.setOnItemClickListener(new mainRecycleAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(DataItems data) {
                Intent dataIntent=new Intent(MainActivity.this,detailed_data.class);
                dataIntent.putExtra(DATA_ID,data.getDate());
                dataIntent.putExtra(CHECK,"yes");
                MainActivity.this.startActivity(dataIntent);
            }
        });
        datePickerForSearch.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        items= myDatabase.getDbINSTANCE(MainActivity.this).Dao().getRoww(selection);
                    }
                }).start();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myDatabase.DELETE_INSTANCE();
                if (items==null){
                   LayoutInflater layoutInflater=getLayoutInflater();
                   View layout=layoutInflater.inflate(R.layout.cusom_toast,findViewById(R.id.toast_custom));
                   TextView textView=layout.findViewById(R.id.tvtoast);
                   textView.setText(makeDate(selection)+"Not found");
                   textView.setTextColor(Color.rgb(0, 132, 219));
                   textView.setTextSize(40);
                   Toast toast=new Toast(getApplicationContext());
                   toast.setView(layout);
                   toast.setDuration(Toast.LENGTH_LONG);
                   toast.setGravity(Gravity.CENTER,0,0);
                   toast.show();
               }
                else{
                    Intent intent=new Intent(MainActivity.this,detailed_data.class);
                    intent.putExtra(CHECK,"yes");
                    intent.putExtra(DATA_ID,selection);
                    startActivity(intent);
                }
            }
        });
    }

    void setDataItemsList(List<DataItems> dataItems){
        List<DataItems> dataItemsList = new ArrayList<>();
        if (dataItemsList ==null)
            dataItemsList =new ArrayList<>();
        dataItemsList.clear();
        dataItemsList.addAll(dataItems);
        if (dataItemsList !=null)
            adapter.setDataItemsList(dataItems);
        adapter.notifyDataSetChanged();
    }
    String makeDate(long l){
        return DateFormat.format("dd/MM/yy",new Date(l)).toString();
    }
}
