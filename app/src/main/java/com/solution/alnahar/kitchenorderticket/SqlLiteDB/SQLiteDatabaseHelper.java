package com.solution.alnahar.kitchenorderticket.SqlLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.solution.alnahar.kitchenorderticket.model.ItemModel;

import java.util.ArrayList;


public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    //database
    private static final String DATABASE_NAME = "KITCHEN_ORDER_TICKET.db";
    //tables
    private static final String ITEMS = "items";
    private static final String SETTINGS = "settings";


    // table fields
    private static final String ITEMS_ID = "items_id";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String QTY = "qty";
    private static final String UOM = "uom";
    private static final String AMOUNT = "amount";
    private static final String TOTAL_AMOUNT = "total_amount";
    private static final String ADDED_ITEM = "add_item";


    private static final String IP_ADRESS = "ip_adress";
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";


    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


//        db.execSQL("Create Table " + CART + "(" + CART_ID + " Integer PRIMARY KEY AUTOINCREMENT, " + PRODUCT_ID + " Integer, " +
//                PRODUCT__CART_QTY + " Integer)");

        // db.execSQL("Create Table " + CATEGORIES + "(" + CATID + " Integer, " + NAME + " text," + DESCRIPTION + " text," + UID + " integer)");

        // db.execSQL("Create Table " + SUB_CATEGORIES + "(" + CATID + " Integer, " + NAME + " text," + DESCRIPTION + " text," + UID + " integer," +
        // SUBCATID + " integer," + CATEGORY_NAME + " text)");

        db.execSQL("Create Table " + ITEMS + "(" + ITEMS_ID + " text,  " + NAME + " text," + ADDED_ITEM + " text," + UOM + " text," + QTY + " Integer, " + PRICE + " Integer, " + AMOUNT + " Integer, " + TOTAL_AMOUNT + " Integer)");

        db.execSQL("Create Table " + SETTINGS + "(" + IP_ADRESS + " text,  " + USER_NAME + " text," + PASSWORD + " text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES);
        //  db.execSQL("DROP TABLE IF EXISTS " + SUB_CATEGORIES);
        // db.execSQL("DROP TABLE IF EXISTS " + CART);
        db.execSQL("DROP TABLE IF EXISTS " + ITEMS);
        onCreate(db);
    }


    public String[] itemIdCheckingInSqlLite(long itemId) {

        SQLiteDatabase db = getWritableDatabase();

        String[] temp = new String[6];

        Cursor itemsCursor = db.rawQuery("SELECT   *  FROM " + ITEMS + " WHERE " + ITEMS_ID + "=" + itemId, null);


        {

            while (itemsCursor.moveToNext()) {

                temp[0] = String.valueOf(itemsCursor.getColumnIndex(ITEMS_ID));
                temp[1] = String.valueOf(itemsCursor.getColumnIndex(UOM));
                temp[2] = String.valueOf(itemsCursor.getColumnIndex(NAME));
                temp[3] = String.valueOf(itemsCursor.getColumnIndex(QTY));
                temp[4] = String.valueOf(itemsCursor.getColumnIndex(PRICE));
                temp[5] = String.valueOf(itemsCursor.getColumnIndex(AMOUNT));

            }
            itemsCursor.close();
            db.close();
            return temp;


        }

    }


    public void ipConfiguration(String ipAdress, String userName, String password) {


        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("delete from " + SETTINGS);

        ContentValues S = new ContentValues();
        S.put(IP_ADRESS, ipAdress);
        S.put(USER_NAME, userName);
        S.put(PASSWORD, password);
        db.insert(SETTINGS, null, S);
        db.close();


    }

    public   ArrayList<String>  getIpAdress() {


        SQLiteDatabase db = getWritableDatabase();

       ArrayList<String> list=new ArrayList<>();

        Cursor itemsCursor = db.rawQuery("SELECT  * from " + SETTINGS, null);


        while (itemsCursor.moveToNext()) {

           String ipAdress=itemsCursor.getString(itemsCursor.getColumnIndex(IP_ADRESS));

             String userName=itemsCursor.getString(itemsCursor.getColumnIndex(USER_NAME));
            String pass=itemsCursor.getString(itemsCursor.getColumnIndex(PASSWORD));
            list.add(0,ipAdress);
            list.add(1,userName);
            list.add(2,pass);

        }
        itemsCursor.close();
        db.close();
        return list;
    }



    public  void deleteSetting()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + ITEMS);
        db.close();
    }


    public boolean isAddedToCart(String itemId, String name, int qtyCounter, int unitPrice, int amount, String uom) {

        String item_Id = "'" + itemId + "'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT  " + ITEMS_ID + " FROM " + ITEMS + " WHERE " + ITEMS_ID + "=" + item_Id, null);
        ContentValues S = new ContentValues();
        if (itemsCursor.getCount() > 0) {
            // if already have then update other wise insert
            S.put(QTY, qtyCounter);
            S.put(AMOUNT, amount);
            db.update(ITEMS, S, ITEMS_ID + "= ?", new String[]{String.valueOf(itemId)});
            db.close();
            return true;
        } else {

            S.put(ITEMS_ID, itemId);
            S.put(NAME, name);
            S.put(ADDED_ITEM, "true");
            S.put(UOM, uom);
            S.put(QTY, qtyCounter);
            S.put(PRICE, unitPrice);
            S.put(AMOUNT, amount);
            db.insert(ITEMS, null, S);
            db.close();
            return false;
        }
    }

    public int getTotalItems() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT  " + ITEMS_ID + " FROM " + ITEMS, null);
        int count = itemsCursor.getCount();
        itemsCursor.close();
        db.close();
        return count;
    }


    public ArrayList<ItemModel> getCartItems() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();

        Cursor itemsCursor = db.rawQuery("SELECT  * from " + ITEMS, null);


        while (itemsCursor.moveToNext()) {
            ItemModel itemModel = new ItemModel();

            itemModel.setItemid(itemsCursor.getString(itemsCursor.getColumnIndex(ITEMS_ID)));
            itemModel.setQuantity(Integer.valueOf(itemsCursor.getString(itemsCursor.getColumnIndex(QTY))));
            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
            itemModel.setPrice(itemsCursor.getString(itemsCursor.getColumnIndex(PRICE)));
            itemModel.setAmount(itemsCursor.getInt(itemsCursor.getColumnIndex(AMOUNT)));

            itemModelArrayList.add(itemModel);
        }
        itemsCursor.close();
        db.close();
        return itemModelArrayList;
    }


    public void deleteItem(String itemId) {
        SQLiteDatabase db = getWritableDatabase();


        //db.delete(ITEMS, ITEMS_ID + "= ?", new String[]{String.valueOf(item_Id)}); // used for if id is integer
        db.delete(ITEMS, ITEMS_ID + "= ?", new String[]{itemId});  // used for if id is String
        db.close();
    }


    public void claerAllItems() {
        SQLiteDatabase db = getWritableDatabase();

        // db.delete(ITEMS, null, null);


        db.execSQL("delete from " + ITEMS);
        //db.delete(ITEMS, null, null);

        db.close();
    }


