package com.taobao.gulu.server;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.taobao.gulu.handler.OperationResult;
import com.taobao.gulu.handler.jsch.authorization.PasswordAuthorization;
import com.taobao.gulu.handler.jsch.processhandler.ProcessHandlerExecImpl;
import com.taobao.gulu.tools.ComparisonFailureHandle;

/**
 * <p>
 * Title: NginxServer.java
 * </p>
 * <p>
 * Description: Nginx Server Manage
 * </p>
 * 
 * @author: gongyuan.cz
 * @email: gongyuan.cz@taobao.com
 * @blog: 100continue.iteye.com
 */
public class NginxServer implements Server {
	private static Logger logger = Logger.getLogger(NginxServer.class);

	private String host = "0.0.0.0";
	private int port = -1;
	private String root_url_adress = "http://" + host + ":" + port + "/";
	private String conf_file_directory = "";
	private String execute_cmd = "";
	private String server_file_directory = "";
	private String action_start = "start";
	private String action_stop = "stop";
	private String action_restart = "restart";
	private String default_conf = "";
	private String username = "";
	private String password = "";
	private ArrayList<String> moduleslist = new ArrayList<String>();
	private String nginxSrc = "";

	public String getHost() {
		return host;
	}

	public ArrayList<String> getModuleslist() {
		return moduleslist;
	}

	public void setModuleslist(ArrayList<String> moduleslist) {
		this.moduleslist = moduleslist;
	}

	public String getNginxSrc() {
		return nginxSrc;
	}

