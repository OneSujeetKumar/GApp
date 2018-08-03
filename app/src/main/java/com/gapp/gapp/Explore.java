package com.gapp.gapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Explore extends Fragment implements View.OnClickListener{

    Button myloc;
    ArrayList<ParentI> parentIS;
    ArrayList<ChildI> childIS;
    RecyclerView list;
    TextView title;
    HorizontalScrollView hsv;
    Button bar, bakery, mealdelivery, mealtakeaway, cafe, museum, nightclub, restaurant, spa;
    MyAdapter myAdapter;
    String lati = "", lngi = "";
    LinearLayoutManager llm;

    public Explore() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore, container, false);

        list = view.findViewById(R.id.e_list);
        parentIS = new ArrayList<>();

        hsv = view.findViewById(R.id.e_hsv);

        bar = view.findViewById(R.id.e_bar);
        bakery = view.findViewById(R.id.e_bakery);
        mealdelivery = view.findViewById(R.id.e_mealdelivery);
        mealtakeaway = view.findViewById(R.id.e_mealtakeaway);
        cafe = view.findViewById(R.id.e_cafe);
        museum = view.findViewById(R.id.e_museum);
        nightclub = view.findViewById(R.id.e_nightclub);
        restaurant = view.findViewById(R.id.e_restaurant);
        spa = view.findViewById(R.id.e_spa);

        title = view.findViewById(R.id.e_title);

        llm = new LinearLayoutManager(getActivity());
        list.setHasFixedSize(true);
        list.setLayoutManager(llm);

        myloc = view.findViewById(R.id.e_location);
        myloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
//
        bar.setOnClickListener(this);
        bakery.setOnClickListener(this);
        mealdelivery.setOnClickListener(this);
        mealtakeaway.setOnClickListener(this);
        cafe.setOnClickListener(this);
        museum.setOnClickListener(this);
        nightclub.setOnClickListener(this);
        restaurant.setOnClickListener(this);
        spa.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.e_bar:
                title.setText(getResources().getString(R.string.bar));
                SearchNearBy(IamSingle.nearByLoc(lati, lngi, "bar"));
                break;

            case R.id.e_bakery:
                title.setText(getResources().getString(R.string.bakery));
                SearchNearBy(IamSingle.nearByLoc(lati, lngi, "bakery"));
                break;

            case R.id.e_mealdelivery:
                title.setText(getResources().getString(R.string.mealdelivery));
                SearchNearBy(IamSingle.nearByLoc(lati, lngi, "meal_delivery"));
                break;

            case R.id.e_mealtakeaway:
                title.setText(getResources().getString(R.string.mealtakeaway));
                SearchNearBy(IamSingle.nearByLoc(lati, lngi, "meal_takeaway"));
                break;

            case R.id.e_cafe:
                title.setText(getResources().getString(R.string.cafe));
                SearchNearBy(IamSingle.nearByLoc(lati, lngi, "cafe"));
                break;

            case R.id.e_museum:
                title.setText(getResources().getString(R.string.museum));
                SearchNearBy(IamSingle.nearByLoc(lati, lngi, "museum"));
                break;

            case R.id.e_nightclub:
                title.setText(getResources().getString(R.string.nightclub));
                SearchNearBy(IamSingle.nearByLoc(lati, lngi, "night_club"));
                break;

            case R.id.e_restaurant:
                title.setText(getResources().getString(R.string.restaurant));
                SearchNearBy(IamSingle.nearByLoc(lati, lngi, "restaurant"));
                break;

            case R.id.e_spa:
                title.setText(getResources().getString(R.string.spa));
                SearchNearBy(IamSingle.nearByLoc(lati, lngi, "spa"));
                break;
        }
    }

    void SearchNearBy(String url){
        parentIS.clear();
        title.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            if(jo.getString("status").equals("ZERO_RESULTS")){
                                Toast.makeText(getActivity(), "No results found", Toast.LENGTH_LONG).show();
                            } else {
                                JSONArray ja = jo.getJSONArray("results");
                                for(int i = 0; i < ja.length(); ++i){
                                   JSONObject jo1 = ja.getJSONObject(i);
                                   JSONArray ja1 = jo1.getJSONArray("types");
                                   String s = "";
                                   for(int j = 0; j < ja1.length(); ++j){
                                       if(j == 0){
                                           s = s + ja1.getString(j);
                                       }else{
                                           s = s + ", " + ja1.getString(j);
                                       }
                                   }

                                   childIS = new ArrayList<>();
                                   childIS.add(new ChildI(jo1.getString("vicinity"), s));
                                   parentIS.add(new ParentI(jo1.getString("name"), jo1.getString("icon"), childIS));
                                }

                                if(parentIS.size() == 0){
                                    Toast.makeText(getActivity(), "No Results found" ,Toast.LENGTH_LONG).show();
                                }

                                myAdapter = new MyAdapter(parentIS, getActivity());
                                list.setAdapter(myAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError){
                    Toast.makeText(getActivity(), "Connectivity Issue", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), error.networkResponse.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        IamSingle.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    void getLocation() {
        FusedLocationProviderClient client;
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Location Permission not enabled", Toast.LENGTH_LONG).show();
            return;
        }
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lati = String.valueOf(location.getLatitude());
                    lngi = String.valueOf(location.getLongitude());
                    Toast.makeText(getActivity(), "Your location is been captured", Toast.LENGTH_LONG).show();
                    hsv.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Could not find your location", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
