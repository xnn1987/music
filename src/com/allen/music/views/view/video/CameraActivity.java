package com.allen.music.views.view.video;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.music.R;

public class CameraActivity extends Activity {
	/** Called when the activity is first created. */

	String actionUrl = "http://192.168.6.133:8080/test1/servlet/Videoservlet";

	TextView tv;
	ProgressBar pb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_main);

		tv = (TextView) findViewById(R.id.text);
		pb = (ProgressBar) findViewById(R.id.progressBar1);

		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("test", "onActivityResult() requestCode:" + requestCode + ",resultCode:" + resultCode + ",data:" + data);
		if (null != data) {
			Uri uri = data.getData();
			if (uri == null) {
				return;
			} else {
				Cursor c = getContentResolver().query(uri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null);
				if (c != null && c.moveToFirst()) {

					String filPath = c.getString(0);

					new Upload(filPath).start();
				}
			}
		} else {
			tv.setText("取消上传。");
			pb.setVisibility(View.GONE);
		}
	}

	public class Upload extends Thread {
		String filpath;

		public Upload(String filpath) {
			super();
			this.filpath = filpath;
		}

		Handler handle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					tv.setText("上传成功。");
					pb.setVisibility(View.GONE);
					break;
				case 1:
					tv.setText("无可用网络。");
					pb.setVisibility(View.GONE);
					break;
				case 2:
					tv.setText("找不到服务器地址");
					pb.setVisibility(View.GONE);
					break;
				default:
					break;
				}
			}

		};

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			if (isConnectingToInternet()) {
				if (checkURL(actionUrl)) {
					uploadFile(filpath);
					handle.sendEmptyMessage(0);
				} else {
					handle.sendEmptyMessage(2);
				}
			} else {
				handle.sendEmptyMessage(1);
			}
		}

	}

	public void uploadFile(String imageFilePath) {
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");

			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			File file = new File(imageFilePath);

			FileInputStream fStream = new FileInputStream(file);
			int bufferSize = 1024 * 6;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			while ((length = fStream.read(buffer)) != -1) {
				ds.write(buffer, 0, length);
			}
			fStream.close();
			ds.flush();

			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			ds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

}