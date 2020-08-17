package com.bongobondhuparishad.bloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DonationPostAdapter extends RecyclerView.Adapter<DonationPostAdapter.ViewHolder> {

    private List<DonationPost> listItems;
    private Context context;


    public DonationPostAdapter(List<DonationPost> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public DonationPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_donation_posts, parent, false);

        return new DonationPostAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationPostAdapter.ViewHolder holder, int position) {

        DonationPost listItem = listItems.get(position);

        final SimpleDateFormat out_format = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date input_date= new Date();

        try {
            input_date = in_format.parse(listItem.getPublish_date()==null? "01-01-2020":listItem.getPublish_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.txtViewDetails.setText(listItem.getDetails());
        holder.txtViewName.setText(listItem.getUsername());
        holder.txtViewDistrict.setText(listItem.getLocation());
        holder.txtViewContact.setText(listItem.getContact());
        holder.txtViewDate.setText(out_format.format(input_date));
        holder.txtViewHospital.setText(listItem.getHospital());

        String bloodgroup = listItem.getBlood_group();
        if(bloodgroup.equals("O(+ve)")) holder.imvBloodGroup.setImageResource(R.drawable.o_positive);
        else if(bloodgroup.equals("A(+ve)")) holder.imvBloodGroup.setImageResource(R.drawable.a_positive);
        if(bloodgroup.equals("AB(+ve)")) holder.imvBloodGroup.setImageResource(R.drawable.ab_positive);
        if(bloodgroup.equals("B(+ve)")) holder.imvBloodGroup.setImageResource(R.drawable.b_positive);
        if(bloodgroup.equals("A(-ve)")) holder.imvBloodGroup.setImageResource(R.drawable.neg_a);
        if(bloodgroup.equals("B(-ve)")) holder.imvBloodGroup.setImageResource(R.drawable.neg_b);
        if(bloodgroup.equals("AB(-ve)")) holder.imvBloodGroup.setImageResource(R.drawable.neg_ab);
        if(bloodgroup.equals("O(-ve)")) holder.imvBloodGroup.setImageResource(R.drawable.neg_o);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtViewName,txtViewDetails,txtViewDate,txtViewContact,txtViewHospital,txtViewDistrict;
        ImageView imvBloodGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtViewName = (TextView) itemView.findViewById(R.id.txtPosterName);
            txtViewDetails = (TextView) itemView.findViewById(R.id.txtDetails);
            txtViewDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtViewContact = (TextView) itemView.findViewById(R.id.txtContact);
            txtViewHospital = (TextView) itemView.findViewById(R.id.txtHospital);
            txtViewDistrict = (TextView) itemView.findViewById(R.id.txtDistrict);
            imvBloodGroup = (ImageView) itemView.findViewById(R.id.imv_blood_group);
        }
    }

}