//    public void insertItemsCounter(long itemID, double counter) {
//        SQLiteDatabase db = getWritableDatabase();
//
//        ContentValues S = new ContentValues();
//
//        Cursor itemsCursor = db.rawQuery("SELECT  " + ITEMS_ID + " FROM " + ITEMS + " WHERE " + ITEMS_ID + "=" + itemID, null);
//
//        if (counter > 0) {
//            // if already have then update other wise insert
//            if (itemsCursor.getCount() > 0) {
//                S.put(QTY, counter);
//                db.update(ITEMS, S, ITEMS_ID + "= ?", new String[]{String.valueOf(itemID)});
//            } else {
//                S.put(ITEMS_ID, itemID);
//                S.put(QTY, counter);
//                db.insert(ITEMS, null, S);
//            }
//            S.clear();
//            S.put(IS_ADDED_TO_CART, 1);
//            db.update(PRODUCTS, S, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
//        } else {
//            //delete
//            db.delete(CART, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
//            S.clear();
//            S.put(IS_ADDED_TO_CART, 0);
//            db.update(PRODUCTS, S, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
//        }
//    }


//    public void insertProducts(ArrayList<ItemModel> itemModelArrayList) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues S = new ContentValues();
//        ItemModel itemModel = null;
//
//        for (int i = 0; i < itemModelArrayList.size(); i++) {
//            itemModel = itemModelArrayList.get(i);
//            S.put(PRODUCT_ID, itemModel.getProduct_id());
//            S.put(MEAT_TYPE, itemModel.getMeat_type());
//            S.put(DATE, itemModel.getDate());
//            S.put(NAME, itemModel.getName());
//            S.put(URDU_NAME, itemModel.getUrdu_name());
//            S.put(UOM, itemModel.getUom());
//            S.put(LABOUR, itemModel.getLabour());
//            S.put(PAY_MODE, itemModel.getPay_mode());
//            S.put(PARTY_ID, itemModel.getParty_id());
//            S.put(RATE_EXPT_MEAT, itemModel.getRate_expt_meat());
//            S.put(WEIGHT_MEAT, itemModel.getWeight_meat());
//            S.put(URDU_UOM, itemModel.getUrdu_uom());
//            S.put(SPECIFICATION, itemModel.getSpecification());
//            S.put(PER_PERSON, itemModel.getPer_person());
//            S.put(STATUS, itemModel.getStatus());
//            S.put(ID, itemModel.getId());
//            S.put(SERVING, itemModel.getServing());
//            S.put(ROUND, itemModel.getRound());
//            S.put(UID, itemModel.getUid());
//            S.put(DATE_TIME, itemModel.getDate_time());
//            S.put(CATID, itemModel.getCatid());
//            S.put(SUBCATID, itemModel.getSubcatid());
//            S.put(KITCHENID, itemModel.getKitchenid());
//            S.put(PHOTO, itemModel.getPhoto());
//            S.put(CATEGORY_NAME, itemModel.getCategory_name());
//            S.put(SUBCATEGORY_NAME, itemModel.getSubcategory_name());
//            S.put(KITCHEN_NAME, itemModel.getKitchen_name());
//            S.put(MRATE, itemModel.getMrate());
//            db.insert(PRODUCTS, null, S);
//        }
//        db.close();
//
//
//    }

