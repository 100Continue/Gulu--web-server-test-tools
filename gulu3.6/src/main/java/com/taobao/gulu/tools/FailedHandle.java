package com.taobao.gulu.tools;

import junit.framework.AssertionFailedError;

/**
 * <p>Title: FailedHandle.java</p>
 * <p>Description: failed exception handle</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class FailedHandle extends AssertionFailedError {

	private static final long serialVersionUID = 1L;

	public FailedHandle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FailedHandle(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
