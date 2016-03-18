package org.simpumind.com.comp_scihandbook;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by simpumind on 11/12/15.
 */
public class ViewPagerAdapter extends PagerAdapter {

    Activity activity;
    String fileArray[];

    public ViewPagerAdapter(Activity act, String[] fileArray) {
        this.fileArray = fileArray;
        activity = act;
    }

    public int getCount() {
        return this.fileArray.length;
    }

    public Object instantiateItem(View collection, int position) {
        TextView view = new TextView(activity);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        view.setText(this.fileArray[position]);
        //view.
        ((ViewPager) collection).addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
