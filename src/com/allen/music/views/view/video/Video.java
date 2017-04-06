package com.allen.music.views.view.video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.allen.music.R;
import com.allen.music.views.base.BaseActivity;

public class Video extends BaseActivity {
	/** Called when the activity is first created. */

	private List<Map<String, Object>> list;

	private SimpleAdapter adapter;

	private ListView listview;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentLayout(R.layout.public_lisview_default);

		initTitleView();

		initView();

		initData();
	}

	public class getList extends Thread {

		Handler handle = new Handler() {

			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					String[] str = ((String) msg.obj).split(",");

					HashMap<String, Object> map = null;

					for (int i = 0; i < str.length - 1; i++) {

						String[] str_file = str[i].split("-");

						String name = str_file[0];
						String size = str_file[1];

						map = new HashMap<String, Object>();
						map.put("name", name);
						map.put("size", size);
						list.add(map);
					}
					adapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
			}

		};

		public void run() {
			// TODO Auto-generated method stub
			super.run();
			if (isConnectingToInternet()) {
				String url = "http://192.168.1.101:8080/test1/servlet/Listservlet";

				String data = getContentFromUrl(url);

				if (!data.equals("")) {
					Message msg = new Message();
					msg.obj = data;
					msg.arg1 = 0;
					handle.sendMessage(msg);
				} else {
					handle.sendEmptyMessage(2);
				}
			} else {
				handle.sendEmptyMessage(1);
			}
		}
	}

	public static String getContentFromUrl(String url) {
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpParams httpParams = client.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
			HttpConnectionParams.setSoTimeout(httpParams, 5000);
			HttpResponse response;
			response = client.execute(new HttpPost(url));
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);

				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				reader.close();
			}
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		} else {
			Toast.makeText(this, "请检查联网", 2000).show();
			return false;
		}
	}

	public boolean checkURL(String url) {
		boolean result = false;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setConnectTimeout(30000);
			int code = conn.getResponseCode();
			if (code != 200) {
				result = false;
				Toast.makeText(this, "请检查上传路径", 2000).show();
			} else {
				result = true;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 初始化View
	private void initData() {
		new getList().start();
	}

	// 初始化View
	private void initView() {

		list = new ArrayList<Map<String, Object>>();

		String[] from = { "name", "size" };

		int[] to = { R.id.name, R.id.size };

		adapter = new SimpleAdapter(Video.this, list, R.layout.video_list_item, from, to);
		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Map<String, Object> map = (Map<String, Object>) arg0.getItemAtPosition(arg2);

				Intent intent = new Intent(Video.this, VideoPlayerActivity.class);
				intent.putExtra("path", "http://192.168.1.101:8080/test1/video/" + map.get("name").toString());
				startActivity(intent);
			}
		});

	}

	protected void initTitleView() {
		getbtn_left().setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onKeyDown(KeyEvent.KEYCODE_BACK, null);
			}
		});
	}
}