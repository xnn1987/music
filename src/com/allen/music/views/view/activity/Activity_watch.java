package com.allen.music.views.view.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.allen.music.AppConfig;
import com.allen.music.R;
import com.allen.music.http.HttpResStrToAsync;
import com.allen.music.tools.JsonUtils;
import com.allen.music.tools.StringUtils;
import com.allen.music.views.adapter.ListAdapterBy_Activity_watch;
import com.allen.music.views.base.BaseActivity;
import com.allen.music.views.base.BaseAppException;
import com.allen.music.views.bean.Result;
import com.allen.music.views.model.Bean_watch;
import com.allen.music.views.view.video.Video_player;

public class Activity_watch extends BaseActivity {

	private ListView listview;
	private ListAdapterBy_Activity_watch adapter;
	private List<Object> dataList;

	// 页号
	private int pageNo = AppConfig.PAGE_No;
	// 每页条数
	private int pageSize = AppConfig.PAGE_Size;
	// 是否有下一页
	private int pageNext = AppConfig.PAGE_Next;

	private View HeadView;

	// 加载更多View
	private View loadMoreView;
	// 加载更多View里的button
	private Button loadMoreButton;
	// 加载更多线程
	private Handler handler = new Handler();

	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentLayout(R.layout.activity_watch);

		initTitleView();

		initHeadView();

		initFoodView();

		initView();

		initData();
	}

	// 列表
	private void list(final Map<String, Object> param) throws BaseAppException {

		new HttpResStrToAsync() {
			protected void onPreExecute() {
				showDialog();
			}

			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Result ob = (Result) msg.obj;
					if (ob != null) {
						// try {
						// 数据组装
						// Map<String, Object> all_list =
						// JsonUtils.getJsonToMap(ob.Result);
						//
						// getListData(all_list);
						//
						// } catch (BaseAppException e) {
						// e.makeToast(getApplicationContext());
						// } catch (Exception e) {
						// ToastMessage(getStr(R.string.app_data_code_error));
						// }
					}
				} else if (msg.what == 0) {
					Result ob = (Result) msg.obj;
					ToastMessage(ob.Message);

				} else if (msg.what == -1) {
					((BaseAppException) msg.obj).makeToast(getApplicationContext());
				}
				
				getData();

				closeDialog();
			}
		}.execute_Test(param);
	}

	// 组装
	private void getListData(Map<String, Object> all_list) throws BaseAppException {

		List<Object> temp_list = null;

		if (!StringUtils.isEmpty(all_list.get("list").toString())) {

			temp_list = new ArrayList<Object>();

			List<Map<String, Object>> data = JsonUtils.getJsonToList(all_list.get("list").toString());

			for (Map<String, Object> map : data) {

				// Bean_music_meet bean = new Bean_music_meet();

				// temp_list.add(bean);
			}
		}

		// 追加进adapter
		for (Object object : temp_list) {
			dataList.add(object);
		}

		// 判断是否有下一页
		pageNext = (Integer) all_list.get("next");
		if (pageNext == 1) {
			loadMoreButton.setText("加载更多");
		} else {
			loadMoreButton.setText("数据已加载完毕");
		}

		// 刷新适配器数据
		adapter.refresh(dataList);
	}

	// 点击
	private AdapterView.OnItemClickListener listener1 = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// 要发送给服务器的grxx_id带 过去
			// Bean_User_yhq bean = (Bean_User_yhq)
			// arg0.getItemAtPosition(arg2);
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("data", bean);
			// // 跳转到优惠券详细信息
			// gotoActivity(User_yhq_info.class, bundle);

			// String path =
			// "http://192.168.6.133:8080/test1/video/video_0.mp4";

			//String path = "file:///android_asset/gravity.flv";
			String path = "http://f.youku.com/player/getFlvPath/sid/138455043935120_01/st/flv/fileid/030080010051E94F8926F1010DA3739A89EC40-E7CA-7D08-9C6D-725BBF48DD4C?K=eb6f5cd9d976799d261d57aa&hd=3&ts=90";
			Intent intent = new Intent(Activity_watch.this, Video_player.class);
			intent.putExtra("path", path);
			startActivity(intent);

		}
	};

	// 初始化数据
	private void initData() {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("rows", pageSize);
			param.put("page", pageNo);

			list(param);

		} catch (BaseAppException e) {
			e.makeToast(getApplicationContext());
		} catch (Exception e) {
			ToastMessage(getStr(R.string.app_data_code_error));
		}

	}

	// 初始化view
	private void initView() {
		dataList = new ArrayList<Object>();
		adapter = new ListAdapterBy_Activity_watch(this, dataList, R.layout.activity_watch_item);

		listview = (ListView) findViewById(R.id.listview);
		listview.addHeaderView(HeadView);
		listview.addFooterView(loadMoreView);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(listener1);

		Button signup = (Button) findViewById(R.id.signup);
		signup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoActivity(Activity_signup_add.class);
			}
		});
	}

	// 初始化HeadView
	private void initHeadView() {
		HeadView = getLayoutInflater().inflate(R.layout.activity_watch_head, null);
		Button tab_btn_type_1 = (Button) HeadView.findViewById(R.id.tab_btn_type_1);
		tab_btn_type_1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});

		Button tab_btn_type_2 = (Button) HeadView.findViewById(R.id.tab_btn_type_2);
		tab_btn_type_2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});
	}

	// 初始化FootView
	private void initFoodView() {
		loadMoreView = getLayoutInflater().inflate(R.layout.public_lisview_loading_foot, null);
		loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
		loadMoreButton.setText("暂无数据"); // 恢复按钮文字
		loadMoreButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (pageNext == 1) {
					handler.postDelayed(new Runnable() {
						public void run() {
							pageNo += 1;
							pageSize = AppConfig.PAGE_Size;

							initData();

							loadMoreButton.setText("正在加载中..."); // 设置按钮文字

						}
					}, 200);
				} else {
					loadMoreButton.setText("数据已加载完毕"); // 恢复按钮文字
				}
			}
		});
	}

	// 初始化title
	private void initTitleView() {

		getbtn_left().setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onKeyDown(KeyEvent.KEYCODE_BACK, null);
			}
		});

		setTitle("钢琴之星");
	}

	// 初始化title
	private void getData() {

		dataList = new ArrayList<Object>();

		Bean_watch w1 = new Bean_watch();
		w1.usermusic = "You are beatiful";
		w1.username = "伊斯坦布尔";
		w1.teacher = "推荐老师：徐勤";
		w1.num = "5";

		Bean_watch w2 = new Bean_watch();
		w2.usermusic = "G大调的变奏";
		w2.username = "Jay";
		w2.teacher = "推荐老师：Coco";
		w2.num = "5";

		Bean_watch w3 = new Bean_watch();
		w3.usermusic = "你怎么舍得我难过";
		w3.username = "莫斯科";
		w3.teacher = "推荐老师：汪峰";
		w3.num = "5";

		Bean_watch w4 = new Bean_watch();
		w4.usermusic = "Tanana2";
		w4.username = "布尔";
		w4.teacher = "推荐老师：风之声";
		w4.num = "5";

		Bean_watch w5 = new Bean_watch();
		w5.usermusic = "我的爱";
		w5.username = "梦想家";
		w5.teacher = "推荐老师：韩红";
		w5.num = "5";

		dataList.add(w1);
		dataList.add(w2);
		dataList.add(w3);
		dataList.add(w4);
		dataList.add(w5);

		dataList.add(w3);
		dataList.add(w4);
		dataList.add(w1);
		dataList.add(w2);
		dataList.add(w5);

		adapter.refresh(dataList);
	}
}
