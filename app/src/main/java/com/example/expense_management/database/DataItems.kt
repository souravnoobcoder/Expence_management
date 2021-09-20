package com.example.expense_management.database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
class DataItems {
    @PrimaryKey
    var date: Long = 0
    var grossMoneyExpense = 0
    var grossMoneyGot = 0
    var moneyExpense: ArrayList<Int>? =null
    var moneyGot: ArrayList<Int>? = null
    var moneyGotPurposes: ArrayList<String>? = null
    var moneyExpensePurposes: ArrayList<String>? = null

    constructor() {}

    @Ignore
    constructor(
        date: Long,
        grossMoneyExpense: Int,
        grossMoneyGot: Int,
        moneyExpense: ArrayList<Int>?,
        moneyGot: ArrayList<Int>?,
        moneyGotPurposes: ArrayList<String>?,
        moneyExpensePurposes: ArrayList<String>?
    ) {
        this.date = date
        this.grossMoneyExpense = grossMoneyExpense
        this.grossMoneyGot = grossMoneyGot
        this.moneyExpense = moneyExpense
        this.moneyGot = moneyGot
        this.moneyGotPurposes = moneyGotPurposes
        this.moneyExpensePurposes = moneyExpensePurposes
    }
}