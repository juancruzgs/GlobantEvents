package com.globant.eventscorelib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

public class ConvertImage {

    public static Bitmap convertByteToBitmap(byte[] image) {
        Bitmap bitmap = null;
        if(image != null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }
        return bitmap;
    }

    public static Uri getImageUri(Context context, Bitmap image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "Title", null);
        return Uri.parse(path);
    }
    public static byte[] convertBitmapImageToByteArray(Bitmap Photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static byte[] convertDrawableToByteArray(Drawable drawable) {
        if (drawable!=null){
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            return convertBitmapImageToByteArray(bitmap);
         } else{
            return null;
        }
    }
}
