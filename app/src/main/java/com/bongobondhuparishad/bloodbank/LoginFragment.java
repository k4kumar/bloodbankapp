package com.bongobondhuparishad.bloodbank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText txtRegno;
    private EditText txtPassword;

    private TextView tvRegister;
    private MaterialButton btnLogin;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        txtPassword = (EditText) view.findViewById(R.id.input_password);
        txtRegno = (EditText) view.findViewById(R.id.input_regno);
        tvRegister = (TextView) view.findViewById(R.id.tv_register);

        btnLogin = (MaterialButton) view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("login","Inside onviewcreated");
                if(txtPassword.getText().length()>0 && txtRegno.getText().length()>0)
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
                    Toast.makeText(getActivity(),"Please provide username and password",Toast.LENGTH_LONG).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                Fragment fragment = new AddDonorFragment();
                fragmentManager = getFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        // or  (ImageView) view.findViewById(R.id.foo);
    }

    private class PostAsyncTask extends AsyncTask<String,Void,JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            Map postData = new HashMap<>();
            postData.put("Password",txtPassword.getText());
            postData.put("UserName",txtRegno.getText());
            return post("http://bloodbank.manchitro.info/api/v1/admin/login",postData);
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);
            //All your UI operation can be performed here
            //Response string can be converted to JSONObject/JSONArray like
            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;
            try {
                if(response.getString("code").equals("404"))
                {
                    Log.i("info", "inside 404");
                    Toast.makeText(getActivity(), "Reg no. and password combination wrong", Toast.LENGTH_LONG).show();
                }
                else{
                    JSONObject data = response.getJSONObject("data");
                    JSONObject user = data.getJSONObject("user");
                    Log.i("response:",user.getString("role"));
                    String user_role = user.getString("role");
                    String reg_no = user.getString("username");
                    String name = user.getString("name");
                    int user_id = user.getInt("userId");
                    Log.d(TAG, "user id: "+user_id);
                    Log.i("info", "inside success");

                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("username",reg_no);
                    editor.putString("user_role",user_role);
                    editor.putInt("user_id",user_id);
                    editor.putString("name", name);

                    editor.commit();

                    if(user_role.equals("user"))
                    {
                        Intent t = new Intent(getContext(), MainActivity.class);
                        startActivity(t);
                    }
                    else{
                        Intent t = new Intent(getContext(), MainActivity.class);
                        startActivity(t);
                    }

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
                            "  \"message\": \"Username and password is not correct!\",\n" +
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


