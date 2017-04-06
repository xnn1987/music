package com.allen.music.http;

import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Message;

import com.allen.music.tools.JsonUtils;
import com.allen.music.views.base.BaseAppException;
import com.allen.music.views.bean.Result;

public class HttpResStrToAsync extends AsyncTask<Object, Object, Message> {

	// noTable：返回值为noTable时，表示表不存在或不可用
	// err： 返回值为err时，表示错误
	// no： 返回值为no时，表示数据位空
	// json： 返回值为jso格式的字符串时，为查询到的数据

	// 后台运行
	protected Message doInBackground(Object... parameters) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		try {
			Object action = parameters[0];
			Object params = parameters[1];

			String strResult = HttpUtils.getRequestData(action.toString(), params);

			Result result = Result.parse(strResult);

			if (result.OK()) {
				msg.what = 1;// 成功
				msg.obj = result;
			} else {
				msg.what = 0;// 失败
				msg.obj = result;
			}
		} catch (BaseAppException e) {
			e.printStackTrace();
			msg.what = -1;
			msg.obj = e;
			// 这里的异常包括传传参、拼接、联网、返回值所有的异常都汇集到这里，告诉UI提示不同的消息
		}
		return msg;
	}

	// 执行完毕（整理结果告诉handleMessage）
	protected void onPostExecute(Message msg) {
		// 再这个理也可以不经过handleMessage,在activity中重写onPostExecute方法
		handleMessage(msg);

		super.onPostExecute(msg);
	}

	// 改变UI
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		// 1.handleMessage(msg);
		// 2.public void handleMessage(Message msg) {
	}

	// --------------------------------------------------------------------------
	// 测试
	public void execute_Test(Map<String, Object> params) throws BaseAppException {
		try {
			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("mobileCode", "15972928869");
			param1.put("userID", "");
			execute(ActionConfig.methodName_test, param1);
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.run(e);
		}
	}

	public void execute_select(Map<String, Object> params) throws BaseAppException {
		try {
			String field = JsonUtils.getStringToArrayJson(params.get("field").toString(), "-").toString();
			String val = JsonUtils.getStringToArrayJson(params.get("val").toString(), "-").toString();

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("tablename", params.get("tablename"));
			param.put("field", field);
			param.put("val", val);
			param.put("pagenum", params.get("pagenum"));
			param.put("pagesize", params.get("pagesize"));
			execute(ActionConfig.methodName_select, param);
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.run(e);
		}
	}

	public void execute_add(Map<String, Object> params) throws BaseAppException {
		try {
			String field = JsonUtils.getStringToArrayJson(params.get("field").toString(), "-").toString();
			String val = JsonUtils.getStringToArrayJson(params.get("val").toString(), "-").toString();

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("tablename", params.get("tablename"));
			param.put("field", field);
			param.put("val", val);
			execute(ActionConfig.methodName_add, param);
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.run(e);
		}
	}

	public void execute_upd(Map<String, Object> params) throws BaseAppException {
		try {
			String field = JsonUtils.getStringToArrayJson(params.get("field").toString(), "-").toString();
			String val = JsonUtils.getStringToArrayJson(params.get("val").toString(), "-").toString();

			String uval = JsonUtils.getStringToArrayJson(params.get("uval").toString(), "-").toString();

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("tablename", params.get("tablename"));
			param.put("field", field);
			param.put("val", val);

			param.put("ufield", field);
			param.put("uval", uval);

			execute(ActionConfig.methodName_upd, param);
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.run(e);
		}
	}

	public void execute_del(Map<String, Object> params) throws BaseAppException {
		try {
			String field = JsonUtils.getStringToArrayJson(params.get("field").toString(), "-").toString();
			String val = JsonUtils.getStringToArrayJson(params.get("val").toString(), "-").toString();

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("tablename", params.get("tablename"));
			param.put("field", field);
			param.put("val", val);
			execute(ActionConfig.methodName_del, param);
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.run(e);
		}
	}
}
