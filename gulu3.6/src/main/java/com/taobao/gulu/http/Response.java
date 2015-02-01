package com.taobao.gulu.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.httpclient.Header;
import org.apache.log4j.Logger;

import com.taobao.gulu.tools.FailedHandle;
import com.taobao.gulu.tools.Util;

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
	private String tmpfile = "src/main/resources/responseBody";
	private int tmpfilelength = -1;
	private byte[] body = null;

	public InputStream getResponseBodyAsStream() throws Exception {
		return new FileInputStream(new File(this.tmpfile));
	}

	public void setResponseBodyAsStream(InputStream responseBodyAsStream)
			throws Exception {
		saveToFile(responseBodyAsStream);
		setBody();
	}
	
	public void setBody() throws Exception{
		setTmpFileLength((int) new File(this.tmpfile).length());
		if(getTmpFileLength() < Integer.MAX_VALUE && getTmpFileLength() > 0)
			this.body = Util.readFile(tmpfile, 0, 0);
	}
	
	private void setTmpFileLength(int length){
		this.tmpfilelength = length;
	}

	private int getTmpFileLength(){
		return this.tmpfilelength;
	}
	
	private void saveToFile(InputStream responseBodyAsStream) throws Exception {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;

		if (responseBodyAsStream != null) {
			bis = new BufferedInputStream(responseBodyAsStream);
			fos = new FileOutputStream(this.tmpfile);
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
			bis.close();
		}else{
			fos = new FileOutputStream(this.tmpfile);
			fos.write("".getBytes());
		}
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
		if (getTmpFileLength() > 0 && getTmpFileLength() < Integer.MAX_VALUE) {
			return new String(this.body);
		} else if(getTmpFileLength() == 0) {
			return null;
		} else {
			String warnInfo = "Going to buffer response body of large or unknown size. "
					+ "Using getResponseBodyAsStream instead is recommended."
					+ "or you can read the response body from local file: "
					+ tmpfile;
			logger.error(warnInfo);
			throw new FailedHandle(warnInfo);
		}
	}

	private void showResponseBody() throws Exception {
		if (this.body != null) {
			logger.info(new String(this.body));
		} else if(getTmpFileLength() == 0){
			logger.info("<-- Do not Contain Response body, you should check Status Code. --!>");
		}else {
			logger.info("<-- or Response body is too large can not buffer in memory. --!>");
			logger.info("<-- or unknown size, you can get it in file: "
					+ tmpfile + " --!>");
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
