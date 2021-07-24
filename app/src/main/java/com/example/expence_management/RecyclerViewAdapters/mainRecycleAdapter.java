package com.example.expence_management.RecyclerViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expence_management.Database.DataItems;
import com.example.expence_management.R;

import java.util.ArrayList;
import java.util.List;

public class mainRecycleAdapter extends RecyclerView.Adapter<mainRecycleAdapter.ViewHolder> {

    List<DataItems> dataItemsList;
    private onItemClickListener listener;

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
            date.setText(dataItemsList.get(position).getDate());
            TextView gainMoney=holder.gain;
            gainMoney.setText(""+dataItemsList.get(position).getGrossMoneyGot());
            TextView paidMoney=holder.paid;
            paidMoney.setText(""+dataItemsList.get(position).getGrossMoneyExpense());

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
                        listener.onItemClicked(dataItemsList.get(position),position);
                }
            });
        }
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }
    public interface onItemClickListener {
        void onItemClicked(DataItems data,int position);
    }
}
