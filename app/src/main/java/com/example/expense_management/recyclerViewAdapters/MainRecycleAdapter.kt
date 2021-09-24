package com.example.expense_management.recyclerViewAdapters

import android.text.format.DateFormat
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_management.database.DataItems
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.expense_management.R
import android.widget.TextView
import android.view.animation.ScaleAnimation
import android.view.animation.Animation
import java.util.*

class MainRecycleAdapter : RecyclerView.Adapter<MainRecycleAdapter.ViewHolder>() {
    var dataItemsLt: MutableList<DataItems>? = null
    private var listener: OnItemClickListener? = null
    private var longListener: OnItemLongClickListener? = null
    fun setDataItemsList(dataItemsList: List<DataItems>) {
        this.dataItemsLt = ArrayList()
        if (dataItemsList.isEmpty()) {
            this.dataItemsLt = ArrayList() //setting dataItemsList
        }
        (this.dataItemsLt as ArrayList<DataItems>).clear()
        (this.dataItemsLt as ArrayList<DataItems>).addAll(dataItemsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = holder.date
        val stringDate = DateFormat.format(
            "dd/MM/yy", Date(
                dataItemsLt!![position].date
            )
        ).toString()
        date.text = stringDate
        val gainMoney = holder.gain
        gainMoney.text = "" + dataItemsLt!![position].grossMoneyGot
        val paidMoney = holder.paid
        paidMoney.text = String.format("%d", dataItemsLt!![position].grossMoneyExpense)
        setFadeAnimation(holder.itemView)
    }

    override fun getItemCount(): Int {
        return if (dataItemsLt != null) dataItemsLt!!.size else 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.date_of_data)
        var gain: TextView = itemView.findViewById(R.id.gain_data)
        var paid: TextView = itemView.findViewById(R.id.paid_data)

        init {
            itemView.setOnClickListener {
                val position = this@ViewHolder.adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) listener!!.onItemClicked(
                    dataItemsLt!![position]
                )
            }
            itemView.setOnLongClickListener {
                val position = this@ViewHolder.adapterPosition
                if (longListener != null && position != RecyclerView.NO_POSITION) longListener!!.onItemLongClicked(
                    dataItemsLt!![position]
                )
                true
            }
        }
    }

    fun setOnItemLongClickListener(longListener: OnItemLongClickListener) {
        this.longListener = longListener
    }

    interface OnItemLongClickListener {
        fun onItemLongClicked(data: DataItems)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener=listener
    }

    interface OnItemClickListener {
        fun onItemClicked(data: DataItems)
    }

    companion object {
        fun setFadeAnimation(view: View) {
            val alphaAnimation = ScaleAnimation(
                0.0f,
                1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            alphaAnimation.duration = 1000
            view.startAnimation(alphaAnimation)
        }
    }
}