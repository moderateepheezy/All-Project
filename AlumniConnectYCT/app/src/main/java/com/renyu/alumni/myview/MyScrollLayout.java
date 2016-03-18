package com.renyu.alumni.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class MyScrollLayout extends ViewGroup {

    private Scroller mScroller;
    private int mCurScreen;    						    
	private int mDefaultScreen = 0;    						 
        
	public MyScrollLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}	
	public MyScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public MyScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub		
		init(context);
	}
	
	private void init(Context context) {
		mCurScreen = mDefaultScreen;    	  
	    mScroller = new Scroller(context); 
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub	
		int childLeft = 0;    
        final int childCount = getChildCount();    	                
        for (int i=0; i<childCount; i++) {    
            final View childView = getChildAt(i);    
            if (childView.getVisibility() != View.GONE) {    
                final int childWidth = childView.getMeasuredWidth();    
                childView.layout(childLeft, 0,     
                        childLeft+childWidth, childView.getMeasuredHeight());    
                childLeft += childWidth;    
            }    
        }   
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);		
		final int width = MeasureSpec.getSize(widthMeasureSpec);       
	    		
		final int count = getChildCount();       
        for (int i = 0; i < count; i++) {       
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);       
        }                
        scrollTo(mCurScreen * width, 0);		
	}

	 public void snapToDestination() {    
	        final int screenWidth = getWidth();    
	        final int destScreen = (getScrollX()+ screenWidth/2)/screenWidth;    
	        snapToScreen(destScreen);    
	 }  
	
	 public void snapToScreen(int whichScreen) {    	
	        // get the valid layout page    
	        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));    
	        if (getScrollX() != (whichScreen*getWidth())) {    	                
	            final int delta = whichScreen*getWidth()-getScrollX();    
  	            mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta));
	            
	            mCurScreen = whichScreen;    
	            invalidate();       // Redraw the layout    	            
	            
	        }    
	    }    

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {    
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
            postInvalidate();    
        }   
	}
	
}