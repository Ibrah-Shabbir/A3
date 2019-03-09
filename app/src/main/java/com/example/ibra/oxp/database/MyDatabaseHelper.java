package com.example.ibra.oxp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.SignUp;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.models.User;

import java.util.ArrayList;
import java.util.EnumMap;

public class MyDatabaseHelper  extends Base {


    MyDatabase myhelper;

    public MyDatabaseHelper(Context context) {
        myhelper = new MyDatabase(context);
    }

    ////////INSERT USER DATA////////////
    public long insertUserData(Integer id, String fname, String lname, String city, String email, String password, String address, Integer phonenu, String type) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();

        contentValues1.put(MyDatabase.USER_ID, id);
        contentValues1.put(MyDatabase.USER_FNAME, fname);
        contentValues1.put(MyDatabase.USER_LNAME, lname);
        contentValues1.put(MyDatabase.USER_CITY, city);
        contentValues1.put(MyDatabase.USER_EMAIL, email);
        contentValues1.put(MyDatabase.USER_PASSWORD, password);
        contentValues1.put(MyDatabase.USER_ADDRESS, address);
        contentValues1.put(MyDatabase.USER_PHONENU, phonenu);

        contentValues1.put(MyDatabase.USER_TYPE, type);

        long id2 = dbb.insert(MyDatabase.TABLE_USER, null, contentValues1);

