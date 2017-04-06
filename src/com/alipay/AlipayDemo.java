package com.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.allen.music.R;

public class AlipayDemo extends Activity implements OnClickListener {
	public static final String TAG = "alipay-sdk";

	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;

	private EditText mUserId;

	private Button mLogon;

	private Button mPay;

	private Button mPay2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alipay_test);

		mUserId = (EditText) findViewById(R.id.user_id);
		mLogon = (Button) findViewById(R.id.get_token);
		mLogon.setOnClickListener(this);

		mPay = (Button) findViewById(R.id.get_pay);
		mPay.setOnClickListener(this);

		mPay2 = (Button) findViewById(R.id.get_pay2);
		mPay2.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v == mLogon) {
			doLogin();
		} else if (v == mPay) {
			Alipay();
		} else if (v == mPay2) {
			// Intent intent = new Intent(getApplicationContext(),
			// ExternalPartner.class);
			// startActivity(intent);
		}
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
					AliPay alipay = new AliPay(AlipayDemo.this, mHandler);

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
			Toast.makeText(AlipayDemo.this, "远程调用失败", Toast.LENGTH_SHORT).show();
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
		sb.append("\"&subject=\"测试支付(VIP包月)");
		sb.append("\"&body=\"5592"); // 用户ID
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

	// 快速登录
	private void doLogin() {
		final String userInfo = getUserInfo();
		new Thread() {
			public void run() {
				String result = new AliPay(AlipayDemo.this, mHandler).pay(userInfo);

				Log.i(TAG, "result = " + result);
				Message msg = new Message();
				msg.what = RQF_LOGIN;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	// 获取用户信息
	private String getUserInfo() {
		String userId = mUserId.getText().toString();
		return trustLogin(Keys.DEFAULT_PARTNER, userId);

	}

	// 用户签权
	private String trustLogin(String partnerId, String appUserId) {
		StringBuilder sb = new StringBuilder();
		sb.append("app_name=\"mc\"&biz_type=\"trust_login\"&partner=\"");
		sb.append(partnerId);
		Log.d("TAG", "UserID = " + appUserId);
		if (!TextUtils.isEmpty(appUserId)) {
			appUserId = appUserId.replace("\"", "");
			sb.append("\"&app_id=\"");
			sb.append(appUserId);
		}
		sb.append("\"");

		String info = sb.toString();

		// 请求信息签名
		String sign = Rsa.sign(info, Keys.PRIVATE);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		info += "&sign=\"" + sign + "\"&" + getSignType();

		return info;
	}

	// 这里接收支付结果，支付宝手机端同步通知
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);
			switch (msg.what) {
			case RQF_PAY:
				Toast.makeText(AlipayDemo.this, result.getResult(), Toast.LENGTH_SHORT).show();
				break;
			case RQF_LOGIN: {
				Toast.makeText(AlipayDemo.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};
}