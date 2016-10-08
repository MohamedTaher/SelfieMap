package com.example.taher.selfiemap.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taher.selfiemap.Business.Constants;
import com.example.taher.selfiemap.Business.PictureManager;
import com.example.taher.selfiemap.Models.Picture;
import com.example.taher.selfiemap.R;
import com.example.taher.selfiemap.UI.IconsBar.IIconsBar;
import com.example.taher.selfiemap.UI.IconsBar.IconsBar;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements IIconsBar, SearchView.OnQueryTextListener {

    private Uri imageUri;
    private List<Address> addressList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        selectedIcon();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String location) {
        // User pressed the search button
        if(location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 10);


            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<String> addressesStr = new ArrayList<String>();
            for (Address address : addressList){
                if (address.getAdminArea() != null)
                    addressesStr.add(address.getAdminArea());
            }

            ListView list = (ListView) findViewById(R.id.searchResult);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Address temp = addressList.get(position);

                    IconsBar.map(SearchActivity.this, temp.getLatitude(), temp.getLongitude());
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,addressesStr);
            list.setAdapter(adapter);

            return true;
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }

    @Override
    public void selectedIcon() {
        ImageView imageView = (ImageView) findViewById(R.id.imgSearch);
        imageView.setColorFilter(Color.argb(255, 255, 255, 255));

        TextView textView = (TextView) findViewById(R.id.txtSearch);
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void camera(View view) {
        imageUri = IconsBar.camera(this);
    }

    @Override
    public void map(View view) {
        IconsBar.map(this);
        finish();
    }


    @Override
    public void album(View view) {
        IconsBar.images(this);
        finish();
    }

    @Override
    public void search(View view) {
        //no action
    }

    @Override
    public void favorite(View view) {
        IconsBar.favorite(SearchActivity.this);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.v("Image Path", imageUri.toString());
                    PictureManager.addPicture(new Picture(imageUri.toString(), SearchActivity.this));
                }
        }
    }


}
