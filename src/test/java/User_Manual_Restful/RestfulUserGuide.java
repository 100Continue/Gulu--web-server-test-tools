package User_Manual_Restful;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import Base.BaseCase;

import com.taobao.gulu.http.AssertFileBody;
import com.taobao.gulu.http.AssertStringBody;
import com.taobao.gulu.http.ByteArrayBodyEntity;
import com.taobao.gulu.http.DeleteRequest;
import com.taobao.gulu.http.FileBodyEntity;
import com.taobao.gulu.http.GetRequest;
import com.taobao.gulu.http.InputStreamBodyEntity;
import com.taobao.gulu.http.MultipartBodyEntity;
import com.taobao.gulu.http.PostRequest;
import com.taobao.gulu.http.PutRequest;
import com.taobao.gulu.http.Request.Version;
import com.taobao.gulu.http.Response;
import com.taobao.gulu.http.StringBodyEntity;
import com.taobao.gulu.tools.Util;

public class RestfulUserGuide extends BaseCase {
	String requestURL = "http://1.0.0.0:80/mod_monster_cookies_servlet/TestCase";
	String requestHeaders = "Accept-Charset:GBK,utf-8;q=0.7,*;q=0.3$Connection:keep-alive$Cookie:engine=baidu; lzstat_uv=7153831471059250701827484; ";
	String expectHeadersStr = "Server:Apache-Coyote/1.1$Content-Type:text/html;charset=ISO-8859-1";
	String filePath = "src/main/resources/fileBody";
	String expectFilePath = "src/main/resources/localfile";

	@Test
	public void test_HttpGet() throws Exception {
		GetRequest request = new GetRequest();

		// do request and only specify the url
		Response response = request.doRequest(requestURL);
		// do request and specify url and headers, use '$' to separate each
		// header
		response = request.doRequest(requestURL, requestHeaders);

		// Assert body in contain mode
		AssertStringBody.assertBody(response, "Body ::::end");
		// Assert body in equals mode
		String responsebody = "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::::end\r\n</body></html>\r\n";
		AssertStringBody.assertBodyEQ(response, responsebody);

		// Assert headers use '$' to separate each header
		AssertStringBody.assertHeaders(response, expectHeadersStr);

		// Assert Status Info
		AssertStringBody.assertStatusCode(response, 200);
		AssertStringBody.assertStatusLine(response, "OK");
		AssertStringBody.assertStatusInfo(response, 200, "OK");

		// Assert two Response
		Response response1 = request.doRequest(requestURL, requestHeaders);
		Response response2 = request.doRequest(requestURL, requestHeaders);
		AssertStringBody.assertResponse(response1, response2, "StatusCode",
				"StatusLine", "Headers", "Body");
	}

	@Test
	public void test_HttpDelete() throws Exception {
		DeleteRequest request = new DeleteRequest();

		// do request and only specify the url
		Response response = request.doRequest(requestURL);
		// do request and specify url and headers, use '$' to separate each
		// header
		response = request.doRequest(requestURL, requestHeaders);

		// Assert body in contain mode
		AssertStringBody.assertBody(response, "Body ::::end");
		// Assert body in equals mode
		String responsebody = "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::::end\r\n</body></html>\r\n";
		AssertStringBody.assertBodyEQ(response, responsebody);

		// Assert headers use '$' to separate each header
		AssertStringBody.assertHeaders(response, expectHeadersStr);

		// Assert Status Info
		AssertStringBody.assertStatusCode(response, 200);
		AssertStringBody.assertStatusLine(response, "OK");
		AssertStringBody.assertStatusInfo(response, 200, "OK");

		// Assert two Response
		Response response1 = request.doRequest(requestURL, requestHeaders);
		Response response2 = request.doRequest(requestURL, requestHeaders);
		AssertStringBody.assertResponse(response1, response2, "StatusCode",
				"StatusLine", "Headers", "Body");
	}

