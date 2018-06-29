package com.solution.alnahar.kitchenorderticket.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.solution.alnahar.kitchenorderticket.Cart.CartActivity;
import com.solution.alnahar.kitchenorderticket.R;
import com.solution.alnahar.kitchenorderticket.SqlLiteDB.SQLiteDatabaseHelper;
import com.solution.alnahar.kitchenorderticket.model.ItemModel;
import com.solution.alnahar.kitchenorderticket.model.PendingOrders;
import com.solution.alnahar.kitchenorderticket.pendingOrders.PendingOrdersActivity;

import java.util.ArrayList;

public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.MyViewHolder> {


    ArrayList<PendingOrders> list;
    private Context context;




    public PendingOrdersAdapter(Context context, ArrayList<PendingOrders> list)
    {
        this.context = context;
        this.list = list;



    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PendingOrdersAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pending_orders, parent, false));

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.textViewOrderNo.setText( list.get(position).getOrderNO());
        holder.textViewKotNo.setText( list.get(position).getkOTNo());
        holder.textViewTableNo.setText( list.get(position).getTableNO());
        holder.textViewType.setText( list.get(position).getType());
        holder.textViewAmount.setText( list.get(position).getAmount());
        holder.textViewTime.setText( list.get(position).getTime());
        holder.textViewWaiter.setText( list.get(position).getWaiter());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, CartActivity.class);
                intent.putExtra("vrnoa",list.get(position).getVrnoa());
                context.startActivity(intent);
                ((PendingOrdersActivity) context).finish();

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewOrderNo, textViewKotNo,textViewTableNo, textViewType,textViewAmount,textViewTime,textViewWaiter;

        CardView cardView;
        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;

            cardView=view.findViewById(R.id.card_view);

            textViewOrderNo = view.findViewById(R.id.textViewOrderNo);
            textViewKotNo = view.findViewById(R.id.textViewKotNo);
            textViewTableNo = view.findViewById(R.id.textViewTableNo);
            textViewType = view.findViewById(R.id.textViewType);
            textViewAmount = view.findViewById(R.id.textViewAmount);
            textViewTime = view.findViewById(R.id.textViewTime);
            textViewWaiter = view.findViewById(R.id.textViewWaiter);
        }
    }

}