	public void setNginxSrc(String nginxSrc) {
		this.nginxSrc = nginxSrc;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRoot_url_adress() {
		return root_url_adress;
	}

	public void setRoot_url_adress(String root_url_adress) {
		this.root_url_adress = root_url_adress;
	}

	public String getConf_file_directory() {
		return conf_file_directory;
	}

	public void setConf_file_directory(String conf_file_directory) {
		this.conf_file_directory = conf_file_directory;
	}

	public String getExecute_cmd() {
		return execute_cmd;
	}

	public void setExecute_cmd(String execute_cmd) {
		this.execute_cmd = execute_cmd;
	}

	public String getServer_file_directory() {
		return server_file_directory;
	}

	public void setServer_file_directory(String server_file_directory) {
		this.server_file_directory = server_file_directory;
	}

	public String getAction_start() {
		return action_start;
	}

	public void setAction_start(String action_start) {
		this.action_start = action_start;
	}

	public String getAction_stop() {
		return action_stop;
	}

	public void setAction_stop(String action_stop) {
		this.action_stop = action_stop;
	}

	public String getAction_restart() {
		return action_restart;
	}

	public void setAction_restart(String action_restart) {
		this.action_restart = action_restart;
	}

	public String getDefault_conf() {
		return default_conf;
	}

	public void setDefault_conf(String default_conf) {
		this.default_conf = default_conf;
	}

	public boolean reload() {
		try {
			String default_conf_directory = conf_file_directory + "/"
					+ default_conf;
			doServerCtl(default_conf_directory, "reload");
			return detectServerStatus();
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	@Override
	public boolean start() {
		try {
			String default_conf_directory = conf_file_directory + "/"
					+ default_conf;
			doServerCtl(default_conf_directory, action_start);
			return detectServerStatus();
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	@Override
	public boolean stop() {
		try {
			String default_conf_directory = conf_file_directory + "/"
					+ default_conf;
			doServerCtl(default_conf_directory, action_stop);
			if (detectServerStatus())
				return false;
			else
				return true;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	@Override
	public boolean start(String configFileName) {
		String conf = conf_file_directory + configFileName;
		try {
			doServerCtl(conf, action_start);
			return detectServerStatus();
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	@Override
	public boolean stop(String configFileName) {
		String conf = conf_file_directory + configFileName;
		try {
			doServerCtl(conf, action_stop);
			if (detectServerStatus())
				return false;
			else
				return true;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	@Override
	public boolean restart() {
		String conf = conf_file_directory + default_conf;
		try {
			doServerCtl(conf, action_stop);
			doServerCtl(conf, action_start);
			return detectServerStatus();
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	@Override
	public boolean restart(String configFileName) {
		String conf = conf_file_directory + configFileName;
		try {
			doServerCtl(conf, action_stop);
			doServerCtl(conf, action_start);
			return detectServerStatus();
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	@Override
	public void doServerCtl(String conf, String action) throws Exception {
		doServerCtl(conf, action, "");
	}

	@Override
	public void doServerCtl(String conf, String action, String expectMessage)
			throws Exception {
		ProcessHandlerExecImpl process = getProcessHandler();

		String cmd = execute_cmd + " -c " + conf + " -s " + action;
		OperationResult result;

		if (username == null || password == null || host == null) {
			result = process.executeCmd("", cmd, false);
		} else {
			result = process.executeCmd(host, cmd, false);
		}

		if (!"".equals(expectMessage)) {
			if (!result.getMsg().contains(expectMessage))
				throw new ComparisonFailureHandle(
						"verify command execute output", expectMessage,
						result.getMsg());
		}
	}

	@Override
	public boolean detectServerStatus() {
		try {
			SocketChannel channel = SocketChannel.open();
			channel.socket().connect(new InetSocketAddress(host, port));
			channel.close();
			return true;
		} catch (Exception e) {
			logger.info("detect server status: " + e);
			return false;
		}
	}

	@Override
	public void startServerError(String configFileName, String errorMessage)
			throws Exception {
		String conf = conf_file_directory + configFileName;
		doServerCtl(conf, action_start, errorMessage);
	}

	@Override
	public void startServerError(String errorMessage) throws Exception {
		startServerError(getDefault_conf(), errorMessage);
	}

	@Override
	public void stopServerError(String configFileName, String errorMessage) {
		String conf = conf_file_directory + configFileName;
		try {
			doServerCtl(conf, action_stop, errorMessage);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public void stopServerError(String errorMessage) {
		stopServerError(getDefault_conf(), errorMessage);
	}

	public void deployNginxServerWithDebugBySrc() throws Exception {
		ProcessHandlerExecImpl process = getProcessHandler();

		String deployCMD = "cd " + nginxSrc + "; ./configure --prefix="
				+ server_file_directory;
		deployCMD = deployCMD + addModules(moduleslist);
		deployCMD = deployCMD + " --with-debug";

		process.executeCmd(getHost(), deployCMD, false);
		TimeUnit.SECONDS.sleep(2);

		String makeCMD = "cd " + nginxSrc + "; make -j";
		process.executeCmd(getHost(), makeCMD, false);
		TimeUnit.SECONDS.sleep(2);

		String makeinstallCMD = "cd " + nginxSrc + "; make install";
		process.executeCmd(getHost(), makeinstallCMD, false);
		TimeUnit.SECONDS.sleep(2);
	}

	public void deployNginxServerBySrc() throws Exception {
		ProcessHandlerExecImpl process = getProcessHandler();

		String deployCMD = "cd " + nginxSrc + "; ./configure --prefix="
				+ server_file_directory;
		deployCMD = deployCMD + addModules(moduleslist);

		process.executeCmd(getHost(), deployCMD, false);
		TimeUnit.SECONDS.sleep(2);

		String makeCMD = "cd " + nginxSrc + "; make -j";
		process.executeCmd(getHost(), makeCMD, false);
		TimeUnit.SECONDS.sleep(2);

		String makeinstallCMD = "cd " + nginxSrc + "; make install";
		process.executeCmd(getHost(), makeinstallCMD, false);
		TimeUnit.SECONDS.sleep(2);
	}

	public String deployNginxServerByRPM() throws Exception {
		ProcessHandlerExecImpl process = getProcessHandler();

		String deployCMD = "rpm -ivh " + nginxSrc;
		return process.executeCmdByRoot(getHost(), deployCMD, false).getMsg().trim();
	}

	public String removeNginxServerByRPM() throws Exception {
		ProcessHandlerExecImpl process = getProcessHandler();

		String removeCMD = "rpm -e " + nginxSrc;
		return process.executeCmdByRoot(getHost(), removeCMD, false).getMsg().trim();
	}
	
	public String deployNginxServerByYUM() throws Exception{
		ProcessHandlerExecImpl process = getProcessHandler();
		
		String deployCMD = "yum install -y " + nginxSrc;
		return process.executeCmdByRoot(getHost(), deployCMD, false).getMsg().trim();
	}
	
	public String updateNginxServerByYUM() throws Exception{
		ProcessHandlerExecImpl process = getProcessHandler();
		
		String deployCMD = "yum update -y " + nginxSrc;
		return process.executeCmdByRoot(getHost(), deployCMD, false).getMsg().trim();
	}
	
	public String removeNginxServerByYUM() throws Exception{
		ProcessHandlerExecImpl process = getProcessHandler();

		String removeCMD = "yum remove -y " + nginxSrc;
		return process.executeCmdByRoot(getHost(), removeCMD, false).getMsg().trim();
	
	}

	private String addModules(ArrayList<String> moduleslist) {
		String addModulesCMD = "";
		for (String module : moduleslist) {
			addModulesCMD = addModulesCMD + " --add-module=" + module;
		}

		return addModulesCMD;
	}

	private ProcessHandlerExecImpl getProcessHandler() {
		PasswordAuthorization passwords = new PasswordAuthorization(
				username, password);
		ProcessHandlerExecImpl process = new ProcessHandlerExecImpl(
				passwords);
		return process;
	}
}
