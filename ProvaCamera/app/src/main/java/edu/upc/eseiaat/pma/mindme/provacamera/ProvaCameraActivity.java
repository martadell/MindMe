package edu.upc.eseiaat.pma.mindme.provacamera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProvaCameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST =10;
    private ImageView imageView;

    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    TextView txt_lat, txt_long;


    private void writeLastLocation() {
        try {
            FileOutputStream fos = openFileOutput("Location.txt", Context.MODE_PRIVATE);
            String la = txt_lat.getText().toString();
            String lo = txt_long.getText().toString();

            String line = String.format("%s;%s", la, lo);
            fos.write(line.getBytes());

            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("MindMe", "writeLastLocation: FileNotFoundException");
            Toast.makeText(this, "Unable to create a save file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("MindMe", "writeLastLocation: IOException");
            Toast.makeText(this, "Unable to create a save file", Toast.LENGTH_SHORT).show();
        }
    }

    private void readLastLocation() {
        try {
            FileInputStream fis = openFileInput("Location.txt");
            byte[] buffer = new byte[1000];
            int nread = fis.read(buffer);
            if (nread > 0) {
                String content = new String(buffer, 0, nread);
                String[] location = content.split(";");

                txt_lat.setText(location[0]);
                txt_long.setText(location[1]);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            Log.i("MindMe", "FileNotFoundException");
        } catch (IOException e) {
            Log.e("MindMe", "readLastLocation: IOException");
            Toast.makeText(this, "Unable to read the saved file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeLastLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova_camera);

        imageView = (ImageView) findViewById(R.id.imageView);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        txt_lat = (TextView) findViewById(R.id.txt_lat);
        txt_long = (TextView) findViewById(R.id.txt_long);

        readLastLocation();

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


