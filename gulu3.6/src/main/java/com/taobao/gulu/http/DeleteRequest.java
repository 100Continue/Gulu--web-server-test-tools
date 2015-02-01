package com.taobao.gulu.http;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.DeleteMethod;

/**
 * <p>
 * Title: DeleteRequest.java
 * </p>
 * <p>
 * Description: use delete method to do request
 * </p>
 * 
 * @author: gongyuan.cz
 * @email: gongyuan.cz@taobao.com
 * @blog: 100continue.iteye.com
 */
public class DeleteRequest extends RequestExecution {

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

		DeleteMethod httpMethod = getDeleteMethod();
		return doRequest(httpMethod);
	}

	/**
	 * <p>
	 * Title: doRequestInIpBinding
	 * </p>
	 * <p>
	 * Description: binding virtual IP to exectue request and save result into
	 * String
	 * </p>
	 * 
	 * @param SA_IP
	 *            source address IP
	 * @param SA_Port
	 *            source address Port
	 * @param DAD_IP
	 *            destination address IP
	 * @param DAD_Port
	 *            destination address Port
	 * @return response object
	 * @throws Exception
	 */
	public String doRequestInIpBinding(String SA_IP, int SA_Port,
			String DAD_IP, int DAD_Port) throws Exception {

		return doRequestInIpBinding(SA_IP, SA_Port, DAD_IP, DAD_Port, "DELETE");
	}

	private DeleteMethod getDeleteMethod() {
		DeleteMethod deleteMethod = new DeleteMethod(getUrl());

		Header[] headers = getHeaders();
		if (headers != null)
			for (Header header : headers) {
				if(header.getName().toLowerCase().equals("host")){
					deleteMethod.getParams().setVirtualHost(header.getValue());
				}else{
					deleteMethod.setRequestHeader(header.getName(), header.getValue());
				}
			}

		return deleteMethod;
	}
}
