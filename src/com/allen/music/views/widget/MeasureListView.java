package com.allen.music.views.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MeasureListView extends ListView {

	public MeasureListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MeasureListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MeasureListView(Context context) {
		super(context);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