	@Test
	public void test_HttpPost() throws Exception {
		PostRequest request = new PostRequest();

		// set body, if set the Content-Type and Charset to null, and then it
		// would use default value
		StringBodyEntity stringBody = new StringBodyEntity(
				"string body content", null, null);
		FileBodyEntity fileBody = new FileBodyEntity(filePath, null);
		ByteArrayBodyEntity byteArrayBody = new ByteArrayBodyEntity(
				"byte array body content".getBytes(), null);
		InputStream inputStream = new FileInputStream(new File(filePath));
		InputStreamBodyEntity inputStreamBody = new InputStreamBodyEntity(
				inputStream, -2, null);

		MultipartBodyEntity multipartBody = new MultipartBodyEntity(stringBody,
				fileBody);

		String nameValuePair = "user=admin$password=adminpass$name=value";

		// do request and specify the url and string body
		Response response = request.doRequest(requestURL, stringBody);

		// Assert body in contain mode
		AssertStringBody
				.assertBody(response, "Body ::string body content::end");
		// Assert body in equals mode
		String responsebody = "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::string body content::end\r\n</body></html>\r\n";
		AssertStringBody.assertBodyEQ(response, responsebody);

		// do request and specify the url and byte array body
		response = request.doRequest(requestURL, byteArrayBody);

		// Assert body in contain mode
		AssertStringBody.assertBody(response,
				"Body ::byte array body content::end");
		// Assert body in equals mode
		responsebody = "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::byte array body content::end\r\n</body></html>\r\n";
		AssertStringBody.assertBodyEQ(response, responsebody);

		// do request and specify the url and file body
		response = request.doRequest(requestURL, fileBody);

		// Assert body in contain mode
		AssertStringBody.assertBody(response, "Body ::file body content::end");
		// Assert body in equals mode
		responsebody = "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::file body content::end\r\n</body></html>\r\n";
		AssertStringBody.assertBodyEQ(response, responsebody);

		// do request and specify the url and input stream body
		response = request.doRequest(requestURL, inputStreamBody);

		// Assert body in contain mode
		AssertStringBody.assertBody(response, "Body ::file body content::end");
		// Assert body in equals mode
		responsebody = "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::file body content::end\r\n</body></html>\r\n";
		AssertStringBody.assertBodyEQ(response, responsebody);

		// do request and specify the url and multipart body
		response = request.doRequest(requestURL, multipartBody);

		// Assert body in contain mode
		AssertStringBody.assertBody(response, "file body content");
		// Assert body in equals mode
		// responsebody =
		// "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::file body content::end\r\n</body></html>\r\n";
		// AssertStringBody.assertBodyEQ(response, responsebody);

		// do request and specify the url and name value pairs body
		response = new PostRequest().doRequest(requestURL, requestHeaders,
				nameValuePair);

		// Assert body in contain mode
		AssertStringBody.assertBody(response,
				"user=admin&password=adminpass&name=value");
		// Assert body in equals mode
		responsebody = "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::user=admin&password=adminpass&name=value::end\r\n</body></html>\r\n";
		AssertStringBody.assertBodyEQ(response, responsebody);

		// Assert headers use '$' to separate each header
		AssertStringBody.assertHeaders(response, expectHeadersStr);

		// Assert Status Info
		AssertStringBody.assertStatusCode(response, 200);
		AssertStringBody.assertStatusLine(response, "OK");
		AssertStringBody.assertStatusInfo(response, 200, "OK");

		// Assert two Response
		Response response1 = request.doRequest(requestURL, requestHeaders);
		Response response2 = request.doRequest(requestURL, requestHeaders);
		AssertStringBody.assertResponse(response1, response2, "StatusCode",
				"StatusLine", "Headers", "Body");
	}

