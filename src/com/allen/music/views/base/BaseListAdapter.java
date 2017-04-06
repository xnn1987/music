package com.allen.music.views.base;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.allen.music.AppUrls;
import com.allen.music.tools.BitmapManager;

public class BaseListAdapter extends BaseAdapter {
	// 数据源
	public List<Object> listItems;
	// 视图容器
	public LayoutInflater listItemsContainer;
	// 自定义项视图源
	public int itemViewResource;

	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int position) {
		return listItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------------------
	// 刷新
	public void refresh(List<Object> new_list) {
		listItems = new_list;
		notifyDataSetChanged();
	}

	// 追加
	public void addItem(Map<String, Object> map) {
		listItems.add(map);
		notifyDataSetChanged();
	}

	// 删除
	public void removeItem(int position) {
		listItems.remove(position);
		notifyDataSetChanged();
	}

	// ------------------------------------------------------------------------------------

	// 加载指定图片
	public void loadBitmap(BitmapManager bmpManager, String filename, ImageView image) {
		String url = AppUrls.service + "/" + filename;
		bmpManager.loadBitmap(url, image);
	}

	public void loadBitmap(BitmapManager bmpManager, String filename, ImageView image, Bitmap defaultBmp) {
		String url = AppUrls.service + "/" + filename;
		bmpManager.loadBitmap(url, image, defaultBmp);
	}

	// 加载指定宽高图片
	public void loadBitmap(BitmapManager bmpManager, String filename, ImageView image, int width, int height) {
		String url = AppUrls.service + "/" + filename;
		bmpManager.loadBitmap(url, image, null, width, height);
	}

	public void loadBitmap(BitmapManager bmpManager, String filename, ImageView image, Bitmap defaultBmp, int width, int height) {
		String url = AppUrls.service + "/" + filename;
		bmpManager.loadBitmap(url, image, defaultBmp, width, height);
	}
}
