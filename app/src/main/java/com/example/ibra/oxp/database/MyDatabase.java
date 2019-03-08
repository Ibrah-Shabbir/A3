package com.example.ibra.oxp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "OXP_DATABASE.db";    // Database Name
    public static final String TABLE_USER = "User";          //Table Name
    public static final String TABLE_PRODUCT = "Product";// Table Name
    public static final String TABLE_IMAGE = "Image";// Table Name
    private static final int DATABASE_Version = 1;

    ///////////USER///////////////////////

    public static final String USER_ID = "id";
    public static final String USER_FNAME = "f_name";
    public static final String USER_LNAME = "l_name";
    public static final String USER_EMAIL = "email";
    public static final String USER_CITY = "city";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ADDRESS = "address";
    public static final String USER_PHONENU = "phone_no";
    public static final String USER_TYPE = "type";

    /////////////PRODUCT///////////////////////

    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_FK_EMAIL = "user_email";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_DESCRIPTION = "description";
    public static final String PRODUCT_CREATEDDATE = "created_date";
    public static final String PRODUCT_UPDATEDDATE = "updated_date";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String PRODUCT_IMAGE="image";

    /////////////IMAGE/////////////////////////
    public static final String IMAGE_ID = "id";
    public static final String IMAGE_NAME ="name";
    public static final String IMAGE_IMAGE = "image";

    ////////CREATE TABLE OF USER ///////////////

    private static final String CREATE_USER_TABLE ="CREATE TABLE "
            + TABLE_USER + "(" + USER_ID
            + " TEXT PRIMARY KEY,"
            + USER_EMAIL +" TEXT UNIQUE,"
            + USER_FNAME + " TEXT,"
            + USER_LNAME +" TEXT,"
            + USER_CITY +" TEXT,"
            + USER_PASSWORD+" TEXT,"
            + USER_TYPE+" TEXT,"
            + USER_ADDRESS +" TEXT,"
            + USER_PHONENU +" TEXT );";

    /////////////CREATE TABLE OF PRODUCT//////////////////
    private static final String CREATE_PRODUCT_TABLE= "CREATE TABLE "
            + TABLE_PRODUCT + "(" + PRODUCT_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PRODUCT_FK_EMAIL+" TEXT,"
            + PRODUCT_NAME+" TEXT,"
            + PRODUCT_PRICE +" TEXT,"
            + PRODUCT_DESCRIPTION+" TEXT,"
            + PRODUCT_CREATEDDATE +" TEXT,"
            + PRODUCT_QUANTITY +" TEXT,"
            + PRODUCT_UPDATEDDATE +" TEXT,"
            + PRODUCT_IMAGE+" TEXT,"
            + "FOREIGN KEY ("+PRODUCT_FK_EMAIL+") REFERENCES "+TABLE_USER+" ("+USER_ID+") );";

    /////////////CREATE TABLE OF IMAGE//////////////////
    private static final String CREATE_IMAGE_TABLE= "CREATE TABLE "
            + TABLE_IMAGE +"("
            +IMAGE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +IMAGE_NAME+" TEXT,"
            +IMAGE_IMAGE+" BLOP );";


    ///////// DROP TABLE IF EXIST //////////

    private static final String DROP_USER_TABLE ="DROP TABLE IF EXISTS "+TABLE_USER;
    private static final String DROP_PRODUCT_TABLE ="DROP TABLE IF EXISTS "+TABLE_PRODUCT;
    private static final String DROP_IMAGE_TABLE ="DROP TABLE IF EXISTS "+TABLE_IMAGE;
    private Context context;

    ///////CONSTRUCTOR////////

    public MyDatabase (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_Version);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        //db.execSQL(CREATE_IMAGE_TABLE);



    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_PRODUCT_TABLE);
        //db.execSQL(DROP_IMAGE_TABLE);
        onCreate(db);

    }


}

