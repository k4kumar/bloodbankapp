package com.bongobondhuparishad.bloodbank;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
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
    private OnDonorListener mOnDonorListener;

    public AdminBloodDonorAdapter(List<AdminBloodDonor> listItems, Context context, OnDonorListener onDonorListener) {
        this.listItems = listItems;
        this.context = context;
        this.listItemsFull = new ArrayList<>(listItems);
        this.mOnDonorListener = onDonorListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_admin_blood_donors, parent, false);

        return new AdminBloodDonorAdapter.ViewHolder(v, mOnDonorListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminBloodDonor listItem = listItems.get(position);

        holder.txtViewName.setText(listItem.getName()+"("+listItem.getReg_no()+")");
        holder.txtViewDetails.setText(listItem.getDetails());

        holder.txtViewLastDonationDate.setText(listItem.getLast_donation_date());

        if(listItem.getBlood_group().equals("O(+ve)") || listItem.getBlood_group().equals("o(+ve)") || listItem.getBlood_group().equals("O+") || listItem.getBlood_group().equals("o+")) holder.imvbloodGroup.setImageResource(R.drawable.o_positive);
        else if(listItem.getBlood_group().equals("A(+ve)") || listItem.getBlood_group().equals("A+") || listItem.getBlood_group().equals("a(+ve)") || listItem.getBlood_group().equals("a+")) holder.imvbloodGroup.setImageResource(R.drawable.a_positive);
        else if(listItem.getBlood_group().equals("AB(+ve)") || listItem.getBlood_group().equals("AB+") || listItem.getBlood_group().equals("ab(+ve)") || listItem.getBlood_group().equals("ab+")) holder.imvbloodGroup.setImageResource(R.drawable.ab_positive);
        else if(listItem.getBlood_group().equals("B(+ve)") || listItem.getBlood_group().equals("B+") || listItem.getBlood_group().equals("b(+ve)") || listItem.getBlood_group().equals("b+")) holder.imvbloodGroup.setImageResource(R.drawable.b_positive);
        else if(listItem.getBlood_group().equals("A(-ve)") || listItem.getBlood_group().equals("A-") || listItem.getBlood_group().equals("a(-ve)") || listItem.getBlood_group().equals("a-")) holder.imvbloodGroup.setImageResource(R.drawable.neg_a);
        else if(listItem.getBlood_group().equals("AB(-ve)") || listItem.getBlood_group().equals("AB-") || listItem.getBlood_group().equals("ab(-ve)") || listItem.getBlood_group().equals("ab-")) holder.imvbloodGroup.setImageResource(R.drawable.neg_ab);
        else if(listItem.getBlood_group().equals("B(-ve)") || listItem.getBlood_group().equals("B-") || listItem.getBlood_group().equals("b(-ve)") || listItem.getBlood_group().equals("b-")) holder.imvbloodGroup.setImageResource(R.drawable.neg_b);
        else if(listItem.getBlood_group().equals("O(-ve)") || listItem.getBlood_group().equals("O-") || listItem.getBlood_group().equals("o(-ve)") || listItem.getBlood_group().equals("o-")) holder.imvbloodGroup.setImageResource(R.drawable.neg_o);

        if(!listItem.is_approved())
        {
            holder.txtViewDetails.setBackgroundColor(Color.parseColor("#FFFFC106"));
            holder.txtViewName.setBackgroundColor(Color.parseColor("#FFFFC106"));
        }
        else{
            holder.txtViewDetails.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.txtViewName.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

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
                    if(filterPattern.equals("O(+ve)") || filterPattern.equals("o(+ve)") || filterPattern.equals("O+") || filterPattern.equals("o+")) filterPattern = "O(+ve)";
                    else if(filterPattern.equals("A(+ve)") || filterPattern.equals("A+") || filterPattern.equals("a(+ve)") || filterPattern.equals("a+")) filterPattern = "A(+ve)";
                    else if(filterPattern.equals("AB(+ve)") || filterPattern.equals("AB+") || filterPattern.equals("ab(+ve)") || filterPattern.equals("ab+")) filterPattern = "AB(+ve)";
                    else if(filterPattern.equals("B(+ve)") || filterPattern.equals("B+") || filterPattern.equals("b(+ve)") || filterPattern.equals("b+")) filterPattern = "B(+ve)";
                    else if(filterPattern.equals("A(-ve)") || filterPattern.equals("A-") || filterPattern.equals("a(-ve)") || filterPattern.equals("a-")) filterPattern = "A(-ve)";
                    else if(filterPattern.equals("AB(-ve)") || filterPattern.equals("AB-") || filterPattern.equals("ab(-ve)") || filterPattern.equals("ab-")) filterPattern = "AB(-ve)";
                    else if(filterPattern.equals("B(-ve)") || filterPattern.equals("B-") || filterPattern.equals("b(-ve)") || filterPattern.equals("b-")) filterPattern = "B(-ve)";
                    else if(filterPattern.equals("O(-ve)") || filterPattern.equals("O-") || filterPattern.equals("o(-ve)") || filterPattern.equals("o-")) filterPattern = "O(-ve)";

                    if(item.getBlood_group().contains(filterPattern) || item.getBlood_group().equals(filterPattern))
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtViewName;
        public TextView txtViewDetails, txtViewLastDonationDate;
        public ImageView imvbloodGroup;
        OnDonorListener onDonorListener;

        public ViewHolder(@NonNull View itemView, OnDonorListener onDonorListener) {
            super(itemView);

            txtViewName = (TextView) itemView.findViewById(R.id.textViewName);
            txtViewDetails = (TextView) itemView.findViewById(R.id.textViewDetails);
            txtViewLastDonationDate = (TextView) itemView.findViewById(R.id.textViewDonationDate);
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
