package com.example.jana.multipleimagescapture;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView imagecapture;
    TextView textimages;
    ImageView imgcancel;
    private String selectedImagePath = "";

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private String filepath = null;

    private String imgPath;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public ArrayList<String> map = new ArrayList<String>();
    final private int CAPTURE_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imagecapture = (ImageView) findViewById(R.id.imagecapture);
        textimages = (TextView) findViewById(R.id.textimages);
        imgcancel = (ImageView) findViewById(R.id.imgcancel);

        imagecapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();

            }
        });

        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                textimages.setText("Total Images : 0");
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            selectImage();
        }
    }

    private void selectImage() {




        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        } else {
            File file = new File(setImageUri().getPath());
            Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE);
        }



    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "IMG_" + new Date().getTime() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    public String getImagePath() {
        return imgPath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {

            selectedImagePath = getImagePath();

            filepath = selectedImagePath;


            map.add(filepath);

            Log.e("testing","map = "+map);
            Log.e("testing","map size = "+map.size());

            textimages.setText(" Total Images : "+map.size());


            //filepath = fileUri.getPath();


        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_CANCELED) {

            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();

        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
      /*  if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK)
            onCaptureImageResult(data);*/


        //formupload.setText(filepath);
        Log.e("testing", "filepath = " + filepath);


    }

}