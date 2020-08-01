package com.bongobondhuparishad.bloodbank;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminDonorDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminDonorDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView bloodgroup,email,contact,emergency_contact,name,division,reg_no, comment, last_donation_date;

    public AdminDonorDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminDonorDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminDonorDetailsFragment newInstance(String param1, String param2) {
        AdminDonorDetailsFragment fragment = new AdminDonorDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_donor_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        AdminBloodDonor obj= (AdminBloodDonor) bundle.getSerializable("donor_obj");

        bloodgroup = (TextView) view.findViewById(R.id.tv_bloodgroup);
        email = (TextView) view.findViewById(R.id.tv_email);
        division  = (TextView) view.findViewById(R.id.tv_division);
        contact = (TextView) view.findViewById(R.id.tv_contact);
        emergency_contact = (TextView) view.findViewById(R.id.tv_emergency_contact);
        name = (TextView) view.findViewById(R.id.tv_name);
        comment = (TextView) view.findViewById(R.id.tv_comment);
        reg_no = (TextView) view.findViewById(R.id.tv_reg);
        last_donation_date = (TextView) view.findViewById(R.id.tv_last_donate_date);

        name.setText(obj.getName());
        bloodgroup.setText(obj.getBlood_group());
        comment.setText(obj.getComment());
        reg_no.setText("Reg No.("+obj.getReg_no()+")");
        division.setText(obj.getDivision());
        email.setText(obj.getEmail());
        emergency_contact.setText(obj.getEmergency_contact());
        contact.setText(obj.getMobile());
        last_donation_date.setText(obj.getLast_donation_date());
    }
}