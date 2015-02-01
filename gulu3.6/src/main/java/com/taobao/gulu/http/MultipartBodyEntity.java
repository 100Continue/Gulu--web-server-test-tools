package com.taobao.gulu.http;

import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

/**
 * <p>Title: BodyEntityFromText.java</p>
 * <p>Description: request body is String Text</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class MultipartBodyEntity implements BodyEntity {
	
	/** The content */
    private BodyEntity[]  bodyEntity;
    

	public MultipartBodyEntity(BodyEntity... bodyEntity) throws Exception {
		this.bodyEntity = bodyEntity;
	}

	public BodyEntity[] getBodyEntity(){
		return bodyEntity;
	}
	
	public void setBodyEntity(BodyEntity... bodyEntity){
		this.bodyEntity = bodyEntity;
	}
	
	@Override
	public Part getPart() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Part[] getParts() throws Exception {
		int count = 0;
		for (int i = 0; i < bodyEntity.length; i++) {
			BodyEntity requestEntityBody = bodyEntity[i];
			if(requestEntityBody.getPart() != null)
				count ++;
		}
		
		Part[] parts = new Part[count];
		for (int i = 0, j = 0; i < bodyEntity.length; i++) {
			BodyEntity requestEntityBody = bodyEntity[i];
			if(requestEntityBody.getPart() != null){
				parts[j] = requestEntityBody.getPart();
				j++;
			}
		}
		return parts;
	}


	@Override
	public RequestEntity getRequestEntity() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return "body is multipart";
	}


	@Override
	public String getCharset() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