//    public void insertCategories(ArrayList<CategoriesModel> categoriesModelArrayList) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues S = new ContentValues();
//        CategoriesModel categoriesModel;
//
//        for (int i = 0; i < categoriesModelArrayList.size(); i++) {
//            categoriesModel = categoriesModelArrayList.get(i);
//            S.clear();
//            S.put(CATID, categoriesModel.getCatid());
//            S.put(NAME, categoriesModel.getName());
//            S.put(DESCRIPTION, categoriesModel.getDescription());
//            S.put(UID, categoriesModel.getUid());
//            db.insert(CATEGORIES, null, S);
//        }
//        db.close();
//    }

//    public void insertSubCategories(ArrayList<SubCategoriesModel> subCategoriesModelArrayList) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues S = new ContentValues();
//        SubCategoriesModel subCategoriesModel;
//
//        for (int i = 0; i < subCategoriesModelArrayList.size(); i++) {
//            subCategoriesModel = subCategoriesModelArrayList.get(i);
//            S.clear();
//            S.put(CATID, subCategoriesModel.getCatid());
//            S.put(NAME, subCategoriesModel.getName());
//            S.put(DESCRIPTION, subCategoriesModel.getDescription());
//            S.put(UID, subCategoriesModel.getUid());
//            S.put(SUBCATID, subCategoriesModel.getSubcatid());
//            S.put(CATEGORY_NAME, subCategoriesModel.getCategory_name());
//            db.insert(SUB_CATEGORIES, null, S);
//        }
//        db.close();
//    }

    //    public void insertCartCounter(long productID, double counter) {
//        SQLiteDatabase db = getWritableDatabase();
    ContentValues S = new ContentValues();
//
//        Cursor itemsCursor = db.rawQuery("SELECT  " + PRODUCT_ID + " FROM " + CART + " WHERE " + PRODUCT_ID + "=" + productID, null);
//
//        if (counter > 0) {
//            // if already have then update other wise insert
//            if (itemsCursor.getCount() > 0) {
//                S.put(PRODUCT__CART_QTY, counter);
//                db.update(CART, S, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
//            } else {
//                S.put(PRODUCT_ID, productID);
//                S.put(PRODUCT__CART_QTY, counter);
//                db.insert(CART, null, S);
//            }
//            S.clear();
//            S.put(IS_ADDED_TO_CART, 1);
//            db.update(PRODUCTS, S, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
//        } else {
//            //delete
//            db.delete(CART, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
//            S.clear();
//            S.put(IS_ADDED_TO_CART, 0);
//            db.update(PRODUCTS, S, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
//        }
//    }

//    public int getTotalCartCounter() {
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor itemsCursor = db.rawQuery("SELECT  " + PRODUCT_ID + " FROM " + CART, null);
//        int count = itemsCursor.getCount();
//        itemsCursor.close();
//        db.close();
//        return count;
//    }


