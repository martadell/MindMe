package edu.upc.eseiaat.pma.mindme.mindme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FolderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ViewSwitcher simpleViewSwitcher;
    private BitmapDescriptor dot;
    private ArrayList<Picture> llista_fotos;
    private Bitmap bitmap;
    private double lat;
    private double lon;
    private PicturesAdapter adapter;
    private String pictureName;

    private static final int CAMERA_REQUEST =10;
    private Uri pictureUri;

    static final int REQUEST_LOCATION = 99;
    LocationManager locationManager;
    Location location;

    private static final String FILENAME = "picture_list.txt";
    private static final int MAX_BYTES = 8000;
    File folder;

    private void writePictureList(){
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i = 0; i<llista_fotos.size(); i++){
                Picture it = llista_fotos.get(i);
                String line = String.format("%f;%f\n", it.getLat(), it.getLng());
                fos.write(line.getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("berta", "writeItemList: FileNotFoundException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("berta", "writeItemList: IEOException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        }
    }

    /*public void loadImageFromStorage(String path) {
        path = folder.getPath();
        Bitmap b;
        try {
            for (int i = 0; i<folder.length(); i++){
                File f = new File(path, "hola.jpg");
                b = BitmapFactory.decodeStream(new FileInputStream(f));
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }*/

    private void readPictureList(){
        llista_fotos = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[MAX_BYTES];
            int nread = fis.read(buffer);
            if (nread>0) {
                String content = new String(buffer, 0, nread);
                String[] lines = content.split("\n");
                for (int i = 0; i<folder.length(); i++) {
                    File f = new File(folder.getPath(), "hola.jpg");
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                    for (String line : lines) {
                        String[] parts = line.split(";");
                        llista_fotos.add(new Picture(
                                b,
                                Double.parseDouble(parts[0]),
                                Double.parseDouble(parts[1])));
                    }
                }
            }
            fis.close();
        } catch (FileNotFoundException e) {
            Log.i("berta", "readItemList: FileNotFoundException");
        } catch (IOException e) {
            Log.i("berta", "readItemList: IOEException");
            Toast.makeText(this, R.string.cannot_read, Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("lifecycle", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putString("latitude", String.valueOf(lat));
        outState.putString("longitude", String.valueOf(lon));
        outState.putString("pictureUri", pictureUri.toString());
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        writePictureList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        simpleViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        //llista_fotos = new ArrayList<>();

        /*if(savedInstanceState != null) {
            Bundle state = savedInstanceState;
            lat = Double.parseDouble(state.getString("latitude"));
            lon = Double.parseDouble(state.getString("longitude"));
            pictureUri = Uri.parse(state.getString("pictureUri"));
            getBitmap();
        }*/

        adapter = new PicturesAdapter(this, R.layout.activity_folder, llista_fotos);
        GridView gridview = (GridView) findViewById(R.id.pic_grid);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        readPictureList();

        gridview.setAdapter(adapter);

        mapFragment.getMapAsync(this);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(FolderActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.btn_switch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_switch:
                simpleViewSwitcher.showNext();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(FolderActivity.this, marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void btn_camera(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File pictureDirectory = getPictureDirectory();
        pictureName = getPictureName();

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
            addPicture();
            addMarker();

            adapter.notifyDataSetChanged();
        }
    }

    private void addPicture() {
        llista_fotos.add(new Picture(bitmap,lat,lon));
    }

    private void getBitmap() {
        bitmap = null;

        try {
            bitmap = getThumbnail(pictureUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(FolderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission
                    (FolderActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions
                        (FolderActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_LOCATION);

            } } else {

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                lat = location.getLatitude();
                lon = location.getLongitude();
            } else {
                Toast.makeText(this, "Unable to find location", Toast.LENGTH_SHORT).show();
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
        folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MindMe");
        //crea la carpeta si no existeix
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }

    private void addMarker(){
        LatLng cordinates = new LatLng(lat,lon);
        dot = BitmapDescriptorFactory.fromResource(R.drawable.dot);

        mMap.addMarker(new MarkerOptions()
                .position(cordinates)
                .icon(dot)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLng(cordinates));
    }
}
