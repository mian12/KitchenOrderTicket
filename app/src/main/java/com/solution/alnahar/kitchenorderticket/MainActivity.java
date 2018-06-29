package com.solution.alnahar.kitchenorderticket;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.solution.alnahar.kitchenorderticket.Cart.CartActivity;
import com.solution.alnahar.kitchenorderticket.Categories.CategoriesGridMenu;
import com.solution.alnahar.kitchenorderticket.DBConnection.DbConnection;
import com.solution.alnahar.kitchenorderticket.SqlLiteDB.SQLiteDatabaseHelper;
import com.solution.alnahar.kitchenorderticket.Utilis.MyApplication;
import com.solution.alnahar.kitchenorderticket.Utilis.SharedPreferenceClass;
import com.solution.alnahar.kitchenorderticket.model.CategoriesModel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ParentActivity implements View.OnClickListener {


    Button login;
    EditText usernameEditText, passwordEditText;

    public TextInputLayout tilName, tilPassword;
    private ProgressDialog dialog;
    ImageView settingButton;
    Connection conn;
    ResultSet resultset, resaultSetAfterLogin;
    SQLiteDatabaseHelper databaseHelper;
    public static ArrayList<CategoriesModel> categoriesModelArrayList = null;

    public EditText etUserName, etPassword;
    private static int TIME_OUT = 1000 * 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_log_in);

        login = findViewById(R.id.loginButton);
        usernameEditText = findViewById(R.id.editTextUserName);
        passwordEditText = findViewById(R.id.editTextPassword);

        context = this;

        dialog = new ProgressDialog(context);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tilName = (TextInputLayout) findViewById(R.id.tilName);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);

        settingButton = findViewById(R.id.setting);

        etUserName.addTextChangedListener(new CustomTextWatcher(etUserName));
        etPassword.addTextChangedListener(new CustomTextWatcher(etPassword));

        login.setOnClickListener(this);
        settingButton.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginButton:

                hideKeyboard((ParentActivity) context);

                if (getEditTextValue(etUserName).isEmpty()) {
                    etUserName.setError("Enter User Name");
                    return;
                }
                if (getEditTextValue(etPassword).isEmpty()) {
                    etPassword.setError("Enter Password");
                    return;
                }

                CheckLogin checkLogin = new CheckLogin();
                checkLogin.execute("");



                break;

            case R.id.setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));

                break;

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
//        if (dialog!=null && dialog.isShowing())
//        {
//            dialog.dismiss();
//            dialog=null;
//        }

    }

    @Override
    protected void onStop() {
        super.onStop();

//        if (dialog!=null && dialog.isShowing())
//        {
//            dialog.dismiss();
//            dialog=null;
//        }
    }

    public boolean validationForUserName() {
        if (getEditTextValue(etUserName).isEmpty()) {
            etUserName.setError("Enter User Name");
            requestFocus(etUserName);
            return false;
        } else {
            etUserName.setError(null);
        }
        return true;
    }

    public boolean validationForPassword() {
        if (getEditTextValue(etPassword).isEmpty()) {
            etPassword.setError("Enter Password");
            requestFocus(etPassword);
            return false;
        } else {
            etPassword.setError(null);
        }
        return true;
    }

    public String getEditTextValue(EditText editText) {
        if (editText != null && editText.length() > 0) {
            return editText.getText().toString().trim();
        }
        return "";
    }


    public class CustomTextWatcher implements TextWatcher {

        public View view;

        public CustomTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etUserName:
                    validationForUserName();
                    break;
                case R.id.etPassword:
                    validationForPassword();
                    break;
            }
        }
    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean IsSuccess = false;


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

            MyApplication.userName = etUserName.getText().toString();
            MyApplication.password = etPassword.getText().toString();

            if (MyApplication.userName.trim().equals("") && MyApplication.password.trim().equals("")) {
                z = "Please enter username and password";
            } else {
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
                            z = "Invalid Credientals";
                            IsSuccess = false;

                        }

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
                            if (resaultSetAfterLogin != null) {
                                resaultSetAfterLogin.close();
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (s.equalsIgnoreCase("Login Successful")) {

                Intent intent = new Intent(MainActivity.this, CategoriesGridMenu.class);
                startActivity(intent);
                finish();


            } else {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Date getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date date = new Date();

        return date;

    }
}