package com.solution.alnahar.kitchenorderticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.solution.alnahar.kitchenorderticket.Categories.CategoriesGridMenu;
import com.solution.alnahar.kitchenorderticket.DBConnection.DbConnection;
import com.solution.alnahar.kitchenorderticket.Utilis.MyApplication;
import com.solution.alnahar.kitchenorderticket.Utilis.SharedPreferenceClass;
import com.solution.alnahar.kitchenorderticket.model.CategoriesModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SplashScreen extends AppCompatActivity {


    Connection conn;
    ResultSet resultset,resaultSetAfterLogin;
    public ArrayList<CategoriesModel> categoriesModelArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       // setContentView(R.layout.test_cart);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                SharedPreferenceClass.getInstance(getApplicationContext(), "KitchenOrderTicket", MODE_PRIVATE);

                if (SharedPreferenceClass.getSessionFunc("session").equalsIgnoreCase("true")) {


                    String string = SharedPreferenceClass.getSessionTimeFunc("time");

                    DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
                    try {
                        Date savedDate = format.parse(string);
                        Date currentDate = getCurrentDate();
                        long difference = currentDate.getTime() - savedDate.getTime();

                        long diffSeconds = difference / 1000 % 60;
                        long diffMinutes = difference / (60 * 1000) % 60;
                        long diffHours = difference / (60 * 60 * 1000) % 24;
                        long diffDays = difference / (24 * 60 * 60 * 1000);
                        if (diffMinutes > 10) {

                            Intent loginIntent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(loginIntent);
                            finish();
                        } else {

                            CheckLogin checkLogin = new CheckLogin();
                            checkLogin.execute("");


                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {

                    Intent loginIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(loginIntent);
                    finish();
                }

            }
        }, 1500);
    }


    public Date getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date date = new Date();

        return date;

    }


    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean IsSuccess = false;
        private ProgressDialog dialog = new ProgressDialog(SplashScreen.this);
        ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("please wait");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            DbConnection connect = new DbConnection();


            try {
                conn = connect.connectionclass();
                if (conn == null) {
                    z = "Please check internet connection";
                } else {



                    String encoded_password = Base64.encodeToString(MyApplication.password.getBytes("utf-8"), Base64.NO_WRAP);

                    String query1 = "select * from  login where Uname='" + MyApplication.userName + "' and Pass='" + encoded_password + "'";


                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query1);


                    if (resultset.next()) {

                        String groupId = resultset.getString("Group_Id");

                        MyApplication.groupId=groupId;

                        Log.d("group_id",groupId);


                        z = "Login Successful";


                        String query = "select catid,name,img_path,isnull(Img,'')Img from  catagory order by catid ";
                        Statement stmt1 = conn.createStatement();
                        resaultSetAfterLogin = stmt1.executeQuery(query);


                        categoriesModelArrayList = new ArrayList<>();


                        while (resaultSetAfterLogin.next()) {
                            String category = resaultSetAfterLogin.getString("name");
                            String catId = resaultSetAfterLogin.getString("catid");

                            byte[] imgData = resaultSetAfterLogin.getBytes("Img");
                            Bitmap bitmap;

                            if (imgData.length > 0) {
                                bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                            } else {
                                Drawable myDrawable = getResources().getDrawable(R.drawable.defaultimage);
                                bitmap = ((BitmapDrawable) myDrawable).getBitmap();
                            }


                            CategoriesModel categoriesModel = new CategoriesModel();
                            categoriesModel.setCategory(category);
                            categoriesModel.setCatId(catId);

                            categoriesModel.setImageBitamp(bitmap);


                            categoriesModelArrayList.add(categoriesModel);
                        }


                        // Log.d("Size Category", categoriesModelArrayList.size() + "");

                        SharedPreferenceClass.getInstance(getApplicationContext(), "KitchenOrderTicket", MODE_PRIVATE);

                        Date dateee = getCurrentDate();

                        SharedPreferenceClass.setSessionFunc("session", "true");
                        SharedPreferenceClass.setSessionTimeFunc("time", dateee + "");





                    } else {
                        z = "someting went wrong";
                        IsSuccess = false;

                    }
                    resultset.close();
                }

            } catch (Exception ex) {
                IsSuccess = false;
                z = ex.getMessage();

            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (resultset != null) {
                            resultset.close();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                //resultset.

            }

            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            try {
                if (MainActivity.categoriesModelArrayList.size() > 0) {
                    Intent loginIntent = new Intent(SplashScreen.this, CategoriesGridMenu.class);
                    startActivity(loginIntent);
                    finish();
                }
            } catch (Exception e) {
                e.getMessage();
            }


            if (s.equalsIgnoreCase("Login Successful"))
            {

            }
            else
            {
                Toast.makeText(SplashScreen.this, s, Toast.LENGTH_SHORT).show();
            }


        }
    }

}
