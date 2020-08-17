package com.bongobondhuparishad.bloodbank;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPlasmaDonorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPlasmaDonorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String Tag = "Add Plasma Donor Fragment";
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog.OnDateSetListener onPositiveDateSetListener;

    private EditText txtRegNo, txtName, txtPositiveDate, txtRecoveryDate;
    private MaterialButton btnSubmit;


    public AddPlasmaDonorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPlasmaDonorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPlasmaDonorFragment newInstance(String param1, String param2) {
        AddPlasmaDonorFragment fragment = new AddPlasmaDonorFragment();
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
        return inflater.inflate(R.layout.fragment_add_plasma_donor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        txtName = (EditText) view.findViewById(R.id.txt_name);
        txtRegNo = (EditText) view.findViewById(R.id.txt_regno);
        txtPositiveDate = (EditText) view.findViewById(R.id.txt_affected_date);
        txtRecoveryDate = (EditText) view.findViewById(R.id.txt_recovery_date);

        btnSubmit = (MaterialButton) view.findViewById(R.id.btn_submit);

        SharedPreferences prefs =
                getContext().getSharedPreferences("MyPref", 0);

        String name = prefs.getString("name","Kumarjit Dutta");
        String username = prefs.getString("username","15099");

        txtName.setText(name);
        txtRegNo.setText(username);
        txtName.setEnabled(false);
        txtRegNo.setEnabled(false);

        txtRecoveryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (txtRecoveryDate.hasFocus()) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            getActivity(),
                            R.style.DialogTheme,
                            onDateSetListener,
                            year, month, day);

                    dialog.show();
                }
            }

        });

        txtPositiveDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (txtPositiveDate.hasFocus()) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            getActivity(),
                            R.style.DialogTheme,
                            onPositiveDateSetListener,
                            year, month, day);

                    dialog.show();
                }
            }

        });


        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;
                String date = month + "/" + day + "/" + year;
                txtRecoveryDate.setText(date);

            }
        };

        onPositiveDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;
                String date = month + "/" + day + "/" + year;
                txtPositiveDate.setText(date);

            }
        };

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("login", "Inside onviewcreated");

                if(txtPositiveDate.getText().length()>0 && txtRecoveryDate.getText().length()>0 && txtRegNo.getText().length()>0
                && txtName.getText().length()>0)
                {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if(networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable())
                    {
                        Toast.makeText(getActivity(),"Please enable internet connection",Toast.LENGTH_LONG).show();
                    }
                    else{
                        new PostAsyncTask().execute();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Please fill in all the star marked fields",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private class PostAsyncTask extends AsyncTask<String,Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            Map postData = new HashMap<>();
            postData.put("RegNo",txtRegNo.getText());
            postData.put("Name",txtName.getText());
            postData.put("AffectedDate",txtPositiveDate.getText());
            postData.put("RecoveryDate",txtRecoveryDate.getText());
            postData.put("HasDonated",false);
            postData.put("IsVerified",false);
            return post("http://bloodbank.manchitro.info/api/v1/plasma_donor/add",postData);
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);
            //All your UI operation can be performed here
            //Response string can be converted to JSONObject/JSONArray like


            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;
            try {
                Log.i("response:",response.toString());
                if(response.getString("code").equals("404"))
                {
                    Log.i("info", "inside 404");
                    Toast.makeText(getActivity(), "Can't connect with server.", Toast.LENGTH_LONG).show();
                }
                else if(response.getString("code").equals("406"))
                {
                    Log.i("info", "inside 406");
                    Toast.makeText(getActivity(), "Please provide authentic data.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(),"Plasma Donor Added",Toast.LENGTH_SHORT).show();
                    Log.i("info", "inside success");
                    Fragment fragment = new PlasmaDonationsFragment();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction=fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                //Toast.makeText(getActivity(), String.format("%s : %s",response.getString("message"),response.getString("code")), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), String.format("%s","Something went wrong!!!!!!"), Toast.LENGTH_LONG).show();

            }
            System.out.println(response);
        }
        /**
         * Method allows to HTTP POST request to the server to send data to a specified resource
         * @param REQUEST_URL URL of the API to be requested
         * @param params parameter that are to be send in the "body" of the request Ex: parameter=value&amp;also=another
         * returns response as a JSON object
         */
        public JSONObject post(String REQUEST_URL,Map<String, Object> params){
            JSONObject jsonObject = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(REQUEST_URL);
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setConnectTimeout(8000);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setDoOutput(true);
                connection.getOutputStream().write(postDataBytes);
                connection.connect();

                StringBuilder sb;
                int statusCode = connection.getResponseCode();
                Log.d("Status Code:",""+statusCode);

                if (statusCode == 200) {
                    sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    jsonObject = new JSONObject(sb.toString());
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }

            return jsonObject;
        }
    }
}