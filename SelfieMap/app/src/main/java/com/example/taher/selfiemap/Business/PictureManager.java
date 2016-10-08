package com.example.taher.selfiemap.Business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.taher.selfiemap.Models.Picture;
import com.example.taher.selfiemap.R;
import com.example.taher.selfiemap.Helper.Date;
import com.example.taher.selfiemap.Storage.Transactions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by taher on 12/09/16.
 */
public class PictureManager {

    private static final int IMAGE_MAX_SIZE = 2500;
    private static ArrayList<Picture> pictures = new ArrayList<Picture>();

    //get picture from database
    public static void setPictures() {
        pictures.addAll(Transactions.getPictures());
    }

    public static ArrayList<Picture> getPictures() {
        return pictures;
    }

    public static ArrayList<Picture> getFavPic(){
        ArrayList<Picture> fav = new ArrayList<Picture>();

        for(Picture picture:pictures){
            if (picture.getFavorite() == 1)
                fav.add(picture);
        }

        return fav;
    }

    public static void addPicture(Picture picture){
        //add to DB
        Transactions.insert(picture);

        //add to memory
        pictures.add(picture);
        //Log.v("Current Pictures", pictures.get(0).getPath() + " " +pictures.get(0).getLatitude() + " " + pictures.get(0).getLongitude());
    }

    public static void deletePicture (int position){
        String path = PictureManager.getPictures().get(position).getPath();

        //delete from dp
        Transactions.clearPic(path);

        //delete from folder
        File pic = new File(Uri.parse(path).getPath());
        if (pic.exists())
            pic.delete();

        //delete from memory
        pictures.remove(position);
    }

    public static Uri takePicture(Activity activity, int PhotoTag){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory() + "/" + Constants.dirName
                , Date.getDate() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        activity.startActivityForResult(intent, PhotoTag);

        return Uri.fromFile(photo);
    }

    public static Bitmap getImageToMap(Context context, Uri uri){
        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageURI(null);
        markerImageView.setImageURI(uri);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable draw = customMarkerView.getBackground();
        if (draw != null)
            draw.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public static void favorite (int index){
        //update in memory
        Picture picture = pictures.get(index);
        Transactions.favorite(picture);
    }

    public static Bitmap getImageToAdapter(Uri uri) throws IOException {
        File file = new File(uri.getPath());
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(file);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(file);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();

        Matrix matrix = new Matrix();
        matrix.postRotate(getOrientation(uri.getPath()));
        Bitmap rotatedBitmap = Bitmap.createBitmap(b , 0, 0, b .getWidth(), b .getHeight(), matrix, true);

        return rotatedBitmap;
    }

    private static int getOrientation (String path) throws IOException {
        ExifInterface ei = new ExifInterface(path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                return 0;
        }
    }

}
