package com.taobao.gulu.http;

import java.util.ArrayList;

import org.apache.commons.httpclient.Header;
import org.apache.log4j.Logger;

import com.taobao.gulu.tools.ComparisonFailureHandle;

/**
 * <p>
 * Title: AssertBehaviour.java
 * </p>
 * <p>
 * Description: provide the verify behavior
 * </p>
 * 
 * @author: gongyuan.cz
 * @email: gongyuan.cz@taobao.com
 * @blog: 100continue.iteye.com
 */
public class AssertBehavior {
	private static Logger logger = Logger.getLogger(AssertBehavior.class);

	// // ------------------------ MD5 ------------------------------
	// private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
	// '7',
	// '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	//
	// // ------------------------ MD5 ------------------------------

	/**
	 * verify response status code
	 */
	public static void assertStatusCode(Response actualResponse,
			int expectStatusCode) {
		if (actualResponse.getStatusCode() == expectStatusCode) {
			logger.info("verify status code SUCCESS");
		} else {
			logger.info("verify status code FAILED");
			throw new ComparisonFailureHandle("verify status code",
					Integer.toString(expectStatusCode),
					Integer.toString(actualResponse.getStatusCode()));
		}
	}

	/**
	 * verify response status line
	 */
	public static void assertStatusLine(Response actualResponse,
			String expectStatusLine) {
		if (actualResponse.getStatusLine().equals(expectStatusLine)) {
			logger.info("verify status line SUCCESS");
		} else {
			logger.info("verify status line FAILED");
			throw new ComparisonFailureHandle("verify status line",
					expectStatusLine, actualResponse.getStatusLine());
		}
	}

	/**
	 * verify response headers
	 * 
	 * @param expectHeadersStr
	 *            pattern:
	 *            "a:b$c:d$Set-Cookie:name=value;domain=10.232.4.30:8088;Expires=Thu,01-Jan-1970 00:00:10 GMT; Path=/ cookie:tt=xx;cc=pp @"
	 *            means headers: a:b c:d
	 *            Set-Cookie:name=value;domain=10.232.4.30
	 *            :8088;Expires=Thu,01-Jan-1970 00:00:10 GMT; Path=/
	 *            cookie:tt=xx;cc=pp @
	 * 
	 */
	public static void assertHeaders(Response actualResponse,
			String expectHeadersStr) {
		String keyValuePairArray[] = expectHeadersStr.split("\\$");

		for (int i = 0; i < keyValuePairArray.length; i++) {
			String keyValuePair[] = keyValuePairArray[i].split(":");
			if (actualResponse.getHeader(keyValuePair[0].trim()) != null) {
				if (actualResponse.getHeader(keyValuePair[0].trim()).contains(
						keyValuePair[1].trim())) {
					logger.info("verify headers SUCCESS");
				} else if (keyValuePair[0].trim().toLowerCase()
						.equals("set-cookie")) {
					assertSetCookie(actualResponse.getHeaders(),
							keyValuePair[1].trim());
				} else {
					logger.info("verify headers FAILED");
					String actual = keyValuePair[0] + " = "
							+ actualResponse.getHeader(keyValuePair[0].trim());
					String expect = keyValuePair[0] + " = " + keyValuePair[1];
					throw new ComparisonFailureHandle("verify headers", expect,
							actual);
				}
			} else {
				logger.info("verify headers FAILED");
				String actual = keyValuePair[0] + " = "
						+ actualResponse.getHeader(keyValuePair[0].trim());
				String expect = keyValuePair[0] + " = " + keyValuePair[1];
				throw new ComparisonFailureHandle("verify headers", expect,
						actual);
			}

		}
	}

	private static void assertSetCookie(Header[] actualHeaders,
			String expectSetCookie) {
		String expect = "Set-Cookie:" + expectSetCookie;
		ArrayList<Header> setcookies = new ArrayList<Header>();
		String keyValuePairArray[] = expectSetCookie.split(";");

		for (int index = 0; index < actualHeaders.length; index++) {
			if (actualHeaders[index].getName().toLowerCase()
					.equals("set-cookie")) {
				setcookies.add(actualHeaders[index]);
			}
		}

		if (setcookies.size() == 0) {
			throw new ComparisonFailureHandle("verify Set-Cookie FAILED",
					expect, "no Set-Cookie Header");
		}

		for (int num = 0; num < setcookies.size(); num++) {
			String actulSetCookie = setcookies.get(num).getValue();
			for (int i = 0; i < keyValuePairArray.length; i++) {
				if (!actulSetCookie.contains(keyValuePairArray[i].trim())) {
					break;
				}
				if ((i + 1) == keyValuePairArray.length){
					logger.info("verify Set-Cookie SUCCESS");
					return ;
				}
			}
		}

		logger.info("verify headers FAILED");
		String actual = setcookies.toString();
		throw new ComparisonFailureHandle("verify Set-Cookie FAILED", expect,
				actual);
	}

	/**
	 * verify response headers
	 */
	public static void assertHeaders(Response actualResponse,
			Header[] expectHeaders) {

		for (Header expectHeader : expectHeaders) {
			if (actualResponse.getHeader(expectHeader.getName()) != null) {
				if (actualResponse.getHeader(expectHeader.getName()).equals(
						expectHeader.getValue())) {
					logger.info("verify headers SUCCESS");
				} else if (expectHeader.getName().toLowerCase()
						.equals("set-cookie")) {
					assertSetCookie(actualResponse.getHeaders(),
							expectHeader.getValue());
				}
			} else {
				logger.info("verify headers FAILED");
				String actual = expectHeader.getName() + " = "
						+ actualResponse.getHeader(expectHeader.getName());
				String expect = expectHeader.getName() + " = "
						+ expectHeader.getValue();
				throw new ComparisonFailureHandle("verify headers", expect,
						actual);
			}
		}

		logger.info("verify headers SUCCESS");
	}

	/**
	 * verify response status info
	 */
	public static void assertStatusInfo(Response actualResponse,
			int expectStatusCode, String expectStatusLine) {
		assertStatusCode(actualResponse, expectStatusCode);
		assertStatusLine(actualResponse, expectStatusLine);
	}

}
