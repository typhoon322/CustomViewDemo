package me.androidapp.yanx.customviewdemo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * com.androidapp.yanx.lan_gtd.views
 * Created by @author YANx on 2018/11/24 9:25 PM.
 * Description ${TODO}
 */
public class CustomButtonComponent extends View implements View.OnClickListener {
    private static final String TAG = "CustomButtonComponent";
    Path path1 = new Path();
    Path path2 = new Path();
    int margin = 20;
    int offset = 40;
    RectF rectF1 = new RectF();
    RectF rectF2 = new RectF();
    Region region = new Region();
    Rect textBoundsL = new Rect();
    Rect textBoundsR = new Rect();
    ColorStateList leftTextColorStateList;
    ColorStateList rightTextColorStateList;
    int[] states = new int[]{android.R.attr.state_empty, android.R.attr.state_pressed};
    int[] colors = new int[]{
            Color.BLACK,
            Color.RED,
            Color.GREEN,
            Color.BLUE
    };
    private Paint bgPaint = new Paint();
    private Paint textPaint = new Paint();
    private int colorBgLeft = Color.RED;
    private int colorBgRight = Color.BLUE;
    private String textLeft = "";
    private String textRight = "";
    private int textColorLeft = Color.WHITE;
    private int textColorRight = Color.WHITE;
    private float textSizeLeft;
    private float textSizeRight;
    private int buttonIndex = -1;
    private OnButtonClick onButtonClickListener;
    private boolean mIsWaitUpEvent;

    public CustomButtonComponent(Context context) {
        this(context, null);
    }

    public CustomButtonComponent(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomButtonComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        setOnClickListener(this);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButtonComponent);
        textLeft = typedArray.getString(R.styleable.CustomButtonComponent_textLeft);
        textRight = typedArray.getString(R.styleable.CustomButtonComponent_textRight);
        textColorLeft = typedArray.getColor(R.styleable.CustomButtonComponent_textColorLeft, getContext().getColor(android.R.color.black));
        textColorRight = typedArray.getColor(R.styleable.CustomButtonComponent_textColorRight, getContext().getColor(android.R.color.black));
        textSizeLeft = typedArray.getDimension(R.styleable.CustomButtonComponent_textLeftFontSize, 20);
        textSizeRight = typedArray.getDimension(R.styleable.CustomButtonComponent_textRightFontSize, 20);
        colorBgLeft = typedArray.getColor(R.styleable.CustomButtonComponent_bgColorLeft, getContext().getColor(android.R.color.background_light));
        colorBgRight = typedArray.getColor(R.styleable.CustomButtonComponent_bgColorRight, getContext().getColor(android.R.color.background_light));
        leftTextColorStateList = typedArray.getColorStateList(R.styleable.CustomButtonComponent_bgColorLeft);
        typedArray.recycle();
    }

    @Override
    public boolean performClick() {
        if (buttonIndex == -1) {
            return false;
        }
        if (onButtonClickListener != null) {
            Log.w(TAG, "performClick >>> " + (buttonIndex == 0 ? "LEFT " : "RIGHT "));
            onButtonClickListener.onClick(buttonIndex);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int downX = (int) event.getX();
                int downY = (int) event.getY();
                Log.d(TAG, "onTouchEvent : [ " + downX + " , " + downY + " ]");
                if (isContain(path1, rectF1, downX, downY)) {
                    buttonIndex = 0;
                    Log.i(TAG, "onTouchEvent: DOWN >>> 左边 <<<的东西");
                    mIsWaitUpEvent = true;
                    invalidate();
                    return true;
                } else if (isContain(path2, rectF2, downX, downY)) {
                    buttonIndex = 1;
                    Log.i(TAG, "onTouchEvent: DOWN >>> 右边 <<<的东西");
                    mIsWaitUpEvent = true;
                    invalidate();
                    return true;
                } else {
                    buttonIndex = -1;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (mIsWaitUpEvent) {
                    if (isContain(path1, rectF1, upX, upY)) {
                        if (buttonIndex == 0) {
                            performClick();
                            Toast.makeText(getContext(), "点击左边", Toast.LENGTH_SHORT).show();
                        }
                    } else if (isContain(path2, rectF2, upX, upY)) {
                        if (buttonIndex == 1) {
                            performClick();
                            Toast.makeText(getContext(), "点击右边", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                invalidate();
                buttonIndex = -1;
                mIsWaitUpEvent = false;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                mIsWaitUpEvent = false;
                Log.i(TAG, "onTouchEvent: OUTSIDE >>> ");
                return false;
            case MotionEvent.ACTION_CANCEL:
                mIsWaitUpEvent = false;
                Log.i(TAG, "onTouchEvent: ACTION_CANCEL >>> ");
                return false;
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
        drawBackground(canvas);
        drawText(canvas);
    }

    private void drawBackground(Canvas canvas) {
        bgPaint.setColor(colorBgLeft);
        if (buttonIndex == 0) {
            bgPaint.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#22ffffff"), PorterDuff.Mode.LIGHTEN));
        } else {
            bgPaint.setColorFilter(null);
        }
        path1.moveTo(0, 0);
        float topRight = getMeasuredWidth() / 2 - margin / 2 + offset;
        float bottomRight = getMeasuredWidth() / 2 - margin / 2 - offset;
        path1.lineTo(topRight, 0);
        path1.lineTo(bottomRight, getMeasuredHeight());
        path1.lineTo(0, getMeasuredHeight());
        path1.close();
        path1.computeBounds(rectF1, true);
        canvas.drawPath(path1, bgPaint);

        bgPaint.setColor(colorBgRight);
        if (buttonIndex == 1) {
            bgPaint.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#22ffffff"), PorterDuff.Mode.LIGHTEN));
        } else {
            bgPaint.setColorFilter(null);
        }
        path2.moveTo(getMeasuredWidth() / 2 + margin / 2 + offset, 0);
        path2.lineTo(getMeasuredWidth(), 0);
        path2.lineTo(getMeasuredWidth(), getMeasuredHeight());
        path2.lineTo(getMeasuredWidth() / 2 + margin / 2 - offset, getMeasuredHeight());
        path2.close();
        path2.computeBounds(rectF2, true);
        canvas.drawPath(path2, bgPaint);
    }


    private void drawText(Canvas canvas) {
        textPaint.setAntiAlias(true);
        if (!TextUtils.isEmpty(textLeft)) {
            textPaint.setTextSize(textSizeLeft);
            textPaint.getTextBounds(textLeft, 0, textLeft.length(), textBoundsL);
            textPaint.setColor(textColorLeft);
            canvas.drawText(textLeft, getMeasuredWidth() / 4 - textBoundsL.exactCenterX(), getMeasuredHeight() / 2 - textBoundsL.exactCenterY(), textPaint);
        }
        if (!TextUtils.isEmpty(textRight)) {
            textPaint.setTextSize(textSizeRight);
            textPaint.getTextBounds(textRight, 0, textRight.length(), textBoundsR);
            textPaint.setColor(textColorRight);
            canvas.drawText(textRight, getMeasuredWidth() / 4 * 3 - textBoundsR.exactCenterX(), getMeasuredHeight() / 2 - textBoundsR.exactCenterY(), textPaint);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
    }

    public OnButtonClick getOnButtonClickListener() {
        return onButtonClickListener;
    }

    public void setOnButtonClickListener(OnButtonClick onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public void onClick(View view) {
        performClick();
    }

    public interface OnButtonClick {
        void onClick(int index);
    }
}
