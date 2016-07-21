package com.taobao.gulu.tools;

import junit.framework.ComparisonFailure;


/**
 * <p>Title: ComparisonFailureHandle.java</p>
 * <p>Description: comparison failure exception handle</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class ComparisonFailureHandle extends ComparisonFailure{

	private static final long serialVersionUID = 1L;

	public ComparisonFailureHandle(String message, String expected, String actual) {
		super(message, expected, actual);
		// TODO Auto-generated constructor stub
	}
	
	public ComparisonFailureHandle() {
		super(null, null, null);
		// TODO Auto-generated constructor stub
	}

}
