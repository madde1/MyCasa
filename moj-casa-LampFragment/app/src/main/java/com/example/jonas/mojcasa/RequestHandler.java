package com.example.jonas.mojcasa;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonas on 2018-04-01.
 */

public class RequestHandler {
    private static RequestHandler mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;
    private static final String TAG = MainActivity.class.getName();

    private RequestHandler(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RequestHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestHandler(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void getRequest(String url, final ResponseListener listener) {
        mRequestQueue = getRequestQueue();
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Response :" + response);
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error: " + error.toString());
                listener.onError(error.toString());
            }
        });
        addToRequestQueue(jsonObjRequest);
    }

    public void getRequest(String url, final String apiToken, final ResponseListener listener) {
        mRequestQueue = getRequestQueue();
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Response:" + response);
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error: " + error.toString());
                listener.onError(error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json;charset=utf-8");
                headers.put("Authorization", "Bearer " + apiToken);
                return headers;
            }
        };
        addToRequestQueue(jsonObjRequest);
    }

    public void postRequest(String url, JSONObject jsonObj) {
        mRequestQueue = getRequestQueue();
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "PostRequest response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error in PostRequest response: " + error.getMessage());
            }
        }) {
        };
        addToRequestQueue(jsonObjRequest);
    }

    public void putRequest(String url, final String apiToken, final ResponseListener listener) {
        mRequestQueue = getRequestQueue();
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Response :" + response);
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json;charset=utf-8");
                headers.put("Authorization", "Bearer " + apiToken);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        addToRequestQueue(jsonObjRequest);
    }
}

