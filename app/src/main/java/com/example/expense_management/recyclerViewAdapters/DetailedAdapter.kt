package com.example.expense_management.recyclerViewAdapters

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.expense_management.R.layout
import android.widget.TextView
import com.example.expense_management.R.id
import java.util.ArrayList

class DetailedAdapter(
    value: List<Int>?,
    description: List<String>?,
    cheek: String,
    gain: Boolean
) : RecyclerView.Adapter<DetailedAdapter.Holder>() {
    var value: MutableList<Int>?
    var description: MutableList<String>
    var check = false
    var gain = false
    private var listener: OnItemLongClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout.recycler_view_for_showing_details, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val `val` = holder.`val`
        `val`.text = value!![position].toString()
        val des = holder.des
        des.text = description[position]
        if (gain) {
            `val`.setTextColor(Color.rgb(2, 120, 28))
            des.setTextColor(Color.rgb(2, 120, 28))
        } else {
            `val`.setTextColor(Color.rgb(239, 38, 38))
            des.setTextColor(Color.rgb(239, 38, 38))
        }
        MainRecycleAdapter.setFadeAnimation(holder.itemView)
    }

    override fun getItemCount(): Int {
        return if (value != null) value!!.size else 0
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        this.listener = listener
    }

    interface OnItemLongClickListener {
        fun onItemLongClicked(integer: String, string: String, position: Int)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var aPosition = 0
        var `val`: TextView = itemView.findViewById(id.data_amount)
        var des: TextView = itemView.findViewById(id.data_amount_description)

        init {
            if (check) {
                itemView.setOnLongClickListener {
                    if (listener != null && aPosition != RecyclerView.NO_POSITION) listener!!.onItemLongClicked(
                        value!![aPosition].toString(), description[aPosition], aPosition
                    )
                    true
                }
            }
        }
    }

    init {
        this.gain = gain
        this.value = ArrayList<Int>()
        this.description = ArrayList<String>()
        if (value == null) this.value = ArrayList() else {
            (this.value as ArrayList<Int>).clear()
            (this.value as ArrayList<Int>).addAll(value)
        }
        if (description == null) this.description = ArrayList() else {
            this.description.clear()
            this.description.addAll(description)
        }
        if (cheek === "yes") check = true
        notifyDataSetChanged()
    }
}