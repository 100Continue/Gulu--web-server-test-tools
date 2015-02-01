package com.taobao.gulu.handler.jsch.authorization;

/**
 * <p>Title: AuthorizationInterface.java</p>
 * <p>Description: authorization interface</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public interface AuthorizationInterface {
	public void setUsername(String username);
	public void setHost(String host);
	public void setPort(int port);
	public void setPassword(String password);
	public void setIdentity(String identity);
	
	public String getUsername();
	public String getHost();
	public int getPort();
	public String getPassword();
	public String getIdentity();
}
