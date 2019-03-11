package com.example.ibra.oxp.activities.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.Login;
import com.example.ibra.oxp.activities.SignUp;
import com.example.ibra.oxp.activities.myAccount.ViewMyProducts;
import com.example.ibra.oxp.database.MyDatabaseHelper;
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
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddProduct extends Base {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.add_product_name) EditText name;
    @BindView(R.id.add_product_price) EditText price;
    @BindView(R.id.add_product_description) EditText description;
    @BindView(R.id.add_product_quantity) EditText quantity;
    @BindView(R.id.add_product_button) Button addProduct;
    @BindView(R.id.add_product_image_name) TextView image_name;
    @BindView(R.id.fab) FloatingActionButton floatingActionButton;
    @BindView(R.id.add_product_spinner) Spinner spinner;
    @BindView(R.id.toolbar_title) TextView toolbar_title;
    @BindView(R.id.add_product_image) ImageView image;
    private RequestQueue requestQueue;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    private MyDatabaseHelper myDatabaseHelper;
    private String mCurrentPhotoPath;
    private String encodedImage="";
    private List<String> itemsList;
    private ArrayAdapter<String> adapter;
    private String _category;

    private DialogBuilder dialogBuilder;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();
        dialogBuilder = new DialogBuilder(this);
        requestQueue = Volley.newRequestQueue(this);
        //myDatabaseHelper = new MyDatabaseHelper(this);
        //myDatabaseHelper.Image();
        itemsList = new ArrayList<>();
        requestCategory();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String item = (String) parent.getItemAtPosition(position);
                String item=itemsList.get(position);
                _category=item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick(R.id.add_product_button)
    public void btn_add_product() {
        String _name = name.getText().toString().trim();
        String _price = price.getText().toString().trim();
        String _description = description.getText().toString().trim();
        String _quantity = quantity.getText().toString().trim();

        if (!_name.isEmpty()) {
            //String email = getSharedPreferences("prefs", MODE_PRIVATE).getString("email", null);
            SharedPref sharedPref = new SharedPref(getApplicationContext());
            String _email = sharedPref.readValue("email", "defaultvaluerbeenread");
            Toast.makeText(AddProduct.this, _email, Toast.LENGTH_SHORT).show();
            final MyProduct p = new MyProduct(_name, _description, _price, _quantity, encodedImage,"category");
            //String URL = String.format("http://"+IP_PORT+"/oxp/product/?name=%1$s&price=%2$s&description=%3$s&quantity=%4$s&email=%5$s&", _name, _price, _description, _quantity,email);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", _email);
                jsonObject.put("name", _name);
                jsonObject.put("category", _category);
                jsonObject.put("description", _description);
                jsonObject.put("price", _price);
                jsonObject.put("quantity", _quantity);
                jsonObject.put("image", encodedImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
            dialogBuilder.loadingDialog("Adding Product...");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, product_url, jsonObject, new Response.Listener<JSONObject>() {
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
                        Log.d("PRODUCT ADDED!", response.toString());
                        Toast.makeText(AddProduct.this, string_response, Toast.LENGTH_SHORT).show();
                        goBack();
                    } else {
                        Log.d("PRODUCT NOT ADDED!", response.toString());
                        Toast.makeText(AddProduct.this, string_response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialogBuilder.dismissDialog();
                    Log.d("ADD PRODUCT ERROR!", error.toString());
                    Toast.makeText(AddProduct.this, "PRODUCT VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
            freed_the_fields();

        }

    }

    private void setCategoryData(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemsList);
        spinner.setAdapter(adapter);
    }

    private void freed_the_fields() {
        name.setText("");
        description.setText("");
        price.setText("");
        quantity.setText("");
        image_name.setText("Image Status");
        encodedImage="";
    }

    @OnClick(R.id.fab)
    protected void floating_btn_listener() {
        SelectImage();
    }


    private void SelectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProduct.this);
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
            image.setImageBitmap(bitmap);
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
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

    private void openGallery() {
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, SELECT_FILE);
    }


    private static void copyImageToNewPath(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
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
                            itemsList.add(jsonObject.getString("description"));
                        }
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
                    Toast.makeText(AddProduct.this,string_response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("OXP_TAG", error.toString());
                Toast.makeText(AddProduct.this,"CATEGORY VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void setImageNameView(int casee)
    {
        if(casee==0)
            image_name.setText("Image Captured");
        else if (casee==1)
            image_name.setText("Image Uploaded");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.search).setVisible(false);
        return true;
    }


    private void goBack()
    {
        Intent intent = new Intent(AddProduct.this,ViewMyProducts.class);
        startActivity(intent);
        finish();
    }
}
