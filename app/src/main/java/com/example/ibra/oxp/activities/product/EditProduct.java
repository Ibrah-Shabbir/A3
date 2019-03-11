package com.example.ibra.oxp.activities.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.myAccount.ViewMyProducts;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.models.Category;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.utils.DialogBuilder;
import com.example.ibra.oxp.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProduct extends Base
{
    @BindView(R.id.edit_product_name)
    TextView name;
    @BindView(R.id.edit_product_image)
    ImageView image;
    @BindView(R.id.edit_product_price)
    TextView price;
    @BindView(R.id.edit_product_quantity)
    TextView quantity;
    @BindView(R.id.edit_product_description)
    TextView description;
    @BindView(R.id.toolbar2)
    Toolbar toolbar2;
    @BindView(R.id.edit_product_image_name)
    TextView image_name;
    RequestQueue requestQueue;
    private List<String> categoryList;
    private ArrayAdapter<String> adapter;
    @BindView(R.id.edit_product_spinner)
    Spinner spinner;
    private MyProduct oldProduct;
    String new_category;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    String mCurrentPhotoPath;
    String encodedImage="";
    Bitmap new_bitmap=null;
    MyProduct updatedProduct;
    private DialogBuilder dialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar2);
        bottom();
        dialogBuilder = new DialogBuilder(this);
        requestQueue = Volley.newRequestQueue(this);
        updatedProduct=new MyProduct();
        categoryList = new ArrayList<>();
        receiveData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.search).setVisible(false);
        return true;
    }

    private void setCategoryData(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryList);
        spinner.setAdapter(adapter);
    }

    private void requestCategory()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status_code = "";
                String string_response = "";
                try {
                    status_code = response.getString("status_code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status_code.equals("200")) {
                    //Log.d("PRODUCT ADDED!", response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        int length = jsonArray.length();

                        for (int i = length - 1; i >= 0; i--) //////newly added products will be shown first
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            categoryList.add(jsonObject.getString("description"));
                        }

                        replaceCategory();
                        setCategoryData();
                    }
                    catch(JSONException e) {
                        Log.e("OXP_TAG", e.getMessage());
                    }
                    //Toast.makeText(AddProduct.this, string_response, Toast.LENGTH_SHORT).show();
                } else
                {
                    try { string_response=response.getString("data");}catch(JSONException e) { }
                    Log.d("ERROR ADDING PRODUCT!", string_response);
                    Toast.makeText(EditProduct.this,string_response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("OXP_TAG", error.toString());
                Toast.makeText(EditProduct.this,"CATEGORY VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void setViewData()
    {
        //String contact_no=sharedPref.readValue("contact_no","No contact number to show");
        name.setText(name.getText()+oldProduct.getName());
        description.setText(description.getText()+oldProduct.getDescription());
        quantity.setText(quantity.getText()+oldProduct.getQuantity());
        price.setText(price.getText()+oldProduct.getPrice());
        //contactNumber.setText(contactNumber.getText()+contact_no);

        Glide.with(this)
                .asBitmap()
                .load(oldProduct.getImage())
                .into(new BitmapImageViewTarget(image));
        new_category=oldProduct.getCategory();
    }

    private void getUpdatedViewData()
    {
        updatedProduct.setName(name.getText().toString().trim());
        updatedProduct.setDescription(description.getText().toString().trim());
        updatedProduct.setQuantity(quantity.getText().toString().trim());
        updatedProduct.setPrice(price.getText().toString().trim());
        updatedProduct.setCategory(new_category);
        updatedProduct.setId(oldProduct.getId());
    }

    private void receiveData()
    {
        oldProduct=(MyProduct)getIntent().getSerializableExtra("Product");
        Toast.makeText(EditProduct.this, oldProduct.getName()+" "+oldProduct.getPrice()+" "+oldProduct.getDescription(), Toast.LENGTH_SHORT).show();
        requestCategory();
        setViewData();

    }


    void replaceCategory()
    {
        int index=categoryList.indexOf(new_category);
        //int index2=categoryList.indexOf(new_category);
        //Toast.makeText(this, "Selected "+categoryList.contains(new_category)+" "+index, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, new_category, Toast.LENGTH_SHORT).show();
        categoryList.contains(new_category);
        Collections.swap(categoryList,index,0);

    }

    private void SelectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProduct.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Camera")) {
                    dispatchTakePictureIntent();
                } else if (items[which].equals("Gallery")) {
                    openGallery();

                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.ibra.oxp",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @OnClick(R.id.edit_product_fab)
    public void floating_btn_listener()
    {
        SelectImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                getEncodedImage();
                setImageNameView(0);
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }

        } else if (requestCode == SELECT_FILE) {
            if (resultCode == RESULT_OK) {
                InputStream inputStream = null;
                try
                {
                    inputStream = getContentResolver().openInputStream(data.getData());
                }
                catch (FileNotFoundException e) { e.printStackTrace(); }
                FileOutputStream fileOutputStream = null;
                try
                {
                    fileOutputStream = new FileOutputStream(createImageFile());
                }
                catch (IOException e) { e.printStackTrace(); }
                try
                {
                    copyImageToNewPath(inputStream, fileOutputStream);
                }
                catch (IOException e) { e.printStackTrace(); }
                try
                {
                    fileOutputStream.close();
                }
                catch (IOException e) { e.printStackTrace(); }
                try
                {
                    inputStream.close();
                }
                catch (IOException e) { e.printStackTrace(); }
                getEncodedImage();

                setImageNameView(1);
            }
            else if (resultCode == RESULT_CANCELED) { finish(); }

        }

    }

    private void getEncodedImage() {
        File file = new File(mCurrentPhotoPath);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media
                    .getBitmap(this.getContentResolver(), Uri.fromFile(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            encodedImage = encodeImage(bitmap);
            new_bitmap=bitmap;
            setImage();
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    private void setImageNameView(int casee)
    {
        if(casee==0)
            image_name.setText("Image Captured");
        else if (casee==1)
            image_name.setText("Image Uploaded");
    }

    private static void copyImageToNewPath(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, SELECT_FILE);
    }

    private void setImage()
    {
        image.setImageBitmap(new_bitmap);
    }

    @OnClick(R.id.edit_product_button)
    public void updateProduct()
    {
        getUpdatedViewData();
        SharedPref sharedPref = new SharedPref(getApplicationContext());
        final String id = sharedPref.readValue("id", "0");
        final int user_id = Integer.parseInt(id);
        String completeURL = String.format(product_url+"?user_id=%1$s&id=%2$s&name=%3$s&category=%4$s&description=%5$s&price=%6$s&quantity=%7$s&image=%8$s", user_id,updatedProduct.getId(),updatedProduct.getName(),new_category,updatedProduct.getDescription(),updatedProduct.getPrice(),updatedProduct.getQuantity(),encodedImage);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", updatedProduct.getId());
            jsonObject.put("user_id",user_id);
            jsonObject.put("name", updatedProduct.getName());
            jsonObject.put("category", new_category);
            jsonObject.put("description", updatedProduct.getDescription());
            jsonObject.put("price",updatedProduct.getPrice());
            jsonObject.put("quantity",  updatedProduct.getQuantity());
            jsonObject.put("image", encodedImage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        dialogBuilder.loadingDialog("Updating Product...");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, product_url,jsonObject , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialogBuilder.dismissDialog();
                String status_code = "";
                String string_response = "";
                try {
                    status_code = response.getString("status_code");
                    string_response = response.getString("string_response");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status_code.equals("200")) {
                    //myDatabaseHelper.InsertProduct(p);
                    Log.d("PRODUCT UPDATED!", response.toString());
                    Toast.makeText(EditProduct.this, string_response, Toast.LENGTH_SHORT).show();
                    goBack();
                } else {
                    Log.d("PRODUCT NOT UPDATED!", response.toString());
                    Toast.makeText(EditProduct.this, string_response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogBuilder.dismissDialog();
                Log.d("EDIT PRODUCT ERROR!", error.toString());
                Toast.makeText(EditProduct.this, "PRODUCT PUT VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void goBack()
    {
        if(encodedImage==""){
            updatedProduct.setImage(oldProduct.getImage());
        }
       // else{updatedProduct.setImage(encodedImage);}
        Intent intent = new Intent(EditProduct.this,ProductDetail.class);

        intent.putExtra("Product", updatedProduct);
        startActivity(intent);
        finish();
    }
}
