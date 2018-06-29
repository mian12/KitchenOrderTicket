package com.solution.alnahar.kitchenorderticket.Categories;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.solution.alnahar.kitchenorderticket.App.AppConfig;
import com.solution.alnahar.kitchenorderticket.Cart.CartActivity;
import com.solution.alnahar.kitchenorderticket.DBConnection.DbConnection;
import com.solution.alnahar.kitchenorderticket.MainActivity;
import com.solution.alnahar.kitchenorderticket.ParentActivity;
import com.solution.alnahar.kitchenorderticket.R;
import com.solution.alnahar.kitchenorderticket.SqlLiteDB.SQLiteDatabaseHelper;
import com.solution.alnahar.kitchenorderticket.Utilis.MyApplication;
import com.solution.alnahar.kitchenorderticket.Utilis.SharedPreferenceClass;
import com.solution.alnahar.kitchenorderticket.Utilis.VolleySingleton;
import com.solution.alnahar.kitchenorderticket.model.CartArrayModel;
import com.solution.alnahar.kitchenorderticket.model.CategoriesModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CategoriesGridMenu extends ParentActivity implements View.OnClickListener {



    LinearLayout llOne, llTwo, llThree, llFour, llFive, llSix, llSeven, llEight, llNine;
    LinearLayout[] linearLayouts = {llOne, llTwo, llThree, llFour, llFive, llSix, llSeven, llEight, llNine};
    int[] linearLayoutsIds = {R.id.llOne, R.id.llTwo, R.id.llThree, R.id.llFour, R.id.llFive, R.id.llSix, R.id.llSeven, R.id.llEight, R.id.llNine};


    RelativeLayout rlOne, rlTwo, rlThree, rlFour, rlFive, rlSix, rlSeven, rlEight, rlNine, rlTen, rlEleven, rlTwelve,
            rlThirteen, rlFourteen, rlFifteen, rlSixteen, rlSeventeen, rlEighteen, rlNinteen, rlTwenty, rlTwentyOne,
            rlTwentyTwo, rlTwentyThree, rlTwentyFour, rlTwentyFive;


    RelativeLayout[] realtiveLayout = {rlOne, rlTwo, rlThree, rlFour, rlFive, rlSix, rlSeven, rlEight, rlNine, rlTen, rlEleven, rlTwelve, rlThirteen, rlFourteen, rlFifteen, rlSixteen, rlSeventeen, rlEighteen, rlNinteen, rlTwenty,
            rlTwentyOne, rlTwentyTwo, rlTwentyThree, rlTwentyFour, rlTwentyFive};
    int[] realtiveLayoutIds = {R.id.rlOne, R.id.rlTwo, R.id.rlThree, R.id.rlFour, R.id.rlFive, R.id.rlSix, R.id.rlSeven, R.id.rlEight, R.id.rlNine, R.id.rlTen, R.id.rlEleven, R.id.rlTwelve, R.id.rlThirteen, R.id.rlFourteen,
            R.id.rlFifteen, R.id.rlSixteen, R.id.rlSeventeen, R.id.rlEighteen, R.id.rlNinteen, R.id.rlTwenty, R.id.rlTwentyOne, R.id.rlTwentyTwo, R.id.rlTwentyThree, R.id.rlTwentyFour, R.id.rlTwentyFive};


    ImageView ivOne, ivTwo, ivThree, ivFour, ivFive, ivSix, ivSeven, ivEight, ivNine, ivTen, ivEleven, ivTwelve, ivThieteen, ivFourteen, ivFifteen, ivSixteen, ivSeventeen, ivEighteen, ivNinteen, ivTwenty, ivTwentyOne, ivTwentyTwo, ivTwentyThree, ivTwentyFour, ivTwentyFive;

    ImageView imageView[] = {ivOne, ivTwo, ivThree, ivFour, ivFive, ivSix, ivSeven, ivEight, ivNine, ivTen, ivEleven, ivTwelve, ivThieteen, ivFourteen, ivFifteen, ivSixteen, ivSeventeen, ivEighteen, ivNinteen, ivTwenty, ivTwentyOne, ivTwentyTwo, ivTwentyThree, ivTwentyFour, ivTwentyFive};
    int[] imageViewIds = {R.id.ivOne, R.id.ivTwo, R.id.ivThree, R.id.ivFour, R.id.ivFive, R.id.ivSix, R.id.ivSeven, R.id.ivEight, R.id.ivNine, R.id.ivTen,
            R.id.ivEleven, R.id.ivTwelve, R.id.ivThirteen, R.id.ivFourteen, R.id.ivFifteen, R.id.ivSixteen, R.id.ivSeventeen, R.id.ivEighteen, R.id.ivNinteen, R.id.ivTwenty,
            R.id.ivTwentyOne, R.id.ivTwentyTwo, R.id.ivTwentyThree, R.id.ivTwentyFour, R.id.ivTwentyFive};


    TextView tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvTen, tvEleven, tvTwelve, tvThirteen, tvFourteen, tvFifteen, tvSixteen, tvSeventeen, tvEighteen, tvNinteen, tvTwenty, tvTwentyOne, tvTwentyTwo, tvTwentyThree, tvTwentyFour, tvTwentyFive;
    TextView textView[] = {tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvTen, tvEleven, tvTwelve, tvThirteen, tvFourteen, tvFifteen, tvSixteen, tvSeventeen, tvEighteen, tvNinteen, tvTwenty, tvTwentyOne, tvTwentyTwo, tvTwentyThree, tvTwentyFour, tvTwentyFive};
    int[] textViewIds = {R.id.tvOne, R.id.tvTwo, R.id.tvThree, R.id.tvFour, R.id.tvFive, R.id.tvSix, R.id.tvSeven, R.id.tvEight, R.id.tvNine, R.id.tvTen,
            R.id.tvEleven, R.id.tvTwelve, R.id.tvThirteen, R.id.tvFourteen, R.id.tvFifteen, R.id.tvSixteen, R.id.tvSeventeen, R.id.tvEighteen, R.id.tvNinteen, R.id.tvTwenty,
            R.id.tvTwentyOne, R.id.tvTwentyTwo, R.id.tvTwentyThree, R.id.tvTwentyFour, R.id.tvTwentyFive};


    private Intent intent = null;

    public SQLiteDatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_menu);


        context = this;

        databaseHelper = new SQLiteDatabaseHelper(CategoriesGridMenu.this);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        AppConfig.DEVICE_HEIGHT = size.y;
        AppConfig.DEVICE_WIDTH = size.x;


        for (int i = 0; i < linearLayoutsIds.length; i++) {

            linearLayouts[i] = findViewById(linearLayoutsIds[i]);

            if (i == 0) {
                linearLayouts[i].getLayoutParams().height = 2 * (AppConfig.DEVICE_WIDTH / 3);

            } else {
                linearLayouts[i].getLayoutParams().height = AppConfig.DEVICE_WIDTH / 3;
            }


        }

        for (int i = 0; i < realtiveLayoutIds.length; i++) {

            realtiveLayout[i] = findViewById(realtiveLayoutIds[i]);
            realtiveLayout[i].setOnClickListener(this);

            imageView[i] = findViewById(imageViewIds[i]);
            textView[i] = findViewById(textViewIds[i]);

        }




        try {


            if (MainActivity.categoriesModelArrayList.size() > 0) {


                for (int i = 0; i < textView.length; i++) {
                    textView[i].setText(MainActivity.categoriesModelArrayList.get(i).getCategory());

                    try {
                        Bitmap bitmap = MainActivity.categoriesModelArrayList.get(i).getImageBitamp();

                        imageView[i].setImageBitmap(bitmap);


                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }


        } catch (Exception e) {
            e.getMessage();
        }


        intent = new Intent(CategoriesGridMenu.this, ItemsActivity.class);

    }


    @Override
    protected void onResume() {
        super.onResume();

        /// its caled whenever we add the items then back to home screen  then it called and change the counter of cart
        ((ParentActivity) context).updateCartCounter();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.rlOne:


                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(0).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(0).getCatId());
                    // Bitmap bitmap=MainActivity.categoriesModelArrayList.get(0).getImageBitamp();
                    //intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(6).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }


                startActivity(intent);

                break;


            case R.id.rlTwo:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(1).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(1).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(1).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlThree:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(2).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(2).getCatId());
                    //intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(2).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlFour:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(3).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(3).getCatId());
                    //  intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(3).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlFive:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(4).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(4).getCatId());
                    //intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(4).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlSix:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(5).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(5).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(5).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlSeven:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(6).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(6).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(6).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlEight:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(7).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(7).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(7).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlNine:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(8).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(8).getCatId());
                    //intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(8).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlTen:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(9).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(9).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;


            case R.id.rlEleven:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(10).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(10).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlTwelve:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(11).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(11).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlThirteen:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(12).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(12).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlFourteen:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(13).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(13).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlFifteen:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(14).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(14).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;
            case R.id.rlSixteen:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(15).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(15).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlSeventeen:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(16).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(16).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;
            case R.id.rlEighteen:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(17).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(17).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlNinteen:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(18).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(18).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

            case R.id.rlTwenty:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(19).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(19).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;
            case R.id.rlTwentyOne:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(20).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(20).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;
            case R.id.rlTwentyTwo:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(21).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(21).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;
            case R.id.rlTwentyThree:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(22).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(22).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;
            case R.id.rlTwentyFour:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(23).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(23).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;
            case R.id.rlTwentyFive:

                try {
                    intent.putExtra("CategoryName", MainActivity.categoriesModelArrayList.get(24).getCategory());
                    intent.putExtra("catId", MainActivity.categoriesModelArrayList.get(24).getCatId());
                    // intent.putExtra("imagePath", MainActivity.categoriesModelArrayList.get(9).getImageBitamp());
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivity(intent);
                break;

        }
    }


}