//    public ArrayList<ItemModel> getProductsByCategory(int catID) {
//        SQLiteDatabase db = getWritableDatabase();
//        ArrayList<ItemModel> productModelArrayList = new ArrayList<>();
//        ItemModel itemModel = null;
//        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PRODUCTS + " WHERE " + CATID + "=" + catID, null);
//
//        while (itemsCursor.moveToNext()) {
//            itemModel = new ItemModel();
//            itemModel.setProduct_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_ID)));
//            itemModel.setMeat_type(itemsCursor.getInt(itemsCursor.getColumnIndex(MEAT_TYPE)));
//            itemModel.setDate(itemsCursor.getString(itemsCursor.getColumnIndex(DATE)));
//            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
//            itemModel.setUrdu_name(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_NAME)));
//            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
//            itemModel.setLabour(itemsCursor.getString(itemsCursor.getColumnIndex(LABOUR)));
//            itemModel.setPay_mode(itemsCursor.getString(itemsCursor.getColumnIndex(PAY_MODE)));
//            itemModel.setParty_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PARTY_ID)));
//            itemModel.setRate_expt_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(RATE_EXPT_MEAT)));
//            itemModel.setWeight_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(WEIGHT_MEAT)));
//            itemModel.setUrdu_uom(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_UOM)));
//            itemModel.setSpecification(itemsCursor.getString(itemsCursor.getColumnIndex(SPECIFICATION)));
//            itemModel.setPer_person(itemsCursor.getDouble(itemsCursor.getColumnIndex(PER_PERSON)));
//            itemModel.setStatus(itemsCursor.getInt(itemsCursor.getColumnIndex(STATUS)));
//            itemModel.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(ID)));
//            itemModel.setServing(itemsCursor.getDouble(itemsCursor.getColumnIndex(SERVING)));
//            itemModel.setRound(itemsCursor.getInt(itemsCursor.getColumnIndex(ROUND)));
//            itemModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
//            itemModel.setDate_time(itemsCursor.getString(itemsCursor.getColumnIndex(DATE_TIME)));
//            itemModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
//            itemModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
//            itemModel.setKitchenid(itemsCursor.getInt(itemsCursor.getColumnIndex(KITCHENID)));
//            itemModel.setPhoto(itemsCursor.getString(itemsCursor.getColumnIndex(PHOTO)));
//            itemModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
//            itemModel.setSubcategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(SUBCATEGORY_NAME)));
//            itemModel.setKitchen_name(itemsCursor.getString(itemsCursor.getColumnIndex(KITCHEN_NAME)));
//            itemModel.setMrate(itemsCursor.getDouble(itemsCursor.getColumnIndex(MRATE)));
//
//            itemModel.setIsAddedToCart(itemsCursor.getInt(itemsCursor.getColumnIndex(IS_ADDED_TO_CART)));
//
//            productModelArrayList.add(itemModel);
//        }
//        itemsCursor.close();
//        db.close();
//        return productModelArrayList;
//    }

