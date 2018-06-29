package com.solution.alnahar.kitchenorderticket.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.solution.alnahar.kitchenorderticket.ParentActivity;
import com.solution.alnahar.kitchenorderticket.R;
import com.solution.alnahar.kitchenorderticket.SqlLiteDB.SQLiteDatabaseHelper;
import com.solution.alnahar.kitchenorderticket.Utilis.MyApplication;
import com.solution.alnahar.kitchenorderticket.model.ItemModel;

import java.util.ArrayList;

/**
 * Created by Mian Shahbaz Idrees on 21-Feb-18.
 */

public class cartRecyclerAdapter extends RecyclerView.Adapter<cartRecyclerAdapter.MyViewHolder> {

    private Context context;
    ArrayList<ItemModel> cartArrayList;
    LayoutInflater inflater;

    float TotalAmount = 0;
    float discount=0;

    public SQLiteDatabaseHelper databaseHelper;

    TextView qtyValueTextView,amountValueTextView,netAmountValueTextView,discValueTextView,taxValueTextView,serviceChargesValueTextView;
    EditText editTextDiscount,editTextTax,editTextService;


    public cartRecyclerAdapter(Context context, ArrayList<ItemModel> cartArrayList, SQLiteDatabaseHelper databaseHelper, TextView qtyValueTextView, TextView amountValueTextView, TextView netAmountValueTextView,
                               EditText editTextDiscount, EditText editTextTax, EditText editTextService, TextView discValueTextView,
                               TextView taxValueTextView, TextView serviceChargesValueTextView)
    {
        this.context = context;
        this.cartArrayList = cartArrayList;
        inflater = LayoutInflater.from(context);
        this.databaseHelper=databaseHelper;
        this.qtyValueTextView=qtyValueTextView;
        this.amountValueTextView=amountValueTextView;
        this.netAmountValueTextView=netAmountValueTextView;
        this.editTextDiscount=editTextDiscount;
        this.editTextTax=editTextTax;
        this.editTextService=editTextService;
        this.discValueTextView=discValueTextView;
        this.taxValueTextView=taxValueTextView;
        this.serviceChargesValueTextView=serviceChargesValueTextView;


    }

    @Override
    public cartRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new cartRecyclerAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_deatil, parent, false));
    }


    @Override
    public void onBindViewHolder(final cartRecyclerAdapter.MyViewHolder holder, final int position) {

        final  int pos=position;

        int counter=position+1;


            final String itemId = cartArrayList.get(position).getItemid();
            String name1 = cartArrayList.get(position).getName();
            int qty1 = cartArrayList.get(position).getQuantity();
            int amount1 = cartArrayList.get(position).getAmount();

            // cart setter/////
            holder.srNo.setText(counter + "");
            holder.name.setText(name1);
            holder.qty.setText(qty1 + "");
            holder.amountTV.setText(amount1 + "");

            summery();





        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemsId = cartArrayList.get(pos).getItemid();
                String  itemName = cartArrayList.get(pos).getName();
                String  uom =cartArrayList.get(pos).getUom();

                int qty = cartArrayList.get(pos).getQuantity();
                int unitPrice = Integer.parseInt(cartArrayList.get(pos).getPrice());

                qty += 1;

                cartArrayList.get(pos).setQuantity(qty);


                int amountCal = qty * unitPrice;



               holder.amountTV.setText(amountCal+"");
                holder. qty.setText(qty + "");

                cartArrayList.get(pos).setAmount(amountCal);

                boolean cartEntry = databaseHelper.isAddedToCart(itemsId, itemName, qty,unitPrice, amountCal, uom);
                if (cartEntry) {
                    //already inserted data thats why we  can only update quantity
                } else {
                    //first time item  is enterig in the cart thats we  are icreasing  counter value like below
                    ///  adding  in the cart///
                    MyApplication.cartCounter += 1;
                    ((ParentActivity) context).updateCartCounter();
                }



                notifyDataSetChanged();
                summery();


            }
        });




      holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemsId = cartArrayList.get(pos).getItemid();
                String  itemName = cartArrayList.get(pos).getName();
                String  uom =cartArrayList.get(pos).getUom();

                int qty = cartArrayList.get(pos).getQuantity();
                int unitPrice = Integer.parseInt(cartArrayList.get(pos).getPrice());



                if (qty==1)
                {

                }

                else
                {

                    qty -= 1;
                    cartArrayList.get(pos).setQuantity(qty);


                    int amountCal = qty * unitPrice;



                    holder.amountTV.setText(amountCal+"");
                    holder. qty.setText(qty + "");

                    cartArrayList.get(pos).setAmount(amountCal);

                    boolean cartEntry = databaseHelper.isAddedToCart(itemsId, itemName, qty,unitPrice, amountCal, uom);
                    if (cartEntry) {
                        //already inserted data thats why we  can only update quantity
                    } else {
                        //first time item  is enterig in the cart thats we  are icreasing  counter value like below
                        ///  adding  in the cart///
                        MyApplication.cartCounter -= 1;
                        ((ParentActivity) context).updateCartCounter();
                    }

                }
                notifyDataSetChanged();
                summery();



            }
        });






        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseHelper.deleteItem(itemId);
                cartArrayList.remove(position);

                notifyDataSetChanged();
                //((ParentActivity) context).updateCartCounter();

                summery();
            }
        });






    }


    public  void summery()
    {
        //summery start here///

        if (cartArrayList.size()>0)
        {


        int totalAmount = 0;
        int quantity = 0;
        for (ItemModel data : cartArrayList) {

            totalAmount += data.getAmount();
            quantity += data.getQuantity();
        }

        amountValueTextView.setText(totalAmount + "");
        qtyValueTextView.setText(quantity + "");

        MyApplication.totalAmount=totalAmount;
       // netAmountValueTextView.setText(totalAmount+"");
        // summery end here
        }
        else
        {
            amountValueTextView.setText("0");
            qtyValueTextView.setText("0");

            MyApplication.totalAmount=0;
           // netAmountValueTextView.setText(0+"");


        }

        finalAmountCalculator();


    }


    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView srNo, name, qty, amountTV;

        ImageView removeButton,plusButton, minusButton;

        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;

            srNo = view.findViewById(R.id.tvSr);
            name = view.findViewById(R.id.tvName);
            plusButton = view.findViewById(R.id.btnPlus);
            qty = view.findViewById(R.id.tvQty);
            minusButton = view.findViewById(R.id.btnMinus);
            amountTV = view.findViewById(R.id.tvAmount);
            removeButton = view.findViewById(R.id.btnDelete);
        }
    }




    public float percentCalculator(float perecnt)

    {

        TotalAmount = MyApplication.totalAmount;
        float res = (TotalAmount / 100) * perecnt;
        return res;
    }


    public    void finalAmountCalculator() {
        try
        {

            TotalAmount = MyApplication.totalAmount;

            float discountPercent = validaterInteger(editTextDiscount.getText().toString());

            if (discountPercent!= 0) {
                float discountedRupess = 0;
                discountedRupess = percentCalculator(discountPercent);
                discValueTextView.setText(Math.round(discountedRupess) + "");
            }else {
                discValueTextView.setText("0");
            }



            float servicePercent = validaterInteger(editTextService.getText().toString());

            if (servicePercent != 0)
            {   float serviceRupees = 0;
                serviceRupees = percentCalculator(servicePercent);
                serviceChargesValueTextView.setText(Math.round(serviceRupees) + "");
            }else {
                serviceChargesValueTextView.setText(0 + "");
            }

            float taxPercent = validaterInteger(editTextTax.getText().toString());

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
