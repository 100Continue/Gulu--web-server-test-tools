package com.taobao.gulu.http;

import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

/**
 * <p>Title: BodyEntity.java</p>
 * <p>Description: request body info</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public interface BodyEntity {
	

	public Part getPart() throws Exception;
	
	public RequestEntity getRequestEntity();
	
	public String getContentType();
	
	public String getContent();
	
	public String getCharset();

}