package library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * 图片的相关处理类
 */
public class PhotoUtil
{
    private static HashMap<String, Bitmap> imageCache = new HashMap<String, Bitmap>();

    public static void put(String key, Bitmap bmp)
    {
        imageCache.put(key, bmp);
    }

    public static Bitmap get(String key)
    {
        return imageCache.get(key);
    }

    /**
     * @param picturePath
     * @return Bitmap
     * @notice 由路径获取Bitmap
     */
    public static Bitmap getBitmapFromFile(String picturePath)
    {
        return BitmapFactory.decodeFile(picturePath);
    }

    /**
     * @param view
     * @return Bitmap
     * @notice 从view中获取bitmap
     */
    public static Bitmap getBitmapFromView(View view)
    {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    /**
     * @param image
     * @return 压缩后的bitmap
     * @notice 图片按质量压缩方法，根据Bitmap图片压缩
     */
    public static Bitmap getCompressImageAs(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > 50)
        {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * @param image
     * @return Bitmap
     * @notice 图片按比例大小压缩方法，根据Bitmap图片压缩
     */
    public static Bitmap getCompressImage(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024)
        {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;
        float ww = 480f;
        int be = 1;
        if (w > h && w > ww)
        {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh)
        {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
        {
            be = 1;
        }
        newOpts.inSampleSize = be;
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        // 按比例大小压缩后再进行质量压缩
        return getCompressImageAs(bitmap);
    }

    /**
     * @param bitmap
     * @return Bitmap
     * @notice 获取圆形图片
     */
    public static Bitmap getCircleImage(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height)
        {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else
        {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * @param bitmap
     * @return Bitmap
     * @notice 获取圆角图片
     */
    public static Bitmap getRoundedImage(Bitmap bitmap)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }

    /**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     *
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
    {
        if (null == bitmap || edgeLength <= 0)
        {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength)
        {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try
            {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e)
            {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try
            {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e)
            {
                return null;
            }
        }

        return result;
    }

    /**
     * @param originalBitmap
     * @return Bitmap
     * @notice 获取倒影图片
     */
    public static Bitmap getReflectedImage(Bitmap originalBitmap)
    {
        final int reflectionGap = 4;
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionBitmap = Bitmap.createBitmap(originalBitmap, 0,
                height / 2, width, height / 2, matrix, false);
        Bitmap withReflectionBitmap = Bitmap.createBitmap(width, (height
                + height / 2 + reflectionGap), Config.ARGB_8888);
        Canvas canvas = new Canvas(withReflectionBitmap);
        canvas.drawBitmap(originalBitmap, 0, 0, null);
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
        canvas.drawBitmap(reflectionBitmap, 0, height + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                originalBitmap.getHeight(), 0,
                withReflectionBitmap.getHeight(), 0x70ffffff, 0x00ffffff,
                TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0, height, width, withReflectionBitmap.getHeight(),
                paint);
        return withReflectionBitmap;
    }

    /**
     * @param bitmap
     * @return Bitmap
     * @notice 获取LOMO图片
     */
    public static Bitmap getLomoImage(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int dst[] = new int[width * height];
        bitmap.getPixels(dst, 0, width, 0, 0, width, height);
        int ratio = width > height ? height * 32768 / width : width * 32768
                / height;
        int cx = width >> 1;
        int cy = height >> 1;
        int max = cx * cx + cy * cy;
        int min = (int) (max * (1 - 0.8f));
        int diff = max - min;
        int ri, gi, bi;
        int dx, dy, distSq, v;
        int R, G, B;
        int value;
        int pos, pixColor;
        int newR, newG, newB;
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                pos = y * width + x;
                pixColor = dst[pos];
                R = Color.red(pixColor);
                G = Color.green(pixColor);
                B = Color.blue(pixColor);
                value = R < 128 ? R : 256 - R;
                newR = (value * value * value) / 64 / 256;
                newR = (R < 128 ? newR : 255 - newR);
                value = G < 128 ? G : 256 - G;
                newG = (value * value) / 128;
                newG = (G < 128 ? newG : 255 - newG);
                newB = B / 2 + 0x25;
                // ==========边缘黑暗==============//
                dx = cx - x;
                dy = cy - y;
                if (width > height)
                {
                    dx = (dx * ratio) >> 15;
                } else
                {
                    dy = (dy * ratio) >> 15;
                }
                distSq = dx * dx + dy * dy;
                if (distSq > min)
                {
                    v = ((max - distSq) << 8) / diff;
                    v *= v;
                    ri = newR * v >> 16;
                    gi = newG * v >> 16;
                    bi = newB * v >> 16;
                    newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
                    newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
                    newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
                }
                // ==========边缘黑暗end==============//
                dst[pos] = Color.rgb(newR, newG, newB);
            }
        }
        Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
        return acrossFlushBitmap;
    }

    /**
     * @param bmp
     * @return Bitmap
     * @notice 旧时光特效
     */
    public static Bitmap getOldTimeImage(Bitmap bmp)
    {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++)
        {
            for (int k = 0; k < width; k++)
            {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR,
                        newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * @param bmp
     * @param centerX：中心X坐标
     * @param centerY：中心Y坐标
     * @return Bitmap
     * @notice 暖意特效
     */
    public static Bitmap getWarmImage(Bitmap bmp, int centerX, int centerY)
    {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int radius = Math.min(centerX, centerY);
        final float strength = 150F;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++)
        {
            for (int k = 1, len = width - 1; k < len; k++)
            {
                pos = i * width + k;
                pixColor = pixels[pos];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = pixR;
                newG = pixG;
                newB = pixB;
                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(
                        centerX - k, 2));
                if (distance < radius * radius)
                {
                    int result = (int) (strength * (1.0 - Math.sqrt(distance)
                            / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * @param bm
     * @param saturation：饱和度
     * @param hue：色相
     * @param lum：亮度
     * @return Bitmap
     * @notice 根据饱和度，色相，亮度调整图片
     */
    public static Bitmap handleImage(Bitmap bitmap, int saturation, int hue,
                                     int lum)
    {
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        ColorMatrix mLightnessMatrix = new ColorMatrix();
        ColorMatrix mSaturationMatrix = new ColorMatrix();
        ColorMatrix mHueMatrix = new ColorMatrix();
        ColorMatrix mAllMatrix = new ColorMatrix();
        float mSaturationValue = saturation * 1.0F / 127;
        float mHueValue = hue * 1.0F / 127;
        float mLumValue = (lum - 127) * 1.0F / 127 * 180;
        mHueMatrix.reset();
        mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1);
        mSaturationMatrix.reset();
        mSaturationMatrix.setSaturation(mSaturationValue);
        mLightnessMatrix.reset();
        mLightnessMatrix.setRotate(0, mLumValue);
        mLightnessMatrix.setRotate(1, mLumValue);
        mLightnessMatrix.setRotate(2, mLumValue);
        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);
        mAllMatrix.postConcat(mSaturationMatrix);
        mAllMatrix.postConcat(mLightnessMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bmp;
    }

    /**
     * @param bitmap
     * @param degree：旋转角度
     * @return
     * @notice 旋转图片
     */
    public static Bitmap getRotateImage(Bitmap bitmap, float degree)
    {
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth,
                bmpHeight, matrix, true);
        return resizeBmp;
    }

    /**
     * @param bmp
     * @param flag：0是水平，1是垂直
     * @return
     * @notice 图片翻转
     */
    public static Bitmap getReverseImage(Bitmap bmp, int flag)
    {
        float[] floats = null;
        switch (flag)
        {
            case 0:
                floats = new float[]{-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
                break;
            case 1:
                floats = new float[]{1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
                break;
        }
        if (floats != null)
        {
            Matrix matrix = new Matrix();
            matrix.setValues(floats);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                    bmp.getHeight(), matrix, true);
        }
        return bmp;
    }

    /**
     * @param src
     * @param watermark：水印图片
     * @param x：x位置
     * @param y：y位置
     * @return
     * @notice 给图片添加涂鸦或水印
     */
    public static Bitmap getWatermarkImage(Bitmap src, Bitmap watermark, int x,
                                           int y)
    {
        Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(newb);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(watermark,
                (src.getWidth() - watermark.getWidth()) / 2,
                (src.getHeight() - watermark.getHeight()) / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        watermark.recycle();
        watermark = null;
        return newb;
    }

    /**
     * @param bmp
     * @return
     * @notice 柔化效果，高斯模糊
     */
    public static Bitmap getBlurImage(Bitmap bmp)
    {
        if (get("blurImageAmeliorate") != null)
        {
            return get("blurImageAmeliorate");
        }
        long start = System.currentTimeMillis();
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int delta = 16;
        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++)
        {
            for (int k = 1, len = width - 1; k < len; k++)
            {
                idx = 0;
                for (int m = -1; m <= 1; m++)
                {
                    for (int n = -1; n <= 1; n++)
                    {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + pixR * gauss[idx];
                        newG = newG + pixG * gauss[idx];
                        newB = newB + pixB * gauss[idx];
                        idx++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        Log.d("blurImageAmeliorate", "used time=" + (end - start));
        put("blurImageAmeliorate", bitmap);
        return bitmap;
    }

    /**
     * @param bmp
     * @return
     * @notice 浮雕效果
     */
    public static Bitmap getEmbossImage(Bitmap bmp)
    {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++)
        {
            for (int k = 1, len = width - 1; k < len; k++)
            {
                pos = i * width + k;
                pixColor = pixels[pos];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                pixColor = pixels[pos + 1];
                newR = Color.red(pixColor) - pixR + 127;
                newG = Color.green(pixColor) - pixG + 127;
                newB = Color.blue(pixColor) - pixB + 127;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * @param bmp
     * @return
     * @notice 锐化效果
     */
    public static Bitmap getSharpenImage(Bitmap bmp)
    {
        if (get("sharpenImageAmeliorate") != null)
        {
            return get("sharpenImageAmeliorate");
        }
        long start = System.currentTimeMillis();
        // 拉普拉斯矩阵
        int[] laplacian = new int[]{-1, -1, -1, -1, 9, -1, -1, -1, -1};
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int idx = 0;
        float alpha = 0.3F;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++)
        {
            for (int k = 1, len = width - 1; k < len; k++)
            {
                idx = 0;
                for (int m = -1; m <= 1; m++)
                {
                    for (int n = -1; n <= 1; n++)
                    {
                        pixColor = pixels[(i + n) * width + k + m];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + (int) (pixR * laplacian[idx] * alpha);
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);
                        idx++;
                    }
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        Log.e("sharpenImageAmeliorate", "used time=" + (end - start));
        put("sharpenImageAmeliorate", bitmap);
        return bitmap;
    }

    /**
     * @param bmp
     * @return
     * @notice 底片效果
     */
    public static Bitmap getFilmImage(Bitmap bmp)
    {
        final int MAX_VALUE = 255;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++)
        {
            for (int k = 1, len = width - 1; k < len; k++)
            {
                pos = i * width + k;
                pixColor = pixels[pos];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = MAX_VALUE - pixR;
                newG = MAX_VALUE - pixG;
                newB = MAX_VALUE - pixB;
                newR = Math.min(MAX_VALUE, Math.max(0, newR));
                newG = Math.min(MAX_VALUE, Math.max(0, newG));
                newB = Math.min(MAX_VALUE, Math.max(0, newB));
                pixels[pos] = Color.argb(MAX_VALUE, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * @param bmp
     * @param centerX：光照中心x坐标
     * @param centerY：光照中心要坐标
     * @return
     * @notice 光照效果
     */
    public static Bitmap getSunshineImage(Bitmap bmp, int centerX, int centerY)
    {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int radius = Math.min(centerX, centerY);
        final float strength = 150F; // 光照强度 100~150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++)
        {
            for (int k = 1, len = width - 1; k < len; k++)
            {
                pos = i * width + k;
                pixColor = pixels[pos];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = pixR;
                newG = pixG;
                newB = pixB;
                // 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(
                        centerX - k, 2));
                if (distance < radius * radius)
                {
                    // 按照距离大小计算增加的光照值
                    int result = (int) (strength * (1.0 - Math.sqrt(distance)
                            / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    // -----------------------------------------------素描图片-----------------------------------------------------

    /**
     * @param bmp
     * @return
     * @notice 素描效果，需要用一个线程去打开
     */
    public static Bitmap getSketchImage(Bitmap bmp)
    {
        if (get("sketch") != null)
        {
            return get("sketch");
        }
        long start = System.currentTimeMillis();
        int pos, row, col, clr;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixSrc = new int[width * height];
        int[] pixNvt = new int[width * height];
        bmp.getPixels(pixSrc, 0, width, 0, 0, width, height);
        for (row = 0; row < height; row++)
        {
            for (col = 0; col < width; col++)
            {
                pos = row * width + col;
                pixSrc[pos] = (Color.red(pixSrc[pos])
                        + Color.green(pixSrc[pos]) + Color.blue(pixSrc[pos])) / 3;
                pixNvt[pos] = 255 - pixSrc[pos];
            }
        }
        gaussGray(pixNvt, 5.0, 5.0, width, height);
        for (row = 0; row < height; row++)
        {
            for (col = 0; col < width; col++)
            {
                pos = row * width + col;
                clr = pixSrc[pos] << 8;
                clr /= 256 - pixNvt[pos];
                clr = Math.min(clr, 255);
                pixSrc[pos] = Color.rgb(clr, clr, clr);
            }
        }
        bmp.setPixels(pixSrc, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        Log.d("blurImageAmeliorate", "used time=" + (end - start));
        put("sketch", bmp);
        return bmp;
    }

    private static int gaussGray(int[] psrc, double horz, double vert,
                                 int width, int height)
    {
        int[] dst, src;
        double[] n_p, n_m, d_p, d_m, bd_p, bd_m;
        double[] val_p, val_m;
        int i, j, t, k, row, col, terms;
        int[] initial_p, initial_m;
        double std_dev;
        int row_stride = width;
        int max_len = Math.max(width, height);
        int sp_p_idx, sp_m_idx, vp_idx, vm_idx;

        val_p = new double[max_len];
        val_m = new double[max_len];

        n_p = new double[5];
        n_m = new double[5];
        d_p = new double[5];
        d_m = new double[5];
        bd_p = new double[5];
        bd_m = new double[5];

        src = new int[max_len];
        dst = new int[max_len];

        initial_p = new int[4];
        initial_m = new int[4];

        if (vert > 0.0)
        {
            vert = Math.abs(vert) + 1.0;
            std_dev = Math.sqrt(-(vert * vert) / (2 * Math.log(1.0 / 255.0)));
            findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);

            for (col = 0; col < width; col++)
            {
                for (k = 0; k < max_len; k++)
                {
                    val_m[k] = val_p[k] = 0;
                }

                for (t = 0; t < height; t++)
                {
                    src[t] = psrc[t * row_stride + col];
                }
                sp_p_idx = 0;
                sp_m_idx = height - 1;
                vp_idx = 0;
                vm_idx = height - 1;
                initial_p[0] = src[0];
                initial_m[0] = src[height - 1];
                for (row = 0; row < height; row++)
                {
                    terms = (row < 4) ? row : 4;
                    for (i = 0; i <= terms; i++)
                    {
                        val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i]
                                * val_p[vp_idx - i];
                        val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i]
                                * val_m[vm_idx + i];
                    }
                    for (j = i; j <= 4; j++)
                    {
                        val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
                        val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
                    }
                    sp_p_idx++;
                    sp_m_idx--;
                    vp_idx++;
                    vm_idx--;
                }
                transferGaussPixels(val_p, val_m, dst, 1, height);
                for (t = 0; t < height; t++)
                {
                    psrc[t * row_stride + col] = dst[t];
                }
            }
        }

        if (horz > 0.0)
        {
            horz = Math.abs(horz) + 1.0;
            if (horz != vert)
            {
                std_dev = Math.sqrt(-(horz * horz)
                        / (2 * Math.log(1.0 / 255.0)));
                findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);
            }

            for (row = 0; row < height; row++)
            {
                for (k = 0; k < max_len; k++)
                {
                    val_m[k] = val_p[k] = 0;
                }
                for (t = 0; t < width; t++)
                {
                    src[t] = psrc[row * row_stride + t];
                }

                sp_p_idx = 0;
                sp_m_idx = width - 1;
                vp_idx = 0;
                vm_idx = width - 1;
                initial_p[0] = src[0];
                initial_m[0] = src[width - 1];

                for (col = 0; col < width; col++)
                {
                    terms = (col < 4) ? col : 4;
                    for (i = 0; i <= terms; i++)
                    {
                        val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i]
                                * val_p[vp_idx - i];
                        val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i]
                                * val_m[vm_idx + i];
                    }
                    for (j = i; j <= 4; j++)
                    {
                        val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
                        val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
                    }
                    sp_p_idx++;
                    sp_m_idx--;
                    vp_idx++;
                    vm_idx--;
                }
                transferGaussPixels(val_p, val_m, dst, 1, width);
                for (t = 0; t < width; t++)
                {
                    psrc[row * row_stride + t] = dst[t];
                }
            }
        }

        return 0;
    }

    private static void findConstants(double[] n_p, double[] n_m, double[] d_p,
                                      double[] d_m, double[] bd_p, double[] bd_m, double std_dev)
    {
        double div = Math.sqrt(2 * 3.141593) * std_dev;
        double x0 = -1.783 / std_dev;
        double x1 = -1.723 / std_dev;
        double x2 = 0.6318 / std_dev;
        double x3 = 1.997 / std_dev;
        double x4 = 1.6803 / div;
        double x5 = 3.735 / div;
        double x6 = -0.6803 / div;
        double x7 = -0.2598 / div;
        int i;

        n_p[0] = x4 + x6;
        n_p[1] = (Math.exp(x1)
                * (x7 * Math.sin(x3) - (x6 + 2 * x4) * Math.cos(x3)) + Math
                .exp(x0) * (x5 * Math.sin(x2) - (2 * x6 + x4) * Math.cos(x2)));
        n_p[2] = (2
                * Math.exp(x0 + x1)
                * ((x4 + x6) * Math.cos(x3) * Math.cos(x2) - x5 * Math.cos(x3)
                * Math.sin(x2) - x7 * Math.cos(x2) * Math.sin(x3)) + x6
                * Math.exp(2 * x0) + x4 * Math.exp(2 * x1));
        n_p[3] = (Math.exp(x1 + 2 * x0)
                * (x7 * Math.sin(x3) - x6 * Math.cos(x3)) + Math.exp(x0 + 2
                * x1)
                * (x5 * Math.sin(x2) - x4 * Math.cos(x2)));
        n_p[4] = 0.0;

        d_p[0] = 0.0;
        d_p[1] = -2 * Math.exp(x1) * Math.cos(x3) - 2 * Math.exp(x0)
                * Math.cos(x2);
        d_p[2] = 4 * Math.cos(x3) * Math.cos(x2) * Math.exp(x0 + x1)
                + Math.exp(2 * x1) + Math.exp(2 * x0);
        d_p[3] = -2 * Math.cos(x2) * Math.exp(x0 + 2 * x1) - 2 * Math.cos(x3)
                * Math.exp(x1 + 2 * x0);
        d_p[4] = Math.exp(2 * x0 + 2 * x1);

        for (i = 0; i <= 4; i++)
        {
            d_m[i] = d_p[i];
        }

        n_m[0] = 0.0;
        for (i = 1; i <= 4; i++)
        {
            n_m[i] = n_p[i] - d_p[i] * n_p[0];
        }

        double sum_n_p, sum_n_m, sum_d;
        double a, b;

        sum_n_p = 0.0;
        sum_n_m = 0.0;
        sum_d = 0.0;

        for (i = 0; i <= 4; i++)
        {
            sum_n_p += n_p[i];
            sum_n_m += n_m[i];
            sum_d += d_p[i];
        }

        a = sum_n_p / (1.0 + sum_d);
        b = sum_n_m / (1.0 + sum_d);

        for (i = 0; i <= 4; i++)
        {
            bd_p[i] = d_p[i] * a;
            bd_m[i] = d_m[i] * b;
        }
    }

    private static void transferGaussPixels(double[] src1, double[] src2,
                                            int[] dest, int bytes, int width)
    {
        int i, j, k, b;
        int bend = bytes * width;
        double sum;
        i = j = k = 0;
        for (b = 0; b < bend; b++)
        {
            sum = src1[i++] + src2[j++];

            if (sum > 255)
            {
                sum = 255;
            } else if (sum < 0)
            {
                sum = 0;
            }
            dest[k++] = (int) sum;
        }
    }

    // -----------------------------------------------素描图片-----------------------------------------------------

    /**
     * @param context
     * @param bitmap
     * @return
     * @notice 获取毛玻璃图片
     */
    public static Bitmap getBlurBitmap(Context context, Bitmap bitmap)
    {
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs,
                Element.U8_4(rs));
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        blurScript.setRadius(25.f);
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        allOut.copyTo(outBitmap);
        bitmap.recycle();
        rs.destroy();
        return outBitmap;
    }


    public static Bitmap scaleImage(Bitmap org, int scaleWidth, int scaleHeight)
    {
        if (org == null)
        {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale((float) scaleWidth / org.getWidth(), (float) scaleHeight / org.getHeight());
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }
}
