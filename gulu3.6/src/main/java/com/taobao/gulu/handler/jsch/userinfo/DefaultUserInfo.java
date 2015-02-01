package com.taobao.gulu.handler.jsch.userinfo;

import org.apache.log4j.Logger;
import com.jcraft.jsch.*;

/**
 * <p>Title: DefaultUserInfo.java</p>
 * <p>Description: default user info</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class DefaultUserInfo implements UserInfo {
	private static Logger log = Logger.getLogger(DefaultUserInfo.class);	
	public String getPassword() {
		return null;
	}

	public boolean promptYesNo(String str) {
		return true;
	}

	public String getPassphrase() {
		return null;
	}

	public boolean promptPassphrase(String message) {
		return false;
	}

	public boolean promptPassword(String message) {
		return false;
	}

	public void showMessage(String message) {
		log.info(message);
	}
}
