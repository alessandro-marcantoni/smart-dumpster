package com.example.dumpstermobileapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dumpstermobileapp.MainActivity;
import com.google.common.collect.ImmutableMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class HttpManager {
    private final static String TAG = "HTTP REQUEST";
    private static final String LOG = "HTTP LOG";
    private static final String THROW_SUCCESS_URL = "http://192.168.1.201/sd-service/on-throw-success.php";
    private static final String IS_AVAILABLE_URL = "http://192.168.1.201/sd-service/is-dumpster-available.php";
    private static final Map<String, Pair<String, String>> HTTP_REQUESTS;

    static {
        HTTP_REQUESTS = ImmutableMap.of(
                "THROW_SUCCESS", new Pair<>(THROW_SUCCESS_URL, "POST"),
                "CHECK_AVAILABLE", new Pair<>(IS_AVAILABLE_URL, "GET")
        );
    }

    private MainActivity activity;
    private boolean isNetworkConnected = false;
    private RequestQueue requestQueue;
    private boolean isDumpsterAvailable = false;

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            isNetworkConnected = true;
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            isNetworkConnected = false;
            activity.showHttpPairingOption();
        }
    };

    public HttpManager(MainActivity activity) {
        this.activity = activity;
        this.requestQueue = Volley.newRequestQueue(this.activity);
    }

    /*public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            this.isNetworkConnected = networkInfo.isConnectedOrConnecting();
            if (!this.isNetworkConnected) {
                this.activity.showHttpPairingOption();
            }
        } else {
            this.isNetworkConnected = false;
            this.activity.showHttpPairingOption();
        }
    }*/

    public boolean isNetworkConnected() {
        return this.isNetworkConnected;
    }

    public boolean isDumpsterAvailable() {
        return this.isDumpsterAvailable;
    }

    public void throwSuccess() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, THROW_SUCCESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(LOG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG, error.toString());
            }
        });
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    public void checkAvailable() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, IS_AVAILABLE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    isDumpsterAvailable = response.get("available").toString().equals(C.AVAILABLE) ? true : false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                activity.onResponseAvailability();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LAB", error.toString());
            }
        });
        jsonObjectRequest.setTag(TAG);
        this.requestQueue.add(jsonObjectRequest);
    }

    public void registerNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(networkCallback);
            } else {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                this.isNetworkConnected = networkInfo != null && networkInfo.isConnected();
            }
        }
    }

    public void unregisterNetworkCallback() {
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
        ConnectivityManager connectivityManager = (ConnectivityManager)this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(this.networkCallback);
        }
    }


}
