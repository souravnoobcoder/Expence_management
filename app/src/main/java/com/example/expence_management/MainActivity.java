package com.example.expence_management;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addingData;
    private MaterialDatePicker materialDatePicker;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addingData=findViewById(R.id.adding_element);
       MaterialDatePicker.Builder builder= MaterialDatePicker.Builder.datePicker();
      materialDatePicker= builder.setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

      addingData.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              materialDatePicker.show(getSupportFragmentManager(),"Yes");
          }
      });
      materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
          @Override
          public void onPositiveButtonClick(Object selection) {
              Toast.makeText(MainActivity.this,"date is "+selection.toString(),Toast.LENGTH_LONG).show();
          }
      });
    }

}
