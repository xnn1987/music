package com.allen.music.views.view.activity;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.Keys;
import com.alipay.Rsa;
import com.alipay.android.app.sdk.AliPay;
import com.allen.music.R;
import com.allen.music.http.HttpResStrToAsync;
import com.allen.music.views.base.BaseActivity;
import com.allen.music.views.base.BaseAppException;
import com.allen.music.views.bean.Result;
import com.allen.music.views.view.video.Video_add;

public class Activity_signup_add extends BaseActivity implements OnClickListener {

	public static final String TAG = "alipay-sdk";

	private static final int RQF_PAY = 1;

	Button btnOk1;
	Button btnOk2;

	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentLayout(R.layout.activity_signup_add);

		initTitleView();

		initView();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnOk1) {
			Alipay();
		} else if (v == btnOk2) {
		}
	}

	private void send(final Map<String, Object> param) throws BaseAppException {

		new HttpResStrToAsync() {
			protected void onPreExecute() {// 2
				showDialog();
			}

			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Result ob = (Result) msg.obj;
					if (ob != null) {

					}
				} else if (msg.what == 0) {
					Result ob = (Result) msg.obj;
					ToastMessage(ob.Message);
				} else if (msg.what == -1) {
					((BaseAppException) msg.obj).makeToast(getApplicationContext());
				}
				closeDialog();
			}
		}.execute_Test(param);
	}

	// 初始化view
	private void initView() {
		btnOk1 = (Button) findViewById(R.id.btnOk1);
		btnOk1.setOnClickListener(this);

		btnOk2 = (Button) findViewById(R.id.btnOk2);
		btnOk2.setOnClickListener(this);

		ImageView iv1 = (ImageView) findViewById(R.id.iv1);
		iv1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoActivity(Video_add.class);
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

		setTitle("参赛个人信息");
	}

	@SuppressWarnings("deprecation")
	public void Alipay() {
		// start pay for this order.
		// 根据订单信息开始进行支付
		try {
			String info = getNewOrderInfo();

			String sign = Rsa.sign(info, Keys.PRIVATE);

			sign = URLEncoder.encode(sign);

			info += "&sign=\"" + sign + "\"&" + getSignType();

			Log.i("ExternalPartner", "start pay");

			Log.i(TAG, "info =【 " + info + "】");

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(Activity_signup_add.this, mHandler);

					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);

					Log.i(TAG, "result = " + result);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(Activity_signup_add.this, "远程调用失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * get the selected order info for pay. 获取商品订单信息
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");// 订单名称
		sb.append(getOutTradeNo());
		sb.append("\"&subject=\"音乐云测试支付");
		sb.append("\"&body=\"音乐云测试支付");
		sb.append("\"&total_fee=\"0.00000000000001");// 金额

		// 网址需要做URL编码
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&notify_url=\"");// 支付宝通知使用POST方式
		sb.append(URLEncoder.encode("http://www.mi885.com/paymobile/notify_url.php"));
		sb.append("\"&return_url=\"");// 支付宝通知使用GET方式
		sb.append(URLEncoder.encode("http://m.alipay.com"));

		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);
		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"30m");
		sb.append("\"");

		return new String(sb);
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 * @return
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		Log.d(TAG, "outTradeNo: " + key);
		return key;
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 * @return
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	// 这里接收支付结果，支付宝手机端同步通知
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			com.alipay.Result result = new com.alipay.Result((String) msg.obj);
			switch (msg.what) {
			case RQF_PAY: {
				Toast.makeText(Activity_signup_add.this, result.getResult(), Toast.LENGTH_SHORT).show();

				// 需要调接口就在这里
				// try {
				// Map<String, Object> param = new HashMap<String, Object>();
				//
				// param.put("yhxx_username", user.yhxx_username);
				// param.put("yhxx_money", money);
				//
				// pay(param);
				//
				// } catch (BaseAppException e) {
				// e.makeToast(getApplicationContext());
				// } catch (Exception e) {
				// ToastMessage(getStr(R.string.app_data_code_error));
				// }

				break;
			}
			default:
				break;
			}
		};
	};
}
