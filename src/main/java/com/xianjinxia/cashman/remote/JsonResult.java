package com.xianjinxia.cashman.remote;

public class JsonResult<T> {

	public static final String SUCCESS = "0";
	public static final String FAILURE = "-1";
    public static final String USER_DETAIL_SUCCESS_CODE = "00";//获取用户detail返回成功标识
    public static final String USER_DETAIL_NOT_FIND_CODE = "05";//用户不存在

	private String code;
	private String msg;
	private T data;

	public JsonResult() {
	}

	public JsonResult(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public JsonResult(String code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
