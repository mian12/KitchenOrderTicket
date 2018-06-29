package com.solution.alnahar.kitchenorderticket.Utilis;

import android.app.Application;
import android.content.Context;

import com.solution.alnahar.kitchenorderticket.model.CartArrayModel;
import com.solution.alnahar.kitchenorderticket.model.ItemModel;

import java.util.ArrayList;

/**
 * Created by Mian Shahbaz Idrees on 27-Jan-18.
 */

public class MyApplication extends Application {


    public static int cartCounter = 0;
    public static int totalAmount = 0;



    public static String percent ="0.0";
    public static String tax ="0.0";
    public static String service ="0.0";





    public static String editModeVoucherAll ="";
    public static String orderNo ="";
    public static String kotNo ="";
    public static String foodType ="";
    public static String freeOrRunning ="0";
    public static String tableId ="";
    public static String waiterId ="";




    public  static  String userName=null;
    public  static  String password=null;
    public  static  String groupId=null;

    public  static  String insertRights=null;
    public  static  String updateRights=null;
    public  static  String deleteRights=null;
    public  static  String printRights=null;



    public  static  String ipAdress=null;
    public  static  String userDatabase=null;
    public  static  String passwordDatabase=null;

    private static MyApplication mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());
    }

    public static MyApplication getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }


}
