package com.example.expense_management.RecyclerViewAdapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.expense_management.R.*;

import com.example.expense_management.R;

public class detailed_adapter extends RecyclerView.Adapter<detailed_adapter.Holder>  {

    List<Integer> value;
    List<String> description;
    boolean check = false,gain=false;
    private onItemLongClickListener listener;

    public detailed_adapter(List<Integer> value, List<String> description, String cheek,boolean gain) {
        this.gain=gain;
        this.value = new ArrayList<>();
        this.description = new ArrayList<>();
        if (value==null)
            this.value = new ArrayList<>();
        else {
            this.value.clear();
            this.value.addAll(value);
        }
        if (description==null)
            this.description = new ArrayList<>();
        else {
            this.description.clear();
            this.description.addAll(description);
        }

        if (cheek=="yes")
            check = true;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public detailed_adapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(layout.recycler_view_for_showing_details,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull detailed_adapter.Holder holder, int position) {
        TextView val=holder.val;
        val.setText(String.valueOf(value.get(position)));
        TextView des=holder.des;
        des.setText(description.get(position));
        if (gain){
            val.setTextColor(Color.rgb(2,120,28));
            des.setTextColor(Color.rgb(2,120,28));
        }
        else{
            val.setTextColor(Color.rgb(239,38,38));
            des.setTextColor(Color.rgb(239,38,38));
        }
        mainRecycleAdapter.setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        if (value != null)
            return value.size();
        return 0;
    }

    public void setOnItemLongClickListener(onItemLongClickListener listener) {
        this.listener = listener;
    }

    public interface onItemLongClickListener {
        void onItemLongClicked( String integer, String string,int position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        int position;
        TextView val, des;

        public Holder(@NonNull View itemView) {
            super(itemView);
            val = itemView.findViewById(id.data_amount);
            des = itemView.findViewById(id.data_amount_description);
            if (check) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                            position=Holder.this.getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION)
                            listener.onItemLongClicked( String.valueOf(value.get(position))
                                    , description.get(position),position);

                        return true;
                    }
                });
            }
        }
    }
}
