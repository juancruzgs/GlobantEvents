package com.globant.eventmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

/*
    QRCodeEncoder qrCodeEncoder = new QRCodeEncoder("nXL6KSa0KH", getActivity());
    Bitmap bitmap = null;
    try {
        bitmap = qrCodeEncoder.encodeAsBitmap();
        ImageView myImage = (ImageView) rootView.findViewById(R.id.imageView);
        myImage.setImageBitmap(bitmap);
    } catch (WriterException e) {
        e.printStackTrace();
    }
*/

public final class QRCodeEncoder {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private static final String TITLE = "Text";

    private int dimension = Integer.MIN_VALUE;
    private String contents = null;
    private BarcodeFormat format = null;
    private boolean encoded = false;

    public QRCodeEncoder(String data, Context context) {
        getDimension(context);
        encoded = encodeContents(data);
    }

    private void getDimension(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        dimension = width < height ? width : height;
        dimension = dimension * 3/4;
    }

    public String getTitle() {
        return TITLE;
    }

    private boolean encodeContents(String data) {
        this.format = BarcodeFormat.QR_CODE;
        if (data != null && data.length() > 0) {
            contents = data;
        }
        return contents != null && contents.length() > 0;
    }

    public Bitmap encodeAsBitmap() throws WriterException {
        if (!encoded) return null;

        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(contents, format, dimension, dimension, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) { return "UTF-8"; }
        }
        return null;
    }

}