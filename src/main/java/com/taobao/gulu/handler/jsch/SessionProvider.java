package com.taobao.gulu.handler.jsch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.taobao.gulu.handler.jsch.authorization.AuthorizationInterface;
import com.taobao.gulu.handler.jsch.userinfo.DefaultUserInfo;
import com.taobao.gulu.handler.jsch.userinfo.KeyboardInteractiveUserInfo;
import com.taobao.gulu.tools.OperationException;

/**
 * <p>Title: SessionProvider.java</p>
 * <p>Description: provide the session for channel</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class SessionProvider {

	private static Logger log = Logger.getLogger(SessionProvider.class);

	private AuthorizationInterface authorization;
	private UserInfo userInfo;

	private String authType;  // 1. Private/public key authorization; 2. password authorization; 3. keyboard interactive authorization

	private JSch jsch =new JSch();
	
	protected Session session = null;


	public SessionProvider(AuthorizationInterface authorization)
			throws OperationException {
		this.authorization = authorization;
	}
	
	public void setAuthType() throws OperationException {
		if (authorization.getIdentity() != null && authorization.getPassword() == null) { // Private/public key authorization
			this.authType = "key authorization";
		} else if (authorization.getPassword() != null && authorization.getIdentity() == null) { // Password authorization
			this.userInfo = new DefaultUserInfo();
			this.authType = "password authorization";
		} else if (authorization.getPassword() == null && authorization.getIdentity() == null) { // keyboard interactive authorization
			this.userInfo = new KeyboardInteractiveUserInfo();
			this.authType = "keyboard interactive authorization";
		} else {
			log.error("you should user correct authorization object.");
			throw new OperationException("authorization object incorrect");
		}
	}


	public Session getSession() throws OperationException {
		
		initSession();
		return this.session;
	}

	/**
	 * <p>Title: initSession</p>
	 * <p>Description: Initialize SSH session. When the parameters is not right, It will throw an operation exception.</p>
	 * @throws OperationException
	 */
	@SuppressWarnings("static-access")
	private void initSession() throws OperationException {
		try {
			validate();
			jsch.setConfig("StrictHostKeyChecking", "no");

			if (authType.equals("key authorization")){
				log.info("JSCH identity:" + authorization.getIdentity());
				jsch.addIdentity(authorization.getIdentity());
			}
			
			session = jsch.getSession(authorization.getUsername(), authorization.getHost(), authorization.getPort());

			if (authType.equals("password authorization"))
				session.setUserInfo(userInfo);

			if (authorization.getPassword() != null)
				session.setPassword(authorization.getPassword());

			if (authType.equals("keyboard interactive authorization"))
				session.setUserInfo(userInfo);
			
			log.info("JSCH session init success.");
		} catch (JSchException e) {
			log.error(e.getMessage());
			throw new OperationException(e.toString());
		}
	}

	/**
	 * <p>Title: validate</p>
	 * <p>Description: validate parameters</p>
	 * @throws JSchException
	 * @throws OperationException
	 */
	private void validate() throws JSchException, OperationException {
		if (authorization.getUsername() == null || authorization.getUsername().isEmpty()) {
			throw new JSchException("Parameter:username is empty.");
		}
		if (authorization.getHost() == null || authorization.getHost().isEmpty()) {
			throw new JSchException("Parameter:host is empty.");
		} else {
			try {
				InetAddress inet = InetAddress.getByName(authorization.getHost());
				authorization.setHost(inet.getHostAddress());
				log.info("JSCH connection address:" + authorization.getHost());
			} catch (UnknownHostException e) {
				throw new JSchException(e.getMessage(), e);
			}
		}

		setAuthType();

		if (authType.equals("password authorization") && (userInfo == null)) {
			throw new JSchException("Parameter:userInfo is empty.");
		}

	}
}
