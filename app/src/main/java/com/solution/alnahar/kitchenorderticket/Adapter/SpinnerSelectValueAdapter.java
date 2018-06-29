package com.solution.alnahar.kitchenorderticket.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by Engr Shahbaz Idrees on 7/12/2016.
 */

public class SpinnerSelectValueAdapter extends ArrayAdapter<String> {

    public SpinnerSelectValueAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub

    }

    @Override
    public int getCount() {

        // TODO Auto-generated method stub
        int count = super.getCount();

        return count>0 ? count-1 : count ;


    }

}
