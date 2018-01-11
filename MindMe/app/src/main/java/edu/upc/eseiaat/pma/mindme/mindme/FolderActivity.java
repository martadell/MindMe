package edu.upc.eseiaat.pma.mindme.mindme;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FolderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ViewSwitcher simpleViewSwitcher;
    private BitmapDescriptor dot;
    private ArrayList<Picture> llista_fotos;
    private double lat;
    private double lon;
    private static final int REQUEST_LOCATION = 99;
    private static final int CAMERA_REQUEST = 10;
    private static final int MAX_BYTES = 8000;
    private PicturesAdapter adapter;
    private String pictureName;
    private String mCurrentPhotoPath;
    private static final String FILENAME = "picture_list.txt";
    private Uri pictureUri;
    LocationManager locationManager;
    Location location;
    File folder;

    private void writePictureList(){
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i = 0; i<llista_fotos.size(); i++){
                Picture it = llista_fotos.get(i);
                String line = String.format("%s;%f;%f\n",
                        it.getFoto(),
                        it.getLat(),
                        it.getLng());
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

    private void readPictureList(){
        llista_fotos = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[MAX_BYTES];
            int nread = fis.read(buffer);
            if (nread>0) {
                String content = new String(buffer, 0, nread);
                String[] lines = content.split("\n");
                for (String line : lines) {
                        String[] parts = line.split(";");
                        llista_fotos.add(new Picture(
                                parts[0],
                                Double.parseDouble(parts[1]/*.replace(',', '.')*/),
                                Double.parseDouble(parts[2]/*.replace(',', '.')*/)));
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

    @Override
    protected void onStop() {
        super.onStop();
        writePictureList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        readPictureList();

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        simpleViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        adapter = new PicturesAdapter(this, R.layout.activity_folder, llista_fotos);
        GridView gridview = (GridView) findViewById(R.id.pic_grid);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        gridview.setAdapter(adapter);

        mapFragment.getMapAsync(this);

        /*for (int i = 0; i<llista_fotos.size(); i++){
            addMarker(llista_fotos.get(i));
        }*/

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(FolderActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View item, int pos, long id) {
                maybeRemoveItem(pos);
                return false;
            }
        });
    }

    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.confirm_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                llista_fotos.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
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
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK  && requestCode == CAMERA_REQUEST) {

            Log.i("URI path", pictureUri.getPath());

            getLocation();
            addPicture();
            addMarker(llista_fotos.get(llista_fotos.size()-1));

            adapter.notifyDataSetChanged();
        }
    }

    private void addPicture() {
        llista_fotos.add(new Picture(mCurrentPhotoPath,lat,lon));
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
        folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/MindMe");
        //crea la carpeta si no existeix
        if (!folder.exists()) {
            Toast.makeText(this, "S'ha creat la carpeta", Toast.LENGTH_SHORT).show();
            folder.mkdir();
        }
        else {
            Toast.makeText(this, folder.toString() , Toast.LENGTH_SHORT).show();
        }
        return folder;
    }

    private void addMarker(Picture pic){
        LatLng cordinates = new LatLng(pic.getLat(),pic.getLng());
        dot = BitmapDescriptorFactory.fromResource(R.drawable.dot);

        mMap.addMarker(new MarkerOptions()
                .position(cordinates)
                .icon(dot)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLng(cordinates));
    }
}
