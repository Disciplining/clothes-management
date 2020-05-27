package com.lyx.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 返回数据封装
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResult<T>
{
	private Boolean success;

	private String msg; // 要传给前端的数据

	private T data; // 要传给前端的数据

	private CommonResult()
	{
	}

	private CommonResult(Boolean success, String msg, T data)
	{
		this.success = success;
		this.msg = msg;
		this.data = data;
	}

	public static <T> CommonResult<T> success()
	{
		return new CommonResult<T>(true,null,null);
	}

	public static <T> CommonResult<T> successMsg(String msg)
	{
		return new CommonResult<T>(true, msg, null);
	}

	public static <T> CommonResult<T> successData(T data)
	{
		return new CommonResult<T>(true,null,data);
	}

	public static <T> CommonResult<T> successMsgData(String msg, T data)
	{
		return new CommonResult<T>(true,msg,data);
	}

	public static <T> CommonResult<T> errorMsg(String msg)
	{
		return new CommonResult<T>(false, msg, null);
	}
}