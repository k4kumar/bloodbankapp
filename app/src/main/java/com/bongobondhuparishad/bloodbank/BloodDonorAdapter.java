package com.bongobondhuparishad.bloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.ViewHolder> {

    private List<AdminBloodDonor> listItems;
    private Context context;


    public BloodDonorAdapter(List<AdminBloodDonor> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public BloodDonorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_admin_blood_donors, parent, false);

        return new BloodDonorAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodDonorAdapter.ViewHolder holder, int position) {
        AdminBloodDonor listItem = listItems.get(position);

        holder.txtViewName.setText(listItem.getName());
        holder.txtViewDetails.setText(listItem.getDetails());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtViewName;
        public TextView txtViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtViewName = (TextView) itemView.findViewById(R.id.textViewName);
            txtViewDetails = (TextView) itemView.findViewById(R.id.textViewDetails);
        }
    }
}
