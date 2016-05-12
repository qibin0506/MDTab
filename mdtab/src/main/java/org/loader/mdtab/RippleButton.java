package org.loader.mdtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by qibin on 16-5-7.
 */
public class RippleButton extends TextView {
	
	private Paint mPaint;
	
	private int mStepSize;
	private int mMinRadius = 0;
	private int mRadius;
	private int mMaxRadius;
	private int mCenterX;
	private int mCenterY;
	private boolean isAnimating;
	
	private OnBeforeClickedListener mListener;
	
	public RippleButton(Context context) {
		this(context, null, 0);
	}

	public RippleButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RippleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		resolveAttrs(context, attrs, defStyle);
	}

	private void resolveAttrs(Context context, AttributeSet attrs, int defStyle) {
		TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Bar, defStyle, 0);
		mPaint.setColor(ta.getColor(R.styleable.Bar_ripple_color, Color.TRANSPARENT));
		ta.recycle();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mMaxRadius = Math.max(getMeasuredWidth(), getMeasuredHeight()) / 2;
		mCenterX = getMeasuredWidth() / 2;
		mCenterY = getMeasuredHeight() / 2;
		mStepSize = mMaxRadius / 20;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			if(mListener != null) mListener.onBeforeClicked(this);
			mRadius = mMinRadius;
			isAnimating = true;
			postInvalidate();
		}
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(!isAnimating) {
			super.onDraw(canvas);
			return;
		}
		
		if(isAnimating && mRadius > mMaxRadius) {
			isAnimating = false;
			mRadius = mMinRadius;
			performClick();
			super.onDraw(canvas);
			return;
		}
		
		mRadius += mStepSize;
		canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
		super.onDraw(canvas);
		postInvalidate();
	}
	
	public void setRippleColor(int color) {
		mPaint.setColor(color);
	}
	
	public void cancel() {
		isAnimating = false;
	}
	
	public void setOnBeforeClickedListener(OnBeforeClickedListener li) {
		mListener = li;
	}
	
	public interface OnBeforeClickedListener {
		void onBeforeClicked(View view);
	}
}
