package edu.upc.eseiaat.pma.mindme.provacamera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProvaCameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST =10;
    private ImageView imageView;
    private Uri pictureUri;

    static final int REQUEST_LOCATION = 99;
    LocationManager locationManager;
    TextView txt_lat, txt_long;
    Location location;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("lifecycle", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putString("latitude", txt_lat.getText().toString());
        outState.putString("longitude", txt_long.getText().toString());
        outState.putString("pictureUri", pictureUri.toString());
    }

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
            txt_long.setText("Longitude");
        }
        else {
            Bundle state = savedInstanceState;
            txt_lat.setText(state.getString("latitude"));
            txt_long.setText(state.getString("longitude"));
            pictureUri = Uri.parse(state.getString("pictureUri"));
            getBitmap();
        }
    }


    public void btn_camera(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File pictureDirectory = getPictureDirectory();
        String pictureName = getPictureName();

        File imageFile = new File(pictureDirectory, pictureName);
        pictureUri = Uri.fromFile(imageFile);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(cameraIntent,CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK  && requestCode == CAMERA_REQUEST) {

            Log.i("URI path", pictureUri.getPath());

            getBitmap();
            getLocation();

        }
    }

    //Mètode per obtenir el bitmal (crida als mètodes següents)
    private void getBitmap() {
        Bitmap bitmap = null;
        try {
            bitmap = getThumbnail(pictureUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
    }

    //Construir pel bitmap a partir d'un URI
    public Bitmap getThumbnail(Uri uri) throws IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 1000) ? (originalSize / 1000) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    //Més coses pel bitmap
    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    //Obtenir la ubicació actual (longitud i latitud)
    private void getLocation() {

        //TODO: NO VA ??????
        if (ActivityCompat.checkSelfPermission(ProvaCameraActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission
                    (ProvaCameraActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions
                        (ProvaCameraActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_LOCATION);

            } } else {

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

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

    //Obtenir el nom de l'arxiu al qual es guardarà la imatge
    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "MindMe_" + timestamp + ".jpg";
    }

    //Assignar la ubicaciíó on es guardarà la imatge
    private File getPictureDirectory() {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MindMe");
       //crea la carpeta si no existeix
        if (!folder.exists()) {
            folder.mkdir(); }

        return folder;
    }
}


