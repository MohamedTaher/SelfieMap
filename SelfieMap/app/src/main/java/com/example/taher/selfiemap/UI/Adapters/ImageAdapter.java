package com.example.taher.selfiemap.UI.Adapters;

/**
 * Created by taher on 17/09/16.
 */

import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.taher.selfiemap.Business.PictureManager;
import com.example.taher.selfiemap.Helper.Check;
import com.example.taher.selfiemap.Models.Picture;
import com.example.taher.selfiemap.R;

public class ImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<Picture> pictures;
    private LayoutInflater inflater;

    // constructor
    public ImageAdapter(Activity activity,
                        ArrayList<Picture> pictures) {
        this._activity = activity;
        this.pictures = pictures;
    }

    @Override
    public int getCount() {
        return this.pictures.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imgDisplay;
        CheckBox box;
        Picture temp = pictures.get(position);
        final int pos = position;

        inflater = (LayoutInflater) _activity.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        try {
            imgDisplay.setImageBitmap(PictureManager.getImageToAdapter(Uri.parse(pictures.get(position).getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        imgDisplay.setImageURI(null);
        imgDisplay.setImageURI(Uri.parse(pictures.get(position).getPath()));*/
        //imgDisplay.setRotation(-90);



        box = (CheckBox) viewLayout.findViewById(R.id.star);
        if (temp.getFavorite() == 1)
            box.setChecked(true);

        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureManager.favorite(pos);
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

    private static class holder {
        ImageView imgDisplay;
        CheckBox box;
    }


}