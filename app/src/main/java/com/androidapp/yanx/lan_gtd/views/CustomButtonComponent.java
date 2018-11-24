package com.androidapp.yanx.lan_gtd.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * com.androidapp.yanx.lan_gtd.views
 * Created by @author YANx on 2018/11/24 9:25 PM.
 * Description ${TODO}
 */
public class CustomButtonComponent extends View {
    private static final String TAG = "CustomButtonComponent";
    Path path1 = new Path();
    Path path2 = new Path();
    int margin = 20;
    int offset = 40;
    RectF rectF1 = new RectF();
    RectF rectF2 = new RectF();
    Path tempPath = new Path();
    Region region = new Region();
    private Paint bgPaint = new Paint();
    private Paint textPaint = new Paint();
    private int colorBg1 = Color.RED;
    private int colorBg2 = Color.BLUE;

    public CustomButtonComponent(Context context) {
        super(context);
    }

    public CustomButtonComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButtonComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Log.d(TAG, "onTouchEvent : [ " + x + " , " + y + " ]");
        if (isContain(path1, rectF1, x, y)) {
            Log.i(TAG, "onTouchEvent: 点击了>>> 左边 <<<的东西");
        }
        if (isContain(path2, rectF2, x, y)) {
            Log.i(TAG, "onTouchEvent: 点击了>>> 右边 <<<的东西");
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            default:
        }
        return super.onTouchEvent(event);
    }

    private boolean isContain(Path path, RectF rectF, int x, int y) {
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return region.contains(x, y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bgPaint.setColor(colorBg1);
        path1.moveTo(0, 0);
        float topRight = getMeasuredWidth() / 2 - margin / 2 + offset;
        float bottomRight = getMeasuredWidth() / 2 - margin / 2 - offset;
        path1.lineTo(topRight, 0);
        path1.lineTo(bottomRight, getMeasuredHeight());
        path1.lineTo(0, getMeasuredHeight());
        path1.close();
        path1.computeBounds(rectF1, true);
        canvas.drawPath(path1, bgPaint);

        bgPaint.setColor(colorBg2);
        path2.moveTo(getMeasuredWidth() / 2 + margin / 2 + offset, 0);
        path2.lineTo(getMeasuredWidth(), 0);
        path2.lineTo(getMeasuredWidth(), getMeasuredHeight());
        path2.lineTo(getMeasuredWidth() / 2 + margin / 2 - offset, getMeasuredHeight());
        path2.close();
        path2.computeBounds(rectF2, true);
        canvas.drawPath(path2, bgPaint);

    }
}
