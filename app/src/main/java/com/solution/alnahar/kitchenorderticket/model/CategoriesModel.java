package com.solution.alnahar.kitchenorderticket.model;

import android.graphics.Bitmap;

/**
 * Created by Yasir on 5/17/2017.
 */

public class CategoriesModel {

    private String category;
    private String description;
    private int uid;
    private String catId;

    private Bitmap imageBitamp;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }


    public Bitmap getImageBitamp() {
        return imageBitamp;
    }

    public void setImageBitamp(Bitmap imageBitamp) {
        this.imageBitamp = imageBitamp;
    }
}
