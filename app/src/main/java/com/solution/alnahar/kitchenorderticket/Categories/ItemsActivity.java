package com.solution.alnahar.kitchenorderticket.Categories;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.solution.alnahar.kitchenorderticket.Adapter.ItemsAdapter;
import com.solution.alnahar.kitchenorderticket.DBConnection.DbConnection;
import com.solution.alnahar.kitchenorderticket.MainActivity;
import com.solution.alnahar.kitchenorderticket.ParentActivity;
import com.solution.alnahar.kitchenorderticket.R;
import com.solution.alnahar.kitchenorderticket.SqlLiteDB.SQLiteDatabaseHelper;
import com.solution.alnahar.kitchenorderticket.Utilis.MyApplication;
import com.solution.alnahar.kitchenorderticket.model.CartArrayModel;
import com.solution.alnahar.kitchenorderticket.model.ItemModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ItemsActivity extends ParentActivity {

    private  String categoryName,subCategoryName,catId,imagePath;
    Connection conn;
    ResultSet resultset;
    public RecyclerView rvItems;
    ArrayList<ItemModel> itemModelArrayList;
    ItemsAdapter adapter;

    public SQLiteDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.items);


        context = this;

        databaseHelper = new SQLiteDatabaseHelper(ItemsActivity.this);


        try {

            Intent intent=getIntent();
            categoryName = getIntent().getExtras().getString("CategoryName");
            catId = getIntent().getExtras().getString("catId");
          // Bitmap bitmap =  (Bitmap) intent.getParcelableExtra("imagePath");
          //  subCategoryName = getIntent().getExtras().getString("SubCategoryName");
        }
        catch (Exception e)
        {
            e.getMessage();
        }



        rvItems = (RecyclerView) findViewById(R.id.rvItems);

        LinearLayoutManager itemsLayoutManager = new LinearLayoutManager(ItemsActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(itemsLayoutManager);



        GetItemsAsyncTask getItemsAsyncTask= new GetItemsAsyncTask();
        getItemsAsyncTask.execute("");

    }


    @Override
    protected void onResume() {
        super.onResume();
        // we call again  thread because we wana update the values of items on recyclerview list  which get
       // complete data list from  main databas then  on Post method  compare that list with sqlite to show updated results
        // thats why i called here again  backend thread
        GetItemsAsyncTask getItemsAsyncTask= new GetItemsAsyncTask();
        getItemsAsyncTask.execute("");


        /// just upadte the cart  method to get  latest value
        ((ParentActivity) context).updateCartCounter();
    }

    public class GetItemsAsyncTask extends AsyncTask<String, String, String> {
        String response = "";

        private ProgressDialog dialog = new ProgressDialog(ItemsActivity.this);;
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


                    String query="select * from Item where catId="+catId;

                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);

                    ResultSetMetaData metadata = resultset.getMetaData();
                    int numberOfColumns = metadata.getColumnCount();

                    itemModelArrayList=new ArrayList<>();


                    while (resultset.next()) {
                        String itemsName = resultset.getString("item_des");
                        String itemsId = resultset.getString("item_id");
                        String srate = String.valueOf(resultset.getFloat("srate"));

                        String uom = resultset.getString("uom");

                        ItemModel  itemModel=new ItemModel();
                        itemModel.setName(itemsName);
                        itemModel.setItemid(itemsId);
                        itemModel.setUom(uom);
                        itemModel.setPrice(itemsId);
                        itemModelArrayList.add(itemModel);
                    }




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

            try {
                    if (itemModelArrayList.size() > 0) {


                        ArrayList<ItemModel> cartArrayList = databaseHelper.getCartItems();


                        for (ItemModel mainItem : itemModelArrayList) {

                            for (ItemModel _cartModel : cartArrayList) {
                                if (mainItem.getItemid().equalsIgnoreCase(_cartModel.getItemid()) && _cartModel.getQuantity() > 0) {
                                    //  Log.e("cart","yes");
                                    String qty = _cartModel.getQuantity() + "";
                                    String amount = _cartModel.getAmount() + "";
                                    mainItem.setQuantity(Integer.parseInt(qty));
                                    mainItem.setAmount(Integer.parseInt(amount));
                                    break;
                                } else {
                                    // Log.e("cart","not found");
                                }
                            }


                        }


                        adapter = new ItemsAdapter(ItemsActivity.this, itemModelArrayList, databaseHelper);

                        rvItems.setAdapter(adapter);

                    }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }


}
