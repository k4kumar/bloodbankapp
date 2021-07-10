package com.bongobondhuparishad.bloodbank;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String Tag = "Profile Fragment";
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private EditText txtName,txtDivision,txtEmergencyContact,txtLastDonationDate,txtEmail,txtNickname,
            txtPassword, txtContact;
    private TextView tvName,tvReg;
    private Spinner spinnerBloodGroup;
    private String url;

    private MaterialButton btnUpdate;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        txtName = (EditText) view.findViewById(R.id.txtName);
        txtNickname = (EditText) view.findViewById(R.id.txtNickname);
        txtEmergencyContact = (EditText) view.findViewById(R.id.txtEmergencyContact);
        spinnerBloodGroup = (Spinner) view.findViewById(R.id.input_bloodgroup);
        txtDivision = (EditText) view.findViewById(R.id.txtDivision);
        txtLastDonationDate = (EditText) view.findViewById(R.id.input_last_donate_date);
        txtEmail = (EditText) view.findViewById(R.id.txtEmail);
        txtPassword = (EditText) view.findViewById(R.id.txtPassword);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvReg = (TextView) view.findViewById(R.id.tv_reg);
        txtContact = (EditText) view.findViewById(R.id.txtMobile);

        btnUpdate = (MaterialButton) view.findViewById(R.id.btn_update);

        SharedPreferences prefs =
                getContext().getSharedPreferences("MyPref", 0);

        String username = prefs.getString("username","15099");

        url = getResources().getString(R.string.api_web_address)+"api/v1/blood_donor/get/"+username;

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable())
        {
            Toast.makeText(getActivity(),"Please enable internet connection and retry",Toast.LENGTH_LONG).show();
        }
        else
        {
            loadProfileData();
        }
        txtLastDonationDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(txtLastDonationDate.hasFocus())
                {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            getActivity(),
                            R.style.DialogTheme,
                            onDateSetListener,
                            year,month,day);

                    dialog.show();
                }
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;
                String date = month +"/"+day+"/"+ year;
                txtLastDonationDate.setText(date);

            }
        };


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Profile Fragment","Inside onviewcreated");
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable())
                {
                    Toast.makeText(getActivity(),"Please enable internet connection and retry",Toast.LENGTH_LONG).show();
                }
                else{
                    new PostAsyncTask().execute();
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
            postData.put("RegNo",tvReg.getText());
            postData.put("Name",txtName.getText());
            postData.put("NickName",txtNickname.getText());
            postData.put("Comment","");
            postData.put("Division",txtDivision.getText());
            postData.put("Mobile",txtContact.getText());
            postData.put("Email",txtEmail.getText());
            postData.put("BloodGroup",spinnerBloodGroup.getSelectedItem());
            postData.put("LastDonatedDate",txtLastDonationDate.getText());
            postData.put("EmergencyContact",txtEmergencyContact.getText());
            postData.put("Password",txtPassword.getText());
            return post(getResources().getString(R.string.api_web_address)+"api/v1/blood_donor/update",postData);
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
                else if(response.getString("code").equals(300))
                {
                    Log.i("info", "inside 300");
                    Toast.makeText(getActivity(), "Please provide an unique registration number.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(),"Profile Updated",Toast.LENGTH_SHORT).show();
                    Log.i("info", "inside success");
                    Fragment fragment = new HomeFragment();
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

    private void loadProfileData() {
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
                            JSONObject blooddonor = data.getJSONObject("bloodDonor");

                            ArrayAdapter<String> bloodDonorAdapter = new ArrayAdapter<String>(getActivity(),
                                    R.layout.spnr_bloodgroup,
                                    getResources().getStringArray(R.array.bloodgroups));
                            bloodDonorAdapter.setDropDownViewResource(R.layout.drpdn_bloodgroup);
                            spinnerBloodGroup.setAdapter(bloodDonorAdapter);

                            Date input_date = in_format.parse(blooddonor.getString("lastDonatedDate"));
                            int spinner_position = bloodDonorAdapter.getPosition(blooddonor.getString("bloodGroup"));

                            tvName.setText(blooddonor.getString("name"));
                            tvReg.setText(blooddonor.getString("regNo"));
                            txtEmail.setText(blooddonor.getString("email"));
                            txtDivision.setText(blooddonor.getString("division"));
                            txtEmergencyContact.setText(blooddonor.getString("emergencyContact"));
                            txtNickname.setText(blooddonor.getString("nickName"));
                            txtLastDonationDate.setText(out_format.format(input_date));
                            txtName.setText(blooddonor.getString("name"));
                            spinnerBloodGroup.setSelection(spinner_position);
                            txtContact.setText(blooddonor.getString("mobile"));

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