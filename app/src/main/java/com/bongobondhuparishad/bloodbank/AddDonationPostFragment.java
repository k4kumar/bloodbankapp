package com.bongobondhuparishad.bloodbank;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddDonationPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDonationPostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String Tag = "Add Donation Post Fragment";
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private EditText txtHospital, txtMobileNo, txtDate, txtDetails;
    private Spinner spinnerBloodGroup, spinnerDistrict;

    private MaterialButton btnSubmit;

    public AddDonationPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddDonationPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddDonationPostFragment newInstance(String param1, String param2) {
        AddDonationPostFragment fragment = new AddDonationPostFragment();
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
        return inflater.inflate(R.layout.fragment_add_donation_post, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        txtDate = (EditText) view.findViewById(R.id.input_post_date);
        txtDetails = (EditText) view.findViewById(R.id.input_details);
        txtMobileNo = (EditText) view.findViewById(R.id.input_mobile);
        txtHospital = (EditText) view.findViewById(R.id.input_hospital);
        spinnerBloodGroup = (Spinner) view.findViewById(R.id.input_bloodgroup);
        spinnerDistrict = (Spinner) view.findViewById(R.id.input_district);

        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spnr_district,
                getResources().getStringArray(R.array.districts));
        districtAdapter.setDropDownViewResource(R.layout.drpdn_district);
        spinnerDistrict.setAdapter(districtAdapter);

        ArrayAdapter<String> bloodDonorAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spnr_bloodgroup,
                getResources().getStringArray(R.array.bloodgroups));
        bloodDonorAdapter.setDropDownViewResource(R.layout.drpdn_bloodgroup);
        spinnerBloodGroup.setAdapter(bloodDonorAdapter);


        btnSubmit = (MaterialButton) view.findViewById(R.id.btn_submit);

        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (txtDate.hasFocus()) {
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

        txtMobileNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    textView.clearFocus();
                    spinnerBloodGroup.requestFocus();
                    spinnerBloodGroup.performClick();
                }
                return true;
            }
        });


        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;
                String date = month + "/" + day + "/" + year;
                txtDate.setText(date);

            }
        };

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("login", "Inside onviewcreated");

                if (txtMobileNo.getText().length() > 0 && txtDate.getText().length() > 0 &&
                        spinnerBloodGroup.getSelectedItem().toString().length() > 0 && txtHospital.getText().length() > 0 &&
                        spinnerDistrict.getSelectedItem().toString().length()>0)
                {
                    new AddDonationPostFragment.PostAsyncTask(getContext()).execute();
                }
                else{
                    Toast.makeText(getActivity(),"Please fill out all star marked fields",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private class PostAsyncTask extends AsyncTask<String, Void, JSONObject> {

            Context context;

            public PostAsyncTask(Context context) {
                this.context = context;
            }

        @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(String... params) {

                SharedPreferences prefs =
                        context.getSharedPreferences("MyPref", 0);

                int user_id = prefs.getInt("user_id",0);

                Log.d(TAG, "User id: "+user_id);

                Map postData = new HashMap<>();
                postData.put("PublishDate", txtDate.getText());
                postData.put("Location", spinnerDistrict.getSelectedItem().toString());
                postData.put("Hospital", txtHospital.getText());
                postData.put("Contact", txtMobileNo.getText());
                postData.put("BloodGroup", spinnerBloodGroup.getSelectedItem().toString());
                postData.put("Details", txtDetails.getText());
                postData.put("UserId", user_id);
                return post("http://bloodbank.manchitro.info/api/v1/donation_post/add", postData);
            }

            @Override
            protected void onPostExecute(JSONObject response) {
                super.onPostExecute(response);
                //All your UI operation can be performed here
                //Response string can be converted to JSONObject/JSONArray like

                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                try {
                    Log.i("response:", response.getString("code"));
                    if (response.getString("code").equals("404")) {
                        Log.i("info", "inside 404");
                        Toast.makeText(getActivity(), "Can't connect with server.", Toast.LENGTH_LONG).show();
                    } else if (response.getString("code").equals("406")) {
                        Log.i("info", "inside 406");
                        Toast.makeText(getActivity(), "Please provide authentic data.", Toast.LENGTH_LONG).show();
                    } else if (response.getString("code").equals(300)) {
                        Log.i("info", "inside 300");
                        Toast.makeText(getActivity(), "Please provide an unique registration number.", Toast.LENGTH_LONG).show();
                    } else {
                        Log.i("info", "inside success");
                        Fragment fragment = new DonationPostsFragment();
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    //Toast.makeText(getActivity(), String.format("%s : %s",response.getString("message"),response.getString("code")), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), String.format("%s", "Something went wrong!!!!!!"), Toast.LENGTH_LONG).show();

                }
                System.out.println(response);
            }

            /**
             * Method allows to HTTP POST request to the server to send data to a specified resource
             * @param REQUEST_URL URL of the API to be requested
             * @param params parameter that are to be send in the "body" of the request Ex: parameter=value&amp;also=another
             * returns response as a JSON object
             */
            public JSONObject post(String REQUEST_URL, Map<String, Object> params) {
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
                    Log.d("Status Code:", "" + statusCode);

                    if (statusCode == 200) {
                        sb = new StringBuilder();
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        jsonObject = new JSONObject(sb.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return jsonObject;
            }
        }
}
