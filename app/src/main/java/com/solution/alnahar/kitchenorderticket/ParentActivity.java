package com.solution.alnahar.kitchenorderticket;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.solution.alnahar.kitchenorderticket.Cart.CartActivity;
import com.solution.alnahar.kitchenorderticket.Categories.CategoriesGridMenu;
import com.solution.alnahar.kitchenorderticket.Categories.ItemsActivity;
import com.solution.alnahar.kitchenorderticket.Categories.SubCategoriesActivity;
import com.solution.alnahar.kitchenorderticket.SqlLiteDB.SQLiteDatabaseHelper;
import com.solution.alnahar.kitchenorderticket.Utilis.MyApplication;
import com.solution.alnahar.kitchenorderticket.Utilis.SharedPreferenceClass;
import com.solution.alnahar.kitchenorderticket.model.CategoriesModel;
import com.solution.alnahar.kitchenorderticket.model.SubCategoriesModel;
import com.solution.alnahar.kitchenorderticket.pendingOrders.PendingOrdersActivity;

import java.util.ArrayList;

/**
 * Created by Mian Shahbaz Idrees on 23-Jan-18.
 */


public class ParentActivity extends AppCompatActivity {

    public Context context;

    public SQLiteDatabaseHelper databaseHelper;


    MenuItem item;
    BadgeStyle badgeStyle;
    ActionItemBadge.ActionItemBadgeListener listener;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        if (context instanceof CategoriesGridMenu || context instanceof SubCategoriesActivity
                || context instanceof CartActivity
                || context instanceof ItemsActivity ) {
            // Inflate the menu; this adds items to the action bar if it is present.
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.main_menu, menu);


            item = menu.findItem(R.id.action_cart);
          //MenuItem exit= menu.findItem(R.id.signout);


            if (context instanceof CartActivity)
            {
                item.setVisible(false);
            }
            else {

                badgeStyle = ActionItemBadge.BadgeStyles.GREY.getStyle();
                badgeStyle.setTextColor(Color.WHITE);
                listener = new ActionItemBadge.ActionItemBadgeListener() {
                    @Override
                    public boolean onOptionsItemSelected(MenuItem menu) {
                        Intent cartIntent = new Intent(context, CartActivity.class);
                        startActivity(cartIntent);
                        return false;
                    }
                };

                ActionItemBadge.update(this, item, getResources().getDrawable(R.mipmap.ic_action_cart), badgeStyle, 0);
            }

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;

            case R.id.signout:


//                MyApplication.percent="";
//                MyApplication.tax="";
//                MyApplication.service="";


                new AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                SharedPreferenceClass.getInstance(getApplicationContext(), "KitchenOrderTicket", MODE_PRIVATE);
                                SharedPreferenceClass.clearAll();

                                // it will   finish all activities which are stores in stack

                                finishAffinity();



                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


                break;
        }
        return false;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        long totalCartItems = sqliteDatabaseHelper.getTotalGridOrderItems();
        if (context instanceof SubCategoriesActivity || context instanceof CategoriesGridMenu
                || context instanceof ItemsActivity) {
            updateCartCounter();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateCartCounter() {

        databaseHelper = new SQLiteDatabaseHelper(ParentActivity.this);

        int cartItemsDB = databaseHelper.getTotalItems();

        int totalCartItems = cartItemsDB;

        badgeStyle = ActionItemBadge.BadgeStyles.GREY.getStyle();

        if (totalCartItems > 0) {
            badgeStyle.setColor(Color.GRAY);
            ActionItemBadge.update(this, item, getResources().getDrawable(R.mipmap.ic_action_cart), badgeStyle, totalCartItems, listener);

        } else {
            badgeStyle.setColor(Color.TRANSPARENT);
            ActionItemBadge.update(this, item, getResources().getDrawable(R.mipmap.ic_action_cart), badgeStyle, null, listener);

        }
    }

}
