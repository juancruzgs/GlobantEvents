package com.globant.eventscorelib.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

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

    public static Intent performCrop(Uri picUri) {
        // take care of exceptions
        // call the standard crop action intent (the user device may not
        // support it)
        Intent cropIntent = new Intent(CoreConstants.IMAGE_CROP);
        // indicate image type and Uri
        cropIntent.setDataAndType(picUri, CoreConstants.URI_NAME);
        // set crop properties
        cropIntent.putExtra(CoreConstants.EXTRA_CROP, CoreConstants.EXTRA_TRUE);
        // indicate aspect of desired crop
        cropIntent.putExtra(CoreConstants.EXTRA_ASPECTX, 720);
        cropIntent.putExtra(CoreConstants.EXTRA_ASPECTY, 360);
        // indicate output X and Y
        cropIntent.putExtra(CoreConstants.EXTRA_OUTPUTX, 720);
        cropIntent.putExtra(CoreConstants.EXTRA_OUTPUTY, 360);
        // retrieve data on return
        cropIntent.putExtra(CoreConstants.EXTRA_RETURN_DATA, true);
        // Fix a problem with the java.lang.SecurityException: Permission Denial: grantUriPermission
        cropIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (Build.VERSION.SDK_INT >= 19)
            cropIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        return cropIntent;
    }

    public static Bitmap cropRectangularImage(Bitmap subscriberPicture) {
        if (subscriberPicture.getWidth() >= subscriberPicture.getHeight()){
            return  Bitmap.createBitmap(
                    subscriberPicture,
                    subscriberPicture.getWidth()/2 - subscriberPicture.getHeight()/2,
                    0,
                    subscriberPicture.getHeight(),
                    subscriberPicture.getHeight()
            );

        }else{

            return Bitmap.createBitmap(
                    subscriberPicture,
                    0,
                    subscriberPicture.getHeight()/2 - subscriberPicture.getWidth()/2,
                    subscriberPicture.getWidth(),
                    subscriberPicture.getWidth()
            );
        }
    }

    public static int calculateInSampleSize(
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
