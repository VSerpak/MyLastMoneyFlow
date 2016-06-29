package com.example.vserp.viewpager.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.vserp.viewpager.R;

/**
 * Created by vserp on 6/16/2016.
 */

public class RoundChart extends View {

    private int mDiameter;

    private int mColorPlan = 0;
    private int mColorCurrent = 0;

    private float mAngleValue;
    private float mAnglePercent;

    public RoundChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint mPaint = new Paint();

        RectF mRectF = new RectF(0, 0, mDiameter, mDiameter);
        mRectF.offset(mDiameter/2,mDiameter-mDiameter/2);

        mPaint.setAntiAlias(true);

        mPaint.setColor(mColorPlan);
        canvas.drawArc(mRectF,0,360,true,mPaint);

        mPaint.setColor(mColorCurrent);
        canvas.drawArc(mRectF,270, mAngleValue,true,mPaint);

        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mDiameter,mDiameter,mDiameter/3,mPaint);

        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mDiameter/5);

        float textWidth = mPaint.measureText(mAnglePercent + "%");
        canvas.drawText(mAnglePercent + "%",
                mDiameter - textWidth /2,
                mDiameter + mDiameter/10-mDiameter/60,
                mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mDiameter = getMeasuredHeight()/2;

        setMeasuredDimension(mDiameter*2,mDiameter*2);
    }

    public void setValues (int percentOfPlan){

        if (percentOfPlan <= 100) {
            this.mAnglePercent = percentOfPlan;
            mColorPlan = getResources().getColor(R.color.lightGreen);
            mColorCurrent = getResources().getColor(R.color.darkGreen);
            mAngleValue = (float) mAnglePercent *3.6f;
        }else{
            this.mAnglePercent = percentOfPlan;
            mColorPlan = getResources().getColor(R.color.lightRed);
            mColorCurrent = getResources().getColor(R.color.darkRed);
            mAngleValue = (float) (mAnglePercent - 100) *3.6f;
        }
        draw(new Canvas());
    }
}
