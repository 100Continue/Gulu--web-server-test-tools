package com.taobao.gulu.handler;

import com.taobao.gulu.tools.*;

/**
 * <p>Title: ProcessHandler.java</p>
 * <p>Description: process handler interface</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public interface ProcessHandler
{
	/**
	 * <p>Title: executeCmd</p>
	 * <p>Description: execute command</p>
	 * @param destServer - destination server IP, if empty means execute command in local server
	 * @param cmd - command for execute
	 * @param isBackground - run in backgroup or not
	 * @return
	 * @throws OperationException
	 */
	public OperationResult executeCmd(String destServer, String cmd, boolean isBackground) throws OperationException;
	
	/**
	 * <p>Title: executeCmdByUser</p>
	 * <p>Description: execute command by user</p>
	 * @param username - the user to execute command
	 * @param destServer - destination server IP
	 * @param cmd - command
	 * @param isBackground - run in backgroup or not
	 * @return
	 * @throws OperationException
	 */
	public OperationResult executeCmdByUser(String username, String destServer, String cmd, boolean isBackground) throws OperationException;
	
	/**
	 * <p>Title: executeCmdByRoot</p>
	 * <p>Description: execute command by root</p>
	 * @param destServer - destination server IP
	 * @param cmd - command
	 * @param isBackground - run in backgroup or not
	 * @return
	 * @throws OperationException
	 */
	public OperationResult executeCmdByRoot(String destServer, String cmd, boolean isBackground) throws OperationException;
	
	/**
	 * <p>Title: getPidByProcName</p>
	 * <p>Description: get the process id by process name</p>
	 * @param username - the process user
	 * @param destServer - destination server IP
	 * @param procName - process name
	 * @return
	 * @throws OperationException
	 */
	public int[] getPidByProcName(String username, String destServer, String procName) throws OperationException;
	
		/**
	 * <p>Title: killProcess</p>
	 * <p>Description: kill the process</p>
	 * @param destServer - destination server IP
	 * @param pid - process id
	 * @return
	 * @throws OperationException
	 */
	public OperationResult killProcess(String destServer, int pid) throws OperationException;
	
	/**
	 * <p>Title: killProcess</p>
	 * <p>Description: kill the process</p>
	 * @param destServer - destination server IP
	 * @param procName - process name
	 * @return
	 * @throws OperationException
	 */
	public OperationResult killProcess(String destServer, String procName) throws OperationException;
	
	/**
	 * <p>Title: killProcessNow</p>
	 * <p>Description: kill process immediately</p>
	 * @param destServer - destination server IP
	 * @param pid - process id
	 * @return
	 * @throws OperationException
	 */
	public OperationResult killProcessNow(String destServer, int pid) throws OperationException;
	
	/**
	 * <p>Title: killProcessNow</p>
	 * <p>Description: kill process immediately</p>
	 * @param destServer - destination server IP
	 * @param procName - process name
	 * @return
	 * @throws OperationException
	 */
	public OperationResult killProcessNow(String destServer, String procName) throws OperationException;
	
}
