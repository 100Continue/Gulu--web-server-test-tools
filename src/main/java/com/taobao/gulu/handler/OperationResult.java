package com.taobao.gulu.handler;

/**
 * <p>Title: OperationResult.java</p>
 * <p>Description: store the implement result</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class OperationResult 
{
	private boolean success = false;
	private String  msg = "";
	private int returnCode;

	
	public OperationResult(){}
	
	public OperationResult(String msg, boolean success)
	{
		this.success = success;
		this.msg = msg;
	}
	
	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public int getReturnCode()
	{
		return returnCode;
	}

	public void setReturnCode(int returnCode)
	{
		this.returnCode = returnCode;
	}
	
	public String toString()
	{
		return "The operation result is: " + success + " and the detail message is: " + msg;
	}
}
