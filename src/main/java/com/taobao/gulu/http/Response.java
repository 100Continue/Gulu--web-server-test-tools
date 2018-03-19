package com.taobao.gulu.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Header;
import org.apache.log4j.Logger;

/**
 * <p>
 * Title: Response.java
 * </p>
 * <p>
 * Description: contain response info
 * </p>
 *
 * @author: gongyuan.cz
 * @email: gongyuan.cz@taobao.com
 * @blog: 100continue.iteye.com
 */
public class Response {
	private Logger logger = Logger.getLogger(Response.class);

	private int statusCode = 200;
	private String statusLine = "OK";
	private Header[] headers = null;
	private String reponseBody = null;

	public InputStream getResponseBodyAsStream() throws Exception {
		return new ByteArrayInputStream(this.reponseBody.getBytes("utf-8"));
	}

	public void setResponseBodyAsStream(InputStream responseBodyAsStream)
			throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseBodyAsStream));
		boolean firstLine = true;
		String line = null;
		while((line = bufferedReader.readLine()) != null){
			if(!firstLine){
				stringBuilder.append(System.getProperty("line.separator"));
			}else{
				firstLine = false;
			}
			stringBuilder.append(line);
		}
		this.reponseBody = stringBuilder.toString();
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusLine() {
		return statusLine;
	}

	public void setStatusLine(String statusLine) {
		this.statusLine = statusLine;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public String getHeader(String key) {
		for (int i = 0; i < headers.length; i++) {
			if (headers[i].getName().equals(key))
				return headers[i].getValue();
		}
		return null;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	public String getResponseBodyAsString() throws Exception {
		return reponseBody;
	}

	private void showResponseBody() throws Exception {
		if (this.reponseBody != null) {
			logger.info(new String(this.reponseBody));
		} else {
			logger.info("<-- or Response body is too large can not buffer in memory. --!>");
			logger.info("<-- or unknown size, you can get it in file: "
					+ reponseBody + " --!>");
		}
	}

	/**
	 * <p>
	 * Title: showResponse
	 * </p>
	 * <p>
	 * Description: show Response info
	 * </p>
	 *
	 * @throws Exception
	 */
	public void showResponse() throws Exception {
		logger.info("Response Info: ---------------> ");
		logger.info("Status Code:" + getStatusCode());
		logger.info("Status Text:" + getStatusLine());

		showHeaders();

		logger.info("Response Body:");
		showResponseBody();
		logger.info("End of Response Info ---------------> ");
	}

	public void showHeaders() {
		for (Header header : this.headers) {
			logger.info(header.getName() + ":" + header.getValue());
		}
	}

	public String getHeadersStr() {
		String headers = "";
		for (Header header : this.headers) {
			headers = headers + header.getName() + ":" + header.getValue();
		}
		return headers;
	}
}