	@Test
	public void test_HttpPut() throws Exception {
		PutRequest request = new PutRequest();

		// set body, if set the Content-Type and Charset to null, and then it
		// would use default value
		StringBodyEntity stringBody = new StringBodyEntity(
				"string body content", null, null);
		FileBodyEntity fileBody = new FileBodyEntity(filePath, null);
		ByteArrayBodyEntity byteArrayBody = new ByteArrayBodyEntity(
				"byte array body content".getBytes(), null);
		InputStream inputStream = new FileInputStream(new File(filePath));
		InputStreamBodyEntity inputStreamBody = new InputStreamBodyEntity(
				inputStream, -2, null);

		MultipartBodyEntity multipartBody = new MultipartBodyEntity(stringBody,
				fileBody);

		// do request and specify the url and string body
		Response response = request.doRequest(requestURL, stringBody);

		// Assert body in contain mode
		AssertStringBody
				.assertBody(response, "Body ::string body content::end");
		// Assert body in equals mode
		String responsebody = "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::string body content::end\r\n</body></html>\r\n";
		AssertStringBody.assertBodyEQ(response, responsebody);

		// do request and specify the url and byte array body
		response = request.doRequest(requestURL, byteArrayBody);

		// Assert body in contain mode
		AssertStringBody.assertBody(response,
				"Body ::byte array body content::end");
		// Assert body in equals mode
		responsebody = "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::byte array body content::end\r\n</body></html>\r\n";
		AssertStringBody.assertBodyEQ(response, responsebody);

		// do request and specify the url and file body
		response = request.doRequest(requestURL, fileBody);

		// Assert body in contain mode
		AssertStringBody.assertBody(response, "Body ::file body content::end");
		// Assert body in equals mode
		responsebody = "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::file body content::end\r\n</body></html>\r\n";
		AssertStringBody.assertBodyEQ(response, responsebody);

		// do request and specify the url and input stream body
		response = request.doRequest(requestURL, inputStreamBody);

		// Assert body in contain mode
		AssertStringBody.assertBody(response, "Body ::file body content::end");
		// Assert body in equals mode
		responsebody = "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::file body content::end\r\n</body></html>\r\n";
		AssertStringBody.assertBodyEQ(response, responsebody);

		// do request and specify the url and multipart body
		response = request.doRequest(requestURL, multipartBody);

		// Assert body in contain mode
		AssertStringBody.assertBody(response, "file body content");
		// Assert body in equals mode
		// responsebody =
		// "<html>\r\n<head><title>test</title></head>\r\n<body> Body ::file body content::end\r\n</body></html>\r\n";
		// AssertStringBody.assertBodyEQ(response, responsebody);

		// Assert headers use '$' to separate each header
		AssertStringBody.assertHeaders(response, expectHeadersStr);

		// Assert Status Info
		AssertStringBody.assertStatusCode(response, 200);
		AssertStringBody.assertStatusLine(response, "OK");
		AssertStringBody.assertStatusInfo(response, 200, "OK");

		// Assert two Response
		Response response1 = request.doRequest(requestURL, requestHeaders);
		Response response2 = request.doRequest(requestURL, requestHeaders);
		AssertStringBody.assertResponse(response1, response2, "StatusCode",
				"StatusLine", "Headers", "Body");
	}

	@Test
	public void test_Assert() throws Exception{
		GetRequest request = new GetRequest();
		Response response = request.doRequest(requestURL);
		
		// assert response body with local file
		AssertFileBody.assertBody(response, expectFilePath);
		// assert response body with local file in certain offset and size
		AssertFileBody.assertBody(response, expectFilePath, 0, 0);
	}
	
	@Test
	public void test_InstallNginxServer() throws Exception{
		NGINX.setNginxSrc("/home/admin/tengine_src");
		ArrayList<String> moduleslist = new ArrayList<String>();
		moduleslist.add("/home/admin/echo-nginx-module-master");
		moduleslist.add("/home/admin/mod_eagleeye");
		moduleslist.add("/home/admin/mod_tair");
		NGINX.setModuleslist(moduleslist);
		NGINX.deployNginxServerWithDebugBySrc();
		
		NGINX.start();
		GetRequest request = new GetRequest();
		request.doRequest(NGINX.getRoot_url_adress());
	}
	
