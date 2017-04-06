package com.allen.music.views.base;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class BasePagerAdapter extends PagerAdapter {
	
	List<View> list = new ArrayList<View>();

	public BasePagerAdapter(ArrayList<View> list) {
		this.list = list;
	}

	public void destroyItem(ViewGroup container, int position, Object object) {
		ViewPager pViewPager = ((ViewPager) container);
		pViewPager.removeView(list.get(position));
	}

	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	public int getCount() {
		return list.size();
	}

	public Object instantiateItem(View arg0, int arg1) {
		ViewPager pViewPager = ((ViewPager) arg0);
		pViewPager.addView(list.get(arg1));
		return list.get(arg1);
	}

	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	public Parcelable saveState() {
		return null;
	}

	public void startUpdate(View arg0) {
	}
}