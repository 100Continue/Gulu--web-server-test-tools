package com.taobao.gulu.http;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.log4j.Logger;

import com.taobao.gulu.tools.FailedHandle;

/**
 * <p>
 * Title: PostRequest.java
 * </p>
 * <p>
 * Description: use post method to do request
 * </p>
 * 
 * @author: gongyuan.cz
 * @email: gongyuan.cz@taobao.com
 * @blog: 100continue.iteye.com
 */
public class PostRequest extends RequestExecution {
	private static Logger logger = Logger.getLogger(PostRequest.class);

	/*
	 * set postMethod
	 */
	private PostMethod getPostMethod() throws Exception {
		PostMethod postMethod = new PostMethod(getUrl());

		Header[] headers = getHeaders();
		if (headers != null)
			for (Header header : headers) {
				if(header.getName().toLowerCase().equals("host")){
					postMethod.getParams().setVirtualHost(header.getValue());
				}else{
					postMethod.setRequestHeader(header.getName(), header.getValue());
				}
			}

		if (getMultipartBody() != null) {

			postMethod.setRequestEntity(new MultipartRequestEntity(
					getMultipartBody().getParts(), postMethod.getParams()));
		} else if (getBody() != null) {
			BodyEntity requestEntityBody = getBody();
			postMethod
					.setRequestHeader(
							"Content-Type",
							requestEntityBody.getContentType() == null ? DEFAULT_CONTENT_TYPE
									: requestEntityBody.getContentType()
											+ "; charset="
											+ requestEntityBody.getCharset() == null ? DEFAULT_CHARSET
											: requestEntityBody.getCharset());
			postMethod.setRequestEntity(requestEntityBody.getRequestEntity());
		} else if (getPairsBody() != null) {
			postMethod.setRequestBody(getPairsBody());
		} else {
			logger.info("Post Method should have body!");
			throw new FailedHandle("Post Method should have body!");
		}

		return postMethod;
	}

	/*
	 * execute request and save result into response object
	 * 
	 * @throws Exception
	 */
	public Response doRequest() throws Exception {

		PostMethod httpMethod = getPostMethod();
		return doRequest(httpMethod);
	}

	@Override
	public String doRequestInIpBinding(String SA_IP, int SA_Port, String DA_IP,
			int DA_Port) throws Exception {
		String errorInfo = "post method do not support do request in ip binding, you can waitting for next version";
		logger.info(errorInfo);
		throw new FailedHandle(errorInfo);
	}

}