	@Test
	public void test_doreq() throws Exception{
		GetRequest request = new GetRequest();
		request.doRequest(NGINX.getRoot_url_adress());
	}
	
	@Test
	public void test_InstallNginxServerByRPM() throws Exception{
		NGINX.setNginxSrc("/home/admin/nginx-1.2.5-11.el5.x86_64.rpm");
		NGINX.deployNginxServerByRPM();
	}
	
	@Test
	public void test_removeNginxServerByRPM() throws Exception{
		NGINX.setNginxSrc("nginx");
		NGINX.removeNginxServerByRPM();
	}
	
	@Test
	public void test_show(){
		System.out.println(Util.getEncryptedPasswords("taobao!@#"));
	}
	
	@Test
	public void test_InstallNginxServerByYUM() throws Exception{
		NGINX.setNginxSrc("nginx");
		NGINX.deployNginxServerByYUM();
	}
	
	@Test
	public void test_removeNginxServerByYUM() throws Exception{
		NGINX.setNginxSrc("nginx");
		NGINX.removeNginxServerByYUM();
	}
	
	@Test
	public void test_test() throws Exception{
		GetRequest get = new GetRequest();
		String url = "http://1.0.0.0:80/i6/200/000/2000041667368/xx"; 
		String headerStr = "Accept-Language: zh-CN,zh;q=0.8$If-Modified-Since: Wed, 24 Jul 2013 11:09:25 GMT$Accept-Charset: GBK,utf-8;q=0.7,*;q=0.3$Accept-Encoding: gzip,deflate,sdch$Referer: http://item.daily.taobao.net/item.htm?id=2000041667368$User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 Safari/537.31$Accept: */*$Connection: close$Cache-Control: max-age=0";
		get.setHeaders(headerStr);
		get.setUrl("http://1.0.0.0:8066/");
//		get.setUrl(url);
//		get.setProtocolVersion(Version.HTTP09);
		
		get.showRequest();
		Response response1 = get.doRequest();
//		System.out.println(response1.getResponseBodyAsString());	
		
		get.setUrl(url);
		Response response2 = get.doRequest();
		System.out.println(response2.getResponseBodyAsString());
		AssertStringBody.assertResponse(response1, response2, "Body");
	}
	
	@Test
	public void test_temp() throws Exception{
		GetRequest get = new GetRequest();
		String headerStr = "Host:ab$Accept-Language: zh-CN,zh;q=0.8$If-Modified-Since: Wed, 24 Jul 2013 11:09:25 GMT$Accept-Charset: GBK,utf-8;q=0.7,*;q=0.3$Accept-Encoding: gzip,deflate,sdch$Referer: http://item.daily.taobao.net/item.htm?id=2000041667368$User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 Safari/537.31$Accept: */*$Connection: close$Cache-Control: max-age=0";
		get.setHeaders(headerStr);
		get.setUrl("http://1.0.0.0:8066/");
		
		get.showRequest();
		Response response1 = get.doRequest();
	}
	
	@Test
	public void test_temp_02() throws JSONException {
		JSONObject json = new JSONObject("{\"dns\":[{\"host\":\"img01.taobaocdn.com\",\"ips\":[{\"ip\":\"114.80.174.40\",\"spdy\":1}, {\"ip\":\"114.80.174.50\", \"spdy\":0}],\"ttl\":300}],\"port\":8108}");
		JSONArray jarray = json.getJSONArray("dns");
		
		System.out.println(jarray.get(0));
		
		System.out.println(jarray.isNull(0));
		
		JSONObject index0 = jarray.getJSONObject(0);
		System.out.println(index0.get("ips"));
		
	}
}
