package com.riotapps.word.utils;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class ImageHelper {
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels, int borderPixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        //pixels = 8;
        
        if (borderPixels < 0){borderPixels = 0;}
        
        if (borderPixels > 11110){
	        final int colorBorder = Color.BLACK;
	        final Paint paintBorder = new Paint();
	        final Rect rectBorder = new Rect(0, 0, bitmap.getWidth() + borderPixels, bitmap.getHeight() + borderPixels);
	        final RectF rectFBorder = new RectF(rectBorder);
	        final float roundPxBorder = pixels;
	
	        paintBorder.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paintBorder.setColor(colorBorder);
	        canvas.drawRoundRect(rectFBorder, roundPxBorder, roundPxBorder, paintBorder);
	       // return output;	        
	      //  paintBorder.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	      //  canvas.drawPaint(paintBorder); //(bitmap, rectBorder, rectBorder,paintBorder);
        }

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }
}
