package com.example.imdc;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class TrackingAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<TrackingItem> trackingItemList;


    public TrackingAdapter(Context context,List<TrackingItem> trackingItemList) {
        this.context = context;
        this.trackingItemList = trackingItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        //Inflate layout
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_list_item,parent,false);
        return new TrackingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TrackingItem trackingItem = (TrackingItem) trackingItemList.get(position);
        //bind item
        ((TrackingItemViewHolder)holder).bind(trackingItem);
    }

    @Override
    public int getItemCount() {
        return trackingItemList.size();
    }

    private class TrackingItemViewHolder extends RecyclerView.ViewHolder{
        TextView tagName , tagStatus;
        public TrackingItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = (TextView) itemView.findViewById(R.id.tagNameLabel);
            tagStatus = (TextView) itemView.findViewById(R.id.statusLabel);
        }
        void bind(TrackingItem trackingItem){
            tagName.setText(trackingItem.getTag());
            tagStatus.setText(trackingItem.getStatus());
        }
    }
}
