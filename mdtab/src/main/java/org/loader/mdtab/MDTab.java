package org.loader.mdtab;

import org.loader.mdtab.RippleButton.OnBeforeClickedListener;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by qibin on 16-5-7.
 */
public class MDTab extends LinearLayout {
	
	private TabAdapter mAdapter;
	
	private int mCheckedItemColor;
	private int mNormalItemColor;
	private int mRippleColor;
	
	private int mTextSize;
	private int mTabPadding;
	private float mCheckedSizePercent;
	
	private int mCheckedPosition;
	
	private OnItemCheckedListener mItemCheckedListener;
	
	public MDTab(Context context) {
		this(context, null, 0);
	}

	public MDTab(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MDTab(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		resolveAttrs(context, attrs, defStyle);
		setOrientation(LinearLayout.HORIZONTAL);
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable state = super.onSaveInstanceState();
		Bundle bundle = new Bundle();
		bundle.putParcelable("state", state);
		bundle.putInt("checked", mCheckedPosition);
		return bundle;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if(state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			mCheckedPosition = bundle.getInt("checked");
			state = bundle.getParcelable("state");
			itemChecked(mCheckedPosition);
		}
		
		super.onRestoreInstanceState(state);
	}
	
	private void resolveAttrs(Context context, AttributeSet attrs, int defStyle) {
		TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Bar, defStyle, 0);
		mCheckedItemColor = ta.getColor(R.styleable.Bar_checked_color, Color.BLACK);
		mNormalItemColor = ta.getColor(R.styleable.Bar_normal_color, Color.BLACK);
		mRippleColor = ta.getColor(R.styleable.Bar_ripple_color, Color.TRANSPARENT);
		mTextSize = ta.getDimensionPixelSize(R.styleable.Bar_android_textSize, 15);
		mTabPadding = ta.getDimensionPixelSize(R.styleable.Bar_tab_padding, 0);
		mCheckedSizePercent = ta.getFraction(R.styleable.Bar_checked_percent, 1, 1, 1);
		ta.recycle();
	}
	
	public void itemChecked(int pos) {
		mCheckedPosition = pos;
		int itemCount = getChildCount();
		RippleButton ripple;
		Drawable drawable;
		for (int i = 0; i < itemCount; i++) {
			ripple = (RippleButton) getChildAt(i);
			drawable = ripple.getCompoundDrawables()[1];
			ripple.cancel();
			if(i == mCheckedPosition) {
				ripple.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize * mCheckedSizePercent);
				ripple.setTextColor(mCheckedItemColor);
				if(drawable != null) {
					drawable.setColorFilter(new PorterDuffColorFilter(mCheckedItemColor, 
							PorterDuff.Mode.SRC_IN));
				}
				continue;
			}
			
			ripple.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
			ripple.setTextColor(mNormalItemColor);
			if(drawable != null) {
				drawable.clearColorFilter();
			}
		}
	}
	
	public int getCheckedPosition() {
		return mCheckedPosition;
	}
	
	public void setAdapter(TabAdapter adapter) {
		mAdapter = adapter;
		mAdapter.registerObserver(mObserver);
		mAdapter.notifyDataSetChanged();
	}
	
	public TabAdapter getAdapter() {
		return mAdapter;
	}
	
	private DataSetObserver mObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			onInvalidated();
			if(mAdapter == null) return;
			int itemCount = mAdapter.getItemCount();

			LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
			params.weight = 1;

			for (int i = 0; i < itemCount; i++) {
				addView(buildRipple(i), params);
			}
		}
		
		@Override
		public void onInvalidated() {
			removeAllViews();
		}
		
		private RippleButton buildRipple(final int pos) {
			RippleButton ripple = new RippleButton(getContext());
			ripple.setGravity(Gravity.CENTER);
			ripple.setRippleColor(mRippleColor);
			ripple.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
			ripple.setPadding(0, mTabPadding, 0, mTabPadding);
			
			ripple.setTextColor(mNormalItemColor);
			ripple.setText(mAdapter.getText(pos));
			
			ripple.setCompoundDrawablesWithIntrinsicBounds(null, mAdapter.getDrawable(pos),
					null, null);
			ripple.setOnBeforeClickedListener(new OnBeforeClickedListener() {
				@Override
				public void onBeforeClicked(View view) {
					if(mItemCheckedListener != null && pos != mCheckedPosition) { 
						mItemCheckedListener.onItemChecked(pos, getChildAt(pos));
					}
					
					itemChecked(pos);
				}
			});
			
			return ripple;
		}
	};
	
	public abstract static class TabAdapter {
		private DataSetObserver mObserver;

		public void registerObserver(DataSetObserver observer) {
			mObserver = observer;
		}
		
		public void unregisterObserver() {
			mObserver = null;
		}
		
		public void notifyDataSetChanged() {
			if(mObserver != null) mObserver.onChanged();
		}
		
		public void notifyDataSetInvalidate() {
			if(mObserver != null) mObserver.onInvalidated();
		}
		
		public abstract int getItemCount();
		public abstract Drawable getDrawable(int position);
		public abstract CharSequence getText(int position);
	}
	
	public void setOnItemCheckedListener(OnItemCheckedListener li) {
		mItemCheckedListener = li;
	}
	
	public interface OnItemCheckedListener {
		void onItemChecked(int position, View view);
	}
}
