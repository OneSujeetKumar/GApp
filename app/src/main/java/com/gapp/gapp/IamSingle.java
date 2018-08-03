package com.gapp.gapp;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Sujeet on 11/9/2017.
 */

class IamSingle {

    private static IamSingle mInstance;
    private RequestQueue requestQueue;
    private Context context;

    private final static String LOC_API = "https://maps.googleapis.com/maps/api/place/";
    private final static String KEY = "AIzaSyDhTgx95ZqpV_Sq0Nr6H7s58R4bzH2pwik";

    private IamSingle(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue(){

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    static synchronized IamSingle getInstance(Context context){
        if(mInstance == null){
            mInstance = new IamSingle(context);
        }
        return mInstance;
    }

    <T> void addToRequestQueue(Request<T> request){ requestQueue.add(request); }

    public static String nearByLoc(String lat, String lng, String type){
        return LOC_API + "nearbysearch" + "/json?location=" + lat + "," + lng
                + "&radius=1500&type="+ type +"&key=" + KEY;
    }

    public static String textSearch(String name){
        return LOC_API + "textsearch/json?query=" + name
                + "&type=restaurant&key=" + KEY;
    }

    static void hideKeyboard(Context ctx, View view){
        InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
