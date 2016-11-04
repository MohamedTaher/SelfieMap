package com.example.taher.selfiemap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.session.MediaSession;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.taher.selfiemap.Business.Constants;
import com.example.taher.selfiemap.Business.MapManager;
import com.example.taher.selfiemap.Business.PictureManager;
import com.example.taher.selfiemap.Models.Picture;
import com.example.taher.selfiemap.UI.Activity.ImagesActivity;
import com.example.taher.selfiemap.UI.Activity.MapsActivity;
import com.example.taher.selfiemap.Helper.Check;
import com.example.taher.selfiemap.UI.IconsBar.IconsBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuickAccess extends AppCompatActivity {

    @BindView(R.id.map)
    LinearLayout map;

    @BindView(R.id.camera)
    LinearLayout camera;

    @BindView(R.id.images)
    LinearLayout images;

    @BindView(R.id.search)
    LinearLayout search;

    @BindView(R.id.favorite)
    LinearLayout favorite;

    @BindView(R.id.about)
    LinearLayout about;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_access);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IconsBar.map(QuickAccess.this);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = IconsBar.camera(QuickAccess.this);
            }
        });

        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IconsBar.images(QuickAccess.this);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IconsBar.search(QuickAccess.this);
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IconsBar.favorite(QuickAccess.this);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog myQuittingDialogBox =new AlertDialog.Builder(QuickAccess.this)
                        //set message, title, and icon
                        .setTitle("About")
                        .setMessage("This app is Images gallery related by geography" +
                                ", it shows each image that you capture it on google map " +
                                "and you can display each image by click on it, also you " +
                                "can choose any image to add to your favorite list")
                        .setIcon(R.drawable.black_info)

                        .setNegativeButton("close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();

                AlertDialog diaBox = myQuittingDialogBox;
                diaBox.show();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.v("Image Path", imageUri.toString());
                    PictureManager.addPicture(new Picture(imageUri.toString(), QuickAccess.this));
                }
        }
    }

}
