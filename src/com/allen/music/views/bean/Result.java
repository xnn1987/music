package com.allen.music.views.bean;

import com.allen.music.views.base.BaseAppException;
import com.allen.music.views.base.BaseBean;

/**
 * 数据操作结果实体类
 * 
 * @version 1.0
 * @created 2012-3-21
 */
public class Result extends BaseBean {

	private static final long serialVersionUID = 1L;

	// noTable：返回值为noTable时，表示表不存在或不可用
	// err： 返回值为err时，表示错误
	// no： 返回值为no时，表示数据位空
	// ok： 返回值为ok时，表示操作成功
	// json： 返回值为jso格式的字符串时，为查询到的数据

	public int Code;

	public String Message;

	public String Result;

	public boolean OK() {
		return Code == 1;
	}

	public static Result parse(String responseData) throws BaseAppException {
		Result result = new Result();

		if (responseData.equals("noTable")) {

			result.Code = 0;
			result.Message = "表不存在或不可用";
			result.Result = "";

		} else if (responseData.equals("err")) {

			result.Code = 0;
			result.Message = "错误";
			result.Result = "";

		} else if (responseData.equals("no")) {

			result.Code = 0;
			result.Message = "数据位空";
			result.Result = "";

		} else if (responseData.equals("ok")) {

			result.Code = 1;
			result.Message = "操作成功";
			result.Result = "";

		} else {

			result.Code = 1;
			result.Message = "返回成功";
			result.Result = responseData;
		}

		return result;
	}
}
