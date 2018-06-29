package com.solution.alnahar.kitchenorderticket.Cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.solution.alnahar.kitchenorderticket.Adapter.SpinnerSelectValueAdapter;
import com.solution.alnahar.kitchenorderticket.Adapter.cartRecyclerAdapter;
import com.solution.alnahar.kitchenorderticket.Categories.CategoriesGridMenu;
import com.solution.alnahar.kitchenorderticket.DBConnection.DbConnection;
import com.solution.alnahar.kitchenorderticket.ParentActivity;
import com.solution.alnahar.kitchenorderticket.R;
import com.solution.alnahar.kitchenorderticket.SqlLiteDB.SQLiteDatabaseHelper;
import com.solution.alnahar.kitchenorderticket.Utilis.MyApplication;
import com.solution.alnahar.kitchenorderticket.model.ItemModel;
import com.solution.alnahar.kitchenorderticket.model.GetVrnoAllBySingleIdModel;
import com.solution.alnahar.kitchenorderticket.model.TabelModel;
import com.solution.alnahar.kitchenorderticket.model.WaiterModel;
import com.solution.alnahar.kitchenorderticket.pendingOrders.PendingOrdersActivity;
import com.solution.alnahar.kitchenorderticket.print.PrintForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatCodePointException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class CartActivity extends ParentActivity  implements  View.OnClickListener{


    public SQLiteDatabaseHelper databaseHelper;



    ArrayList<TabelModel> tabelNumberList;
    ArrayList<WaiterModel> waiterNumberList;


    public cartRecyclerAdapter adapter;

    public RecyclerView rvCart;

ImageView resetButton,saveButton,deleteButton,printButton,pendingOrdersButton;

    TextView qtyValueTextView, amountValueTextView, discValueTextView, taxValueTextView, serviceChargesValueTextView, netAmountValueTextView;
    EditText editTextDiscount, editTextTax, editTextServiceCharges;

    TextView orderValue,kotValue;
    EditText editTextVrValue;
    ImageView vrUpImageView,vrDownImageView;

    Spinner tableSpinner,waiterSpinner;
    LinearLayout cartLinearLayout;


    float TotalAmount = 0;

    int vrnoa_all=0;

    Connection conn;
    ResultSet resultset;

    String[] waiterNameArray=null;
    String[] tableNumberArray=null;

    public  boolean flagtable;
    public  boolean flagWaiter;


    String waiter_ID=null;
    String waiter_NAME=null;
    String table_ID=null;
    String table_NAME=null;
    String freeOrRunning=null;
    String diinOrDelivery=null;




    String ETYPE="KOT";
    String DESCRIPTION="DES";
    String STATUS=null;
    String NOFG=null;
    String KOT_ID=null;
    String VR=null;
    String ORDER=null;
    String KOT=null;
    String TABLE_ID="0.0";
    String WAITER_ID="0.0";
    String RADIO_FREE_RUNNING=null;
    String RADIO_DIIN_DELIVERY_TAKEWAY=null;
    String TOTAL_QTY="0.0";
    String TOTAL_AMOUNT=null;
    String DISCOUNT_RUPEES=null;
    String DISCOUNT_PERCENT="0";
    String TAX_RUPEES=null;
    String TAX_PERCENT=null;
    String SERVICE_RUPEES=null;
    String SERVICE_PERCENT=null;
    String NET_AMOUNT=null;

    int vrCounter=0;
    String vrnoaFromPendingOrders=null;

public  ArrayList<GetVrnoAllBySingleIdModel> getVrnoAllModelArrayList=null;

boolean  SPINNER_TABLE_INVISIBLE=false;


    private RadioGroup mRadioGroupNewRunning,mRadioGroupDininDeliver;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Tahoma Regular font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


      // setContentView(R.layout.cart_activity);
        setContentView(R.layout.test_cart);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart Details");


        context = this;


        databaseHelper = new SQLiteDatabaseHelper(CartActivity.this);


        refreshCreate();



    }

    public  void refreshCreate()
    {


        editTextDiscount = findViewById(R.id.editTextDiscount);
        editTextTax = findViewById(R.id.editTextTax);
        editTextServiceCharges = findViewById(R.id.editTextService);


        try {

            vrnoaFromPendingOrders = getIntent().getExtras().getString("vrnoa");
        }
        catch (Exception e)
        {
            e.getMessage();
        }


        if (!MyApplication.percent.equalsIgnoreCase("0.0"))
        {
            editTextDiscount.setText(MyApplication.percent);
        }
        if (!MyApplication.tax.equalsIgnoreCase("0.0"))
        {
            editTextTax.setText(MyApplication.tax);
        }

        if (!MyApplication.service.equalsIgnoreCase("0.0"))
        {
            editTextServiceCharges.setText(MyApplication.service);
        }


        //cartLinearLayout=findViewById(R.id.cartLinearLayout);


        rvCart =(RecyclerView) findViewById(R.id.rvCart);


        vrUpImageView=findViewById(R.id.imageViewVrUp);
        vrDownImageView=findViewById(R.id.imageViewVrDown);



        resetButton=findViewById(R.id.reset);
        saveButton=findViewById(R.id.save);
        deleteButton=findViewById(R.id.delete);
        printButton=findViewById(R.id.print);
        pendingOrdersButton=findViewById(R.id.pendingOrders);


        resetButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        printButton.setOnClickListener(this);
        pendingOrdersButton.setOnClickListener(this);

        vrUpImageView.setOnClickListener(this);
        vrDownImageView.setOnClickListener(this);





        qtyValueTextView = findViewById(R.id.totalQtyValue);
        amountValueTextView = findViewById(R.id.amountValue);
        discValueTextView = findViewById(R.id.discValue);
        taxValueTextView = findViewById(R.id.taxValue);
        serviceChargesValueTextView = findViewById(R.id.serviceChargesValue);
        netAmountValueTextView = findViewById(R.id.netAmountValue);



        editTextVrValue=findViewById(R.id.editTextVrValue);




        editTextVrValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            // not work without set property in xml in edittext like
            // android:imeOptions="actionDone"
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    //do stuff

                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

                    databaseHelper.claerAllItems();


                    try
                    {
                        vrCounter=Integer.parseInt(v.getText().toString());
                    }
                    catch (Exception e)
                    {

                    }

                    GetVrnoAllBySingleId res=new GetVrnoAllBySingleId();
                    res.execute("");

                    return true;
                }
                return false;
            }
        });



        orderValue=findViewById(R.id.orderValue);
        kotValue=findViewById(R.id.kotValue);

        tableSpinner=findViewById(R.id.spinnerTable);
        waiterSpinner=findViewById(R.id.spinnerWaiter);







        // bydefault check......
        freeOrRunning="New";
        mRadioGroupNewRunning=findViewById(R.id.radioGroupRunning);
        mRadioGroupNewRunning.check(R.id.newOrder);


        // bydefault check......
        diinOrDelivery="dinin";
        mRadioGroupDininDeliver=findViewById(R.id.radioGroupDinin);
        mRadioGroupDininDeliver.check(R.id.dinin);


        mRadioGroupNewRunning.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.newOrder) {

                    freeOrRunning="New";

                    GetTableNumber getTableNumber=new GetTableNumber();
                    getTableNumber.execute("");

                } else  if (checkedId == R.id.running) {

                    freeOrRunning="Running";


                    GetTableNumber getTableNumber=new GetTableNumber();
                    getTableNumber.execute("");
                }

            }
        });


        mRadioGroupDininDeliver.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.dinin) {
                    //do work when radioButton1 is active
                    diinOrDelivery="dinin";

                    tableSpinner.setVisibility(View.VISIBLE);

                    SPINNER_TABLE_INVISIBLE=false;

                }
                else  if (checkedId == R.id.Delivery) {
                    //do work when radioButton2 is active
                    diinOrDelivery="Delivery";

                    tableSpinner.setVisibility(View.INVISIBLE);

                    SPINNER_TABLE_INVISIBLE=true;
                }
                else  if (checkedId == R.id.takeWay) {
                    //do work when radioButton2 is active
                    diinOrDelivery="takeWay";
                    tableSpinner.setVisibility(View.INVISIBLE);
                    SPINNER_TABLE_INVISIBLE=true;
                }
            }
        });





        if (vrnoaFromPendingOrders!=null) {

            vrCounter=Integer.parseInt(vrnoaFromPendingOrders);

            GetVrnoAllBySingleId getVrnoAllBySingleId=new GetVrnoAllBySingleId();
            getVrnoAllBySingleId.execute("");


        }
        else {

            if (!MyApplication.editModeVoucherAll.isEmpty())
            {


                vrnoa_all=Integer.parseInt(MyApplication.editModeVoucherAll);

                orderValue.setText(MyApplication.orderNo);
                kotValue.setText(MyApplication.kotNo);
                editTextVrValue.setText(MyApplication.editModeVoucherAll);

                if (MyApplication.foodType.equalsIgnoreCase("dinin"))
                {
                    diinOrDelivery="dinin";
                    MyApplication.foodType="dinin";
                    mRadioGroupDininDeliver.check(R.id.dinin);
                }
                else if(MyApplication.foodType.equalsIgnoreCase("Delivery"))
                {
                    diinOrDelivery="Delivery";
                    MyApplication.foodType="Delivery";
                    mRadioGroupDininDeliver.check(R.id.Delivery);
                }
                else
                {
                    diinOrDelivery="takeWay";
                    MyApplication.foodType="takeWay";
                    mRadioGroupDininDeliver.check(R.id.takeWay);

                }




                if (MyApplication.tableId.equalsIgnoreCase("0"))
                {

                }
                else
                {
                    GetTableNumberRetrive getTableNumberRetrive = new GetTableNumberRetrive();
                    getTableNumberRetrive.execute(MyApplication.tableId);
                }


                GetWaiterRetrive getWaiterRetrive=new GetWaiterRetrive();
                getWaiterRetrive.execute(MyApplication.waiterId);


                freeOrRunning="Running";
                mRadioGroupNewRunning=findViewById(R.id.radioGroupRunning);
                mRadioGroupNewRunning.check(R.id.running);


            }

            else {

                gettingALLData();
            }
        }




        final ArrayList<ItemModel> cartArrayList = databaseHelper.getCartItems();

        if (cartArrayList.size()>0)
        {
            // cartLinearLayout.setBackgroundColor(Color.WHITE);
        }


        LinearLayoutManager itemsLayoutManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
        rvCart.setLayoutManager(itemsLayoutManager);

        adapter = new cartRecyclerAdapter(CartActivity.this, cartArrayList, databaseHelper, qtyValueTextView, amountValueTextView, netAmountValueTextView,
                editTextDiscount, editTextTax, editTextServiceCharges, discValueTextView, taxValueTextView, serviceChargesValueTextView);


        rvCart.setAdapter(adapter);


        textchangerPercentDiscount();
        textchangerTaxPercent();
        textchangerServiceCharges();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public  void gettingALLData(){


        GetUserRights getUserRights=new GetUserRights();
        getUserRights.execute("");


        GetVrnoAll getVrnoAll=new GetVrnoAll();
        getVrnoAll.execute("");

        GetOrderNo getOrderNo=new GetOrderNo();
        getOrderNo.execute("");

        GetVrno getVrno = new GetVrno();
        getVrno.execute("");



        GetKot getKot=new GetKot();
        getKot.execute("");

        GetTableNumber getTableNumber=new GetTableNumber();
        getTableNumber.execute("");


        GetWaiter getWaiter=new GetWaiter();
        getWaiter.execute("");


    }

    public  void saveButtonRunning(){

        GetVrno getVrno = new GetVrno();
        getVrno.execute("");

        GetVrnoAll getVrnoAll=new GetVrnoAll();
        getVrnoAll.execute("");

        GetKot getKot=new GetKot();
        getKot.execute("");
    }


    public  void saveButtonFree(){

        GetVrno getVrno = new GetVrno();
        getVrno.execute("");

        GetVrnoAll getVrnoAll=new GetVrnoAll();
        getVrnoAll.execute("");

        GetOrderNo getOrderNo=new GetOrderNo();
        getOrderNo.execute("");

        GetKot getKot=new GetKot();
        getKot.execute("");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case  R.id.imageViewVrUp:
                vrCounter= Integer.parseInt(editTextVrValue.getText().toString());
                vrCounter+=1;
                editTextVrValue.setText(vrCounter+"");

                databaseHelper.claerAllItems();

                GetVrnoAllBySingleId res=new GetVrnoAllBySingleId();
                res.execute("");

                 break;

            case  R.id.imageViewVrDown:
                vrCounter= Integer.parseInt(editTextVrValue.getText().toString());
                vrCounter-=1;

                if (vrCounter<1)
                {
                    vrCounter=1;
                }

                databaseHelper.claerAllItems();

                GetVrnoAllBySingleId res1=new GetVrnoAllBySingleId();
                res1.execute("");

                break;


            case R.id.reset:
                resetDataFromSqlLite();
                break;
            case R.id.save:

                if (MyApplication.insertRights.equalsIgnoreCase("1"))
                {


                    if (vrnoa_all==0) {
                        if (freeOrRunning.equalsIgnoreCase("Running")) {
                            saveButtonRunning();
                        } else {
                            // if type is New or Free then this work
                            saveButtonFree();
                        }
                    }

                    dataInsertIntoDatabase();

                }
                else
                {
                    Toast.makeText(CartActivity.this,"Sorry you have not rights",Toast.LENGTH_SHORT).show();

                }




                break;
            case R.id.delete:



                if (MyApplication.deleteRights.equalsIgnoreCase("1"))
                {

                    try {
                        if (vrnoa_all != 0) {



                            VR = String.valueOf(validaterInteger(editTextVrValue.getText().toString()));

                            int vrnoa = new Double(VR).intValue();

                            String[] param={"KOT",vrnoa+""};

                            DeleteVoucher deleteVoucher=new DeleteVoucher();
                            deleteVoucher.execute(param);
                        }
                    }
                    catch (Exception e)
                    {
                        e.getMessage();
                    }


                }
                else
                {
                    Toast.makeText(CartActivity.this,"Sorry you have not rights",Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.print:


                if (MyApplication.printRights.equalsIgnoreCase("1"))
                {
                    final ArrayList<ItemModel> cartArrayList = databaseHelper.getCartItems();

                    PrintForm printForm=new PrintForm(CartActivity.this,cartArrayList,"Chiniot Palace",kotValue.getText().toString(),
                            waiter_NAME,table_NAME,orderValue.getText().toString(),diinOrDelivery,
                            "Al Nahar Solutions","Pax",netAmountValueTextView.getText().toString());

                }
                else
                {
                    Toast.makeText(CartActivity.this,"Sorry you have not rights",Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.pendingOrders:
                startActivity(new Intent(CartActivity.this, PendingOrdersActivity.class));
                finish();
                break;

        }

    }

    private void dataInsertIntoDatabase() {

        TABLE_ID= String.valueOf(validaterInteger(table_ID));
        WAITER_ID=String.valueOf(validaterInteger(waiter_ID));
        TOTAL_QTY=String.valueOf(validaterInteger(qtyValueTextView.getText().toString()));


        if (SPINNER_TABLE_INVISIBLE)
        {
            TABLE_ID="0";
        }


        if (TABLE_ID.equalsIgnoreCase("0.0")  || WAITER_ID.equalsIgnoreCase("0.0") || TOTAL_QTY.equalsIgnoreCase("0.0"))
        {
            Toast.makeText(this, " plz fill all fields", Toast.LENGTH_SHORT).show();
        }
        else
        {



            ETYPE="KOT";

            NOFG="nofg";


            VR=String.valueOf(validaterInteger(editTextVrValue.getText().toString()));
            ORDER=String.valueOf(validaterInteger(orderValue.getText().toString()));
            KOT=String.valueOf(validaterInteger(kotValue.getText().toString()));

            freeOrRunning="Running";
            RADIO_FREE_RUNNING=String.valueOf(freeOrRunning);
            RADIO_DIIN_DELIVERY_TAKEWAY=String.valueOf(diinOrDelivery);

            TOTAL_AMOUNT=String.valueOf(validaterInteger(amountValueTextView.getText().toString()));
            DISCOUNT_RUPEES=String.valueOf(validaterInteger(discValueTextView.getText().toString()));
            DISCOUNT_PERCENT=String.valueOf( validaterInteger(editTextDiscount.getText().toString()));
            TAX_RUPEES=String.valueOf(validaterInteger(taxValueTextView.getText().toString()));
            TAX_PERCENT=String.valueOf(validaterInteger(editTextTax.getText().toString()));
            SERVICE_RUPEES=String.valueOf(validaterInteger(serviceChargesValueTextView.getText().toString()));
            SERVICE_PERCENT=String.valueOf(validaterInteger(editTextServiceCharges.getText().toString()));
            NET_AMOUNT=String.valueOf(validaterInteger(netAmountValueTextView.getText().toString()));

            InsertKotMain insertKotMain=new InsertKotMain();
            insertKotMain.execute("");

        }


    }

    public class InsertKotMain extends AsyncTask<String, String, String> {
        String z = "";
        Boolean IsSuccess = false;

        private ProgressDialog dialog = new ProgressDialog(CartActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("please wait");
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


                   String currentDate= getCurrentDate();


                    // Set auto commit as false.
                    conn.setAutoCommit(false);


                    if (vrnoa_all!=0)
                    {
                       // delete kotdetail against Vrnoa and Etype

                        String query="delete from KotDetail where KOTID IN (SELECT KOTID FROM KOTMAIN WHERE ETYPE ='"+ETYPE+"' AND VRNOA ="+new Double(VR).intValue()+")";
                        Statement stmt = conn.createStatement();
                        int res = stmt.executeUpdate(query);

                        String UpdateQuery = "UPDATE KotMain SET  VrNo ="+new Double(KOT).intValue()+", VrNoa ="+new Double(VR).intValue()+", VrDate ='"+currentDate+"', Description ='des', " +
                            "OrderNo ="+new Double(ORDER).intValue()+", Type ='"+RADIO_DIIN_DELIVERY_TAKEWAY+"', EType ='"+ETYPE+"', Discount ="+DISCOUNT_RUPEES+", " +
                            "DiscPercent ="+DISCOUNT_PERCENT+", Charges ="+SERVICE_RUPEES+", TotalAmount ="+TOTAL_AMOUNT+", TotalQty ="+TOTAL_QTY+", " +
                            "Change =0, NetAmount ="+NET_AMOUNT+", Service_Percent ="+SERVICE_PERCENT+", tableId ="+new Double(TABLE_ID).intValue()+"," +
                            "waiterId ="+new Double(WAITER_ID).intValue()+", tax ="+TAX_RUPEES+", taxPercent ="+TAX_PERCENT+", UId =1, " +
                            "Food_Type ='', HoldPrint =0  WHERE ETYPE ='"+ETYPE+"' AND VRNOA ="+new Double(VR).intValue()+" ";


                        stmt = conn.createStatement();
                         res = stmt.executeUpdate(UpdateQuery);
                         z=res+"";

                    }else {



                        String sqlQueryKotMin = "INSERT INTO KotMain(KotId,VrNo,VrNoa,VrDate,Description,PartyId,OrderNo,Type,EType,Discount,DiscPercent,Charges,TotalAmount,TotalQty,Paid,Change,NetAmount,Other1,Service_Percent,tableId,waiterId,status,nofg,tax,taxPercent,UId,CustomerId,Other2,Food_Type,EType2,HoldPrint) " +
                                "VALUES (" + KOT_ID + "," + KOT + "," + VR + ",'" + currentDate + "','" + DESCRIPTION + "','0004'," + ORDER + ",'" + RADIO_DIIN_DELIVERY_TAKEWAY + "','" + ETYPE + "'," + DISCOUNT_RUPEES + "," + DISCOUNT_PERCENT + "," + SERVICE_RUPEES + "," + TOTAL_AMOUNT + "," + TOTAL_QTY + ",0,0," + NET_AMOUNT + ",'other1'," + SERVICE_PERCENT + "," + TABLE_ID + "," + WAITER_ID + ",'" + RADIO_FREE_RUNNING + "','" + NOFG + "'," + TAX_RUPEES + "," + TAX_PERCENT + ",1,0,'other2','" + diinOrDelivery + "','other2','0')";

                        Statement stmt = conn.createStatement();
                        int res = stmt.executeUpdate(sqlQueryKotMin);
                        z = res + "";



                        if (!SPINNER_TABLE_INVISIBLE)
                        {
                            int id = Integer.parseInt(table_ID);

                            String query = "update Tables set Status ='Running' where Table_Id =" + id + " ";
                            Statement stmt1 = conn.createStatement();
                            int res1 = stmt1.executeUpdate(query);

                        }



                    }
                    final ArrayList<ItemModel> cartArrayList = databaseHelper.getCartItems();

                    for (int i = 0; i < cartArrayList.size(); i++) {


                        String sqlQueryKotDetail = "insert into KotDetail (KotId,Item_Id,Qty,Rate,Amount,KOT_NO,other2,Type,Godown1,Godown2,Status) values (?,?,?,?,?,?,?,?,?,?,?)";


                        PreparedStatement ps = conn.prepareStatement(sqlQueryKotDetail);

                        if (vrnoa_all!=0)
                        {
                            if (!MyApplication.editModeVoucherAll.isEmpty())
                            {
                                ps.setInt(1, Integer.parseInt(MyApplication.editModeVoucherAll));
                            }
                            else
                            {
                                ps.setInt(1, new Double(getVrnoAllModelArrayList.get(0).getKotId()).intValue());
                            }

                        }
                        else
                        {
                            ps.setInt(1, Integer.parseInt(KOT_ID));
                        }


                        ps.setString(2, cartArrayList.get(i).getItemid() + "");
                        ps.setFloat(3, cartArrayList.get(i).getQuantity());
                        ps.setFloat(4, Float.parseFloat(cartArrayList.get(i).getPrice()));
                        ps.setFloat(5, cartArrayList.get(i).getAmount());
                        ps.setInt(6, 5);
                        ps.setString(7, "OTHER2");
                        ps.setString(8, ETYPE);
                        ps.setString(9, "g1");
                        ps.setString(10, "g2");
                        ps.setString(11, "st");

                        int res2 = ps.executeUpdate();
                        z=res2+"";
                        //  ResultSet rs=0;
                        // rs = ps.getGeneratedKeys();

                    }
                    // Commit data here.
                    conn.commit();



                }

            } catch (Exception ex) {
                IsSuccess = false;
                z = ex.getMessage();
                try{
                    if(conn!=null)
                        conn.rollback();
                }catch(SQLException sqlEx){
                    String sql=sqlEx.getMessage();
                }

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (dialog.isShowing()) {
                dialog.dismiss();
            }


                if (s.equalsIgnoreCase("1")) {
                if (vrnoa_all!=0)
                {
                    vrnoa_all=0;



                    SPINNER_TABLE_INVISIBLE=false;
                    tableSpinner.setVisibility(View.VISIBLE);


                    Toast.makeText(CartActivity.this, "Data Updated successfully", Toast.LENGTH_SHORT).show();
                }
                else {


                    SPINNER_TABLE_INVISIBLE=false;

                    tableSpinner.setVisibility(View.VISIBLE);

                    Toast.makeText(CartActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                }

                    databaseHelper.claerAllItems();
                    vrnoa_all=0;
                    MyApplication.editModeVoucherAll="";

                    MyApplication.percent="";
                    MyApplication.tax="";
                    MyApplication.service="";
                    MyApplication.kotNo="";
                    MyApplication.orderNo="";
                    MyApplication.foodType="";
                    MyApplication.tableId="";
                    MyApplication.waiterId="";



                    Intent  intent=new Intent(CartActivity.this,CategoriesGridMenu.class);
                    startActivity(intent);
                    finish();



                }
                else
                {
                    Toast.makeText(CartActivity.this, "Failed, Somethng went wrong", Toast.LENGTH_SHORT).show();

                }

        }
    }

    public  void  resetDataFromSqlLite(){

        vrnoa_all=0;

        MyApplication.editModeVoucherAll="";

        MyApplication.percent="";
        MyApplication.tax="";
        MyApplication.service="";
        MyApplication.kotNo="";
        MyApplication.orderNo="";
        MyApplication.foodType="";
        MyApplication.tableId="";
        MyApplication.waiterId="";



        ArrayList<ItemModel>    cartArrayList = databaseHelper.getCartItems();
        if (cartArrayList.size()>0) {
            databaseHelper.claerAllItems();
            Intent intent = new Intent(CartActivity.this, CategoriesGridMenu.class);
//            //Intent intent = new Intent(CartActivity.this, CartActivity.class);
//            intent.putExtra("finish", true);
           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
            startActivity(intent);
           finish();

           // adapter.notifyDataSetChanged();

          //  gettingALLData();

        }
    }




    public class DeleteVoucher extends AsyncTask<String, String, String> {
        String z = "";
        Boolean IsSuccess = false;

        public ProgressDialog dialog = new ProgressDialog(CartActivity.this);;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            DbConnection connect = new DbConnection();

           String Etype= strings[0];
           int Vrnoa= Integer.parseInt(strings[1]);


            try {
                conn = connect.connectionclass();
                if (conn == null) {
                    z = "Please check internet connection";
                } else {

                    // Set auto commit as false.
                    conn.setAutoCommit(false);

                    String queryKotDetail="delete FROM KotDetail WHERE  kotId in(select kotid from kotmain where ETYPE ='"+Etype+"' AND VRNOA ="+Vrnoa+")  ";
                    String query="delete FROM KOTMAIN WHERE ETYPE ='"+Etype+"' AND VRNOA ="+Vrnoa+" ";


                    Statement stmt = conn.createStatement();
                    int res = stmt.executeUpdate(query);
                    int res1 = stmt.executeUpdate(queryKotDetail);

                        z=res+"";
                    // Commit data here.
                    conn.commit();



                }

            } catch (Exception ex) {
                IsSuccess = false;
                z = ex.getMessage();
                Toast.makeText(CartActivity.this, "Failed, Somethng went wrong", Toast.LENGTH_SHORT).show();
                try{
                    if(conn!=null)
                        conn.rollback();
                }catch(SQLException sqlEx){
                    String sql=sqlEx.getMessage();
                }

            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.getMessage();
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
                Toast.makeText(CartActivity.this, "Voucher deleted successfully", Toast.LENGTH_SHORT).show();
            resetDataFromSqlLite();


        }
    }






    public void textchangerPercentDiscount() {


        editTextDiscount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                finalAmountCalculator();


            }
        });


    }


    public void textchangerTaxPercent() {




        editTextTax.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                finalAmountCalculator();

            }
        });


    }


    public void textchangerServiceCharges() {




        editTextServiceCharges.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                finalAmountCalculator();


            }
        });


    }


    public float percentCalculator(float perecnt)    {
        TotalAmount = MyApplication.totalAmount;
        float res = (TotalAmount / 100) * perecnt;
        return res;
    }


    public    void finalAmountCalculator() {
        try
        {

        TotalAmount = MyApplication.totalAmount;

            float discountPercent = validaterInteger(editTextDiscount.getText().toString());


            MyApplication.percent=discountPercent+"";

            if (discountPercent!= 0) {
                float discountedRupess = 0;
                discountedRupess = percentCalculator(discountPercent);
                discValueTextView.setText(Math.round(discountedRupess) + "");
            }else {
                discValueTextView.setText("0");
            }


        float servicePercent = validaterInteger(editTextServiceCharges.getText().toString());

            MyApplication.service=servicePercent+"";


            if (servicePercent != 0)
        {   float serviceRupees = 0;
            serviceRupees = percentCalculator(servicePercent);
            serviceChargesValueTextView.setText(Math.round(serviceRupees) + "");
        }else {
            serviceChargesValueTextView.setText(0 + "");
        }

        float taxPercent = validaterInteger(editTextTax.getText().toString());

            MyApplication.tax=taxPercent+"";

        if (taxPercent!= 0) {
            float taxRupess = 0;
            taxRupess = percentCalculator(taxPercent);
            taxValueTextView.setText(Math.round(taxRupess)+ "");
        }else {
            taxValueTextView.setText("0");
        }

        float serviceAmount = validaterInteger(serviceChargesValueTextView.getText().toString());
        float discAmount = validaterInteger(discValueTextView.getText().toString());
        float taxAmount = validaterInteger(taxValueTextView.getText().toString());

        float sum = TotalAmount - discAmount + taxAmount + serviceAmount;

        netAmountValueTextView.setText(Math.round(sum) + "");
}
catch ( Exception ex)
{
    Log.d("Exception",ex.getMessage() );
}

    }





    public class GetUserRights extends AsyncTask<String, String, String> {
        String z = "";
        Boolean IsSuccess = false;


        @Override
        protected String doInBackground(String... strings) {

            DbConnection connect = new DbConnection();


            try {
                conn = connect.connectionclass();
                if (conn == null) {
                    z = "Please check internet connection";
                } else {



                    String query = "select [Insert],[Update],[Delete],[Print] from VoucherRights where Group_Id='"+MyApplication.groupId+"'"+
                            " and Form_Id='KitchenOrderTicketKOTToolStripMenuItem'";
                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);

                    while (resultset.next()) {

                        Byte insertRights = resultset.getByte("Insert");

                        Byte updateRights = resultset.getByte("Update");
                        Byte deleteRights = resultset.getByte("Delete");
                        Byte printRights = resultset.getByte("Print");

                        Log.d("insertRights",insertRights+"");
                        Log.d("updateRights",updateRights+"");
                        Log.d("deleteRights",deleteRights+"");
                        Log.d("printRights",printRights+"");


                        MyApplication.insertRights=insertRights+"";
                        MyApplication.updateRights=updateRights+"";
                        MyApplication.deleteRights=deleteRights+"";
                        MyApplication.printRights=printRights+"";





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
        protected void onPostExecute(String vrno) {
            super.onPostExecute(vrno);
        }
    }




    public class GetRunningOrderNo extends AsyncTask<String, String, String> {
        String z = "";
        Boolean IsSuccess = false;

        private ProgressDialog dialog = new ProgressDialog(CartActivity.this);;
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

                    String query = "select OrderNo from KotMain where EType='KOT' AND VrDate='"+currentDate+"' and status='Running' AND tableId="+table_ID;


                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);
                       while (resultset.next()) {
                        String orderNO = resultset.getString("OrderNo");
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

            orderValue.setText(orderNo);
        }
    }



    public class GetVrno extends AsyncTask<String, String, String> {
        String z = "";
        Boolean IsSuccess = false;


        @Override
        protected String doInBackground(String... strings) {

            DbConnection connect = new DbConnection();


            try {
                conn = connect.connectionclass();
                if (conn == null) {
                    z = "Please check internet connection";
                } else {


                   String currentDate=getCurrentDate();
                    String query = "select isnull(MAX(VrNo),0)+1 as VrNo from KotMain where EType='KOT' AND VrDate='"+ currentDate +"'";
                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);

                    while (resultset.next()) {
                        String vrNo = resultset.getString("VrNo");
                        z = vrNo;
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
        protected void onPostExecute(String vrno) {
            super.onPostExecute(vrno);
            kotValue.setText(vrno);
        }
    }



    public class GetVrnoAll extends AsyncTask<String, String, String> {
        String z = "";
        Boolean IsSuccess = false;


        @Override
        protected String doInBackground(String... strings) {

            DbConnection connect = new DbConnection();

            try {
                conn = connect.connectionclass();
                if (conn == null) {
                    z = "Please check internet connection";
                } else {

                    String query = "select isnull(MAX(VrNoa),0)+1 as VrNo_All from KotMain where EType='KOT'";

                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);
                        while (resultset.next()) {
                            String vrNoAll = resultset.getString("VrNo_All");
                            z = vrNoAll;
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
        protected void onPostExecute(String vrNoAll) {
            super.onPostExecute(vrNoAll);

            editTextVrValue.setText(vrNoAll);
        }
    }



    public class GetVrnoAllBySingleId extends AsyncTask<String, String, String> {
        String z = "";
        Boolean IsSuccess = false;

        private ProgressDialog dialog = new ProgressDialog(CartActivity.this);;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("please wait");
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


                    String query = "SELECT KotMain.KotId as kotId,KotMain.VrNo as vrNo,KotMain.VrNoa as vrNoa" +
                            ",KotMain.OrderNo as orderNo,KotMain.Type as type,KotMain.EType as etype," +
                            "KotMain.Discount as discount,KotMain.DiscPercent as discPercent," +
                            "KotMain.Charges as serviceCharges,KotMain.TotalAmount as totalAmount," +
                            "KotMain.TotalQty as totalQty,KotMain.NetAmount as netAmount," +
                            "KotMain.Service_Percent as servicePercent,KotMain.tableId as tableId," +
                            "KotMain.waiterId as waiterId,KotMain.status as status,KotMain.tax as tax," +
                            "KotMain.taxPercent as taxPercent,KotDetail.KotdId as kotdid,KotDetail.KotId as kotId," +
                            "KotDetail.Item_Id as itemId,KotDetail.Qty as qty,KotDetail.Rate as rate," +
                            "KotDetail.Amount as amount,item.item_des as description FROM KotDetail INNER JOIN KotMain" +
                            " ON KotMain.KotId = KotDetail.KotId " +
                            " INNER JOIN item ON item.item_id = KotDetail.Item_Id " +
                            " INNER JOIN Waiter ON Waiter.Waiter_Id =KotMain.waiterId " +
                            " Left JOIN Tables ON Tables.Table_Id =KotMain.tableId " +
                            " WHERE EType='KOT' AND VrNoa="+vrCounter;


                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);
                    getVrnoAllModelArrayList=new ArrayList<>();

                    while (resultset.next()) {




                        String kotId = resultset.getString("kotId");
                        String vrNo = resultset.getString("vrNo");
                        String vrNoa = resultset.getString("vrNoa");
                        String orderNo = resultset.getString("orderNo");
                        String type = resultset.getString("type");

                        vrnoa_all=new Double(vrNoa).intValue();

                        MyApplication.editModeVoucherAll=vrnoa_all+"";

                       String etype = resultset.getString("etype");
                        String discount = resultset.getString("discount");
                        String discPercent = resultset.getString("discPercent");
                        String serviceCharges = resultset.getString("serviceCharges");
                        String totalAmount = resultset.getString("totalAmount");
                        String totalQty = resultset.getString("totalQty");
                        String netAmount = resultset.getString("netAmount");
                        String servicePercent = resultset.getString("servicePercent");
                        String tableId = resultset.getString("tableId");
                        String waiterId = resultset.getString("waiterId");
                        String status = resultset.getString("status");
                        String tax = resultset.getString("tax");
                        String taxPercent = resultset.getString("taxPercent");
                        String kotdid = resultset.getString("kotdid");
                        String itemId = resultset.getString("itemId");
                        String qty = resultset.getString("qty");
                        String rateUnitPrice = resultset.getString("rate");
                        String amount = resultset.getString("amount");
                        String itemNameDescription = resultset.getString("description");


                        GetVrnoAllBySingleIdModel object=new GetVrnoAllBySingleIdModel();

                        object.setKotId(kotId);
                        object.setKot_did(kotdid);
                        object.setVrNO(vrNo);
                        object.setOrderNo(orderNo);
                        object.setType(type);
                        object.setVrnoAll(vrNoa);
                        object.setWaiterId(waiterId);
                        object.setTableId(tableId);
                        object.setStatus(status);
                        object.setDiscPercent(discPercent);
                        object.setDiscAmount(discount);
                        object.setTaxPercent(taxPercent);
                        object.setTaxAmount(tax);
                        object.setServicePercent(servicePercent);
                        object.setServiceAmount(serviceCharges);
                        object.setEtype(etype);

                        getVrnoAllModelArrayList.add(object);

                        boolean cartEntry = databaseHelper.isAddedToCart(itemId, itemNameDescription,new Double(qty).intValue() ,new Double(rateUnitPrice).intValue(), new Double(amount).intValue(), " ");
                        if (cartEntry) {
                            //already inserted data thats why we  can only update quantity
                        } else {
                            //first time item  is enterig in the cart thats we  are icreasing  counter value like below
                            ///  adding  in the cart///
                            MyApplication.cartCounter += 1;
                            //((ParentActivity) context).updateCartCounter();
                        }


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
        protected void onPostExecute(String vrNoAll) {
            super.onPostExecute(vrNoAll);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            editTextVrValue.setText(vrCounter+"");
            orderValue.setText("");
            kotValue.setText("");

            qtyValueTextView.setText("");
            amountValueTextView.setText("");
            netAmountValueTextView.setText("");
            editTextDiscount.setText("");
            discValueTextView.setText("");
            editTextTax.setText("");
            taxValueTextView.setText("");
            editTextServiceCharges.setText("");
            serviceChargesValueTextView.setText("");
            netAmountValueTextView.setText("");



            if (getVrnoAllModelArrayList.size()>0) {


                if (getVrnoAllModelArrayList.size()>0)
                {
                   // cartLinearLayout.setBackgroundColor(Color.WHITE);
                }

                orderValue.setText(new Double(getVrnoAllModelArrayList.get(0).getOrderNo()).intValue()+"");
                kotValue.setText(new Double(getVrnoAllModelArrayList.get(0).getVrNO()).intValue()+"");


                MyApplication.orderNo=orderValue.getText().toString();
                MyApplication.kotNo=kotValue.getText().toString();


                editTextDiscount.setText(new Double(getVrnoAllModelArrayList.get(0).getDiscPercent()).intValue()+"");
                editTextTax.setText( new Double(getVrnoAllModelArrayList.get(0).getTaxPercent()).intValue()+"");
                editTextServiceCharges.setText( new Double(getVrnoAllModelArrayList.get(0).getServicePercent()).intValue()+"");

                ETYPE=getVrnoAllModelArrayList.get(0).getEtype();

                if (getVrnoAllModelArrayList.get(0).getType().equalsIgnoreCase("dinin"))
                {
                     diinOrDelivery="dinin";
                     MyApplication.foodType="dinin";
                    mRadioGroupDininDeliver.check(R.id.dinin);
                }else if(getVrnoAllModelArrayList.get(0).getType().equalsIgnoreCase("Delivery"))
                {
                    diinOrDelivery="Delivery";
                    MyApplication.foodType="Delivery";
                    mRadioGroupDininDeliver.check(R.id.Delivery);
                }
                else
                {
                    diinOrDelivery="takeWay";
                    MyApplication.foodType="takeWay";
                    mRadioGroupDininDeliver.check(R.id.takeWay);
                }

                freeOrRunning=getVrnoAllModelArrayList.get(0).getStatus();

                mRadioGroupNewRunning.check(R.id.running);


                String retriveTabelId=getVrnoAllModelArrayList.get(0).getTableId();

                if (retriveTabelId.equalsIgnoreCase("0"))
                {

                }
                else {

                    MyApplication.tableId=getVrnoAllModelArrayList.get(0).getTableId();
                    GetTableNumberRetrive getTableNumberRetrive = new GetTableNumberRetrive();
                    getTableNumberRetrive.execute(getVrnoAllModelArrayList.get(0).getTableId());
                }



                MyApplication.waiterId=getVrnoAllModelArrayList.get(0).getWaiterId();
                GetWaiterRetrive getWaiterRetrive=new GetWaiterRetrive();
                getWaiterRetrive.execute(getVrnoAllModelArrayList.get(0).getWaiterId());

            }
            ((ParentActivity) context).updateCartCounter();

            final ArrayList<ItemModel> cartArrayList = databaseHelper.getCartItems();


            LinearLayoutManager itemsLayoutManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
            rvCart.setLayoutManager(itemsLayoutManager);

            adapter = new cartRecyclerAdapter(CartActivity.this, cartArrayList, databaseHelper, qtyValueTextView, amountValueTextView, netAmountValueTextView,
                    editTextDiscount, editTextTax, editTextServiceCharges, discValueTextView, taxValueTextView, serviceChargesValueTextView);


            rvCart.setAdapter(adapter);


        }
    }



    public class GetOrderNo extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected String doInBackground(String... strings) {

            DbConnection connect = new DbConnection();



            try {
                conn = connect.connectionclass();
                if (conn == null) {
                    z = "Please check internet connection";
                } else {

                    String currentDate=getCurrentDate();
                    String query = "select isnull(MAX(OrderNo),0)+1 as OrderNo from KotMain where EType='KOT' AND VrDate='"+currentDate+"'";
                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);

                        while (resultset.next()) {
                            String orderNO = resultset.getString("OrderNo");
                            z = orderNO;

                        }

                    resultset.close();
                }

            } catch (Exception ex) {
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
        protected void onPostExecute(String orderNO) {
            super.onPostExecute(orderNO);
            orderValue.setText(orderNO);
        }


    }


    public class GetKot extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected String doInBackground(String... strings) {

            DbConnection connect = new DbConnection();



            try {
                conn = connect.connectionclass();
                if (conn == null) {
                    z = "Please check internet connection";
                } else {

                    String query = "select isnull(MAX(KotId),0)+1 as KOTId from KotMain";

                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);
                    while (resultset.next()) {

                        String kotId = resultset.getString("KOTId");
                        z =kotId;
                    }
                    resultset.close();
                }

            } catch (Exception ex) {
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
        protected void onPostExecute(String kotId) {
            super.onPostExecute(kotId);

            KOT_ID=kotId;


        }
    }




    public class GetTableNumberRetrive extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected String doInBackground(String... params) {

            String  tableId=params[0];

            DbConnection connect = new DbConnection();


            try {
                conn = connect.connectionclass();
                if (conn == null) {
                    z = "Please check internet connection";
                } else {

                    String query ="select * from Tables where Status='"+freeOrRunning+"' and  Table_Id="+tableId;

                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);

                    tabelNumberList = new ArrayList<>();



                    while (resultset.next()) {

                        String tabel_id = resultset.getString("Table_Id");
                        String tabel_Number = resultset.getString("TableNum");

                        TabelModel object = new TabelModel();

                        object.setTableId(tabel_id);
                        object.setTableNumber(tabel_Number);


                        tabelNumberList.add(object);
                    }


                    // spinner for Table//
                    tableNumberArray=new String[tabelNumberList.size()];

                    for (int i=0; i<tabelNumberList.size(); i++){
                        tableNumberArray[i]=tabelNumberList.get(i).getTableNumber();

                    }

                    Log.d("Size table_List", tabelNumberList.size() + "");

                    resultset.close();
                }

            } catch (Exception ex) {
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            tableSpinnerRetrive(tableSpinner,tableNumberArray);

        }
    }




    public class GetTableNumber extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected String doInBackground(String... params) {


            DbConnection connect = new DbConnection();


            try {
                conn = connect.connectionclass();
                if (conn == null) {
                    z = "Please check internet connection";
                } else {

                    String query ="select * from Tables where Status='"+freeOrRunning+"'";

                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);

                    tabelNumberList = new ArrayList<>();



                    while (resultset.next()) {

                        String tabel_id = resultset.getString("Table_Id");
                        String tabel_Number = resultset.getString("TableNum");

                        TabelModel object = new TabelModel();

                        object.setTableId(tabel_id);
                        object.setTableNumber(tabel_Number);


                        tabelNumberList.add(object);
                    }


                    // spinner for Table//
                    tableNumberArray=new String[tabelNumberList.size()];

                    for (int i=0; i<tabelNumberList.size(); i++){
                        tableNumberArray[i]=tabelNumberList.get(i).getTableNumber();

                    }

                    Log.d("Size table_List", tabelNumberList.size() + "");

                    resultset.close();
                }

            } catch (Exception ex) {
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
          protected void onPostExecute(String s) {
              super.onPostExecute(s);

              tableSpinner(tableSpinner,tableNumberArray,0);
          }
      }


    public class GetWaiterRetrive extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected String doInBackground(String... params) {

            String  waiterId=params[0];
            DbConnection connect = new DbConnection();


            try {
                conn = connect.connectionclass();
                if (conn == null) {
                    z = "Please check internet connection";
                } else {

                    String query ="select * from Waiter where Waiter_Id="+waiterId;

                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);

                    waiterNumberList = new ArrayList<>();



                    while (resultset.next()) {

                        String waiter_id = resultset.getString("Waiter_Id");
                        String name = resultset.getString("Name");

                        WaiterModel object = new WaiterModel();

                        object.setWaiterId(waiter_id);
                        object.setWaiterName(name);


                        waiterNumberList.add(object);
                    }

                    // spinner for waiter//
                    waiterNameArray=new String[waiterNumberList.size()];

                    for (int i=0; i<waiterNumberList.size(); i++){
                        waiterNameArray[i]=waiterNumberList.get(i).getWaiterName();

                    }


                    Log.d("Size waiter_List", waiterNumberList.size() + "");

                    resultset.close();
                }

            } catch (Exception ex) {
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            waiterSpinnerRetrive(waiterSpinner,waiterNameArray);
        }


    }

    public class GetWaiter extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected String doInBackground(String... strings) {

            DbConnection connect = new DbConnection();


            try {
                conn = connect.connectionclass();
                if (conn == null) {
                    z = "Please check internet connection";
                } else {

                    String query ="select * from Waiter ";

                    Statement stmt = conn.createStatement();
                    resultset = stmt.executeQuery(query);

                    waiterNumberList = new ArrayList<>();



                    while (resultset.next()) {

                        String waiter_id = resultset.getString("Waiter_Id");
                        String name = resultset.getString("Name");

                        WaiterModel object = new WaiterModel();

                        object.setWaiterId(waiter_id);
                        object.setWaiterName(name);


                        waiterNumberList.add(object);
                    }

                    // spinner for waiter//
                    waiterNameArray=new String[waiterNumberList.size()];

                    for (int i=0; i<waiterNumberList.size(); i++){
                        waiterNameArray[i]=waiterNumberList.get(i).getWaiterName();

                    }


                    Log.d("Size waiter_List", waiterNumberList.size() + "");

                    resultset.close();
                }

            } catch (Exception ex) {
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            waiterSpinner(waiterSpinner,waiterNameArray,0);
        }


    }






    public  void tableSpinnerRetrive(final Spinner spinnerTableId, String[] tableArray)
    {


        try {


            SpinnerSelectValueAdapter adapter = new SpinnerSelectValueAdapter(CartActivity.this, android.R.layout.simple_list_item_1);
            adapter.addAll(tableArray);
            adapter.add("Select Table");
            spinnerTableId.setSelection(adapter.getCount());
            spinnerTableId.setAdapter(adapter);
            spinnerTableId.setSelection(0);

            spinnerTableId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub


                    String tableNumber = spinnerTableId.getSelectedItem().toString();

                    int indexNew = (int) id;

                    String TblId = tabelNumberList.get(indexNew).getTableId();
                    String TblNumber = tabelNumberList.get(indexNew).getTableNumber();
                    table_NAME=TblNumber;
                    table_ID = TblId;
                    flagtable = true;
                  ///  MyApplication.tableId=table_ID;


                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });

        }
        catch (Exception e)
        {
            e.getMessage();
        }


    }



    public  void tableSpinner(final Spinner spinnerTableId, String[] tableArray, final int index)
    {


        try {


            SpinnerSelectValueAdapter adapter = new SpinnerSelectValueAdapter(CartActivity.this, android.R.layout.simple_list_item_1);
            adapter.addAll(tableArray);
            adapter.add("Select Table");
            spinnerTableId.setAdapter(adapter);
            spinnerTableId.setSelection(adapter.getCount());

            if (flagtable) {

                spinnerTableId.setSelection(index);
                flagtable = false;
            }

            spinnerTableId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub

                    if (spinnerTableId.getSelectedItem() == "Select Table") {
                        //Do nothing.
                    } else {

                        String tableNumber = spinnerTableId.getSelectedItem().toString();

                        int indexNew = (int) id;

                        String TblId = tabelNumberList.get(indexNew).getTableId();
                        String TblNumber = tabelNumberList.get(indexNew).getTableNumber();

                        table_NAME=TblNumber;

                        table_ID = TblId;
                        flagtable = true;


                        if (freeOrRunning.equalsIgnoreCase("Running")) {
                            GetRunningOrderNo getRunningOrderNo = new GetRunningOrderNo();
                            getRunningOrderNo.execute("");
                        }


                    }


                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });

        }

        catch (Exception e)
        {
            e.getMessage();
        }


    }



    public  void waiterSpinnerRetrive(final Spinner spinnerWaiterId, String[] waiterArray)
    {

        try{

        SpinnerSelectValueAdapter adapter = new SpinnerSelectValueAdapter(CartActivity.this, android.R.layout.simple_list_item_1);
        adapter.addAll(waiterArray);
        adapter.add("Select Waiter");
        spinnerWaiterId.setAdapter(adapter);
        spinnerWaiterId.setSelection(adapter.getCount());
        spinnerWaiterId.setSelection(0);

        spinnerWaiterId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {


                    String waiterName =spinnerWaiterId.getSelectedItem().toString();
                    int  indexNew= (int) id;

                    String waiterId=waiterNumberList.get(indexNew).getWaiterId();
                    String name=waiterNumberList.get(indexNew).getWaiterName();
                     waiter_NAME=name;

                    waiter_ID=waiterId;
                 ///   MyApplication.waiterId=waiter_ID;



            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        }
        catch (Exception e)
        {
            e.getMessage();
        }


    }


    public  void waiterSpinner(final Spinner spinnerWaiterId, String[] waiterArray,int index)
    {

        try {




        SpinnerSelectValueAdapter adapter = new SpinnerSelectValueAdapter(CartActivity.this, android.R.layout.simple_list_item_1);
        adapter.addAll(waiterArray);
        adapter.add("Select Waiter");
        spinnerWaiterId.setAdapter(adapter);
        spinnerWaiterId.setSelection(adapter.getCount());

        if (flagWaiter)
        {

            spinnerWaiterId.setSelection(index);
            flagWaiter=false;
        }



        spinnerWaiterId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                if ( spinnerWaiterId.getSelectedItem() == "Select Waiter")
                {
                    //Do nothing.
                }
                else
                {

                    String waiterName =spinnerWaiterId.getSelectedItem().toString();
                    int  indexNew= (int) id;

                    String waiterId=waiterNumberList.get(indexNew).getWaiterId();
                    String name=waiterNumberList.get(indexNew).getWaiterName();

                    waiter_NAME=name;

                    waiter_ID=waiterId;
                    flagWaiter=true;


                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        }
        catch (Exception e)
        {
            e.getMessage();
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


    public  float  validaterInteger(String  val)
    {  float   result=0;

        try
        {
            if (val.length()!=0)
            {
                result=Float.parseFloat(val);


            }
        }
        catch (Exception ex)
        {
            return  0;

        }
        return  result;
    }

}
