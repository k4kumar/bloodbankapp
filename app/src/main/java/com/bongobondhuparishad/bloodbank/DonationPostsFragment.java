package com.bongobondhuparishad.bloodbank;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonationPostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonationPostsFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String url;

    private RecyclerView recyclerView;
    private DonationPostAdapter adapter;

    private List<DonationPost> listItems;


    public DonationPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DonationPostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DonationPostsFragment newInstance(String param1, String param2) {
        DonationPostsFragment fragment = new DonationPostsFragment();
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
        return inflater.inflate(R.layout.fragment_donation_posts, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listItems = new ArrayList<DonationPost>();
        url = "http://bloodbank.manchitro.info/api/v1/donation_posts";

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable())
        {
            Toast.makeText(getActivity(),"Please enable internet connection and reopen the page",Toast.LENGTH_LONG).show();
        }
        else{
            loadRecyclerViewData();
        }

    }

    private void loadRecyclerViewData() {
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
                            JSONArray array = data.getJSONArray("donationPosts");

                            for(int i = 0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                JSONObject user = o.getJSONObject("user");
                                JSONObject bloodDonor = user.getJSONObject("bloodDonor");

                                String date="";

                                SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    date = in_format.parse(o.getString("publishDate")).toString();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                DonationPost donationPost = new DonationPost(
                                       date,
                                        o.getString("location"),
                                        o.getString("hospital"),
                                        o.getString("contact"),
                                        o.getString("bloodGroup"),
                                        o.getString("details"),
                                        bloodDonor.getString("name")
                                );

                                listItems.add(donationPost);
                            }

                            adapter = new DonationPostAdapter(listItems,getActivity());
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
}