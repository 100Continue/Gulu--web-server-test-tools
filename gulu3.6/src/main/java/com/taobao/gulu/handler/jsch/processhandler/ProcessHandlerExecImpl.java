package com.taobao.gulu.handler.jsch.processhandler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelExec;
import com.taobao.gulu.handler.OperationResult;
import com.taobao.gulu.handler.ProcessHandler;
import com.taobao.gulu.handler.jsch.authorization.AuthorizationInterface;
import com.taobao.gulu.tools.OperationException;

/**
 * <p>Title: ProcessHandlerExecImpl.java</p>
 * <p>Description: process handler implement in Exec</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class ProcessHandlerExecImpl implements ProcessHandler {
	private static Logger log = Logger.getLogger(ProcessHandlerExecImpl.class);
	private AuthorizationInterface authorization;
	private ExecChannelProvider execChannelProvider = null;
	private ChannelExec channel = null;
	private boolean isPty = false;

	public ProcessHandlerExecImpl(AuthorizationInterface authorization) {
		this.authorization = authorization;
	}

	@Override
	public OperationResult executeCmd(String destServer, String cmd,
			boolean isBackground) throws OperationException {
		if ("".equals(cmd))
			return new OperationResult("The commands is empty!", false);

		if ("".equals(destServer) || null == destServer) {
			if (isBackground)
				cmd = cmd + " &";

			return executeCmdinLocalMachine(cmd);

		} else {
			this.authorization.setHost(destServer);
			setExecChannelProvider(this.authorization);

			if (isBackground)
				return execBackground(cmd);
			else
				return execCommand(cmd);
		}
	}

	@Override
	public OperationResult executeCmdByUser(String username, String destServer,
			String cmd, boolean isBackground) throws OperationException {
		if ("".equals(cmd) || "".equals(username))
			return new OperationResult("The commands and user name is empty!",
					false);

		if ("".equals(destServer)) {
			cmd = "su - " + username + " -c \"" + cmd + "\"";

			if (isBackground)
				cmd = cmd + " &";

			return executeCmdinLocalMachine(cmd);

		} else {

			this.authorization.setHost(destServer);
			setExecChannelProvider(this.authorization);

			if (isBackground)
				throw new OperationException(
						"sorry you can not use executeCmdByUser in background, "
								+ "we suggest you use executeCmd in the user you want.");
			else
				return sudo(" -u " + username + " " + cmd);
		}
	}

	@Override
	public OperationResult executeCmdByRoot(String destServer, String cmd,
			boolean isBackground) throws OperationException {
		if ("".equals(cmd))
			return new OperationResult("The commands is empty!", false);

		if ("".equals(destServer)) {
			if (isBackground)
				cmd = cmd + " &";

			return executeCmdinLocalMachine(cmd);

		} else {
			this.authorization.setHost(destServer);
			setExecChannelProvider(this.authorization);

			if (isBackground)
				throw new OperationException(
						"sorry you can not use executeCmdByUser in background, "
								+ "we suggest you use executeCmd in the user you want.");
			else
				return sudo(cmd);
		}
	}

	@Override
	public OperationResult killProcess(String destServer, int pid)
			throws OperationException {
		this.authorization.setHost(destServer);
		setExecChannelProvider(this.authorization);

		String cmd = "kill " + pid;
		return execCommand(cmd);
	}

	@Override
	public OperationResult killProcess(String destServer, String procName)
			throws OperationException {
		this.authorization.setHost(destServer);
		setExecChannelProvider(this.authorization);

		String cmd = "killall " + procName;
		return execCommand(cmd);
	}

	@Override
	public OperationResult killProcessNow(String destServer, int pid)
			throws OperationException {
		this.authorization.setHost(destServer);
		setExecChannelProvider(this.authorization);

		String cmd = "kill -9 " + pid;
		return execCommand(cmd);
	}

	@Override
	public OperationResult killProcessNow(String destServer, String procName)
			throws OperationException {
		this.authorization.setHost(destServer);
		setExecChannelProvider(this.authorization);

		String cmd = "killall -9 " + procName;
		return execCommand(cmd);
	}


	@Override
	public int[] getPidByProcName(String username, String destServer,
			String procName) throws OperationException {
		int[] id;
		this.authorization.setHost(destServer);
		setExecChannelProvider(this.authorization);

		String command = "ps x|grep -v grep|grep \'" + procName
				+ "\'| gawk '{print $1}'";
		OperationResult result = sudo("-u " + username + " " + command);

		if (result.isSuccess()) {
			if (!result.getMsg().isEmpty() && result.getMsg() != null) {
				String[] tmp = result.getMsg().split("\\r\\n");
				id = new int[tmp.length];
				for (int i = 0; i < tmp.length; i++) {
					id[i] = Integer.parseInt(tmp[i]);
				}
				return id;
			} else
				return null;
		} else {
			return null;
		}

	}

	private void setExecChannelProvider(AuthorizationInterface authorization)
			throws OperationException {
		this.execChannelProvider = new ExecChannelProvider(authorization);
	}

	private OperationResult execCommand(String command)
			throws OperationException {
		log.debug("command :" + command);
		try {
			this.channel = execChannelProvider.getChannelWithOutPty(command);
			return getOperationResult(channel.getInputStream(), channel.getErrStream());
		} catch (Exception e) {
			throw new OperationException(e);
		}
	}

	private OperationResult execBackground(String command)
			throws OperationException {
		log.debug("command :" + command);
		OperationResult result = new OperationResult();
		try {
			this.channel = execChannelProvider
					.getChannelInBackgroundMod(command);

			result.setReturnCode(channel.getExitStatus());
			if (channel.getExitStatus() == 0) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			result.setMsg("execute in background.");

			disconnect();

			return result;
		} catch (Exception e) {
			disconnect();
			throw new OperationException(e.toString());
		}
	}

	private OperationResult sudo(String command) throws OperationException {
		log.debug("command: sudo " + command);
		String cmd = "sudo -S -p '' " + command;

		try {
			this.channel = execChannelProvider.getChannelWithPty(cmd);
			isPty = true;
			InputStream in = this.channel.getInputStream();
			OutputStream out = this.channel.getOutputStream();
			InputStream er = this.channel.getErrStream();

			if (authorization.getPassword() == null) {
				out.write((channel.getSession().getUserInfo().getPassword() + "\n")
						.getBytes());
			} else {
				out.write((authorization.getPassword() + "\n").getBytes());
			}
			out.flush();

			return getOperationResult(in, er);
		} catch (Exception e) {
			throw new OperationException(e.toString());
		}
	}

	private OperationResult getOperationResult(InputStream in, InputStream er)
			throws OperationException {
		OperationResult result = new OperationResult();

		try {
			byte[] tmp = new byte[1024];
			StringBuffer stringBuffer = new StringBuffer();
			StringBuffer errorBuffer = new StringBuffer();

			while (true) {

				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;

					stringBuffer.append(new String(tmp, 0, i));
				}
				String msg = stringBuffer.toString();
				
				while (er.available() > 0) {
					int i = er.read(tmp, 0, 1024);
					if (i < 0)
						break;

					errorBuffer.append(new String(tmp, 0, i));
				}
				msg = msg + errorBuffer.toString();
				
				if (channel.isClosed()) {
					if (this.isPty) {
						String password;
						if (authorization.getPassword() == null)
							password = channel.getSession().getUserInfo()
									.getPassword();
						else
							password = authorization.getPassword();
						if (msg.length() >= password.length()
								&& msg.substring(0, password.length())
										.contains(
												password.substring(0,
														password.length()))) {
							if (msg.substring(password.length() + 2,
									password.length() + 4).contains("\n")) {
								result.setMsg(msg.substring(password.length() + 4));
							} else {
								result.setMsg(msg.substring(password.length() + 2));
							}
						} else {
							result.setMsg(msg);
						}
					} else {
						result.setMsg(msg);
					}
					log.debug(result.getMsg());
					result.setReturnCode(channel.getExitStatus());
					if (channel.getExitStatus() == 0) {
						result.setSuccess(true);
					} else {
						result.setSuccess(false);
					}
					break;
				}

				Thread.sleep(1000);
			}
			disconnect();
		} catch (Exception e) {
			disconnect();
			throw new OperationException(e);
		}
		return result;
	}

	/**
	 * @throws OperationException
	 *             release connections.
	 * 
	 * @author
	 * @throws
	 */
	private void disconnect() {
		try {
			channel.disconnect();
			channel.getSession().disconnect();
		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	private OperationResult executeCmdinLocalMachine(String cmd)
			throws OperationException {
		OperationResult result = new OperationResult();

		try {
			log.info("Executing command: " + cmd);

			Process p = Runtime.getRuntime().exec(cmd);

			char[] buf = new char[1024];
			StringBuilder outputString = new StringBuilder();
			InputStreamReader inputReader = new InputStreamReader(
					p.getInputStream());

			int got = 0;
			while ((got = inputReader.read(buf)) != -1) {
				outputString.append(buf, 0, got);
			}

			InputStreamReader errorReader = new InputStreamReader(
					p.getErrorStream());

			while ((got = errorReader.read(buf)) != -1) {
				outputString.append(buf, 0, got);
			}

			p.waitFor();

			if (p.exitValue() != 0) {
				result.setSuccess(false);
				result.setMsg(String.format("execute [%s] fail [code:%d]", cmd,
						p.exitValue()));
			} else {
				result.setSuccess(true);
				result.setMsg(outputString.toString());
			}
		} catch (Exception e) {
			throw new OperationException(e);
		}

		return result;
	}
}
