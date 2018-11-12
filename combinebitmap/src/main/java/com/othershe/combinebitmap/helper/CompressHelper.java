package com.othershe.combinebitmap.helper;


import android.content.SyncAdapterType;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.FileDescriptor;

public class CompressHelper {

    public CompressHelper() {
    }

    public static CompressHelper getInstance() {
        return CompressHelper.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final CompressHelper instance = new CompressHelper();
    }

    /*
     * 压缩资源（采样率）
     */
    public Bitmap compressResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /*
     *
     */
    public Bitmap compressDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    /*
     * 按比例进行矩阵缩放
     */
    public Bitmap compressResource(Bitmap bitmap, int reqWidth, int reqHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
         // 计算缩放比例
        float scaleWidth = ((float) reqWidth) / width;
        float scaleHeight = ((float) reqHeight) / height;

         // 一个矩阵就可以缩放了
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /*
     * 计算采样率
     */
    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

             // 实际 宽/采样率，高/采样率 都大于所需要的尺寸
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
