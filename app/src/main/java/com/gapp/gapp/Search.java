package com.gapp.gapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Search extends Fragment {

    EditText searchloc;
    ImageButton proceed;
    ArrayList<ParentI> parentIS;
    ArrayList<ChildI> childIS;
    RecyclerView list;
    LinearLayoutManager llm;
    MyAdapter myAdapter;

    public Search() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);

        searchloc = view.findViewById(R.id.s_search);
        proceed = view.findViewById(R.id.s_proceed);
        list = view.findViewById(R.id.s_list);

        parentIS = new ArrayList<>();

        llm = new LinearLayoutManager(getActivity());
        list.setHasFixedSize(true);
        list.setLayoutManager(llm);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = searchloc.getText().toString();
                if(s.isEmpty()){
                    Toast.makeText(getActivity(), "Empty Search", Toast.LENGTH_SHORT).show();
                } else {
                    s = s.replaceAll(" ", "+");
                    SearchIt(IamSingle.textSearch(s));
                }
                IamSingle.hideKeyboard(getActivity(), v);
            }
        });

        return view;
    }

    void SearchIt(String url){
        parentIS.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            if(jo.getString("status").equals("ZERO_RESULTS")){
                                Toast.makeText(getActivity(), "Path cannot be found", Toast.LENGTH_LONG).show();
                            } else {
                                JSONArray ja = jo.getJSONArray("results");
                                for(int i = 0; i < ja.length(); ++i){
                                    JSONObject jo1 = ja.getJSONObject(i);
                                    JSONArray ja1 = jo1.getJSONArray("types");
                                    String s = "";
                                    for(int j = 0; j < ja1.length(); ++j){
                                        if(j == 0){
                                            s = s + ja1.getString(j);
                                        }else {
                                            s = s + ", " + ja1.getString(j);
                                        }
                                    }

                                    childIS = new ArrayList<>();
                                    childIS.add(new ChildI(jo1.getString("formatted_address"), s));
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
                }
                else {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        IamSingle.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
