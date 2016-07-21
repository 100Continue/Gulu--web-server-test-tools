package com.taobao.gulu.http;

import java.io.InputStream;

import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

/**
 * <p>Title: BodyEntityFromText.java</p>
 * <p>Description: request body is String Text</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class InputStreamBodyEntity implements BodyEntity {
	
	/**
     * The content length will be calculated automatically. This implies
     * buffering of the content.
     */
    public static final int CONTENT_LENGTH_AUTO = -2;
    
	/** The content */
    private InputStreamRequestEntity inputStreamRequestEntity = null;
    

	public InputStreamBodyEntity(InputStream content){
		inputStreamRequestEntity = new InputStreamRequestEntity(content, CONTENT_LENGTH_AUTO, null);
	}
	
	public InputStreamBodyEntity(InputStream content, String contentType) throws Exception {
		inputStreamRequestEntity = new InputStreamRequestEntity(content, CONTENT_LENGTH_AUTO, contentType);
	}
	
	public InputStreamBodyEntity(InputStream content, long contentLength) throws Exception {
		inputStreamRequestEntity = new InputStreamRequestEntity(content, contentLength, null);
	}
	
	public InputStreamBodyEntity(InputStream content, long contentLength, String contentType) throws Exception {
		inputStreamRequestEntity = new InputStreamRequestEntity(content, contentLength, contentType);
	}
	

	@Override
	public RequestEntity getRequestEntity() {
		return inputStreamRequestEntity;
	}

	@Override
	public Part getPart() throws Exception {
		return null;
	}

	@Override
	public String getContentType() {
		return inputStreamRequestEntity.getContentType();
	}

	@Override
	public String getContent() {
		return "body is from inputstream";
	}

	@Override
	public String getCharset() {
		// TODO Auto-generated method stub
		return null;
	}

}