//    public ArrayList<ItemModel> getProductsByCatAndSubCat(int catID, int subCatID) {
//        SQLiteDatabase db = getWritableDatabase();
//        ArrayList<ItemModel> productModelArrayList = new ArrayList<>();
//        ItemModel itemModel = null;
//        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PRODUCTS + " WHERE " + CATID + "=" + catID + " AND " + SUBCATID + "=" + subCatID, null);
//
//        while (itemsCursor.moveToNext()) {
//            itemModel = new ItemModel();
//            itemModel.setProduct_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_ID)));
//            itemModel.setMeat_type(itemsCursor.getInt(itemsCursor.getColumnIndex(MEAT_TYPE)));
//            itemModel.setDate(itemsCursor.getString(itemsCursor.getColumnIndex(DATE)));
//            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
//            itemModel.setUrdu_name(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_NAME)));
//            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
//            itemModel.setLabour(itemsCursor.getString(itemsCursor.getColumnIndex(LABOUR)));
//            itemModel.setPay_mode(itemsCursor.getString(itemsCursor.getColumnIndex(PAY_MODE)));
//            itemModel.setParty_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PARTY_ID)));
//            itemModel.setRate_expt_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(RATE_EXPT_MEAT)));
//            itemModel.setWeight_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(WEIGHT_MEAT)));
//            itemModel.setUrdu_uom(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_UOM)));
//            itemModel.setSpecification(itemsCursor.getString(itemsCursor.getColumnIndex(SPECIFICATION)));
//            itemModel.setPer_person(itemsCursor.getDouble(itemsCursor.getColumnIndex(PER_PERSON)));
//            itemModel.setStatus(itemsCursor.getInt(itemsCursor.getColumnIndex(STATUS)));
//            itemModel.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(ID)));
//            itemModel.setServing(itemsCursor.getDouble(itemsCursor.getColumnIndex(SERVING)));
//            itemModel.setRound(itemsCursor.getInt(itemsCursor.getColumnIndex(ROUND)));
//            itemModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
//            itemModel.setDate_time(itemsCursor.getString(itemsCursor.getColumnIndex(DATE_TIME)));
//            itemModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
//            itemModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
//            itemModel.setKitchenid(itemsCursor.getInt(itemsCursor.getColumnIndex(KITCHENID)));
//            itemModel.setPhoto(itemsCursor.getString(itemsCursor.getColumnIndex(PHOTO)));
//            itemModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
//            itemModel.setSubcategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(SUBCATEGORY_NAME)));
//            itemModel.setKitchen_name(itemsCursor.getString(itemsCursor.getColumnIndex(KITCHEN_NAME)));
//            itemModel.setMrate(itemsCursor.getDouble(itemsCursor.getColumnIndex(MRATE)));
//            itemModel.setIsAddedToCart(itemsCursor.getInt(itemsCursor.getColumnIndex(IS_ADDED_TO_CART)));
//
//            productModelArrayList.add(itemModel);
//        }
//        itemsCursor.close();
//        db.close();
//        return productModelArrayList;
//    }
//
//    public ArrayList<ItemModel> getProductsBySubCategory(int subCatID) {
//        SQLiteDatabase db = getWritableDatabase();
//        ArrayList<ItemModel> productModelArrayList = new ArrayList<>();
//        ItemModel itemModel = null;
//        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PRODUCTS + " WHERE " + SUBCATID + "=" + subCatID, null);
//
//        while (itemsCursor.moveToNext()) {
//            itemModel = new ItemModel();
//            itemModel.setProduct_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_ID)));
//            itemModel.setMeat_type(itemsCursor.getInt(itemsCursor.getColumnIndex(MEAT_TYPE)));
//            itemModel.setDate(itemsCursor.getString(itemsCursor.getColumnIndex(DATE)));
//            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
//            itemModel.setUrdu_name(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_NAME)));
//            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
//            itemModel.setLabour(itemsCursor.getString(itemsCursor.getColumnIndex(LABOUR)));
//            itemModel.setPay_mode(itemsCursor.getString(itemsCursor.getColumnIndex(PAY_MODE)));
//            itemModel.setParty_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PARTY_ID)));
//            itemModel.setRate_expt_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(RATE_EXPT_MEAT)));
//            itemModel.setWeight_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(WEIGHT_MEAT)));
//            itemModel.setUrdu_uom(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_UOM)));
//            itemModel.setSpecification(itemsCursor.getString(itemsCursor.getColumnIndex(SPECIFICATION)));
//            itemModel.setPer_person(itemsCursor.getDouble(itemsCursor.getColumnIndex(PER_PERSON)));
//            itemModel.setStatus(itemsCursor.getInt(itemsCursor.getColumnIndex(STATUS)));
//            itemModel.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(ID)));
//            itemModel.setServing(itemsCursor.getDouble(itemsCursor.getColumnIndex(SERVING)));
//            itemModel.setRound(itemsCursor.getInt(itemsCursor.getColumnIndex(ROUND)));
//            itemModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
//            itemModel.setDate_time(itemsCursor.getString(itemsCursor.getColumnIndex(DATE_TIME)));
//            itemModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
//            itemModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
//            itemModel.setKitchenid(itemsCursor.getInt(itemsCursor.getColumnIndex(KITCHENID)));
//            itemModel.setPhoto(itemsCursor.getString(itemsCursor.getColumnIndex(PHOTO)));
//            itemModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
//            itemModel.setSubcategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(SUBCATEGORY_NAME)));
//            itemModel.setKitchen_name(itemsCursor.getString(itemsCursor.getColumnIndex(KITCHEN_NAME)));
//            itemModel.setMrate(itemsCursor.getDouble(itemsCursor.getColumnIndex(MRATE)));
//            itemModel.setIsAddedToCart(itemsCursor.getInt(itemsCursor.getColumnIndex(IS_ADDED_TO_CART)));
//
//            productModelArrayList.add(itemModel);
//        }
//        itemsCursor.close();
//        db.close();
//        return productModelArrayList;
//    }
//
//    public ArrayList<CategoriesModel> getCategories() {
//        SQLiteDatabase db = getWritableDatabase();
//        ArrayList<CategoriesModel> categoriesModelArrayList = new ArrayList<>();
//        CategoriesModel categoriesModel = null;
//        Cursor itemsCursor = db.rawQuery("select * from " + CATEGORIES, null);
//
//        while (itemsCursor.moveToNext()) {
//            categoriesModel = new CategoriesModel();
//            categoriesModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
//            categoriesModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
//            categoriesModel.setDescription(itemsCursor.getString(itemsCursor.getColumnIndex(DESCRIPTION)));
//            categoriesModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
//            categoriesModelArrayList.add(categoriesModel);
//        }
//        itemsCursor.close();
//        db.close();
//        return categoriesModelArrayList;
//    }

