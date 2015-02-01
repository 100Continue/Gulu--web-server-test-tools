package com.taobao.gulu.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;
import com.taobao.gulu.tools.ComparisonFailureHandle;
import com.taobao.gulu.tools.Util;

/**
 * expect body is from file
 * 
 * @author gongyuan.cz
 * 
 */
/**
 * <p>Title: AssertFileBody.java</p>
 * <p>Description: verify response(expect body is from file) info</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class AssertFileBody extends AssertBehavior {

	private static Logger logger = Logger.getLogger(AssertFileBody.class);

	public static void assertBody(Response actualResponse, String expectFilePath, int expectOffset, int expectSize) throws Exception{
		String actualMD5 = Util.getMD5(actualResponse.getResponseBodyAsStream());
		InputStream inputStream = new FileInputStream(new File(expectFilePath));
		String expectMD5 = Util.getMD5(inputStream, expectOffset, expectSize);
		if(actualMD5.equals(expectMD5)){
			logger.info("verify response body SUCCESS");
		}else{
			logger.info("verify response body MD5 FAILED");
			throw new ComparisonFailureHandle("verify response body MD5", actualMD5, expectMD5);
		}
	}
	
	public static void assertBody(Response actualResponse, String expectFilePath, int expectOffset) throws Exception{
		assertBody(actualResponse, expectFilePath, expectOffset, 0);
	}
	
	public static void assertBody(Response actualResponse, String expectFilePath) throws Exception{
		assertBody(actualResponse, expectFilePath, 0, 0);
	}

}
