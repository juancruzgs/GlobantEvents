package com.globant.eventscorelib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.TypedValue;

import com.squareup.picasso.Transformation;

public class CropCircleTransformation implements Transformation {
	
	private Context context;
	public CropCircleTransformation(Context context){
		this.context = context;
	}
	
	@Override
	public Bitmap transform(Bitmap source_a) {
		
		Resources r = context.getResources();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics());
		
		int size = Math.min(source_a.getHeight(), source_a.getWidth());
		float scale = (float)px/(float)size;
		int aWidth = (int) (source_a.getWidth()*scale);
		int aHeight = (int) (source_a.getHeight()*scale);
		
		Bitmap source = Bitmap.createScaledBitmap(source_a,aWidth, aHeight, false);
		source_a.recycle();
		
		Bitmap output = Bitmap.createBitmap(px, px, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffffffff;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, px, px);
		int difference = (size-px)/4;
		final Rect rect_center = new Rect(-difference, -difference, px, px);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(px / 2, px / 2, px / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(source, rect, rect, paint);

//		if (output != source) {
//			source.recycle();
//		}
        source.recycle();

		return output;
	}

	@Override
	public String key() {
		return "square()";
	}
}