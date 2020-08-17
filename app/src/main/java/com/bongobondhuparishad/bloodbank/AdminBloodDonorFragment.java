package com.bongobondhuparishad.bloodbank;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminBloodDonorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminBloodDonorFragment extends Fragment implements AdminBloodDonorAdapter.OnDonorListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String url;

    private static final String TAG = "Admin Donor Fragment";

    private RecyclerView recyclerView;
    private AdminBloodDonorAdapter adapter;

    private EditText searchFilter;

    private List<AdminBloodDonor> listItems;

    public AdminBloodDonorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminBloodDonorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminBloodDonorFragment newInstance(String param1, String param2) {
        AdminBloodDonorFragment fragment = new AdminBloodDonorFragment();
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
        return inflater.inflate(R.layout.fragment_admin_blood_donor, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchFilter = (EditText) view.findViewById(R.id.searchFilter);

        listItems = new ArrayList<AdminBloodDonor>();
        url = "http://bloodbank.manchitro.info/api/v1/admin/blooddonors";

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable())
        {
            Toast.makeText(getActivity(),"Please enable internet connection and reopen the page",Toast.LENGTH_LONG).show();
        }
        else{
            loadRecyclerViewData(this);
        }

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

    private void loadRecyclerViewData(final AdminBloodDonorAdapter.OnDonorListener onDonorListener) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray array = data.getJSONArray("blooddonors");

                            for(int i = 0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                AdminBloodDonor adminBloodDonor = new AdminBloodDonor(
                                        o.getString("name"),
                                        o.getInt("id"),
                                        o.getString("mobile"),
                                        o.getString("bloodGroup"),
                                        o.getString("email"),
                                        o.getString("division"),
                                        o.getString("lastDonatedDate"),
                                        o.getString("regNo"),
                                        o.getString("comment"),
                                        o.getString("emergencyContact"),
                                        o.getBoolean("isVerified"),
                                        o.getBoolean("hasDonated"),
                                        o.getString("bloodGroup")+"\n"+o.getString("division")
                                );

                                if(!adminBloodDonor.is_approved())
                                    adminBloodDonor.setBackground_color("Red");

                                listItems.add(adminBloodDonor);
                            }

                            adapter = new AdminBloodDonorAdapter(listItems,getActivity(),onDonorListener);
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

        Log.d(TAG, "onDonorClick: "+position);
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        Fragment fragment = new AdminDonorDetailsFragment();
        Bundle bundle = new Bundle();
        AdminBloodDonor obj = listItems.get(position);
        bundle.putSerializable("donor_obj", obj);
        fragment.setArguments(bundle);
        fragmentManager = getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}