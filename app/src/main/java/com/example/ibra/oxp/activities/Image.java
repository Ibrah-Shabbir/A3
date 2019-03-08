package com.example.ibra.oxp.activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Image extends Base {

    @BindView(R.id.product_image)
    ImageView product_image;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    MyDatabaseHelper myDatabaseHelper;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        myDatabaseHelper=new MyDatabaseHelper(this);
        myDatabaseHelper.Image();


    }

    @OnClick(R.id.fab)
    protected void floating_btn_listener() {
        SelectImage();
    }

    private void SelectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Image.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Camera"))
                {
                    dispatchTakePictureIntent();
                }
                else if (items[which].equals("Gallery"))
                {
                    Toast.makeText(Image.this, "GALLERY!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);
                }
                else if (items[which].equals("Cancel"))
                { dialog.dismiss(); }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA)
        {
            if(resultCode==RESULT_OK)
            {
                File file = new File(mCurrentPhotoPath);
                Bitmap bitmap = null;
                try
                {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(file));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    final String image_string=Base64.encodeToString(byteArray,Base64.DEFAULT);
                }
                catch (IOException e) { e.printStackTrace(); }
                if(bitmap!=null)
                {
                    product_image.setImageBitmap(bitmap);
                    product_image.setColorFilter(0);
                    product_image.setPadding(0, 0, 0, 0);
                }

            }
            else if (resultCode == RESULT_CANCELED) { finish(); }

        }
        else if(requestCode==SELECT_FILE)
        {
            if(resultCode==RESULT_OK)
            {

            }
            else if (resultCode == RESULT_CANCELED) { finish(); }
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            } catch (IOException ex) { }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.mapandgallerysample",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }
}







