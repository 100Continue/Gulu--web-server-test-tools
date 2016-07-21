package com.taobao.gulu.handler.jsch.authorization;

/**
 * <p>Title: KeyAuthorization.java</p>
 * <p>Description: use rsa to authorization </p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class KeyAuthorization implements AuthorizationInterface {

	private String username;
	private String host;
	private int port;
	private String identity;
	
	public KeyAuthorization(String username, String identity, String host, int port){
		this.username = username;
		this.identity = identity;
		this.host = host;
		this.port = port;
	}
	
	public KeyAuthorization(String username, String identity){
		this.username = username;
		this.identity = identity;
	}
	
	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void setPassword(String password) {
	}

	@Override
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String getHost() {
		return this.host;
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getIdentity() {
		return this.identity;
	}

}
