package com.taobao.gulu.http;

import java.io.File;

import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;


/**
 * <p>Title: BodyEntityFromFile.java</p>
 * <p>Description: request body is from file</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class FileBodyEntity implements BodyEntity {

	private File file = null;
    private String contentType = null;
    private String charset = null;
	
	public FileBodyEntity(String filepath, String contentType) {
		this.file = new File(filepath);
		this.contentType = contentType;
	}
	
	public FileBodyEntity(String filepath, String contentType, String charset) {
		this.file = new File(filepath);
		this.contentType = contentType;
		this.charset = charset;
	}
	
	public FileBodyEntity(String filepath) {
		this.file = new File(filepath);
		this.contentType = null;
	}
	
	public FileBodyEntity(File file, String contentType, String charset) {
		this.file = file;
		this.contentType = contentType;
		this.charset = charset;
	}
	
	public FileBodyEntity(File file) {
		this.file = file;
		this.contentType = null;
	}

	@Override
	public FilePart getPart() throws Exception{
		return new FilePart("FILE", this.file, this.contentType, this.charset);
	}

	@Override
	public RequestEntity getRequestEntity() {
		return new FileRequestEntity(this.file, this.contentType);
	}

	@Override
	public String getContentType() {
		return new FileRequestEntity(this.file, this.contentType).getContentType();
	}

	@Override
	public String getContent() {
		return "body from file: " + file.getAbsolutePath();
	}

	@Override
	public String getCharset() {
		return null;
	}
	
	

}
