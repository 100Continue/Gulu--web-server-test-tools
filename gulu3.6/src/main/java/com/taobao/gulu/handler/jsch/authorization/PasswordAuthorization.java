package com.taobao.gulu.handler.jsch.authorization;

import org.jasypt.util.TextEncryptor;

/**
 * <p>Title: PasswordAuthorization.java</p>
 * <p>Description: use password to authorization</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class PasswordAuthorization implements AuthorizationInterface {
	private String username;
	private String host;
	private int port = 22;
	private String password;

	public PasswordAuthorization(String username, String password, int port) {
		this.username = username;
		this.password = getDecryptPasswords(password);
		this.port = port;
	}

	public PasswordAuthorization(String username, String password) {
		this.username = username;
		this.password = getDecryptPasswords(password);
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
		this.password = getDecryptPasswords(password);
	}

	@Override
	public void setIdentity(String identity) {
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
		return this.password;
	}

	@Override
	public String getIdentity() {
		return null;
	}

	private String getDecryptPasswords(String encryptedPasswords) {
		if (null == encryptedPasswords || "".equals(encryptedPasswords))
			return null;
		TextEncryptor textEncryptor = new TextEncryptor();
		textEncryptor.setPassword("password");
		try {
			return textEncryptor.decrypt(encryptedPasswords);
		} catch (Exception e) {
			return "";
		}
	}
}