        return id2;

    }

    public void InsertData(User u)
    {
        //Toast.makeText(MyDatabaseHelper.this,u.get_fname()+"  "+u.get_lname()+""+u.get_city()+" "+u.get_address()+" "+u.get_phone()+" "+u.get_password()+" "+u.get_email(),Toast.LENGTH_SHORT).show();
        SQLiteDatabase sql_db = myhelper.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        //content_values.put(dbHandler.user_id_col, i);
        content_values.put(MyDatabase.USER_FNAME, u.get_fname());
        content_values.put(MyDatabase.USER_LNAME, u.get_lname());
        content_values.put(MyDatabase.USER_CITY, u.get_city());
        content_values.put(MyDatabase.USER_ADDRESS, u.get_address());
        content_values.put(MyDatabase.USER_PHONENU, u.get_phone());
        content_values.put(MyDatabase.USER_EMAIL, u.get_email());
        content_values.put(MyDatabase.USER_PASSWORD, u.get_password());
        long id = sql_db.insert(MyDatabase.TABLE_USER, null, content_values);


        sql_db.close();
        //return id;
    }

    public boolean validate_user(String email,String password)
    {
        boolean flag=false;
        String query = "SELECT* FROM "+myhelper.TABLE_USER+" WHERE ("+myhelper.USER_EMAIL+"='"+email+"' and "+myhelper.USER_PASSWORD+"='"+password+"')";
        SQLiteDatabase sql_db= myhelper.getReadableDatabase();
        Cursor cursor= sql_db.rawQuery(query,null);
        String[]data=null;
        if(cursor.getCount()!=0 && cursor.getCount()==1)
        {
             flag=true;
        }
        return flag;
    }

    //////////INSERT SERVICE DATA////////////////

    public long insertServiceData(Integer id, String name,  String description) {
        SQLiteDatabase dbbb = myhelper.getWritableDatabase();
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(MyDatabase.SERVICE_ID, id);

        contentValues2.put(MyDatabase.SERVICE_NAME, name);

        contentValues2.put(MyDatabase.SERVICE_DESCRIPTION, description);
        long id1 = dbbb.insert(MyDatabase.TABLE_SERVICE, null, contentValues2);
        return id1;

    }
      //////////////////GET USER DATA//////////////



    public void InsertService( MyService s)
    {
        boolean flag=false;
        //Toast.makeText(MyDatabaseHelper.this,u.get_fname()+"  "+u.get_lname()+""+u.get_city()+" "+u.get_address()+" "+u.get_phone()+" "+u.get_password()+" "+u.get_email(),Toast.LENGTH_SHORT).show();
        SQLiteDatabase sql_db = myhelper.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        //content_values.put(dbHandler.user_id_col, i);
        content_values.put(MyDatabase.SERVICE_NAME, s.getName());
        content_values.put(MyDatabase.SERVICE_DESCRIPTION, s.getDescription());


        long id = sql_db.insert(MyDatabase.TABLE_SERVICE, null, content_values);


        sql_db.close();
        //return id;
    }






    public String getUserData ()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {MyDatabase.USER_ID, MyDatabase.USER_FNAME, MyDatabase.USER_LNAME, MyDatabase.USER_CITY, MyDatabase.USER_EMAIL, MyDatabase.USER_PASSWORD, MyDatabase.USER_ADDRESS, MyDatabase.USER_PHONENU, MyDatabase.USER_TYPE};
        Cursor cursor = db.query(MyDatabase.TABLE_USER, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(cursor.getColumnIndex(MyDatabase.USER_ID));
            String fname = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_FNAME));
            String lname = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_LNAME));
            String city = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_CITY));
            String email = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_EMAIL));
            String password = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_PASSWORD));
            String address = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_ADDRESS));
            String phonenu = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_PHONENU));
            String type = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_TYPE));

            buffer.append(cid + "   " + fname + "   " + lname + "   " + city + "   " + email + " " + address + "   " + phonenu + "   " + type + "   "+ password + " \n");
        }
        return buffer.toString();
    }
    public long insertProductData(Integer id, String name, Integer price, String description, Integer createdDate, Integer updatedDate, Integer quantity) {
        SQLiteDatabase dbbb = myhelper.getWritableDatabase();
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(MyDatabase.PRODUCT_ID, id);

        contentValues2.put(MyDatabase.PRODUCT_NAME, name);
        contentValues2.put(MyDatabase.PRODUCT_PRICE, price);
        contentValues2.put(MyDatabase.PRODUCT_DESCRIPTION, description);
        contentValues2.put(MyDatabase.PRODUCT_CREATEDDATE, createdDate);
        contentValues2.put(MyDatabase.PRODUCT_UPDATEDDATE, updatedDate);
        contentValues2.put(MyDatabase.PRODUCT_QUANTITY, quantity);
        long id1 = dbbb.insert(MyDatabase.TABLE_PRODUCT, null, contentValues2);
        return id1;

    }
    //////////////////GET USER DATA//////////////



    public void InsertProduct( MyProduct p)
    {
        boolean flag=false;
        //Toast.makeText(MyDatabaseHelper.this,u.get_fname()+"  "+u.get_lname()+""+u.get_city()+" "+u.get_address()+" "+u.get_phone()+" "+u.get_password()+" "+u.get_email(),Toast.LENGTH_SHORT).show();
        SQLiteDatabase sql_db = myhelper.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        //content_values.put(dbHandler.user_id_col, i);
        content_values.put(MyDatabase.PRODUCT_NAME, p.getName());
        content_values.put(MyDatabase.PRODUCT_DESCRIPTION, p.getDescription());
        content_values.put(MyDatabase.PRODUCT_PRICE, p.getPrice());
        content_values.put(MyDatabase.PRODUCT_QUANTITY, p.getQuantity());
        content_values.put(MyDatabase.PRODUCT_IMAGE, p.getImage());

        long id = sql_db.insert(MyDatabase.TABLE_PRODUCT, null, content_values);


        sql_db.close();
        //return id;
    }



    /////////////// GET PRODUCT DATA ////////////////////////

    /*public String getProductData ()
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        String[] columns = {MyDatabase.PRODUCT_ID, MyDatabase.PRODUCT_NAME, MyDatabase.PRODUCT_PRICE, MyDatabase.PRODUCT_DESCRIPTION, MyDatabase.PRODUCT_CREATEDDATE, MyDatabase.PRODUCT_UPDATEDDATE, MyDatabase.PRODUCT_QUANTITY};
        Cursor cursor = dbb.query(MyDatabase.TABLE_PRODUCTS, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {

            int ccid = cursor.getInt(cursor.getColumnIndex(MyDatabase.PRODUCT_ID));
            double price = cursor.getDouble(cursor.getColumnIndex(MyDatabase.PRODUCT_PRICE));
            String name = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_NAME));
            String description = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_DESCRIPTION));
            String createdDate = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_CREATEDDATE));
            String updatedDate = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_UPDATEDDATE));
            String quantity = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_QUANTITY));

            buffer.append(ccid + "   " + price + "   " + name + "   " + description + "   " + createdDate + " " + updatedDate + "   " + quantity + " \n");
        }
        return buffer.toString();
    }*/

    //public ArrayList<MyService> getProductData2 (ArrayList<MyService>list)
     public void getProductData2 (ArrayList<MyProduct>list)
    {
        //ArrayList<MyService> list=new ArrayList<MyService>();
        //list=new ArrayList<MyService>();
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        String[] columns = {MyDatabase.PRODUCT_ID,MyDatabase.PRODUCT_FK_EMAIL, MyDatabase.PRODUCT_NAME, MyDatabase.PRODUCT_PRICE, MyDatabase.PRODUCT_DESCRIPTION, MyDatabase.PRODUCT_CREATEDDATE, MyDatabase.PRODUCT_UPDATEDDATE, MyDatabase.PRODUCT_QUANTITY};
        Cursor cursor = dbb.query(MyDatabase.TABLE_PRODUCT, columns, null, null, null, null, null);
        //StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {

            int ccid = cursor.getInt(cursor.getColumnIndex(MyDatabase.PRODUCT_ID));
            String  price = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_PRICE));
            String name = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_NAME));
            String description = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_DESCRIPTION));
            String quantity = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_QUANTITY));
            MyProduct p=new MyProduct(1,name,description,price,quantity,"jj","ppp");
            list.add(p);
           // buffer.append(ccid + "   " + price + "   " + name + "   " + description + "   " + createdDate + " " + updatedDate + "   " + quantity + " \n");
        }
        //return list;
    }


    public void getServiceData2 (ArrayList<MyService>list)
    {
        //ArrayList<MyService> list=new ArrayList<MyService>();
        //list=new ArrayList<MyService>();
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        String[] columns = {MyDatabase.SERVICE_ID,MyDatabase.SERVICE_FK_EMAIL, MyDatabase.SERVICE_NAME,  MyDatabase.SERVICE_DESCRIPTION};
        Cursor cursor = dbb.query(MyDatabase.TABLE_SERVICE, columns, null, null, null, null, null);
        //StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {

            int ccid = cursor.getInt(cursor.getColumnIndex(MyDatabase.SERVICE_ID));
            String name = cursor.getString(cursor.getColumnIndex(MyDatabase.SERVICE_NAME));
            String description = cursor.getString(cursor.getColumnIndex(MyDatabase.SERVICE_DESCRIPTION));

            MyService s=new  MyService(1,name,description);
            list.add(s);
            // buffer.append(ccid + "   " + price + "   " + name + "   " + description + "   " + createdDate + " " + updatedDate + "   " + quantity + " \n");
        }
        //return list;
    }


    public void Image ()
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();

    }

}
