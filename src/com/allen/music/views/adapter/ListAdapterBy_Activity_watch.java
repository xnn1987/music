package com.allen.music.views.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.music.R;
import com.allen.music.views.base.BaseListAdapter;
import com.allen.music.views.model.Bean_watch;

public class ListAdapterBy_Activity_watch extends BaseListAdapter {

	private Context context;

	//private BitmapManager bmpManager;

	public ListAdapterBy_Activity_watch(Context ct, List<Object> data, int resource) {
		this.context = ct;
		this.listItemsContainer = LayoutInflater.from(context);
		this.itemViewResource = resource;
		this.listItems = data;
		//this.bmpManager = new BitmapManager(ImageUtils.getBitmap(context, R.drawable.user_icon_bg));
	}

	private class ListItemView {
		ImageView iv_image;
		TextView tv_usermusic;
		TextView tv_username;
		TextView tv_teacher;
		TextView tv_num;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ListItemView listItemView = null;
		if (convertView == null) {
			convertView = listItemsContainer.inflate(itemViewResource, null);

			listItemView = new ListItemView();
			listItemView.iv_image = (ImageView) convertView.findViewById(R.id.image);
			listItemView.tv_usermusic = (TextView) convertView.findViewById(R.id.t1);
			listItemView.tv_username = (TextView) convertView.findViewById(R.id.t2);
			listItemView.tv_teacher = (TextView) convertView.findViewById(R.id.t3);
			listItemView.tv_num = (TextView) convertView.findViewById(R.id.t4);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		Bean_watch bean = (Bean_watch) listItems.get(position);

		listItemView.tv_usermusic.setText(bean.usermusic);
		listItemView.tv_username.setText(bean.username);
		listItemView.tv_teacher.setText(bean.teacher);
		listItemView.tv_num.setText(bean.num);

		//loadBitmap(bmpManager, "", listItemView.iv_image);

		return convertView;
	}
}
