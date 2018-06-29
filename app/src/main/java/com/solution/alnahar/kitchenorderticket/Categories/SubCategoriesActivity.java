package com.solution.alnahar.kitchenorderticket.Categories;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.solution.alnahar.kitchenorderticket.Adapter.SubCategoriesAdapter;
import com.solution.alnahar.kitchenorderticket.DBConnection.DbConnection;
import com.solution.alnahar.kitchenorderticket.MainActivity;
import com.solution.alnahar.kitchenorderticket.ParentActivity;
import com.solution.alnahar.kitchenorderticket.R;
import com.solution.alnahar.kitchenorderticket.Utilis.MyApplication;
import com.solution.alnahar.kitchenorderticket.model.CartArrayModel;
import com.solution.alnahar.kitchenorderticket.model.CategoriesModel;
import com.solution.alnahar.kitchenorderticket.model.SubCategoriesModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class SubCategoriesActivity extends ParentActivity implements View.OnClickListener {

    private  RecyclerView rvSubCategories;
    private  String  categoryName=null;

    Connection conn;
    ResultSet resultset;

    public static ArrayList<SubCategoriesModel> subCategoriesModelArrayList=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategories);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.sub_categories);

        initView();
    }

    private void initView() {


        context = this;

        categoryName=getIntent().getExtras().getString("Category");




        GetSubCategoryAsyncTask  getSubCategoryAsyncTask= new GetSubCategoryAsyncTask();
        getSubCategoryAsyncTask.execute("");


        rvSubCategories = (RecyclerView) findViewById(R.id.rvSubCategories);
        LinearLayoutManager itemsLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvSubCategories.setLayoutManager(itemsLayoutManager);
       // rvSubCategories.addItemDecoration(new VerticalSpaceItemDecoration((int) PixelDpConverter.convertPixelsToDp(getResources().getInteger(R.integer.dp5), context)));





    }


    @Override
    protected void onResume() {
        super.onResume();

        /// its called whenever we add the items then back  change the counter of cart
            ((ParentActivity) context).updateCartCounter();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {



        }

    }




    public class GetSubCategoryAsyncTask extends AsyncTask<String, String, String> {
        String response = "";

        private ProgressDialog dialog = new ProgressDialog(SubCategoriesActivity.this);;
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
                        response = "Please check internet connection";
                    } else {


                        String query= "select distinct sub_catagory from Item where catagory='"+categoryName+"'";



                        Statement stmt = conn.createStatement();
                        resultset = stmt.executeQuery(query);

                        ResultSetMetaData metadata = resultset.getMetaData();
                        int numberOfColumns = metadata.getColumnCount();

                        subCategoriesModelArrayList=new ArrayList<>();


                           while (resultset.next()) {
                                String sub_category = resultset.getString("sub_catagory");


                               SubCategoriesModel subCategoriesModel=new SubCategoriesModel();
                               subCategoriesModel.setName(sub_category);



                               subCategoriesModelArrayList.add(subCategoriesModel);
                            }






                        Log.d("Size Sub_Category",subCategoriesModelArrayList.size()+"");




                        resultset.close();
                    }

                } catch (Exception ex) {

                    response = ex.getMessage();

                }
                finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        try {
                            if (resultset!=null)
                            {
                                resultset.close();
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                }


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            if (subCategoriesModelArrayList.size()>0)
            {
                SubCategoriesAdapter adapter=new SubCategoriesAdapter(SubCategoriesActivity.this,subCategoriesModelArrayList,categoryName);

                rvSubCategories.setAdapter(adapter);
            }
        }
    }


}
