package com.taobao.gulu.http;

import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

/**
 * <p>Title: BodyEntityFromText.java</p>
 * <p>Description: request body is String Text</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class ByteArrayBodyEntity implements BodyEntity {
	
	/** The content */
    private ByteArrayRequestEntity byteArrayRequestEntity = null;
    

	public ByteArrayBodyEntity(byte[] content, String contentType){
		byteArrayRequestEntity = new ByteArrayRequestEntity(content, contentType);
	}
	
	public ByteArrayBodyEntity(byte[] content){
		byteArrayRequestEntity = new ByteArrayRequestEntity(content, null);
	}
	

	@Override
	public RequestEntity getRequestEntity() {
		return byteArrayRequestEntity;
	}

	@Override
	public Part getPart() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		return byteArrayRequestEntity.getContentType();
	}

	@Override
	public String getContent() {
		return new String(byteArrayRequestEntity.getContent());
	}

	@Override
	public String getCharset() {
		return null;
	}

}
