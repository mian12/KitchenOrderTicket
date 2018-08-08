package com.solution.alnahar.kitchenorderticket.DBConnection;

import android.annotation.SuppressLint;
import android.database.SQLException;
import android.os.StrictMode;
import android.util.Log;

import com.solution.alnahar.kitchenorderticket.SettingActivity;
import com.solution.alnahar.kitchenorderticket.SqlLiteDB.SQLiteDatabaseHelper;
import com.solution.alnahar.kitchenorderticket.Utilis.MyApplication;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Shahbaz on 7.02.2018.
 */

public class DbConnection {
    @SuppressLint("NewApi")
    public Connection connectionclass()

    {


//        String user="sa";
//        String database="CP_Restaurant_2017";
//        String password="123";
//        String server="192.168.8.108";




        String user=MyApplication.userDatabase;
        String password=MyApplication.passwordDatabase;
         String server=MyApplication.ipAdress;
        String database="CP_Restaurant_2017";



        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection=null;
        String ConnectionURL=null;
        try
        {


            Class.forName("net.sourceforge.jtds.jdbc.Driver");
           ConnectionURL="jdbc:jtds:sqlserver://"+server+";"+"DatabaseName="+database+";user="+user+";password="+password+";"+"integratedSecurity=false;";
            //ConnectionURL="jdbc:jtds:sqlserver://"+server+";"+"DatabaseName="+database+";user="+user+";password="+password;
            connection= DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1:",se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2:",e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3:",e.getMessage());
        }
        return connection;

    }
}
