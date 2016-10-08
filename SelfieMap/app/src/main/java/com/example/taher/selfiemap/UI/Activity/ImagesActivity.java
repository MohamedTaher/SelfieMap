package com.example.taher.selfiemap.UI.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.taher.selfiemap.R;
import com.example.taher.selfiemap.UI.Adapters.ImageAdapter;
import com.example.taher.selfiemap.UI.IconsBar.IIconsBar;
import com.example.taher.selfiemap.UI.IconsBar.IconsBar;

import java.util.ArrayList;

public class ImagesActivity extends AppCompatActivity implements IIconsBar {

    private ViewPager viewPager;
    private Uri imageUri;
    private ImageAdapter adapter;
    private boolean fav = false;

    private ArrayList<Picture> getPictures(){
        if (!fav)
            return PictureManager.getPictures();

        return PictureManager.getFavPic();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        Intent intent = getIntent();
        fav = intent.getBooleanExtra(getString(R.string.favoriteShow), false);

        viewPager = (ViewPager) findViewById(R.id.pager);
        //viewPager.setOffscreenPageLimit(1);
        Intent i = getIntent();
        int position = i.getIntExtra(getString(R.string.imgPosition), 0);

        adapter = new ImageAdapter(this, getPictures());

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);

        setTitle(getString(R.string.images));
        selectedIcon();
    }

    @Override
    public void selectedIcon() {
        int id;
        if (fav){
            ImageView imageView = (ImageView) findViewById(R.id.imgFavorite);
            imageView.setColorFilter(Color.argb(255, 255, 255, 255));

            TextView textView = (TextView) findViewById(R.id.txtFavorite);
            textView.setVisibility(View.VISIBLE);
        } else {
            ImageView imageView = (ImageView) findViewById(R.id.imgImages);
            imageView.setColorFilter(Color.argb(255, 255, 255, 255));

            TextView textView = (TextView) findViewById(R.id.txtImages);
            textView.setVisibility(View.VISIBLE);
        }
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
        if (fav) {
            IconsBar.images(this);
            finish();
        }
    }

    @Override
    public void search(View view) {
        IconsBar.search(this);
        finish();
    }

    @Override
    public void favorite(View view) {
        IconsBar.favorite(ImagesActivity.this);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.v("Image Path", imageUri.toString());
                    PictureManager.addPicture(new Picture(imageUri.toString(), ImagesActivity.this));
                    refresh();

                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.images_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (getPictures().size() == 0)return true;
        int position = viewPager.getCurrentItem();

        if (id == R.id.deletePic) {

            final int pos = position;
            AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                    //set message, title, and icon
                    .setTitle("Delete")
                    .setMessage("Do you want to Delete")
                    .setIcon(R.drawable.black_trash)

                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //deleting code
                            PictureManager.deletePicture(pos);
                            refresh();

                            dialog.dismiss();
                        }

                    })

                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    })
                    .create();

            AlertDialog diaBox = myQuittingDialogBox;
            diaBox.show();

            return true;
        }

        if (id == R.id.mapPic){
            Picture temp = getPictures().get(position);
            IconsBar.map(this, temp.getLatitude(), temp.getLongitude());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh(){
        adapter = new ImageAdapter(this, getPictures());
        viewPager.setAdapter(adapter);
    }



}
