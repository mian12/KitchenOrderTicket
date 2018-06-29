package com.solution.alnahar.kitchenorderticket.pendingOrders;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.solution.alnahar.kitchenorderticket.Adapter.PendingOrdersAdapter;
import com.solution.alnahar.kitchenorderticket.Adapter.cartRecyclerAdapter;
import com.solution.alnahar.kitchenorderticket.Cart.CartActivity;
import com.solution.alnahar.kitchenorderticket.DBConnection.DbConnection;
import com.solution.alnahar.kitchenorderticket.ParentActivity;
import com.solution.alnahar.kitchenorderticket.R;
import com.solution.alnahar.kitchenorderticket.model.PendingOrders;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PendingOrdersActivity extends AppCompatActivity {

    Connection conn;
    ResultSet resultset;
    RecyclerView rvPendingOrders;

    ArrayList<PendingOrders> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pending Orders");


        rvPendingOrders=findViewById(R.id.rvPendingOrders);


        GettingPendingOrders gettingPendingOrders=new GettingPendingOrders();
        gettingPendingOrders.execute("");
    }




    public class GettingPendingOrders extends AsyncTask<String, String, String> {
        String z = "";
        Boolean IsSuccess = false;

        private ProgressDialog dialog = new ProgressDialog(PendingOrdersActivity.this);;
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


                    String currentDate=getCurrentDate();

                    String query = "select distinct KOTMAIN.OrderNo as Ord#,dbo.KotMain.VrNo AS KOT#, dbo.KotMain.Vrnoa AS vrnoa, " +
                            " TABLES.tableNum as Table#,KOTMAIN.TYPE AS Type, isnull(sum(kotmain.NetAmount),0) as Amount," +
                            "convert(varchar,kotmain.Date_Time,108) as Time,dbo.Waiter.Name as Waiter " +
                            "from KotMain " +
                            "LEFT join Tables on tables.table_Id = KotMain.tableId LEFT JOIN dbo.Waiter ON dbo.Waiter.Waiter_Id=dbo.KotMain.waiterId " +
                            "where EType ='kot' and kotmain.status ='running' and vrdate='"+currentDate+"' " +
                            "group by  vrnoa,orderno,KotMain.VrNo,TABLES.tableNum,KOTMAIN.TYPE,KOTMAIN.DATE_TIME,dbo.Waiter.Name";


                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);

                    list=new ArrayList<>();

                    while (resultset.next()) {
                        String orderNO = resultset.getString("Ord#");
                        String vrnoa = resultset.getString("vrnoa");
                        String kOTNo = resultset.getString("KOT#");
                        String tableNO = resultset.getString("Table#");
                        String type = resultset.getString("Type");

                        String amount = resultset.getString("Amount");
                        String time= resultset.getString("Time");
                        String waiter = resultset.getString("Waiter");



                        PendingOrders object=new PendingOrders();

                        object.setOrderNO(orderNO);
                        object.setVrnoa(vrnoa);
                        object.setkOTNo(kOTNo);
                        object.setTableNO(tableNO);
                        object.setType(type);

                        object.setAmount(amount);
                        object.setTime(time);
                        object.setWaiter(waiter);


                        list.add(object);
                        z = orderNO;
                        IsSuccess = true;


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
            }


            return z;
        }

        @Override
        protected void onPostExecute(String orderNo) {
            super.onPostExecute(orderNo);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            try {
                if (list.size() > 0) {


                    LinearLayoutManager itemsLayoutManager = new LinearLayoutManager(PendingOrdersActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvPendingOrders.setLayoutManager(itemsLayoutManager);

                    PendingOrdersAdapter adapter = new PendingOrdersAdapter(PendingOrdersActivity.this, list);


                    rvPendingOrders.setAdapter(adapter);


                }
            }
            catch (Exception e)
            {
                e.getMessage();
            }





        }
    }



    public  String  getCurrentDate()
    {
        String currentDate=null;

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date today = Calendar.getInstance().getTime();
        currentDate = df.format(today);

        return  currentDate;

    }



}