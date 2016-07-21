package com.taobao.gulu.handler.jsch.processhandler;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import com.taobao.gulu.handler.jsch.SessionProvider;
import com.taobao.gulu.handler.jsch.authorization.AuthorizationInterface;
import com.taobao.gulu.tools.OperationException;

/**
 * <p>Title: ExecChannelProvider.java</p>
 * <p>Description: provide Exec channel</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class ExecChannelProvider {
	private static Logger log = Logger.getLogger(ExecChannelProvider.class);
	private Session session = null;
	private ChannelExec channel = null;

	public ExecChannelProvider(AuthorizationInterface authorization)
			throws OperationException {
		this.session = new SessionProvider(authorization).getSession();
	}

	private void initChannelInBackgroundMod(String command)
			throws OperationException {
		try {
			session.connect();
			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(command + " &");
			channel.setPty(false);
			channel.setInputStream(null);
			channel.setOutputStream(null); // remote processes STDOUT
			channel.setErrStream(null);
			log.warn("Executing background command " + command + " &");
			channel.connect();
			TimeUnit.SECONDS.sleep(1);
		} catch (Exception e) {
			throw new OperationException(e.toString());
		}
	}

	private void initChannelWithPty(String command) throws OperationException {
		try {
			session.connect();
			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(command);
			channel.setPty(true);
			channel.setErrStream(System.err);
			channel.connect();
		} catch (Exception e) {
			throw new OperationException(e.toString());
		}
	}
	
	private void initChannelWithOutPty(String command) throws OperationException{
		try{
			session.connect();
			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(command);
			channel.setPty(false);
			channel.setInputStream(null);
			channel.setErrStream(System.err);
			channel.connect();
		}catch (Exception e) {
			throw new OperationException(e.getMessage());
		}
	}

	public ChannelExec getChannelInBackgroundMod(String command) throws OperationException{
		initChannelInBackgroundMod(command);
		return this.channel;
		
	}
	
	public ChannelExec getChannelWithOutPty(String command) throws OperationException{
		initChannelWithOutPty(command);
		return this.channel;
	}
	
	public ChannelExec getChannelWithPty(String command) throws OperationException{
		initChannelWithPty(command);
		return this.channel;
	}
}
