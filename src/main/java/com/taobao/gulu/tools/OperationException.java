package com.taobao.gulu.tools;


public class OperationException extends Exception
{
	private static final long serialVersionUID = 1123415143L;
	
	private Exception innerException;

	public OperationException()
	{
		super();
	}
	
	public OperationException(String msg)
	{
		super(msg);
	}
	
	public OperationException(Exception e)
	{
		super();
		this.innerException = e;
	}
	
	public OperationException(Exception e, String message)
	{
		super(message);
		this.innerException = e;
	}

	public Exception getInnerException()
	{
		return innerException;
	}

	public void setInnerException(Exception innerException)
	{
		this.innerException = innerException;
	}
}
