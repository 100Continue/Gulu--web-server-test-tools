package com.taobao.gulu.http;

import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.StringPart;

/**
 * <p>Title: BodyEntityFromText.java</p>
 * <p>Description: request body is String Text</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class StringBodyEntity implements BodyEntity {
	
	/** The content */
    private StringRequestEntity stringRequestEntity;
    

	public StringBodyEntity(String content) throws Exception {
		stringRequestEntity = new StringRequestEntity(content, null, null);
	}
	
	public StringBodyEntity(String content, String contentType, String charset) throws Exception {
		stringRequestEntity = new StringRequestEntity(content, contentType, charset);
	}
	
	@Override
	public StringPart getPart() {
		return new StringPart("TEXT", stringRequestEntity.getContent(), stringRequestEntity.getCharset());
	}

	@Override
	public RequestEntity getRequestEntity() {
		return stringRequestEntity;
	}

	@Override
	public String getContentType() {
		return stringRequestEntity.getContentType();
	}

	@Override
	public String getContent() {
		return stringRequestEntity.getContent();
	}

	@Override
	public String getCharset() {
		return stringRequestEntity.getCharset();
	}

}
