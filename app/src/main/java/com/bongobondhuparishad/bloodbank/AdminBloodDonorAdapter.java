package com.bongobondhuparishad.bloodbank;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdminBloodDonorAdapter extends RecyclerView.Adapter<AdminBloodDonorAdapter.ViewHolder> implements Filterable {

    private List<AdminBloodDonor> listItems;
    private List<AdminBloodDonor> listItemsFull;
    private Context context;

    public AdminBloodDonorAdapter(List<AdminBloodDonor> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
        this.listItemsFull = new ArrayList<>(listItems);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_admin_blood_donors, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminBloodDonor listItem = listItems.get(position);

        holder.txtViewName.setText(listItem.getName());
        holder.txtViewDetails.setText(listItem.getDetails());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<AdminBloodDonor> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(listItemsFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(AdminBloodDonor item: listItemsFull) {
                    if(item.getName().toLowerCase().contains(filterPattern) || item.getDetails().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            listItems.clear();
            listItems.addAll((List<AdminBloodDonor>) results.values);
            notifyDataSetChanged();
        }
    };

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
