package com.bongobondhuparishad.bloodbank;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlasmaDonationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlasmaDonationsFragment extends Fragment implements PlasmaDonorAdapter.OnDonorListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String url;

    private static final String TAG = "Plasma Donor Fragment";

    private RecyclerView recyclerView;
    private PlasmaDonorAdapter adapter;

    private EditText searchFilter;

    private List<PlasmaDonor> listItems;

    public PlasmaDonationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlasmaDonationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlasmaDonationsFragment newInstance(String param1, String param2) {
        PlasmaDonationsFragment fragment = new PlasmaDonationsFragment();
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
        return inflater.inflate(R.layout.fragment_plasma_donations, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchFilter = (EditText) view.findViewById(R.id.searchFilter);

        listItems = new ArrayList<PlasmaDonor>();
        url = "http://bloodbank.manchitro.info/api/v1/plasma_donors";
        loadRecyclerViewData(this);

        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadRecyclerViewData(final PlasmaDonorAdapter.OnDonorListener onDonorListener) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        final SimpleDateFormat out_format = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray array = data.getJSONArray("plasmadonors");


                            for(int i = 0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                JSONObject bloodDonor = o.getJSONObject("bloodDonor");
                                Date affected_date = in_format.parse(o.getString("affectedDate"));
                                Date recovery_date = in_format.parse(o.getString("recoveryDate"));


                                PlasmaDonor plasmaDonor = new PlasmaDonor(
                                        bloodDonor.getString("name"),
                                        bloodDonor.getString("bloodGroup"),
                                        bloodDonor.getInt("id"),
                                        bloodDonor.getString("mobile"),
                                        bloodDonor.getString("bloodGroup"),
                                        bloodDonor.getString("regNo"),
                                        "Affected Date: "+out_format.format(affected_date),
                                        "Recovery Date: "+out_format.format(recovery_date)
                                );

                                listItems.add(plasmaDonor);
                            }

                            Log.d(TAG, "onResponse: "+listItems.size());
                            adapter = new PlasmaDonorAdapter(listItems,getActivity(),onDonorListener);
                            recyclerView.setAdapter(adapter);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onDonorClick(int position) {

//        Log.d(TAG, "onDonorClick: "+position);
//        PlasmaDonor obj = listItems.get(position);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("donor_obj", obj);
//        Log.d(TAG, "onDonorClick: "+obj.getName());
//
//
//        FragmentManager fragmentManager;
//        FragmentTransaction fragmentTransaction;
//        Fragment fragment = new DonorDetailsFragment();
//
//        fragment.setArguments(bundle);
//        fragmentManager = getFragmentManager();
//        fragmentTransaction=fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

    }
}