//    public ArrayList<SubCategoriesModel> getSubCategoriesByCatID(int catID) {
//        SQLiteDatabase db = getWritableDatabase();
//        ArrayList<SubCategoriesModel> subCategoriesModelArrayList = new ArrayList<>();
//        SubCategoriesModel subCategoriesModel = null;
//        Cursor itemsCursor = db.rawQuery("select * from " + SUB_CATEGORIES + " Where " + CATID + "=" + catID, null);
//
//        while (itemsCursor.moveToNext()) {
//            subCategoriesModel = new SubCategoriesModel();
//            subCategoriesModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
//            subCategoriesModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
//            subCategoriesModel.setDescription(itemsCursor.getString(itemsCursor.getColumnIndex(DESCRIPTION)));
//            subCategoriesModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
//            subCategoriesModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
//            subCategoriesModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
//            subCategoriesModelArrayList.add(subCategoriesModel);
//        }
//        itemsCursor.close();
//        db.close();
//        return subCategoriesModelArrayList;
//    }

//    public int getTotalCategories() {
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor itemsCursor = db.rawQuery("SELECT  " + CATID + " FROM " + CATEGORIES, null);
//        int count = itemsCursor.getCount();
//        itemsCursor.close();
//        db.close();
//        return count;
//    }
//
//    public int getTotalSubCategories() {
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor itemsCursor = db.rawQuery("SELECT  " + SUBCATID + " FROM " + SUB_CATEGORIES, null);
//        return itemsCursor.getCount();
//    }

//    public ItemModel getProductDetail(long productID) {
//        SQLiteDatabase db = getWritableDatabase();
//        ItemModel itemModel = new ItemModel();
//        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PRODUCTS + " Where " + PRODUCT_ID + "=" + productID, null);
//        while (itemsCursor.moveToNext()) {
//            itemModel.setProduct_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_ID)));
//            itemModel.setMeat_type(itemsCursor.getInt(itemsCursor.getColumnIndex(MEAT_TYPE)));
//            itemModel.setDate(itemsCursor.getString(itemsCursor.getColumnIndex(DATE)));
//            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
//            itemModel.setUrdu_name(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_NAME)));
//            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
//            itemModel.setLabour(itemsCursor.getString(itemsCursor.getColumnIndex(LABOUR)));
//            itemModel.setPay_mode(itemsCursor.getString(itemsCursor.getColumnIndex(PAY_MODE)));
//            itemModel.setParty_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PARTY_ID)));
//            itemModel.setRate_expt_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(RATE_EXPT_MEAT)));
//            itemModel.setWeight_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(WEIGHT_MEAT)));
//            itemModel.setUrdu_uom(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_UOM)));
//            itemModel.setSpecification(itemsCursor.getString(itemsCursor.getColumnIndex(SPECIFICATION)));
//            itemModel.setPer_person(itemsCursor.getDouble(itemsCursor.getColumnIndex(PER_PERSON)));
//            itemModel.setStatus(itemsCursor.getInt(itemsCursor.getColumnIndex(STATUS)));
//            itemModel.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(ID)));
//            itemModel.setServing(itemsCursor.getDouble(itemsCursor.getColumnIndex(SERVING)));
//            itemModel.setRound(itemsCursor.getInt(itemsCursor.getColumnIndex(ROUND)));
//            itemModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
//            itemModel.setDate_time(itemsCursor.getString(itemsCursor.getColumnIndex(DATE_TIME)));
//            itemModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
//            itemModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
//            itemModel.setKitchenid(itemsCursor.getInt(itemsCursor.getColumnIndex(KITCHENID)));
//            itemModel.setPhoto(itemsCursor.getString(itemsCursor.getColumnIndex(PHOTO)));
//            itemModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
//            itemModel.setSubcategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(SUBCATEGORY_NAME)));
//            itemModel.setKitchen_name(itemsCursor.getString(itemsCursor.getColumnIndex(KITCHEN_NAME)));
//            itemModel.setMrate(itemsCursor.getDouble(itemsCursor.getColumnIndex(MRATE)));
//            itemModel.setIsAddedToCart(itemsCursor.getInt(itemsCursor.getColumnIndex(IS_ADDED_TO_CART)));
//        }
//        itemsCursor.close();
//        db.close();
//        return itemModel;
//    }


