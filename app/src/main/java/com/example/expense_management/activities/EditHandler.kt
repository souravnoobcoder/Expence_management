package com.example.expense_management.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.example.expense_management.R
import com.example.expense_management.dataClasses.ConstantFuntions.CHECK
import com.example.expense_management.dataClasses.ConstantFuntions.DATA_ID
import com.example.expense_management.dataClasses.ConstantFuntions.LIST_POSITION
import com.example.expense_management.dataClasses.ConstantFuntions.LOOK
import com.example.expense_management.dataClasses.ConstantFuntions.OUR_DATE
import com.example.expense_management.dataClasses.ConstantFuntions.UPDATE_MONEY
import com.example.expense_management.dataClasses.ConstantFuntions.UPDATE_MONEY_DESCRIPTION
import com.example.expense_management.dataClasses.ConstantFuntions.setGrossMoney
import com.example.expense_management.database.DataItems
import com.example.expense_management.database.DataViewModel
import com.example.expense_management.database.ExpenseDatabase
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class EditHandler : AppCompatActivity() {
    private var position = -1
    private var date: TextView? = null
    private var gain: MaterialRadioButton? = null
    private var paid: MaterialRadioButton? = null
    private var money: TextInputEditText? = null
    private var description: TextInputEditText? = null
    private var toolbar: MaterialToolbar? = null
    private var model: DataViewModel? = null
    private var upDate: String? = null
    private var updateMoney: String? = null
    private var updateMoneyDescription: String? = null
    private var dateId: Long = -1
    private var d: DataItems? = null
    private var see = false
    private var look = false
    private var aBoolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_handler)
        toolbar = findViewById(R.id.update_toolbar)
        date = findViewById(R.id.date_of_update)
        gain = findViewById(R.id.update_gain)
        paid = findViewById(R.id.update_paid)
        money = findViewById(R.id.update_money)
        description = findViewById(R.id.update_money_explain)
        model = ViewModelProviders.of(this@EditHandler).get(DataViewModel::class.java)
        val intent = intent
        see = intent.getBooleanExtra(CHECK, false)
        if (!see) {
            look = intent.getBooleanExtra(LOOK, false)
            position = intent.getIntExtra(LIST_POSITION, -1)
            upDate = intent.getStringExtra(OUR_DATE)
            dateId = intent.getLongExtra(DATA_ID, -1)
            updateMoney = intent.getStringExtra(UPDATE_MONEY)
            updateMoneyDescription = intent.getStringExtra(UPDATE_MONEY_DESCRIPTION)
            if (look) {
                gain?.isChecked = true
                paid?.visibility = View.GONE
            } else {
                paid?.isChecked = true
                gain?.visibility=View.GONE

            }
        } else {
            aBoolean = true
            dateId = intent.getLongExtra(DATA_ID, -1)
            upDate = intent.getStringExtra(OUR_DATE)
        }
        date?.text = upDate
        money?.setText(updateMoney)
        description?.setText(updateMoneyDescription)
        toolbar?.setNavigationOnClickListener {
            val intent = Intent(this@EditHandler, DetailedData::class.java)
            intent.putExtra(CHECK, "yes")
            intent.putExtra(DATA_ID, dateId)
            startActivity(intent)
            finish()
        }
        toolbar?.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.save_edit_data) {
                val updatedMoney = money?.text.toString().toInt()
                val updatedDescription = description?.text.toString()
                when {
                    see -> {
                        addNew(updatedMoney, updatedDescription)
                        return@OnMenuItemClickListener true
                    }
                    position == -1 -> makeToast("Sorry for you effort")
                    else -> {
                        setNewData(position, updatedMoney, updatedDescription)
                        return@OnMenuItemClickListener true
                    }
                }
            }
            false
        })
        if (see) {
            CoroutineScope(IO).launch { d=ExpenseDatabase.getDbINSTANCE(this@EditHandler)
                ?.dao()?.getRoww(dateId) }

        } else {
            model!!.getRow(dateId)?.observe(this@EditHandler, { items -> d = items })
        }
    }

    private fun setNewData(
         position: Int, money: Int,
         description: String
    ) {
        when {
            gain!!.isChecked -> {
                d?.moneyGot?.set(position,money)
                d?.moneyGotPurposes?.set(position,description)
                d!!.grossMoneyGot = setGrossMoney(d!!.moneyGot)
                model!!.update(d)
                makeToast("$money $description Updated")
            }
            paid!!.isChecked -> {
                d?.moneyExpense?.set(position,money)
                d?.moneyExpensePurposes?.set(position,description)
                d!!.grossMoneyExpense = setGrossMoney(d!!.moneyExpense)
                model!!.update(d)
                makeToast("$money $description Updated")
            }
            else -> {
                makeToast("" + position)
            }
        }
    }

    private fun addNew(money: Int, description: String) {
        when {
            gain!!.isChecked -> {
                d?.moneyGot?.add(money)
                d?.moneyGotPurposes?.add(description)
                d?.grossMoneyGot = setGrossMoney(d!!.moneyGot)
                makeToast("$money $description Added")
                model!!.update(d)
            }
            paid!!.isChecked -> {
                d!!.moneyExpense?.add(money)
                d!!.moneyExpensePurposes?.add(description)
                d!!.grossMoneyExpense = setGrossMoney(d!!.moneyExpense)
                model!!.update(d)
                makeToast("$money $description Added")
            }
            else -> makeToast("Select Paid Or gain")
        }
        if (aBoolean) makeNullAgain()
    }

    private fun makeToast(message: String) {
        Toast.makeText(this@EditHandler, message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        val intent = Intent(this@EditHandler, DetailedData::class.java)
        intent.putExtra(CHECK, "yes")
        intent.putExtra(DATA_ID, dateId)
        startActivity(intent)
        finish()
    }

    private fun makeNullAgain() {
        money?.text=null
        description?.text=null
    }
}