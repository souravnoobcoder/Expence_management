package com.example.expense_management.dataClasses

import android.content.Context
import com.example.expense_management.database.myDatabase
import android.content.Intent
import android.text.format.DateFormat
import androidx.core.util.Pair
import com.example.expense_management.EditHandler
import java.util.*

object psfs {
    const val UPDATE_MONEY = "OK"
    const val UPDATE_MONEY_DESCRIPTION = "hello"
    const val OUR_DATE = "date"
    const val LIST_POSITION = "position"
    const val LOOK = "look"
    const val DETAIL_DATE = "da"
    const val DETAIL_GROSS_MONEY_GOT = "wildDog"
    const val DETAIL_GROSS_MONEY_PAID = "leopard"
    const val DETAIL_MONEY_EXPENSE = "cheetah"
    const val DETAIL_MONEY_GOT = "lion"
    const val DETAIL_MONEY_EXPENSE_PURPOSE = "hyena"
    const val DETAIL_MONEY_GOT_PURPOSE = "dragonLizard"
    const val DATE_KEY = "selected date"
    const val CHECK = "check"
    const val DATA_ID = "idkd"
    lateinit var dates: Array<List<Long?>?>
    @JvmStatic
    fun setGrossMoney(money: List<Int>?): Int {
        var i: Int
        var gross = 0
        var value: Int
        if (money == null) return 0
        i = 0
        while (i < money.size) {
            value = money[i]
            gross += value
            i++
        }
        return gross
    }

    @JvmStatic
    fun makeDate(l: Long): String {
        return DateFormat.format("dd/MM/yy", Date(l)).toString()
    }

    @JvmStatic
    fun checkDate(date: Long, context: Context?): Int {

        Thread { dates = arrayOf(myDatabase.getDbINSTANCE(context).Dao().allDate()) }.start()
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        myDatabase.DELETE_INSTANCE()
        return Arrays.binarySearch(dates[0]!!.toTypedArray(), date)
    }

    @JvmStatic
    fun setForAdapterIntent(
        adapterCheck: Boolean, integer: String?, string: String?, listPosition: Int,
        dateId: Long, date: String?, context: Context?
    ): Pair<Intent, Boolean> {
        val forEditIntent: Intent
        var adap = false
        if (adapterCheck) {
            forEditIntent = Intent(context, EditHandler::class.java)
            forEditIntent.putExtra(DATA_ID, dateId)
            forEditIntent.putExtra(UPDATE_MONEY, integer)
            forEditIntent.putExtra(UPDATE_MONEY_DESCRIPTION, string)
            forEditIntent.putExtra(OUR_DATE, date)
            forEditIntent.putExtra(LIST_POSITION, listPosition)
            forEditIntent.putExtra(CHECK, false)
            forEditIntent.putExtra(LOOK, true)
            adap = true
        } else {
            forEditIntent = Intent(context, EditHandler::class.java)
            forEditIntent.putExtra(DATA_ID, dateId)
            forEditIntent.putExtra(UPDATE_MONEY, integer)
            forEditIntent.putExtra(UPDATE_MONEY_DESCRIPTION, string)
            forEditIntent.putExtra(OUR_DATE, date)
            forEditIntent.putExtra(CHECK, false)
            forEditIntent.putExtra(LIST_POSITION, listPosition)
            forEditIntent.putExtra(LOOK, false)
        }
        return Pair.create(forEditIntent, adap)
    }
}