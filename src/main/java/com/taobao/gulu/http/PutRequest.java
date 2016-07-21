package com.taobao.gulu.http;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.log4j.Logger;

import com.taobao.gulu.tools.FailedHandle;

/**
 * Request Method is PUT
 * 
 * @author gongyuan.cz
 * 
 */
public class PutRequest extends RequestExecution {
	private static Logger logger = Logger.getLogger(PutRequest.class);

	/*
	 * set putMethod
	 */
	private PutMethod getPutMethod() throws Exception {
		PutMethod putMethod = new PutMethod(getUrl());

		Header[] headers = getHeaders();
		if (headers != null)
			for (Header header : headers) {
				if(header.getName().toLowerCase().equals("host")){
					putMethod.getParams().setVirtualHost(header.getValue());
				}else{
					putMethod.setRequestHeader(header.getName(), header.getValue());
				}
			}

		if (getMultipartBody() != null) {
			MultipartBodyEntity multipartbody = getMultipartBody();
			BodyEntity[] multipartbodyEntity = multipartbody.getBodyEntity();
			Part[] parts = new Part[multipartbodyEntity.length];
			for (int i = 0; i < multipartbodyEntity.length; i++) {
				BodyEntity requestEntityBody = multipartbodyEntity[i];
				parts[i] = requestEntityBody.getPart();
			}
			putMethod.setRequestEntity(new MultipartRequestEntity(parts,
					putMethod.getParams()));
		} else if (getBody() != null) {
			BodyEntity requestEntityBody = getBody();
			putMethod
					.setRequestHeader(
							"Content-Type",
							requestEntityBody.getContentType() == null ? DEFAULT_CONTENT_TYPE
									: requestEntityBody.getContentType()
											+ "; charset="
											+ requestEntityBody.getCharset() == null ? DEFAULT_CHARSET
											: requestEntityBody.getCharset());
			putMethod.setRequestEntity(requestEntityBody.getRequestEntity());
		} else if (getPairsBody() != null) {
			logger.info("Put Method should not use pairs body!");
			throw new FailedHandle("Put Method should not use pairs body!");
		} else {
			logger.info("Put Method should have body!");
			throw new FailedHandle("Put Method should have body!");
		}

		return putMethod;
	}

	/*
	 * execute request and save result into response object
	 * 
	 * @throws Exception
	 */
	public Response doRequest() throws Exception {

		PutMethod httpMethod = getPutMethod();
		return doRequest(httpMethod);
	}

	@Override
	public String doRequestInIpBinding(String SA_IP, int SA_Port, String DA_IP,
			int DA_Port) throws Exception {
		logger.info("put method do not support do request in ip binding");
		throw new FailedHandle(
				"put method do not support do request in ip binding");
	}
}
