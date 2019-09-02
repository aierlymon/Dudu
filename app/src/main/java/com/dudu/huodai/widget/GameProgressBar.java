package com.dudu.huodai.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.R;

public class GameProgressBar extends View {

    private final String TAG = "tag";
    private Paint defalutPaint;
    private Paint selectBGPaint;

    private float max = 100.0f;
    private int radis = 60;

    private int currentPorgress;
    private Paint selectCirclePaint;
    private Paint selectCircleStrokePaint;
    private Paint textPaint;

    public int getCurrentPorgress() {
        return currentPorgress;
    }

    public void setCurrentPorgress(int currentPorgress) {
        if (currentPorgress >= max) {
            this.currentPorgress = 100;
        } else {
            this.currentPorgress = currentPorgress;
        }
        invalidate();
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public GameProgressBar(Context context) {
        super(context);
        init();
    }

    public GameProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private int StrokeWidth = 4;
    private int rx = 40, ry = 40;
    private int commomIndex;

    private void init() {

        // radis=getHeight()/4;
        //方形进度条
        defalutPaint = new Paint();
        defalutPaint.setAntiAlias(true);
        defalutPaint.setStyle(Paint.Style.FILL);


        selectBGPaint = new Paint();
        selectBGPaint.setAntiAlias(true);
        selectBGPaint.setStyle(Paint.Style.FILL);
        selectBGPaint.setColor(Color.parseColor("#FFCC00"));


        selectCirclePaint = new Paint();
        selectCirclePaint.setAntiAlias(true);
        selectCirclePaint.setColor(Color.parseColor("#FFCC00"));
        selectCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        selectCircleStrokePaint = new Paint();
        selectCircleStrokePaint.setAntiAlias(true);
        selectCircleStrokePaint.setColor(Color.parseColor("#FFFFFF"));
        selectCircleStrokePaint.setStyle(Paint.Style.STROKE);
        selectCircleStrokePaint.setStrokeWidth(StrokeWidth);


        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#FFFFFF"));
        textPaint.setTextSize(textSize);
    }

    private int textSize = 30;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        commomIndex = getHeight() / 2 + 30;
        //画白底进度条，方形的
        //限制坐标
        defalutPaint.setColor(Color.parseColor("#4906B5"));
        float x = radis / 4 + commomIndex;
        float y = radis / 3 * 2 + commomIndex;
        RectF rect = new RectF(radis, x, getWidth() - radis, y);
        canvas.drawRoundRect(rect, rx, ry, defalutPaint);
        //更新进度
        refreshProgress(canvas, x, y);

        defalutPaint.setColor(Color.parseColor("#ffffff"));
        //画圆
        drawCircle(canvas, 15, 40, 60, 75, 100);

        //画抽奖次数
        textPaint.setTextSize(textSize);
        canvas.drawText(getResources().getString(R.string.numberofdraws), 10, (radis * 1.8f) + commomIndex, textPaint);
        textPaint.setTextSize(textSize * 3 / 2);
        String extra=getResources().getString(R.string.extradraw);
        canvas.drawText(extra, getWidth() / 2 - textPaint.measureText(extra) / 2, radis - 10, textPaint);
    }

    private int[] drawBitmaps = {R.mipmap.circlegame15, R.mipmap.circlegame30, R.mipmap.circlegame60, R.mipmap.circlegame75, R.mipmap.circlegame15};

    private void drawCircle(Canvas canvas, int... index) {
        //其实这个4是按比例的，不过这里只用这样
        for (int i = 0; i < index.length; i++) {
            if (i == index.length - 1) break;
            Log.i(TAG, "drawCircle: " + index[i]);
            RectF rectF = new RectF(0 + (getWidth() / max) * index[i], commomIndex, radis + (getWidth() / max) * index[i], radis + commomIndex);
            defalutPaint.setTextSize(textSize);
            String text = String.valueOf(index[i]);


            canvas.drawText(text, 0 + (getWidth() / max) * index[i] + radis / 3 - defalutPaint.measureText(text) / 2 + 10, (radis * 1.8f) + commomIndex, defalutPaint);

            if (currentPorgress >= index[i]) {
                RectF rectF2 = new RectF((float) (0 + (getWidth() / max) * index[i] + StrokeWidth / 2), 0 + StrokeWidth / 2f + commomIndex, radis + (getWidth() / max) * index[i] - StrokeWidth / 2f, radis - StrokeWidth / 2f + commomIndex);
                canvas.drawArc(rectF2, 0, 360, true, selectCirclePaint);
                rectF = new RectF(0 + (getWidth() / max) * index[i] + StrokeWidth, 0 + StrokeWidth + commomIndex, radis + (getWidth() / max) * index[i] - StrokeWidth, radis - StrokeWidth + commomIndex);
                canvas.drawArc(rectF, 0, 360, true, selectCircleStrokePaint);
            } else {
                canvas.drawArc(rectF, 0, 360, true, defalutPaint);
            }
            Bitmap bitmap = resize(BitmapFactory.decodeResource(getResources(), drawBitmaps[i]), (int) (radis * 1.5), (int) (radis * 1.5));
            canvas.drawBitmap(bitmap, 0 + (getWidth() / max) * index[i] + radis / 3 - bitmap.getWidth() / 3, commomIndex - (radis * 1.7f), null);
        }
    }

    private void refreshProgress(Canvas canvas, float x, float y) {
        //选中进度
        if (currentPorgress >= max) {
            currentPorgress = (int) max;
        }
        //画进度的进度条，方形的

        float imageWidth = (((getWidth() - radis) / max) * currentPorgress)+radis;
        if(imageWidth>getWidth() - radis){
            imageWidth=getWidth() - radis;
        }
        RectF rectSelect = new RectF(radis, x, imageWidth ,y);
        canvas.drawRoundRect(rectSelect, rx, ry, selectBGPaint);
    }

    public Bitmap decodeBitmapFromResource(Resources resources, int resId, int width, int height) {

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(resources, resId, options);
        //获取采样率
        options.inSampleSize = calculateInSampleSize(options, width, height);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(resources, resId, options);

    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;

        int inSampleSize = 1;


        if (originalHeight > reqHeight || originalWidth > reqHeight) {
            int halfHeight = originalHeight / 2;
            int halfWidth = originalWidth / 2;
            //压缩后的尺寸与所需的尺寸进行比较
            while ((halfWidth / inSampleSize) >= reqHeight && (halfHeight / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }

        }

        return inSampleSize;

    }

    public Bitmap resize(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap mbitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return mbitmap;
    }

}
