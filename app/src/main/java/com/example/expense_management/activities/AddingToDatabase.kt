package com.example.expense_management.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.example.expense_management.R
import com.example.expense_management.dataClasses.ConstantFuntions.CHECK
import com.example.expense_management.dataClasses.ConstantFuntions.DATE_KEY
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_DATE
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_GROSS_MONEY_GOT
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_GROSS_MONEY_PAID
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_MONEY_EXPENSE
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_MONEY_EXPENSE_PURPOSE
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_MONEY_GOT
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_MONEY_GOT_PURPOSE
import com.example.expense_management.dataClasses.ConstantFuntions.makeDate
import com.example.expense_management.dataClasses.ConstantFuntions.setGrossMoney
import com.example.expense_management.database.DataItems
import com.example.expense_management.database.DataViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class AddingToDatabase : AppCompatActivity() {
    private var amount: TextInputEditText? = null
    private var detail: TextInputEditText? = null
    private var gain: MaterialRadioButton? = null
    private var paid: MaterialRadioButton? = null
    private var model: DataViewModel? = null
    private var moneyExpense: ArrayList<Int>? = null
    private var moneyGot: ArrayList<Int>? = null
    private var mEPurpose: ArrayList<String>? = null
    private var mGPurpose: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.date_input)
        amount = findViewById(R.id.money)
        detail = findViewById(R.id.money_explain)
        val addingMore = findViewById<FloatingActionButton>(R.id.add_more_subData)
        gain = findViewById(R.id.gain)
        paid = findViewById(R.id.paid)
        val bar = findViewById<MaterialToolbar>(R.id.input_toolbar)
        val date = findViewById<TextView>(R.id.date_of_input)
        model = ViewModelProviders.of(this).get(DataViewModel::class.java)
        moneyExpense = ArrayList()
        moneyGot = ArrayList()
        mEPurpose = ArrayList()
        mGPurpose = ArrayList()
        bar.setNavigationOnClickListener {
            val intent = Intent(this@AddingToDatabase, MainActivity::class.java)
            startActivity(intent)
        }
        val intent = intent
        val inputDate = intent.getLongExtra(DATE_KEY, -1)
        val stringDate = makeDate(inputDate)
        date.text = stringDate
        // Menu clickListener
        bar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            val itemId = item.itemId
            if (itemId == R.id.preview) {
                val i = Intent(this@AddingToDatabase, DetailedData::class.java)
                val exp = setGrossMoney(moneyExpense)
                val go = setGrossMoney(moneyGot)
                i.putExtra(DETAIL_DATE, stringDate)
                i.putExtra(DETAIL_GROSS_MONEY_PAID, exp.toString())
                i.putExtra(DETAIL_GROSS_MONEY_GOT, go.toString())
                i.putIntegerArrayListExtra(DETAIL_MONEY_EXPENSE, moneyExpense)
                i.putIntegerArrayListExtra(DETAIL_MONEY_GOT, moneyGot)
                i.putStringArrayListExtra(DETAIL_MONEY_EXPENSE_PURPOSE, mEPurpose)
                i.putStringArrayListExtra(DETAIL_MONEY_GOT_PURPOSE, mGPurpose)
                i.putExtra(CHECK, "no")
                startActivity(i)
                return@OnMenuItemClickListener true
            } else if (itemId == R.id.save_data) {
                setInsert(inputDate, moneyExpense!!, moneyGot!!, mGPurpose!!, mEPurpose!!)
                makeToast("Your data is saved")
                backOnSave(1000)
                makeAllListNull()
                return@OnMenuItemClickListener true
            }
            false
        })
        addingMore.setOnClickListener {
            val amountUsed = amount?.text.toString()
            val amountPurpose = detail?.text.toString()
            when {
                gain?.isChecked == true -> {
                    settingToGain(amountUsed, amountPurpose)
                    makeEditTextNullAgain()
                }
                paid?.isChecked == true -> {
                    settingToExpense(amountUsed, amountPurpose)
                    makeEditTextNullAgain()
                }
                else -> {
                    makeToast("please select Gain or Paid")
                }
            }
        }
    }

    private fun settingToGain(amountGain: String, gainAmountPurpose: String) {
        moneyGot!!.add(amountGain.toInt())
        mGPurpose!!.add(gainAmountPurpose)
    }

    private fun settingToExpense(amountExpand: String, expendAmountPurpose: String) {
        moneyExpense!!.add(amountExpand.toInt())
        mEPurpose!!.add(expendAmountPurpose)
    }

    private fun makeToast(message: String) {
        Toast.makeText(this@AddingToDatabase, message, Toast.LENGTH_SHORT).show()
    }

    private fun makeEditTextNullAgain() {
        amount!!.setText("")
        detail!!.setText("")
    }

    private fun setInsert(
        date: Long,
        moneyExpense: ArrayList<Int>,
        moneyGot: ArrayList<Int>,
        moneyGotPurpose: ArrayList<String>,
        moneyExpensePurpose: ArrayList<String>
    ) {
        val grossExpense = setGrossMoney(moneyExpense)
        val grossGot = setGrossMoney(moneyGot)
        val myData = DataItems(
            date, grossExpense, grossGot,
            moneyExpense, moneyGot, moneyGotPurpose, moneyExpensePurpose
        )
        model!!.insertData(myData)
    }

    private fun makeAllListNull() {
        Handler(Looper.getMainLooper())
            .postDelayed({moneyGot!!.clear()
            moneyExpense!!.clear()
            mEPurpose!!.clear()
            mGPurpose!!.clear()},100)
    }

    override fun onBackPressed() {
        makeIntent()
        finish()
    }

    private fun backOnSave(pauseTime: Int) {
        Handler(Looper.getMainLooper())
            .postDelayed({ makeIntent() }, pauseTime.toLong())
    }

    private fun makeIntent() {
        val intent = Intent(this@AddingToDatabase, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}