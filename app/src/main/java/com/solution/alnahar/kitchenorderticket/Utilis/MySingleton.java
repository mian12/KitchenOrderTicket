package com.solution.alnahar.kitchenorderticket.Utilis;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Engr Shahbaz on 6/21/2016.
 */

  public  class MySingleton extends Application {


    private RequestQueue mRequestQueue;
    private static MySingleton mInstance;

    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        getReqQueue();
    }



    public static synchronized MySingleton getInstance() {

        if (mInstance == null) {

            mInstance = new MySingleton();
        }
        return mInstance;


    }

    public RequestQueue getReqQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToReqQueue(Request<T> req, String tag) {

        getReqQueue().add(req);
    }

    public <T> void addToReqQueue(Request<T> req) {

        getReqQueue().add(req);
    }

    public void cancelPendingReq(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}
