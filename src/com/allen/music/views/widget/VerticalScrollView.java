package com.allen.music.views.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 能够兼容ViewPager的ScrollView
 * 
 * @Description: 解决了ViewPager在ScrollView中的滑动反弹问题
 */
public class VerticalScrollView extends ScrollView {

	// 滑动距离及坐标
	private float xDistance, yDistance, xLast, yLast;

	// 带浮动条的ScrollView
	private OnScrollListener onScrollListener = null;
	private View viewInScroll, viewOutScroll;

	public VerticalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			if (xDistance > yDistance) {
				return false;
			}
		}

		return super.onInterceptTouchEvent(ev);
	}

	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (onScrollListener != null) {
			onScrollListener.onScrollChanged(this, x, y, oldx, oldy);
		}
		computeFloatIfNecessary();
	}

	/**
	 * 监听ScrollView滚动接口
	 * 
	 * @author reyo
	 * 
	 */
	public interface OnScrollListener {
		public void onScrollChanged(VerticalScrollView scrollView, int x, int y, int oldx, int oldy);
	}

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		// TODO Auto-generated method stub
		this.onScrollListener = onScrollListener;
	}

	/**
	 * 设置需要浮动的View
	 * 
	 * @param viewInScroll
	 *            ScollView内的view
	 * @param viewFloat
	 *            ScollView外的view，真正需要浮动的view
	 */
	public void setFloatView(View viewInScroll, View viewOutScroll) {
		this.viewInScroll = viewInScroll;
		this.viewOutScroll = viewOutScroll;
	}

	private void computeFloatIfNecessary() {
		if (viewInScroll == null && viewOutScroll == null) {
			return;
		}
		// 获取ScrollView的x,y坐标
		int[] location = new int[2];
		this.getLocationInWindow(location);
		// 获取浮动View的x,y坐标
		int[] loc = new int[2];
		viewInScroll.getLocationOnScreen(loc);
		// 当浮动view的y <= ScrollView的y坐标时，把固定的view显示出来
		System.out.println(loc[1] + "---------" + location[1]);
		if (loc[1] <= location[1]) {
			// 处理一下把原有view设置INVISIBLE，这样显示效果会好点
			viewOutScroll.setVisibility(View.VISIBLE);
			viewInScroll.setVisibility(View.INVISIBLE);
		} else {
			// 记得还原回来
			viewOutScroll.setVisibility(View.GONE);
			viewInScroll.setVisibility(View.VISIBLE);
		}
	}

}