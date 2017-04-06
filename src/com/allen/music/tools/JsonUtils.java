/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.allen.music.tools;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.allen.music.views.base.BaseAppException;

/**
 * json工具类
 * 
 */
public class JsonUtils {

	/**
	 * 流转字符串方法
	 * 
	 * @param is
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String convertStreamToString(InputStream is) throws BaseAppException, UnsupportedEncodingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf8"), 10 * 1024);
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.io(e);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				if (e instanceof BaseAppException)
					throw (BaseAppException) e;
				throw BaseAppException.io(e);
			}
		}
		return sb.toString();
	}

	/**
	 * 流转字节数组方法
	 * 
	 * @param is
	 * @return
	 */
	public static byte[] convertStreamToByte(InputStream is) throws BaseAppException {
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = 0;
		try {
			while ((len = is.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.io(e);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				if (e instanceof BaseAppException)
					throw (BaseAppException) e;
				throw BaseAppException.io(e);
			}
		}
		return bos.toByteArray();
	}

	/**
	 * 字符串转流
	 * 
	 * @param is
	 * @return
	 */
	public static InputStream convertStringStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	// ------------------------------------------------------------
	/**
	 * 把json 转换为ArrayList 形式
	 * 
	 * @param jsonString
	 * @return ArrayList
	 */
	public static List<Map<String, Object>> getJsonToList(String jsonString) throws BaseAppException {
		List<Map<String, Object>> list = null;
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			JSONObject jsonObject;
			list = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				list.add(getJsonToMap(jsonObject.toString()));
			}
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.json(e);
		}
		return list;
	}

	/**
	 * 将json 数组转换为Map 对象
	 * 
	 * @param jsonString
	 * @return map
	 */
	public static Map<String, Object> getJsonToMap(String jsonString) throws BaseAppException {
		JSONObject json;
		Map<String, Object> valueMap = null;
		try {
			json = new JSONObject(jsonString);
			@SuppressWarnings("unchecked")
			Iterator<String> keyIter = json.keys();
			String key;
			Object value;
			valueMap = new HashMap<String, Object>();
			while (keyIter.hasNext()) {
				key = keyIter.next();
				value = json.get(key);
				valueMap.put(key, value);
			}
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.json(e);
		}
		return valueMap;
	}

	// ------------------------------------------------------------
	/**
	 * 字符串转json对象
	 * 
	 * @param str
	 * @param split
	 * @return json
	 */
	public static JSONObject getStringToJson(String str, String split) throws BaseAppException {
		JSONObject json;
		try {
			json = new JSONObject();
			String[] arrStr = str.split(split);
			for (int i = 0; i < arrStr.length; i++) {
				String[] arrKeyValue = arrStr[i].split("=");
				json.put(arrKeyValue[0], arrStr[i].substring(arrKeyValue[0].length() + 1));
			}
			return json;
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.json(e);
		}
	}

	/**
	 * 字符串转JSONArray对象
	 * 
	 * @param str
	 * @param split
	 * @return json
	 */
	public static JSONArray getStringToArrayJson(String str, String split) throws BaseAppException {
		JSONArray json;
		try {
			json = new JSONArray();
			String[] arrStr = str.split(split);
			for (int i = 0; i < arrStr.length; i++) {
				json.put(arrStr[i]);
			}
			return json;
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.json(e);
		}
	}

}