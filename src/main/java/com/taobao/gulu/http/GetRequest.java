package com.taobao.gulu.http;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * <p>
 * Title: GetRequest.java
 * </p>
 * <p>
 * Description: use get method to do request
 * </p>
 * 
 * @author: gongyuan.cz
 * @email: gongyuan.cz@taobao.com
 * @blog: 100continue.iteye.com
 */
public class GetRequest extends RequestExecution {

	/**
	 * <p>
	 * Title: doRequest
	 * </p>
	 * <p>
	 * Description: execute request and save result into response object
	 * </p>
	 * 
	 * @return response object
	 * @throws Exception
	 */
	public Response doRequest() throws Exception {

		GetMethod httpMethod = getGetMethod();
		return doRequest(httpMethod);
	}

	/*
	 * binding virtual IP to exectue request and save result into String
	 */
	public String doRequestInIpBinding(String SA_IP, int SA_Port,
			String DAD_IP, int DAD_Port) throws Exception {

		return doRequestInIpBinding(SA_IP, SA_Port, DAD_IP, DAD_Port, "GET");
	}

	/*
	 * set getMethod
	 */
	private GetMethod getGetMethod() {
		GetMethod getMethod = new GetMethod(getUrl());

		Header[] headers = getHeaders();
		if (headers != null)
			for (Header header : headers) {
				if(header.getName().toLowerCase().equals("host")){
					getMethod.getParams().setVirtualHost(header.getValue());
				}else{
					getMethod.setRequestHeader(header.getName(), header.getValue());
				}
			}

		return getMethod;
	}
}
