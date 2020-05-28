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
	private Integer code; // 0表示正常

	private Boolean success;

	private String msg; // 要传给前端的数据

	private T data; // 要传给前端的数据

	private CommonResult()
	{
	}

	private CommonResult(Integer code, Boolean success, String msg, T data)
	{
		this.code = code;
		this.success = success;
		this.msg = msg;
		this.data = data;
	}

	public static <T> CommonResult<T> success()
	{
		return new CommonResult<T>(0,true,null,null);
	}

	public static <T> CommonResult<T> successMsg(String msg)
	{
		return new CommonResult<T>(0,true, msg, null);
	}

	public static <T> CommonResult<T> successData(T data)
	{
		return new CommonResult<T>(0,true,null,data);
	}

	public static <T> CommonResult<T> successMsgData(String msg, T data)
	{
		return new CommonResult<T>(0,true,msg,data);
	}

	public static <T> CommonResult<T> errorMsg(String msg)
	{
		return new CommonResult<T>(1,false, msg, null);
	}
}