package com.taobao.gulu.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public abstract class RequestExecution extends Request {

	/**
	 * <p>
	 * Title: doRequest
	 * </p>
	 * <p>
	 * Description: execute request and save result into response object
	 * </p>
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract Response doRequest() throws Exception;

	public Response doRequest(HttpMethod httpMethod) throws Exception {

		Response response = new Response();
		
		httpMethod.getParams().setVersion(getProtocolVersion());

		HttpClient httpClient = new HttpClient();
		HttpConnectionManagerParams managerParams = httpClient
				.getHttpConnectionManager().getParams();
		managerParams.setConnectionTimeout(getConnectTimeOut());
		managerParams.setSoTimeout(getSoTimeOut());
		
		httpClient.executeMethod(httpMethod);

		response.setStatusCode(httpMethod.getStatusCode());
		response.setStatusLine(httpMethod.getStatusText());
		response.setHeaders(httpMethod.getResponseHeaders());
		response.setResponseBodyAsStream(httpMethod.getResponseBodyAsStream());

		httpMethod.releaseConnection();
		httpClient.getHttpConnectionManager().closeIdleConnections(0);

		showRequest();
		response.showResponse();

		return response;
	}

	/**
	 * <p>
	 * Title: doRequest
	 * </p>
	 * <p>
	 * Description: execute request and save result into response object
	 * </p>
	 * 
	 * @param requestURL
	 * @param requestHeaders
	 *            headers pattern: "a:b$c:d$cookie:tt=xx;cc=pp" means headers:
	 *            a:b c:d cookie:tt=xx;cc=pp
	 * @return response object
	 * @throws Exception
	 */
	public Response doRequest(String requestURL, String requestHeaders)
			throws Exception {

		setUrl(requestURL);
		setHeaders(requestHeaders);
		return doRequest();
	}

	/**
	 * <p>
	 * Title: doRequest
	 * </p>
	 * <p>
	 * Description: execute request and save result into response object
	 * </p>
	 * 
	 * @param requestURL
	 * @param requestHeaders
	 *            Key-Value£ºHeaderKey-HeaderValue
	 * @return response object
	 * @throws Exception
	 */
	public Response doRequest(String requestURL, Header[] requestHeaders)
			throws Exception {

		setUrl(requestURL);
		setHeaders(requestHeaders);
		return doRequest();
	}

	/**
	 * <p>
	 * Title: doRequest
	 * </p>
	 * <p>
	 * Description: execute request and save result into response object
	 * </p>
	 * 
	 * @param requestURL
	 * @return response object
	 * @throws Exception
	 */
	public Response doRequest(String requestURL) throws Exception {

		setUrl(requestURL);
		return doRequest();
	}

	/*
	 * execute request and save result into response object
	 * 
	 * @throws Exception
	 */
	public Response doRequest(String requestURL, String requestHeaders,
			MultipartBodyEntity multipartBody) throws Exception {
		setUrl(requestURL);
		setHeaders(requestHeaders);
		setMultipartBody(multipartBody);

		return doRequest();
	}

	public Response doRequest(String requestURL, Header[] requestHeaders,
			MultipartBodyEntity multipartBody) throws Exception {
		setUrl(requestURL);
		setHeaders(requestHeaders);
		setMultipartBody(multipartBody);

		return doRequest();
	}

	public Response doRequest(String requestURL,
			MultipartBodyEntity multipartBody) throws Exception {
		setUrl(requestURL);
		setMultipartBody(multipartBody);

		return doRequest();
	}

	public Response doRequest(String requestURL, String requestHeaders,
			String pairsStr) throws Exception {
		setUrl(requestURL);

		if (requestHeaders != null)
			setHeaders(requestHeaders);

		setPairsBody(pairsStr);

		return doRequest();
	}

	public Response doRequest(String requestURL, String requestHeaders,
			NameValuePair[] pairsStr) throws Exception {
		setUrl(requestURL);

		if (requestHeaders != null)
			setHeaders(requestHeaders);

		setPairsBody(pairsStr);

		return doRequest();
	}

	/*
	 * execute request and save result into response object
	 * 
	 * @throws Exception
	 */
	public Response doRequest(String requestURL, String requestHeaders,
			BodyEntity body) throws Exception {
		setUrl(requestURL);
		setHeaders(requestHeaders);
		setBody(body);

		return doRequest();
	}

	public Response doRequest(String requestURL, Header[] requestHeaders,
			BodyEntity body) throws Exception {
		setUrl(requestURL);
		setHeaders(requestHeaders);
		setBody(body);

		return doRequest();
	}

	public Response doRequest(String requestURL, BodyEntity body)
			throws Exception {
		setUrl(requestURL);
		setBody(body);

		return doRequest();
	}

	public abstract String doRequestInIpBinding(String SA_IP, int SA_Port,
			String DA_IP, int DA_Port) throws Exception;

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
	 * @param method
	 *            request method
	 * @return response object
	 * @throws HttpException
	 *             IOException
	 * @throws Exception
	 */
	public String doRequestInIpBinding(String SA_IP, int SA_Port, String DA_IP,
			int DA_Port, String method) throws Exception {

		Socket s = new Socket();
		s.bind(new InetSocketAddress(SA_IP, SA_Port));
		s.connect(new InetSocketAddress(DA_IP, DA_Port));

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				s.getOutputStream()));

		Header[] requestHeaders = getHeaders();
		String url = method + " /%s HTTP/1.1\r\nHost:" + DA_IP;

		for (Header header : requestHeaders) {
			url = url + "\r\n" + header.getName() + ":" + header.getValue();
		}
		url = url + "\r\n\r\n";

		String request = String.format(url, getUrl());
		writer.write(request);
		writer.flush();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				s.getInputStream()));
		String line = reader.readLine();
		StringBuffer sb = new StringBuffer();
		while (line != null) {
			sb.append(line);
			line = reader.readLine();
		}

		reader.close();
		writer.close();
		s.close();

		return sb.toString();
	}

	/*
	 * binding virtual IP to exectue request and save result into String
	 */
	public String doRequestInIpBinding(String requestURL,
			String requestHeaders, String SA_IP, int SA_Port, String DAD_IP,
			int DAD_Port) throws Exception {

		setUrl(requestURL);
		setHeaders(requestHeaders);
		return doRequestInIpBinding(SA_IP, SA_Port, DAD_IP, DAD_Port);
	}

	/*
	 * binding virtual IP to exectue request and save result into String
	 */
	public String doRequestInIpBinding(String requestURL,
			Header[] requestHeaders, String SA_IP, int SA_Port, String DAD_IP,
			int DAD_Port) throws Exception {

		setUrl(requestURL);
		setHeaders(requestHeaders);
		return doRequestInIpBinding(SA_IP, SA_Port, DAD_IP, DAD_Port);
	}

	/*
	 * binding virtual IP to exectue request and save result into String
	 */
	public String doRequestInIpBinding(String requestURL, String SA_IP,
			int SA_Port, String DAD_IP, int DAD_Port) throws Exception {

		setUrl(requestURL);
		return doRequestInIpBinding(SA_IP, SA_Port, DAD_IP, DAD_Port);
	}

}
