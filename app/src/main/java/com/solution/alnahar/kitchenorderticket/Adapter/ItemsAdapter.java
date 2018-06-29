package com.solution.alnahar.kitchenorderticket.Adapter;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.solution.alnahar.kitchenorderticket.App.AppConfig;
import com.solution.alnahar.kitchenorderticket.ParentActivity;
import com.solution.alnahar.kitchenorderticket.R;
import com.solution.alnahar.kitchenorderticket.SqlLiteDB.SQLiteDatabaseHelper;
import com.solution.alnahar.kitchenorderticket.Utilis.MyApplication;
import com.solution.alnahar.kitchenorderticket.model.CartArrayModel;
import com.solution.alnahar.kitchenorderticket.model.ItemModel;

import java.util.ArrayList;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private ArrayList<ItemModel> itemModelArrayList;



    private Context context;


    public SQLiteDatabaseHelper databaseHelper;


    public ItemsAdapter(Context context, ArrayList<ItemModel> itemModelArrayList, SQLiteDatabaseHelper databaseHelper) {
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;

        this.databaseHelper = databaseHelper;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items_layout, parent, false));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ItemModel itemModel = itemModelArrayList.get(position);

        final String itemName = itemModel.getName().toString();
        holder.tvItemName.setText(itemName);
        final int unitPrice = Integer.parseInt(itemModelArrayList.get(position).getPrice());
        holder.tvPriceValue.setText(unitPrice + "");
        final String uom = itemModelArrayList.get(position).getUom();

        String qty = itemModelArrayList.get(position).getQuantity() + "";
        String amount=itemModelArrayList.get(position).getAmount()+"";

        holder.tvQntyValue.setText(qty);
        holder.tvAmountValue.setText(amount + "");


        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemsId = itemModelArrayList.get(position).getItemid();
                int qty = itemModelArrayList.get(position).getQuantity();

                qty += 1;

                //This will retrieve the same object and change it's only qty value
                itemModelArrayList.get(position).setQuantity(qty);

                holder.tvQntyValue.setText(itemModelArrayList.get(position).getQuantity() + "");

                int amount = qty * unitPrice;
                itemModelArrayList.get(position).setAmount(amount);
                holder.tvAmountValue.setText(amount + "");



                boolean cartEntry = databaseHelper.isAddedToCart(itemsId, itemName, qty,unitPrice, amount, uom);
                if (cartEntry) {
                    //already inserted data thats why we  can only update quantity
                } else {
                    //first time item  is enterig in the cart thats we  are icreasing  counter value like below
                    ///  adding  in the cart///
                    MyApplication.cartCounter += 1;
                    ((ParentActivity) context).updateCartCounter();
                }


            }
        });


        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = itemModelArrayList.get(position).getQuantity();
                String itemsId = itemModelArrayList.get(position).getItemid();
                final String uom = itemModelArrayList.get(position).getUom();
                int amount = 0;



                if (qty < 1) {
                    holder.tvQntyValue.setText(0 + "");
                    databaseHelper.deleteItem(itemsId);
                    notifyDataSetChanged();
                    ((ParentActivity) context).updateCartCounter();
                } else {
                    qty -= 1;
                    //This will retrieve the same object and change it's only qty value

                    itemModelArrayList.get(position).setQuantity(qty);

                    holder.tvQntyValue.setText(itemModelArrayList.get(position).getQuantity() + "");

                    amount = qty * unitPrice;
                    itemModelArrayList.get(position).setAmount(amount);
                    holder.tvAmountValue.setText(amount + "");

                    if (qty==0)
                    {
                        holder.tvQntyValue.setText(0 + "");
                        databaseHelper.deleteItem(itemsId);
                        notifyDataSetChanged();
                        ((ParentActivity) context).updateCartCounter();
                    }
                    else {

                        boolean cartEntry = databaseHelper.isAddedToCart(itemsId, itemName, qty, unitPrice, amount, uom);
                        if (cartEntry) {
                            //already inserted data thats why we  can only update quantity
                        } else {
                            //first time item  is enterig in the cart thats we  are icreasing  counter value like below
                            ///  adding  in the cart///
                            MyApplication.cartCounter -= 1;
                            ((ParentActivity) context).updateCartCounter();
                        }
                    }

                }




            }
        });


    }


    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemName, tvPriceName, tvPriceValue, tvQntyName, tvQntyValue, tvAmountName, tvAmountValue;
        Button btnPlus, btnMinus;

        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;

            tvItemName = (TextView) view.findViewById(R.id.itemName);
            tvPriceName = (TextView) view.findViewById(R.id.tvPriceName);
            tvPriceValue = (TextView) view.findViewById(R.id.tvPriceValue);
            tvQntyName = (TextView) view.findViewById(R.id.tvQntyName);
            tvQntyValue = (TextView) view.findViewById(R.id.tvQntyValue);
            tvAmountName = (TextView) view.findViewById(R.id.tvAmountName);
            tvAmountValue = (TextView) view.findViewById(R.id.tvAmountValue);


            btnPlus = (Button) view.findViewById(R.id.buttonPlus);
            btnMinus = (Button) view.findViewById(R.id.buttonMinus);
        }
    }

}
