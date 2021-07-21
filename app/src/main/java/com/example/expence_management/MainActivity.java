package com.example.expence_management;


import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final String DATE_KEY="selected date";
    private FloatingActionButton addingData;
    private MaterialDatePicker<Long> materialDatePicker,datePickerForSearch;
    private MaterialDatePicker<Pair<Long,Long>> forMultiDates;
    private MaterialToolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.tool_bar);


        addingData=findViewById(R.id.adding_element);
       MaterialDatePicker.Builder<Long> builder= MaterialDatePicker.Builder.datePicker();
      materialDatePicker= builder.setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

      datePickerForSearch=builder.setTitleText("Search by date")
              .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
              .build();
      MaterialDatePicker.Builder<Pair<Long, Long>> multiBuilder=MaterialDatePicker.Builder.dateRangePicker();
      forMultiDates=multiBuilder.setTitleText("Select multi dates")
              .build();
      addingData.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              materialDatePicker.show(getSupportFragmentManager(),"Yes");
          }
      });
     materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
         @Override
         public void onPositiveButtonClick(Long selection) {
             String date= DateFormat.format("dd/MM/yy",new Date(selection)).toString();
             Intent intent=new Intent(MainActivity.this,AddingToDatabase.class);
             intent.putExtra(DATE_KEY,date);
             startActivity(intent);
         }
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
    }

}
