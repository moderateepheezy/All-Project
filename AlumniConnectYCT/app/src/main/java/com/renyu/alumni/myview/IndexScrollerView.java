package com.renyu.alumni.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class IndexScrollerView extends View {

	private float mDensity;
	private float mScaledDensity;
	private float mIndexbarWidth;
	private float mIndexbarMargin;
	private float mPreviewPadding;
	private int mListViewWidth;
	private int mListViewHeight;
	private RectF mIndexbarRect;
	private int mCurrentSection=-1;
	private String[] mSections=null;
	private boolean mIsIndexing = false;
	private Context context=null;
	
	public IndexScrollerView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public IndexScrollerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public IndexScrollerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	private void init(Context context) {
		this.context=context;
		mDensity = context.getResources().getDisplayMetrics().density;
		mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		
		mIndexbarWidth = 20 * mDensity;
		mIndexbarMargin = 10 * mDensity;
		mPreviewPadding = 5 * mDensity;
		
		String sections="#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		mSections = new String[sections.length()];
		for (int i = 0; i < sections.length(); i++) {
			mSections[i] = String.valueOf(sections.charAt(i));
		}
			
		
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		mListViewWidth = w;
		mListViewHeight = h;
		mIndexbarRect = new RectF(w - mIndexbarMargin - mIndexbarWidth, mIndexbarMargin, w - mIndexbarMargin, h - mIndexbarMargin);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// mAlphaRate determines the rate of opacity
		Paint indexbarPaint = new Paint();
		indexbarPaint.setColor(Color.BLACK);
		indexbarPaint.setAntiAlias(true);
		canvas.drawRoundRect(mIndexbarRect, 5 * mDensity, 5 * mDensity, indexbarPaint);
		
		if (mSections != null && mSections.length > 0) {
			// Preview is shown when mCurrentSection is set
			if (mCurrentSection >= 0) {
				Paint previewPaint = new Paint();
				previewPaint.setColor(Color.BLACK);
				previewPaint.setAlpha(96);
				previewPaint.setAntiAlias(true);
				previewPaint.setShadowLayer(3, 0, 0, Color.argb(64, 0, 0, 0));
				
				Paint previewTextPaint = new Paint();
				previewTextPaint.setColor(Color.WHITE);
				previewTextPaint.setAntiAlias(true);
				previewTextPaint.setTextSize(50 * mScaledDensity);
				
				float previewTextWidth = previewTextPaint.measureText(mSections[mCurrentSection]);
				float previewSize = 2 * mPreviewPadding + previewTextPaint.descent() - previewTextPaint.ascent();
				RectF previewRect = new RectF((mListViewWidth - previewSize) / 2
						, (mListViewHeight - previewSize) / 2
						, (mListViewWidth - previewSize) / 2 + previewSize
						, (mListViewHeight - previewSize) / 2 + previewSize);
				
				canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity, previewPaint);
				canvas.drawText(mSections[mCurrentSection], previewRect.left + (previewSize - previewTextWidth) / 2 - 1
						, previewRect.top + mPreviewPadding - previewTextPaint.ascent() + 1, previewTextPaint);
			}
			
			Paint indexPaint = new Paint();
			indexPaint.setColor(Color.WHITE);
			indexPaint.setAntiAlias(true);
			indexPaint.setTextSize(12 * mScaledDensity);
			
			float sectionHeight = (mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length;
			float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;
			for (int i = 0; i < mSections.length; i++) {
				float paddingLeft = (mIndexbarWidth - indexPaint.measureText(mSections[i])) / 2;
				canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft
						, mIndexbarRect.top + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint);
			}
		}		
	}
	
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// If down event occurs inside index bar region, start indexing
			if (contains(ev.getX(), ev.getY())) {				
				// It demonstrates that the motion event started from index bar
				mIsIndexing = true;
				// Determine which section the point is in, and move the list to that section
				mCurrentSection = getSectionByPoint(ev.getY());
				lis.getSection(mCurrentSection);
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mIsIndexing) {
				// If this event moves inside index bar
				if (contains(ev.getX(), ev.getY())) {
					// Determine which section the point is in, and move the list to that section
					mCurrentSection = getSectionByPoint(ev.getY());
					lis.getSection(mCurrentSection);
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mIsIndexing) {
				mIsIndexing = false;
				mCurrentSection = -1;
			}
			break;
		}
		return false;
	}
	
	private boolean contains(float x, float y) {
		// Determine if the point is in index bar region, which includes the right margin of the bar
		return (x >= mIndexbarRect.left && y >= mIndexbarRect.top && y <= mIndexbarRect.top + mIndexbarRect.height());
	}
	
	private int getSectionByPoint(float y) {
		if (mSections == null || mSections.length == 0)
			return 0;
		if (y < mIndexbarRect.top + mIndexbarMargin)
			return 0;
		if (y >= mIndexbarRect.top + mIndexbarRect.height() - mIndexbarMargin)
			return mSections.length - 1;
		return (int) ((y - mIndexbarRect.top - mIndexbarMargin) / ((mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length));
	}
	
	OnSectionListener lis=null;
	
	public void setOnSectionListener(OnSectionListener lis) {
		this.lis=lis;
	}
	
	public interface OnSectionListener {
		public void getSection(int section);
	};

}
