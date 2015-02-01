package com.taobao.gulu.http;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.taobao.gulu.tools.ComparisonFailureHandle;
import com.taobao.gulu.tools.FailedHandle;

/**
 * <p>Title: AssertJsonBody.java</p>
 * <p>Description: verify response(expect body is JsonObject) info</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class AssertJsonBody extends AssertBehavior {

	private static Logger logger = Logger.getLogger(AssertJsonBody.class);
			
	public static void assertBody(Response actualResponse, String expectJsonStr) throws Exception {
		if(expectJsonStr == null){
			logger.error("expect json body is null.");
			throw new FailedHandle("expect json body is null");
		}

		JSONObject expectJson = new JSONObject(expectJsonStr); 
		assertBody(actualResponse, expectJson);
	}

	public static void assertBody(Response actualResponse, JSONObject expectJson) throws Exception {
		String actualResponsebody = null;
		try {
			actualResponsebody = actualResponse
					.getResponseBodyAsString();
		} catch (FailedHandle e) {
			throw new FailedHandle(
					"actual response body too large or empty, you should use AssertFileBody instead.");
		}
		
		if(actualResponsebody == null){
			logger.error("actual body is null.");
			throw new FailedHandle("actual body is null");
		}
		
		try {
			for (Iterator<?> iter = expectJson.keys(); iter.hasNext();) {
				String key = (String) iter.next();

				if (!(expectJson.get(key).toString().equals(new JSONObject(
						actualResponsebody).get(key).toString()))){
					logger.info("verify response body FAILED");
					throw new ComparisonFailureHandle("verify response body", expectJson.toString(), actualResponsebody);
				}
			}
		} catch (JSONException e) {
			logger.info(e);
			throw new FailedHandle(e.getMessage());
		}
		logger.info("verify response body SUCCESS");
	}
	
	public static void assertResponse(Response actualResponse, Response expectResponse, String...assertArgs ) throws Exception{
		for(int i = 0; i < assertArgs.length; i++){
			if(assertArgs[i].toLowerCase().equals("statuscode")){
				assertStatusCode(actualResponse, expectResponse.getStatusCode());
			}
			if(assertArgs[i].toLowerCase().equals("statusline")){
				assertStatusLine(actualResponse, expectResponse.getStatusLine());
			}
			if(assertArgs[i].toLowerCase().equals("headers")){
				assertHeaders(actualResponse, expectResponse.getHeaders());
			}
			if(assertArgs[i].toLowerCase().equals("body")){
				String expectResponsebody = null;
				try{
					expectResponsebody = expectResponse.getResponseBodyAsString();
				}catch (FailedHandle e){
					throw new FailedHandle("expect response body too large or empty, you should use assertFileBody instead.");
				}
					assertBody(actualResponse, expectResponsebody);
			}
		}
	}


	public static String getJSONValue(JSONObject expectJson, String key) throws Exception {
		return expectJson.getString(key);
	}
	

	public static String getJSONValue(String expectJsonStr, String key) throws Exception {
		JSONObject expectJson = new JSONObject(expectJsonStr); 
		return expectJson.getString(key);
	}

	
}
