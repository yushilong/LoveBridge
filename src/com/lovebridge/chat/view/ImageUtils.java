package com.lovebridge.chat.view;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import com.lovebridge.application.MainApplication;
import com.lovebridge.chat.utils.IOUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils
{
    abstract class CorrectlyOrientedImageScaleCalculator implements ImageScaleCalculator
    {
        public CorrectlyOrientedImageScaleCalculator()
        {
            super();
        }

        CorrectlyOrientedImageScaleCalculator(CorrectlyOrientedImageScaleCalculator arg1)
        {
            super();
        }

        public float calculateScale(int angle, int width, int height)
        {
            int i1;
            int i;
            if (angle == 0x5A || angle == 0x10E)
            {
                i = height;
                i1 = width;
            }
            else
            {
                i = width;
                i1 = height;
            }
            return this.calculateScale(i, i1);
        }

        protected abstract float calculateScale(int arg1, int arg2);
    }

    interface ImageScaleCalculator
    {
        float calculateScale(int arg1, int arg2, int arg3);
    }

    public ImageUtils()
    {
        super();
    }

    public static Bitmap decodeBitmapFromUri(Context context, Uri uri, BitmapFactory.Options options)
    {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try
        {
            inputStream = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        }
        catch (Throwable throwable)
        {
            IOUtils.closeQuietly(inputStream);
        }
        IOUtils.closeQuietly(inputStream);
        return bitmap;
    }

    public static int dpToPx(double dp)
    {
        return ((int) Math.round(MainApplication.getInstance().getResources().getDisplayMetrics().density * dp));
    }

    public static Bitmap getOrientedImageResizedToFill(Uri imageUri, final int maxWidth, final int maxHeight,
            boolean scaleUp) throws IOException
    {
        return ImageUtils.getOrientedImageResized(imageUri, scaleUp,
                new ImageUtils().new CorrectlyOrientedImageScaleCalculator()
                {
                    protected float calculateScale(int rotatedWidth, int rotatedHeight)
                    {
                        return Math.max(maxWidth / rotatedWidth, maxHeight / rotatedHeight);
                    }
                });
    }

    public static Bitmap getOrientedImageResizedToFit(Uri imageUri, final int maxWidth, final int maxHeight)
            throws IOException
    {
        return ImageUtils.getOrientedImageResized(imageUri, false,
                new ImageUtils().new CorrectlyOrientedImageScaleCalculator()
                {
                    protected float calculateScale(int rotatedWidth, int rotatedHeight)
                    {
                        return Math.min(maxWidth / rotatedWidth, maxHeight / rotatedHeight);
                    }
                });
    }

    public static Bitmap getOrientedImageResizedToMaxPixels(Uri photoUri, final int maxPixels) throws IOException
    {
        return ImageUtils.getOrientedImageResized(photoUri, false, new ImageScaleCalculator()
        {
            public float calculateScale(int angle, int width, int height)
            {
                float f = 2048f;
                float f1 = ((float) Math.sqrt(maxPixels / (width * height)));
                int i = Math.max(width, height);
                if (i * f1 >= f)
                {
                    f1 = f / i;
                }
                return f1;
            }
        });
    }

    public static String getRealPathFromURI(Context context, Uri uri)
    {
        String string = uri.getPath();
        Cursor cursor = context.getContentResolver().query(uri, new String[] { "_data" }, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            int i = cursor.getColumnIndex("_data");
            if (i >= 0)
            {
                string = cursor.getString(i);
            }
            cursor.close();
        }
        return string;
    }

    private static Bitmap getOrientedImageResized(Uri imageUri, boolean scaleUp, ImageScaleCalculator scaleCalculator)
            throws IOException
    {
        Bitmap bitmap1;
        int i = 0;
        String string = ImageUtils.getRealPathFromURI(MainApplication.getInstance(), imageUri);
        if (string != null)
        {
            switch (new ExifInterface(string).getAttributeInt("Orientation", 1))
            {
                case 3:
                {
                    i = 0xB4;
                }
                case 6:
                {
                    i = 0x5A;
                }
                case 8:
                {
                    i = 0x10E;
                }
            }
        }
        BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
        bitmapFactory.inJustDecodeBounds = true;
        ImageUtils.decodeBitmapFromUri(MainApplication.getInstance(), imageUri, bitmapFactory);
        float f = scaleCalculator.calculateScale(i, bitmapFactory.outWidth, bitmapFactory.outHeight);
        bitmapFactory.inSampleSize = ((int) (1f / f));
        bitmapFactory.inPurgeable = true;
        bitmapFactory.inInputShareable = true;
        Bitmap bitmap = ImageUtils.decodeBitmapFromUri(MainApplication.getInstance(), imageUri, bitmapFactory);
        if (bitmap == null)
        {
            bitmap1 = null;
        }
        else
        {
            Matrix matrix = new Matrix();
            float f1 = scaleCalculator.calculateScale(i, bitmapFactory.outWidth, bitmapFactory.outHeight);
            if (f1 < 1f || (scaleUp))
            {
                matrix.setScale(f1, f1);
            }
            if (i > 0)
            {
                matrix.postRotate(((float) i));
            }
            Bitmap bitmap2 = bitmap;
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap2 != bitmap)
            {
                bitmap2.recycle();
            }
            bitmap1 = bitmap;
        }
        return bitmap1;
    }
}