//    public void clearCart(String IDsString) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues S = new ContentValues();
//        S.put(IS_ADDED_TO_CART, 0);
//        db.update(PRODUCTS, S, PRODUCT_ID + " IN (" + IDsString + ")", null);
//
//        db.delete(CART, null, null);
//        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + CART + "'");
//        db.close();
//    }

//    public ArrayList<ItemModel> getAllProducts() {
//        SQLiteDatabase db = getWritableDatabase();
//        ArrayList<ItemModel> productModelArrayList = new ArrayList<>();
//        ItemModel itemModel = null;
//        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PRODUCTS + " where " + PRODUCTS + "." + IS_ADDED_TO_CART + "=0", null);
//
//        while (itemsCursor.moveToNext()) {
//            itemModel = new ItemModel();
//            itemModel.setProduct_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_ID)));
//            itemModel.setMeat_type(itemsCursor.getInt(itemsCursor.getColumnIndex(MEAT_TYPE)));
//            itemModel.setDate(itemsCursor.getString(itemsCursor.getColumnIndex(DATE)));
//            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
//            itemModel.setUrdu_name(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_NAME)));
//            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
//            itemModel.setLabour(itemsCursor.getString(itemsCursor.getColumnIndex(LABOUR)));
//            itemModel.setPay_mode(itemsCursor.getString(itemsCursor.getColumnIndex(PAY_MODE)));
//            itemModel.setParty_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PARTY_ID)));
//            itemModel.setRate_expt_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(RATE_EXPT_MEAT)));
//            itemModel.setWeight_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(WEIGHT_MEAT)));
//            itemModel.setUrdu_uom(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_UOM)));
//            itemModel.setSpecification(itemsCursor.getString(itemsCursor.getColumnIndex(SPECIFICATION)));
//            itemModel.setPer_person(itemsCursor.getDouble(itemsCursor.getColumnIndex(PER_PERSON)));
//            itemModel.setStatus(itemsCursor.getInt(itemsCursor.getColumnIndex(STATUS)));
//            itemModel.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(ID)));
//            itemModel.setServing(itemsCursor.getDouble(itemsCursor.getColumnIndex(SERVING)));
//            itemModel.setRound(itemsCursor.getInt(itemsCursor.getColumnIndex(ROUND)));
//            itemModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
//            itemModel.setDate_time(itemsCursor.getString(itemsCursor.getColumnIndex(DATE_TIME)));
//            itemModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
//            itemModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
//            itemModel.setKitchenid(itemsCursor.getInt(itemsCursor.getColumnIndex(KITCHENID)));
//            itemModel.setPhoto(itemsCursor.getString(itemsCursor.getColumnIndex(PHOTO)));
//            itemModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
//            itemModel.setSubcategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(SUBCATEGORY_NAME)));
//            itemModel.setKitchen_name(itemsCursor.getString(itemsCursor.getColumnIndex(KITCHEN_NAME)));
//            itemModel.setMrate(itemsCursor.getDouble(itemsCursor.getColumnIndex(MRATE)));
//
//            itemModel.setIsAddedToCart(itemsCursor.getInt(itemsCursor.getColumnIndex(IS_ADDED_TO_CART)));
//
//            productModelArrayList.add(itemModel);
//        }
//        itemsCursor.close();
//        db.close();
//        return productModelArrayList;
//    }
}
