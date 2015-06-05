package com.globant.eventscorelib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;

public class ConvertImage {

    public static byte[] convertBitmapToByteArrayAndCompress(Bitmap image) {
        if (image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //TODO Change the constant. Determine the right quality value
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        } else {
            return null;
        }
    }

    public static Bitmap convertByteArrayToBitmap(byte[] image, int reqWidth, int reqHeight) {
        //TODO Do this operation with an AsyncTask. Look at this: http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(image, 0, image.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
