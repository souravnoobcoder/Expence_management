package com.example.expense_management.RecyclerViewAdapters;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense_management.Database.DataItems;
import com.example.expense_management.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.String.*;

public class mainRecycleAdapter extends RecyclerView.Adapter<mainRecycleAdapter.ViewHolder> {

    List<DataItems> dataItemsList;
    private onItemClickListener listener;
    private onItemLongClickListener longListener;

    public mainRecycleAdapter(){

    }
    public void setDataItemsList(List<DataItems> dataItemsList){
        this.dataItemsList=new ArrayList<>();
        if(dataItemsList.isEmpty()){
            this.dataItemsList=new ArrayList<>();   //setting dataItemsList
        }
        this.dataItemsList.clear();
        this.dataItemsList.addAll(dataItemsList);
        notifyDataSetChanged();
    }
    public DataItems getItemList(int position){
        return dataItemsList.get(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TextView date=holder.date;
            String stringDate= DateFormat.format("dd/MM/yy",new Date(dataItemsList.get(position).getDate())).toString();
            date.setText(stringDate);
            TextView gainMoney=holder.gain;
            gainMoney.setText(""+dataItemsList.get(position).getGrossMoneyGot());
            TextView paidMoney=holder.paid;
            paidMoney.setText(format("%d", dataItemsList.get(position).getGrossMoneyExpense()));
            setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        if (dataItemsList!=null)
            return dataItemsList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       public TextView date,gain,paid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date_of_data);
            gain=itemView.findViewById(R.id.gain_data);
            paid=itemView.findViewById(R.id.paid_data);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=ViewHolder.this.getAdapterPosition();
                    if (listener!=null&&position!=RecyclerView.NO_POSITION)
                        listener.onItemClicked(dataItemsList.get(position));
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position=ViewHolder.this.getAdapterPosition();
                    if (longListener!=null&&position!=RecyclerView.NO_POSITION)
                        longListener.onItemLongClicked(dataItemsList.get(position));
                    return true;
                }
            });
        }
    }
    public void setOnItemLongClickListener(onItemLongClickListener longListener){
        this.longListener=longListener;
    }
    public interface onItemLongClickListener {
        void onItemLongClicked(DataItems data);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }
    public interface onItemClickListener {
        void onItemClicked(DataItems data);
    }
    public static void setFadeAnimation(View view){
        ScaleAnimation alphaAnimation=new ScaleAnimation(0.0f,
                1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        alphaAnimation.setDuration(1000);
        view.startAnimation(alphaAnimation);
    }
}
