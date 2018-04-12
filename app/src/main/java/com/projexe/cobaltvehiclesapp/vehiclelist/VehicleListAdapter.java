package com.projexe.cobaltvehiclesapp.vehiclelist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projexe.cobaltvehiclesapp.R;

import java.util.List;

/**
 * Adapter class providing the binder between the RecyclerView and the data it contains
 * @author Simon Hutton
 * @version 1.0
 */
public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {

    private List<List> mItems;
    private VehicleItemListener mItemListener;

    VehicleListAdapter(List<List> vehicleList, VehicleItemListener itemListener) {
        mItems = vehicleList;
        mItemListener = itemListener;
    }

    @Override
    public VehicleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View vehicleListItemView = inflater.inflate(R.layout.recycle_list_item, parent, false);
        return new ViewHolder(vehicleListItemView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(VehicleListAdapter.ViewHolder holder, int position) {
        List rowItem = mItems.get(position);
        String name = (String) rowItem.get(0);
        holder.vehicleNameTextview.setText(name);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateVehicles(List<List> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private List getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface VehicleItemListener {
        void onVehicleClick(List list);
    }

    //**********************************************************************************************
    /**
     * Viewholders keep the references in memory. When a new view is required a new Viewholder is
     * either created or it is recycled from the stack
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView vehicleNameTextview;
        VehicleItemListener mItemListener;

        ViewHolder(View itemView, VehicleItemListener vehicleItemListener) {
            super(itemView);
            vehicleNameTextview = itemView.findViewById(R.id.item_vehicle_name);
            this.mItemListener = vehicleItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            List item = getItem(getAdapterPosition());
            this.mItemListener.onVehicleClick(item);
            notifyDataSetChanged();
        }
    }
    //**********************************************************************************************
}