package com.solution.alnahar.kitchenorderticket.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.solution.alnahar.kitchenorderticket.Categories.ItemsActivity;
import com.solution.alnahar.kitchenorderticket.R;
import com.solution.alnahar.kitchenorderticket.model.SubCategoriesModel;

import java.util.ArrayList;



public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.MyViewHolder> {

    private ArrayList<SubCategoriesModel> subCategoriesModelArrayList;
    private Context context;
    private  String categoryName=null;


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvSr;
        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;
            tvSr = (TextView) view.findViewById(R.id.tvSr);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    public SubCategoriesAdapter(Context context, ArrayList<SubCategoriesModel> subCategoriesModelArrayList,String categoryName) {
        this.context = context;
        this.subCategoriesModelArrayList = subCategoriesModelArrayList;
        this.categoryName=categoryName;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_subcategories, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SubCategoriesModel subCategoriesModel = subCategoriesModelArrayList.get(position);
        holder.tvSr.setText(String.valueOf(position + 1));
        holder.tvTitle.setText(Html.fromHtml(subCategoriesModel.getName()));

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, ItemsActivity.class);
                intent.putExtra("CategoryName",categoryName);
                intent.putExtra("SubCategoryName",subCategoriesModelArrayList.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoriesModelArrayList.size();
    }
}
