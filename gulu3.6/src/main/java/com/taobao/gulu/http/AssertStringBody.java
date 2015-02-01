package com.taobao.gulu.http;

import org.apache.log4j.Logger;

import com.taobao.gulu.tools.ComparisonFailureHandle;
import com.taobao.gulu.tools.FailedHandle;

/**
 * <p>
 * Title: AssertStringBody.java
 * </p>
 * <p>
 * Description: verify response(expect body is StringText) info
 * </p>
 * 
 * @author: gongyuan.cz
 * @email: gongyuan.cz@taobao.com
 * @blog: 100continue.iteye.com
 */
public class AssertStringBody extends AssertBehavior {
	private static Logger logger = Logger.getLogger(AssertStringBody.class);

	public static void assertBody(Response actualResponse, String expectBody)
			throws Exception {
		String actualResponsebody;
		try {
			actualResponsebody = actualResponse
					.getResponseBodyAsString();
		} catch (FailedHandle e) {
			throw new FailedHandle(
					"actual response body too large or empty, you should use AssertFileBody instead.");
		}
		
		if(expectBody == null){
			logger.error("expect body is null.");
			throw new FailedHandle("expect body is null");
		}
		if(actualResponsebody == null){
			logger.error("actual body is null.");
			throw new FailedHandle("actual body is null");
		}
		
		if (actualResponsebody.contains(expectBody)) {
			logger.info("verify response body SUCCESS");
		} else {
			logger.info("verify response body FAILED");
			throw new ComparisonFailureHandle("verify response body",
					expectBody, actualResponsebody);
		}
	}

	public static void assertBodyEQ(Response actualResponse, String expectBody)
			throws Exception {
		String actualResponsebody = null;
		try {
			actualResponsebody = actualResponse
					.getResponseBodyAsString();
		} catch (FailedHandle e) {
			throw new FailedHandle(
					"actual response body too large or empty, you should use AssertFileBody instead.");
		}
		
		if(expectBody == null){
			logger.error("expect body is null.");
			throw new FailedHandle("expect body is null");
		}
		if(actualResponsebody == null){
			logger.error("actual body is null.");
			throw new FailedHandle("actual body is null");
		}
		
		if (actualResponsebody.equals(expectBody)) {
			logger.info("verify response body SUCCESS");
		} else {
			logger.info("verify response body FAILED");
			throw new ComparisonFailureHandle("verify response body",
					expectBody, actualResponsebody);
		}
	}

	/**
	 * 
	 * <p>
	 * Title: assertResponse
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param actualResponse
	 * @param expectResponse
	 * @param assertArgs
	 *            means define which part to assert, it have "StatusCode" ,
	 *            "StatusLine", "Headers", "Body"
	 * @throws Exception
	 */
	public static void assertResponse(Response actualResponse,
			Response expectResponse, String... assertArgs) throws Exception {
		for (int i = 0; i < assertArgs.length; i++) {
			if (assertArgs[i].toLowerCase().equals("statuscode")) {
				assertStatusCode(actualResponse, expectResponse.getStatusCode());
			}
			if (assertArgs[i].toLowerCase().equals("statusline")) {
				assertStatusLine(actualResponse, expectResponse.getStatusLine());
			}
			if (assertArgs[i].toLowerCase().equals("headers")) {
				assertHeaders(actualResponse, expectResponse.getHeaders());
			}
			if (assertArgs[i].toLowerCase().equals("body")) {
				String expectResponsebody;
				try {
					expectResponsebody = expectResponse
							.getResponseBodyAsString();
				} catch (FailedHandle e) {
					throw new FailedHandle(
							"expect response body too large or empty, you should use assertFileBody instead.");
				}
				
				assertBody(actualResponse, expectResponsebody);
			}
		}
	}

}
