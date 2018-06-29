package com.solution.alnahar.kitchenorderticket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.iconics.utils.Utils;
import com.solution.alnahar.kitchenorderticket.SqlLiteDB.SQLiteDatabaseHelper;
import com.solution.alnahar.kitchenorderticket.Utilis.MyApplication;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity  implements  View.OnClickListener{


    Button saveBtn;
    EditText edtIpAdress, edtUserName, edtPassword;
    SQLiteDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Setting");

        db = new SQLiteDatabaseHelper(SettingActivity.this);


        edtIpAdress = findViewById(R.id.editTextIpAdress);
        edtUserName = findViewById(R.id.editTextUserName);
        edtPassword = findViewById(R.id.editTextPassword);

        saveBtn = findViewById(R.id.buttonSave);


        ArrayList<String> list=db.getIpAdress();

        try {

            if(list.size()>0)
            {
                edtIpAdress.setText(list.get(0));
                edtUserName.setText(list.get(1));
                edtPassword.setText(list.get(2));
            }
//           String ip= db.getIpAdress();
//            if (!ip.equalsIgnoreCase("")) {
//                edtIpAdress.setText(ip);
//            }
        } catch (Exception e) {
            e.getMessage();
        }

     saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:


                try {

                    if (TextUtils.isEmpty(edtIpAdress.getText().toString()) ||  TextUtils.isEmpty(edtUserName.getText().toString())  ||  TextUtils.isEmpty(edtPassword.getText().toString())) {

                        Toast.makeText(SettingActivity.this, "Plz fill all fields", Toast.LENGTH_SHORT).show();
                    } else {


                        String ip = edtIpAdress.getText().toString();

                        String userName = edtUserName.getText().toString();
                        String pass = edtPassword.getText().toString();


                        db.ipConfiguration(ip, userName, pass);

                        ArrayList<String> list=db.getIpAdress();

                        MyApplication.ipAdress =list.get(0);

                        MyApplication.userDatabase =list.get(1);

                        MyApplication.passwordDatabase =list.get(2);




                        Toast.makeText(SettingActivity.this, "Ip Adress Added Successfully", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(SettingActivity.this, MainActivity.class));
                        finish();


                    }
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
        }


    }

}
