package com.bongobondhuparishad.bloodbank;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    NavigationView navigationView;
    LinearLayout admin,new_donor,blood_donors, profile, add_donation_post, donation_posts, add_plasma_donor, plasma_donors;

    private ImageView add_donor_button, home, donor_list_button, donation_post_button, login_button, donation_requests_button,
    profile_button, plasma_donars_button, add_plasma_donor_button;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view1);

        add_donor_button=(ImageView) findViewById(R.id.imv_add_donor);
        donor_list_button=(ImageView) findViewById(R.id.imv_donor_list);
        home = (ImageView) findViewById(R.id.imv_home);
        donation_post_button = (ImageView) findViewById(R.id.imv_add_donation_request);
        login_button = (ImageView) findViewById(R.id.imv_login);
        donation_requests_button = (ImageView) findViewById(R.id.imv_donation_requests);
        profile_button = (ImageView) findViewById(R.id.imv_profile);
        add_plasma_donor_button = (ImageView) findViewById(R.id.imv_add_plasma_donor);
        plasma_donars_button = (ImageView) findViewById(R.id.imv_plasma_donors);


        new_donor=findViewById(R.id.new_donor);
        blood_donors=findViewById(R.id.blood_donors);
        admin=findViewById(R.id.admin);
        profile = findViewById(R.id.profile);
        add_donation_post = findViewById(R.id.add_donation_request);
        donation_posts = findViewById(R.id.donation_requests);
        add_plasma_donor = findViewById(R.id.add_plasma_donor);
        plasma_donors = findViewById(R.id.plasma_donors);


        fragmentManager = getSupportFragmentManager();

        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                startActivity(getIntent());
            }
        });

        add_donor_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Fragment fragment = new AddDonorFragment();
                Log.d("admin","clicked add donor");

                fragmentTransaction=fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new LoginFragment();
                fragmentTransaction=fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        donation_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null){
                    Fragment fragment = new AddDonationPostFragment();
                    Log.d("admin", "clicked donation post");

                    fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }
            }
        });

        donation_requests_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null){
                    Fragment fragment = new DonationPostsFragment();
                    Log.d("admin", "clicked donation request post");

                    fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }
            }
        });

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null){
                    Fragment fragment = new ProfileFragment();
                    Log.d("admin", "clicked profile fragment");

                    fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }
            }
        });

        donor_list_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null) {
                    if (user_role.equals("user")) {
                        Fragment fragment = new BloodDonorFragment();
                        Log.d("admin", "clicked donor list");

                        fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else if (user_role.equals("admin")) {
                        Fragment fragment = new AdminBloodDonorFragment();
                        Log.d("admin", "clicked donor list");

                        fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }


            }
        });

        plasma_donars_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null){
                    Fragment fragment = new PlasmaDonationsFragment();
                    Log.d("admin", "clicked plasma donors");

                    fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }
            }
        });

        add_plasma_donor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null){
                    Fragment fragment = new AddPlasmaDonorFragment();
                    Log.d("admin", "clicked add plasma donors");

                    fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }
            }
        });

        new_donor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Fragment fragment = new AddDonorFragment();
                Log.d("admin","clicked add donor");

                drawer.closeDrawer(navigationView);
                fragmentTransaction=fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        blood_donors.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null) {
                    if (user_role.equals("user")) {
                        Fragment fragment = new BloodDonorFragment();
                        Log.d("admin", "clicked donor list");
                        drawer.closeDrawer(navigationView);
                        fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else if (user_role.equals("admin")) {
                        Fragment fragment = new AdminBloodDonorFragment();
                        Log.d("admin", "clicked donor list");
                        drawer.closeDrawer(navigationView);
                        fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }

            }
        });

        admin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Fragment fragment = new LoginFragment();
                Log.d("admin","clicked admin");

                drawer.closeDrawer(navigationView);
                fragmentTransaction=fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        add_donation_post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null){
                    Fragment fragment = new AddDonationPostFragment();
                    Log.d("admin", "clicked donation post");
                    drawer.closeDrawer(navigationView);
                    fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }
            }
        });

        donation_posts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null){
                    Fragment fragment = new DonationPostsFragment();
                    Log.d("admin", "clicked donation request post");
                    drawer.closeDrawer(navigationView);
                    fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null){
                    Fragment fragment = new ProfileFragment();
                    Log.d("admin", "clicked profile fragment");
                    drawer.closeDrawer(navigationView);
                    fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }
            }
        });

        add_plasma_donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null){
                    Fragment fragment = new AddPlasmaDonorFragment();
                    Log.d("admin", "clicked add plasma donors");
                    drawer.closeDrawer(navigationView);
                    fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }
            }
        });

        plasma_donors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String user_role = pref.getString("user_role",null);

                if(user_role!=null){
                    Fragment fragment = new PlasmaDonationsFragment();
                    Log.d("admin", "clicked plasma donors");
                    drawer.closeDrawer(navigationView);
                    fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_LONG).show();
                }
            }
        });

        setToolbar();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String user_role = pref.getString("user_role",null);
        if(user_role==null)
        {
            Fragment fragment = new LoginFragment();
            Log.d("admin", "clicked profile fragment");

            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("");

        toolbar.findViewById(R.id.navigation_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Click", "keryu");

                if (drawer.isDrawerOpen(navigationView)) {
                    drawer.closeDrawer(navigationView);
                } else {
                    drawer.openDrawer(navigationView);
                }
            }
        });

    }

}
