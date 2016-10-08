package com.example.taher.selfiemap.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taher.selfiemap.Business.Constants;
import com.example.taher.selfiemap.Business.PictureManager;
import com.example.taher.selfiemap.Models.Picture;
import com.example.taher.selfiemap.Service.GPSTracker;
import com.example.taher.selfiemap.R;
import com.example.taher.selfiemap.UI.IconsBar.IIconsBar;
import com.example.taher.selfiemap.UI.IconsBar.IconsBar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, IIconsBar {

    private GoogleMap mMap;
    private ArrayList<MarkerOptions> markers;
    private Uri imageUri;
    private double lat, lng;
    private boolean  otherLoc = false;

    private void addImage(Picture picture){
        Bitmap image = PictureManager.getImageToMap(getApplicationContext(), Uri.parse(picture.getPath()));
        if (image == null){
            //user deleted this picture so i will delete it from dp
            return;
        }
        LatLng loc = new LatLng(picture.getLatitude(), picture.getLongitude());

        MarkerOptions marker = new MarkerOptions().position(loc)
                .icon(BitmapDescriptorFactory.fromBitmap(image));

        markers.add(marker);

        mMap.addMarker(marker);
    }

    private void addImages(){
        ArrayList<Picture> pictures = PictureManager.getPictures();

        for (Picture picture :pictures){
            addImage(picture);
        }
    }

    private void currentLocation (){
        GPSTracker gps = new GPSTracker(this);
        double latitude, longitude;
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            LatLng curLoc = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(curLoc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

            addImages();

        }else{
            gps.showSettingsAlert();
        }
        gps.stopUsingGPS();
    }

    private void setLocation(){
        LatLng curLoc = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        addImages();
    }

    public void currentLocation (View view) {
        currentLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra(getString(R.string.lat),0);
        lng = intent.getDoubleExtra(getString(R.string.lng),0);
        if (lat != 0 && lng != 0)otherLoc = true;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markers = new ArrayList<MarkerOptions>();

        selectedIcon();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (otherLoc)
            setLocation();
        else
            currentLocation();

        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        int pos = -1;

        for (int i = 0; i < markers.size(); i++){
            if (marker.getPosition().equals(markers.get(i).getPosition())){
                pos = i;
                break;
            }
        }

        if (pos == -1)
            return false;

        Intent intent= new Intent(getApplicationContext(),ImagesActivity.class);
        intent.putExtra(getString(R.string.imgPosition),pos);

        startActivity(intent);
        return true;
    }

    @Override
    public void selectedIcon() {
        ImageView imageView = (ImageView) findViewById(R.id.imgMap);
        imageView.setColorFilter(Color.argb(255, 255, 255, 255));

        TextView textView = (TextView) findViewById(R.id.txtMap);
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void camera(View view) {
        imageUri = IconsBar.camera(this);
    }

    @Override
    public void map(View view) {
        // nothing to do
    }

    @Override
    public void album(View view) {
        IconsBar.images(this);
        finish();
    }

    @Override
    public void search(View view) {
        IconsBar.search(this);
        finish();
    }

    @Override
    public void favorite(View view) {
        IconsBar.favorite(MapsActivity.this);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.v("Image Path", imageUri.toString());

                    Picture picture = new Picture(imageUri.toString(), MapsActivity.this);
                    PictureManager.addPicture(picture);
                    addImage(picture);
                }
        }
    }

}
