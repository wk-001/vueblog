package com.wk.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Result<T> implements Serializable {

	/**
	 * 响应状态码 200正常 非200异常
	 */
	private Integer code;

	/**
	 * 提示消息
	 */
	private String msg;

	/**
	 * 响应数据
	 */
	private T data;

	public static<T> Result<T> ok() {
		return ok(200, "操作成功", null);
	}

	public static<T> Result<T> ok(T data) {
		return ok(200, "操作成功", data);
	}

	public static<T> Result<T> ok(int code, String msg, T data) {
		Result<T> r = new Result<T>();
		r.setCode(code);
		r.setMsg(msg);
		r.setData(data);
		return r;
	}

	public static<T> Result<T> fail(String msg) {
		return fail(400, msg, null);
	}

	public static<T> Result<T> fail(String msg, T data) {
		return fail(400, msg, data);
	}

	public static<T> Result<T> fail(int code, String msg, T data) {
		Result<T> r = new Result<T>();
		r.setCode(code);
		r.setMsg(msg);
		r.setData(data);
		return r;
	}
}
