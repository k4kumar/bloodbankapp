package com.bongobondhuparishad.bloodbank;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

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
    private Button btn_approve;

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

        SimpleDateFormat out_format = new SimpleDateFormat("dd/MM/yyyy");
        Bundle bundle = getArguments();
        AdminBloodDonor obj= (AdminBloodDonor) bundle.getSerializable("donor_obj");
        Date date = new Date();

        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            date = in_format.parse(obj.getLast_donation_date());
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        bloodgroup = (TextView) view.findViewById(R.id.tv_bloodgroup);
        email = (TextView) view.findViewById(R.id.tv_email);
        division  = (TextView) view.findViewById(R.id.tv_division);
        contact = (TextView) view.findViewById(R.id.tv_contact);
        emergency_contact = (TextView) view.findViewById(R.id.tv_emergency_contact);
        name = (TextView) view.findViewById(R.id.tv_name);
        comment = (TextView) view.findViewById(R.id.tv_comment);
        reg_no = (TextView) view.findViewById(R.id.tv_reg);
        last_donation_date = (TextView) view.findViewById(R.id.tv_last_donate_date);
        btn_approve = (Button) view.findViewById(R.id.btn_approve);

        boolean approved = obj.is_approved();
        if(approved)
        {
           btn_approve.setVisibility(View.GONE);
        }
        name.setText(obj.getName());
        bloodgroup.setText(obj.getBlood_group());
        comment.setText(obj.getComment());
        if(obj.getReg_no().length()>=11)
        {
            reg_no.setText(obj.getReg_no());
        }
        else{
            reg_no.setText("Reg No.("+obj.getReg_no()+")");
        }
        division.setText(obj.getDivision());
        email.setText(obj.getEmail());
        emergency_contact.setText(obj.getEmergency_contact());
        contact.setText(obj.getMobile());
        last_donation_date.setText(out_format.format(date));
        name.setTag(obj.getId());

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("login","Inside onviewcreated");
                new PostAsyncTask().execute();
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
            Log.d("Approve:", "doInBackground: "+name.getTag());
            Map postData = new HashMap<>();
            postData.put("id",name.getTag());
            return post(getResources().getString(R.string.api_web_address)+"api/v1/admin/blooddonor/approve",postData);
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);
            //All your UI operation can be performed here
            //Response string can be converted to JSONObject/JSONArray like

            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;
            try {
                Log.i("response:",response.getString("code"));
                if(response.getString("code").equals("404"))
                {
                    Log.i("info", "inside 404");
                    Toast.makeText(getActivity(), "Blood Donor Not Found", Toast.LENGTH_LONG).show();
                }
                else{
                    Log.i("info", "inside success");
                    Fragment fragment = new AdminBloodDonorFragment();
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
                else{
                    jsonObject = new JSONObject("{\n" +
                            "  \"data\": {},\n" +
                            "  \"code\": 404,\n" +
                            "  \"message\": \"Blood Donor Not Found!\",\n" +
                            "  \"isSuccess\": false\n" +
                            "}");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return jsonObject;
        }
    }
}