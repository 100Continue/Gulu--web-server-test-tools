package com.taobao.gulu.handler.jsch.filehandler;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.taobao.gulu.handler.jsch.SessionProvider;
import com.taobao.gulu.handler.jsch.authorization.AuthorizationInterface;
import com.taobao.gulu.tools.OperationException;

/**
 * <p>Title: SFTPChannelProvider.java</p>
 * <p>Description: provide the SFTP channel</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class SFTPChannelProvider {
	private Session session = null;
	private ChannelSftp channel = null;

	public SFTPChannelProvider(AuthorizationInterface authorization)
			throws OperationException {
		this.session = new SessionProvider(authorization).getSession();
	}
	
	public ChannelSftp getChannel() throws OperationException{
		initChannel();
		return this.channel;
	}
	

	private void initChannel() throws OperationException {
		try {
			session.connect();
			this.channel = (ChannelSftp) session.openChannel(
					"sftp");
			this.channel.connect();
		} catch (Exception e) {
			throw new OperationException(e.toString());
		}

	}

}
