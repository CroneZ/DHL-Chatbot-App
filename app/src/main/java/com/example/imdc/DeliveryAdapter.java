package com.example.imdc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<DeliveryJob> deliveryJobList;

    public DeliveryAdapter(Context context,List<DeliveryJob> deliveryJobList){
        this.context = context;
        this.deliveryJobList = deliveryJobList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layoutinflater to choose my layout for each object
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_job_item,parent,false);
        //return view holder class
        return new DeliveryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeliveryJob job = (DeliveryJob) deliveryJobList.get(position);
        ((DeliveryHolder)holder).bind(job);
    }

    @Override
    public int getItemCount() {
        return deliveryJobList.size();
    }

    private class DeliveryHolder extends RecyclerView.ViewHolder{

        TextView destination;

        public DeliveryHolder(@NonNull View itemView) {
            super(itemView);
            destination = (TextView) itemView.findViewById(R.id.deliveryAddressText);

        }
        void bind(DeliveryJob job){
            destination.setText(job.getAddress());
        }
    }
}
