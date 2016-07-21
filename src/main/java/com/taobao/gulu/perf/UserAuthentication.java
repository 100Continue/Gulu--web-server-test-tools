package com.taobao.gulu.perf;

import java.io.Serializable;

/**
 * <p>Title: ShellServer.java</p>
 * <p>Description: store server info</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class UserAuthentication implements Serializable {

	private static final long serialVersionUID = -5465198935115265381L;
	private String host = "";
	private String username = "";
	private String password = "";
	
	public UserAuthentication(){
		super();
	}
	
	public UserAuthentication(String host, String username, String password) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
