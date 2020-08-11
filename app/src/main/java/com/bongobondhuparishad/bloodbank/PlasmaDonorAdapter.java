package com.bongobondhuparishad.bloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlasmaDonorAdapter extends RecyclerView.Adapter<PlasmaDonorAdapter.ViewHolder> implements Filterable {

    private List<PlasmaDonor> listItems;
    private List<PlasmaDonor> listItemsFull;
    private Context context;
    private OnDonorListener mOnDonorListener;


    public PlasmaDonorAdapter(List<PlasmaDonor> listItems, Context context, OnDonorListener onDonorListener) {
        this.listItems = listItems;
        this.context = context;
        this.listItemsFull = new ArrayList<>(listItems);
        this.mOnDonorListener = onDonorListener;
    }

    @NonNull
    @Override
    public PlasmaDonorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_plasma_donors, parent, false);

        return new PlasmaDonorAdapter.ViewHolder(v, mOnDonorListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlasmaDonorAdapter.ViewHolder holder, int position) {
        PlasmaDonor listItem = listItems.get(position);

        holder.txtViewName.setText(listItem.getName()+"("+listItem.getReg_no()+")");
        holder.txtViewPositiveDate.setText(listItem.getPostive_date());
        holder.txtViewRecoveryDate.setText(listItem.getRecovery_date());
        holder.txtViewMobile.setText(listItem.getMobile());

        if(listItem.getBlood_group().equals("O(+ve)")) holder.imvbloodGroup.setImageResource(R.drawable.o_positive);
        else if(listItem.getBlood_group().equals("A(+ve)")) holder.imvbloodGroup.setImageResource(R.drawable.a_positive);
        else if(listItem.getBlood_group().equals("AB(+ve)")) holder.imvbloodGroup.setImageResource(R.drawable.ab_positive);
        else if(listItem.getBlood_group().equals("B(+ve)")) holder.imvbloodGroup.setImageResource(R.drawable.b_positive);
        else if(listItem.getBlood_group().equals("A(-ve)")) holder.imvbloodGroup.setImageResource(R.drawable.neg_a);
        else if(listItem.getBlood_group().equals("AB(-ve)")) holder.imvbloodGroup.setImageResource(R.drawable.neg_ab);
        else if(listItem.getBlood_group().equals("B(-ve)")) holder.imvbloodGroup.setImageResource(R.drawable.neg_b);
        else if(listItem.getBlood_group().equals("O(-ve)")) holder.imvbloodGroup.setImageResource(R.drawable.neg_o);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<PlasmaDonor> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(listItemsFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (PlasmaDonor item : listItemsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getReg_no().contains(filterPattern) || item.getBlood_group().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            listItems.clear();
            listItems.addAll((List<PlasmaDonor>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        public TextView txtViewName;
        public TextView txtViewPositiveDate, txtViewRecoveryDate, txtViewMobile;
        public ImageView imvbloodGroup;
        OnDonorListener onDonorListener;

        public ViewHolder(@NonNull View itemView, OnDonorListener onDonorListener) {
            super(itemView);

            txtViewName = (TextView) itemView.findViewById(R.id.textViewName);
            txtViewPositiveDate = (TextView) itemView.findViewById(R.id.textViewPositiveDate);
            txtViewRecoveryDate = (TextView) itemView.findViewById(R.id.textViewRecoveryDate);
            txtViewMobile = (TextView) itemView.findViewById(R.id.textViewContactNumber);
            imvbloodGroup = (ImageView) itemView.findViewById(R.id.imv_blood_group);

            this.onDonorListener = onDonorListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onDonorListener.onDonorClick(getAdapterPosition());
        }
    }

    public interface OnDonorListener{
        void onDonorClick(int position);
    }
}
