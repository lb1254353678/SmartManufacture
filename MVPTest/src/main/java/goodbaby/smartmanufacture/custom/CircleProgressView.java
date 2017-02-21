package goodbaby.smartmanufacture.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by goodbaby on 17/2/15.
 */

public class CircleProgressView extends View {
    private float length = 0;

    private int width,heigth;

    private float mRadius;
    private float mCircleXY;
    private float mShowTextSize;
    private RectF mArcRectF;
    private float mSweepAngle;
    private float mSweepValue = 66;

    public CircleProgressView(Context context) {
        super(context);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        heigth = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("***goodbaby***", "width:" + width);
        Log.e("***goodbaby***", "heigth:" + heigth);
        setMeasuredDimension(width, heigth);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 绘制圆
         */
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        canvas.drawCircle(mCircleXY, mCircleXY, mRadius, paint);

        /**
         * 绘制圆环
         */
        Paint mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(getResources().getColor(android.R.color.holo_blue_bright));
        mArcPaint.setStrokeWidth((float) (length * 0.1));
        mArcPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(mArcRectF, 270, mSweepAngle, false, mArcPaint);

        /**
         * 绘制文字
         */
        Paint txtPaint = new Paint();
        txtPaint.setColor(Color.BLACK);
        txtPaint.setAntiAlias(true);
        txtPaint.setTextSize(mShowTextSize);
        txtPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("test", 0, "test".length(), mCircleXY, mCircleXY + (mShowTextSize / 4), txtPaint);
    }

    /**
     * 初始化
     */
    private void initView(){
        if(heigth >=  width){
            length = width;
        }else {
            length = heigth;
        }
        mCircleXY = length / 2;
        mShowTextSize = setShowTextSize();
        mRadius = (float) (length * 0.5 / 2);

        mArcRectF = new RectF(
                (float) (length * 0.1),
                (float) (length * 0.1),
                (float) (length * 0.9),
                (float) (length * 0.9));
        mSweepAngle = (mSweepValue / 100f) * 360f;
    }

    private float setShowTextSize() {
        this.invalidate();
        return 80;
    }
}
