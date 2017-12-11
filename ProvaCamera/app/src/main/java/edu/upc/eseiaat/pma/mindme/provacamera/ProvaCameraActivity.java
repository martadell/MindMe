package edu.upc.eseiaat.pma.mindme.provacamera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProvaCameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST =10;
    private ImageView imageView;

    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    TextView txt_lat, txt_long;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("lifecycle", "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putString("latitude", txt_lat.getText().toString());
        outState.putString("longitude", txt_long.getText().toString());}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova_camera);

        imageView = (ImageView) findViewById(R.id.imageView);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        txt_lat = (TextView) findViewById(R.id.txt_lat);
        txt_long = (TextView) findViewById(R.id.txt_long);

        if(savedInstanceState == null) {
            txt_lat.setText("Latitude:");
            txt_long.setText("Longitude");}

        else {
            Bundle state = savedInstanceState;
            txt_lat.setText(state.getString("latitude"));
            txt_long.setText(state.getString("longitude")); }
    }

    public void btn_camera(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,CAMERA_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(cameraImage);

                getLocation();
            }
        }
    }

    void getLocation() {

        //TODO: NO VA ??????
        if (ActivityCompat.checkSelfPermission(ProvaCameraActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission
                    (ProvaCameraActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions
                        (ProvaCameraActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_LOCATION);

            } } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                txt_lat.setText("Latitude: " + lat);
                txt_long.setText("Longitude: " + lon);
            } else {
                txt_lat.setText("Unable to find correct location.");
                txt_long.setText("Unable to find correct location. ");
            }
        }

    }
}


