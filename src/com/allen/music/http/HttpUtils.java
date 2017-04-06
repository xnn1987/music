package com.allen.music.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.allen.music.AppUrls;
import com.allen.music.views.base.BaseAppException;

public class HttpUtils {

	/**
	 * 获取网址内容
	 * 
	 * @param url
	 * @return String
	 * @throws Exception
	 */
	public static String getContentFromUrl(String url) {
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpParams httpParams = client.getParams();
			// 设置网络超时参数
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
			HttpConnectionParams.setSoTimeout(httpParams, 5000);
			HttpResponse response;
			response = client.execute(new HttpGet(url));
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

	/**
	 * 获取图片文件
	 * 
	 * @param url
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream getStreamFromURL(String imageURL) {
		final InputStream in;
		HttpClient hc = new DefaultHttpClient();
		HttpGet hg = new HttpGet(imageURL);
		try {
			HttpResponse hr = hc.execute(hg);
			in = hr.getEntity().getContent();
			return in;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取wsdl
	 * 
	 * @param url
	 * @return InputStream
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public static String getWsdlFromURL(String methodName, Map<String, Object> requestMap) throws BaseAppException {
		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(AppUrls.nameSpace, methodName);

		// 设置需调用WebService接口需要传入的参数
		Set set = requestMap.entrySet();
		Iterator it = requestMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String key = entry.getKey().toString();// 返回与此项对应的键
			Object value = entry.getValue();// 返回与此项对应的值
			rpc.addProperty(key, value);
			System.out.println(key + ":----" + value);
		}

		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		HttpTransportSE ht = new HttpTransportSE(AppUrls.endPoint);
		ht.debug = true;
		try {
			ht.call(AppUrls.nameSpace + methodName, envelope);

			Object object = (Object) envelope.getResponse();

			// 获取返回的结果
			String result = object.toString();

			System.out.println(result);

			return result;

		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.io(e);
		}
	}

	public static String getRequestData(String action, Object params) throws BaseAppException {
		try {
			@SuppressWarnings("unchecked")
			String strResult = HttpUtils.getWsdlFromURL(action, (Map<String, Object>) params);
			// String strResult = HttpUtils.getWsdlFromURL2();
			return strResult;
		} catch (Exception e) {
			if (e instanceof BaseAppException)
				throw (BaseAppException) e;
			throw BaseAppException.run(e);
		}
	}

	// 第三方测试
	public static String getWsdlFromURL1() {
		// 命名空间
		String nameSpace = "http://WebXml.com.cn/";
		// 调用的方法名称
		String methodName = "getMobileCodeInfo";
		// EndPoint
		String endPoint = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx";
		// SOAP Action
		String soapAction = "http://WebXml.com.cn/getMobileCodeInfo";

		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
		rpc.addProperty("mobileCode", "15972928869");
		rpc.addProperty("userId", "");

		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		try {
			// 调用WebService
			transport.call(soapAction, envelope);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 获取返回的数据
		SoapObject object = (SoapObject) envelope.bodyIn;
		// 获取返回的结果
		String result = object.getProperty(0).toString();

		// 将WebService返回的结果显示在TextView中
		return result;
	}

	// 音乐云测试
	public static String getWsdlFromURL2() {
		try {
			String tablename = "kf_member";
			String field = "[\"username\",\"password\"]";
			String val = "[\"admin\",\"123456\"]";
			String pagenum = "1";
			String pagesize = "10";

			SoapObject rpc = new SoapObject("http://musicplay.kmusick.com", "select_kmusick");
			rpc.addProperty("tablename", tablename);
			rpc.addProperty("field", field);
			rpc.addProperty("val", val);
			rpc.addProperty("pagenum", pagenum);
			rpc.addProperty("pagesize", pagesize);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			HttpTransportSE ht = new HttpTransportSE("http://musicplay.kmusick.com/server.php");
			ht.debug = true;
			ht.call("http://musicplay.kmusick.com/select_kmusick", envelope);

			Object ob = (Object) envelope.getResponse();

			return ob.toString();

		} catch (HttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
