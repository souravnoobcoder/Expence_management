package com.example.expense_management;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense_management.Database.DataItems;
import com.example.expense_management.Database.DataViewModel;
import com.example.expense_management.Database.myDatabase;
import com.example.expense_management.RecyclerViewAdapters.mainRecycleAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Long> dates;
    public static final String DETAIL_DATE = "da";
    public static final String DETAIL_GROSS_MONEY_GOT = "wildDog";
    public static final String DETAIL_GROSS_MONEY_PAID = "leopard";
    public static final String DETAIL_MONEY_EXPENSE = "cheetah";
    public static final String DETAIL_MONEY_GOT = "lion";
    public static final String DETAIL_MONEY_EXPENSE_PURPOSE = "hyena";
    public static final String DETAIL_MONEY_GOT_PURPOSE = "dragonLizard";
    public static final String DATE_KEY = "selected date";
    public static final String CHECK = "check";
    public static final String DATA_ID = "idkd";
    AlertDialog.Builder dialogBuilder, deleteDialogBuilder, sortAlertBuilder;
    AlertDialog dialog, deleteDialog, sortAlert;
    private mainRecycleAdapter adapter;
    DataItems items;
    boolean doubleBackToExitPressedOnce = false;
    private MaterialDatePicker<Long> materialDatePicker, datePickerForSearch;
    private MaterialDatePicker<Pair<Long, Long>> forMultiDates;
    DataViewModel viewModel;

    public static String makeDate(long l) {
        return DateFormat.format("dd/MM/yy", new Date(l)).toString();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.tool_bar);

        RecyclerView mainListRecyclerView = findViewById(R.id.main_list_recycleView);
        mainListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new mainRecycleAdapter();
        mainListRecyclerView.setAdapter(adapter);

        FloatingActionButton addingData = findViewById(R.id.adding_element);
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        materialDatePicker = builder.setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        viewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        viewModel.getAllDataDescending().observe(this, MainActivity.this::setDataItemsList);
        datePickerForSearch = builder.setTitleText("Search by date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        MaterialDatePicker.Builder<Pair<Long, Long>> multiBuilder = MaterialDatePicker.Builder.dateRangePicker();
        forMultiDates = multiBuilder.setTitleText("Select multi dates")
                .build();
        addingData.setOnClickListener(v -> materialDatePicker.show(MainActivity.this.getSupportFragmentManager(), "Yes"));
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {

            new Thread(() -> dates = myDatabase.getDbINSTANCE(MainActivity.this).Dao().getAllDate()).start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myDatabase.DELETE_INSTANCE();
            int get = Arrays.binarySearch(dates.toArray(), selection);
            if (get < 0) {
                Intent intent = new Intent(MainActivity.this, AddingToDatabase.class);
                intent.putExtra(DATE_KEY, selection);
                startActivity(intent);
            } else {
                makeAlertDailogbBox(selection);
                dialog.show();
            }

        });
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.search_by_date) {
                datePickerForSearch.show(getSupportFragmentManager(), "ust");
                return true;
            } else if (itemId == R.id.search_for_multi_days) {
                forMultiDates.show(getSupportFragmentManager(), "get");
                return true;
            } else if (itemId == R.id.sort) {
                alertSort();
                return true;
            } else if (itemId == R.id.get_backup) {
               makeToast("Get backup feature not available yet");
                return true;
            } else if (itemId == R.id.set_backup) {
                makeToast("Set backup feature not available yet");
                return true;
            }
            return false;
        });
        adapter.setOnItemLongClickListener(this::alertForDelete);
        adapter.setOnItemClickListener(data -> {
            Intent dataIntent = new Intent(MainActivity.this, detailed_data.class);
            dataIntent.putExtra(DATA_ID, data.getDate());
            dataIntent.putExtra(CHECK, "yes");
            MainActivity.this.startActivity(dataIntent);
            finish();
        });
        datePickerForSearch.addOnPositiveButtonClickListener(selection -> {
            new Thread(() -> items = myDatabase.getDbINSTANCE(MainActivity.this).Dao().getRoww(selection)).start();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myDatabase.DELETE_INSTANCE();
            if (items == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                View layout = layoutInflater.inflate(R.layout.cusom_toast, findViewById(R.id.toast_custom));
                TextView textView = layout.findViewById(R.id.tvtoast);
                textView.setText(new StringBuilder().append(makeDate(selection)).append(" Not found").toString());
                textView.setTextColor(Color.rgb(0, 132, 219));
                textView.setTextSize(20);
                Toast toast = new Toast(getApplicationContext());
                toast.setView(layout);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent intent = new Intent(MainActivity.this, detailed_data.class);
                intent.putExtra(CHECK, "yes");
                intent.putExtra(DATA_ID, selection);
                startActivity(intent);
                finish();
            }
        });
    }

    void setDataItemsList(List<DataItems> dataItems) {
        List<DataItems> dataItemsList = new ArrayList<>();
        if (dataItemsList == null)
            dataItemsList = new ArrayList<>();
        dataItemsList.clear();
        dataItemsList.addAll(dataItems);
        if (dataItemsList != null)
            adapter.setDataItemsList(dataItems);
        adapter.notifyDataSetChanged();
    }

    void makeAlertDailogbBox(long longDate) {
        this.dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("Already in Database")
                .setPositiveButton("Update", (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, detailed_data.class);
                    intent.putExtra(CHECK, "yes");
                    intent.putExtra(DATA_ID, longDate);
                    startActivity(intent);
                    finish();
                }).setNegativeButton("Replace", (dialog, which) -> {
            Intent intent = new Intent(MainActivity.this, AddingToDatabase.class);
            intent.putExtra(DATE_KEY, longDate);
            startActivity(intent);
            finish();
        });
        dialog = dialogBuilder.create();
    }

    void alertForDelete(DataItems dataItem) {
        this.deleteDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        deleteDialogBuilder.setTitle("Do you really want to delete ")
                .setMessage("\n" + makeDate(dataItem.getDate()) + " date data")
                .setPositiveButton("Yes", (dialog, which) -> {
                    viewModel.deleteData(dataItem);
                    finish();
                    startActivity(getIntent());
                });
        deleteDialog = deleteDialogBuilder.create();
        deleteDialog.show();
    }

    void alertSort() {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sort_selection, null);
        RadioButton date = dialogView.findViewById(R.id.by_date);
        RadioButton got = dialogView.findViewById(R.id.by_gainMoney);
        RadioButton expense = dialogView.findViewById(R.id.by_expense);
        RadioButton ascending = dialogView.findViewById(R.id.ascending);
        RadioButton descending = dialogView.findViewById(R.id.descending);
        this.sortAlertBuilder = new AlertDialog.Builder(MainActivity.this);
        sortAlertBuilder.setView(dialogView)
                .setTitle("Sorting")
                .setPositiveButton("Ok", (dialog, which) -> {
                    if (date.isChecked() && ascending.isChecked())
                        dateA();
                    else if (date.isChecked() && descending.isChecked())
                        dateD();
                    else if (got.isChecked() && ascending.isChecked())
                        gotA();
                    else if (got.isChecked() && descending.isChecked())
                        gotD();
                    else if (expense.isChecked() && ascending.isChecked())
                        expenseA();
                    else expenseD();
                });
        sortAlert = sortAlertBuilder.create();
        sortAlert.show();
    }

    void gotA() {
        viewModel.getAllGotDataAscending().observe(MainActivity.this, this::setDataItemsList);
    }

    void expenseA() {
        viewModel.getAllExpenseDataAscending().observe(MainActivity.this, this::setDataItemsList);
    }

    void expenseD() {
        viewModel.getAllExpenseDataDescending().observe(MainActivity.this, this::setDataItemsList);
    }

    void gotD() {
        viewModel.getAllGotDataDescending().observe(MainActivity.this, this::setDataItemsList);
    }

    void dateA() {
        viewModel.getAllDataAscending().observe(MainActivity.this, this::setDataItemsList);
    }

    void dateD() {
        viewModel.getAllDataDescending().observe(MainActivity.this, this::setDataItemsList);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            boolean setAppToBackground = moveTaskToBack(true);
            if (!setAppToBackground) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                this.startActivity(intent);
            }
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
void makeToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
}

}