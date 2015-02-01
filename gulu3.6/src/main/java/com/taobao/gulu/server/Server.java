package com.taobao.gulu.server;


/**
 * <p>Title: Server.java</p>
 * <p>Description: web server manage interface</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public interface Server {
	
	/**
	 * start the Server by default configure file
	 * @return true: start successfully    false: fail
	 */
	public boolean start();
	
	/**
	 * stop the Server by default configure file
	 * @return true: stop successfully    false: fail
	 */
	public boolean stop();
	
	/**
	 * start the Server by allocate configure file
	 * @return true: start successfully    false: fail
	 */
	public boolean start(String configFileName);
	
	/**
	 * stop the Server by allocate configure file
	 * @return true: stop successfully    false: fail
	 */
	public boolean stop(String configFileName) ;
	
	/**
	 * restart the Server by default configure file
	 * @return true: restart successfully    false: fail
	 */
	public boolean restart();
	
	/**
	 * restart the Server by allocate configure file
	 * @return true: restart successfully    false: fail
	 */
	public boolean restart(String configFileName);
	
	/**
	 * control the Server by allocate configure file
	 * 
	 * @throws Exception 
	 */
	public void doServerCtl(String conf, String action) throws Exception;
	
	/**
	 * control the Server by allocate configure file
	 * check the process message
	 * @throws Exception 
	 */
	public void doServerCtl(String conf, String action, String expectMessage) throws Exception;
	
	/**
	 * detect the Server status
	 * @return true: alive    false: stop
	 */
	public boolean detectServerStatus();
	
	/**
	 * start the Server fail and varify the errorMessage
	 * @throws Exception 
	 */
	public void startServerError(String configFileName, String errorMessage) throws Exception;
	
	/**
	 * start the Server fail and varify the errorMessage
	 * @throws Exception 
	 */
	public void startServerError(String errorMessage) throws Exception;
	
	
	/**
	 * stop the Server fail and varify the errorMessage
	 */
	public void stopServerError(String configFileName, String errorMessage);
	
	/**
	 * stop the Server fail and varify the errorMessage
	 */
	public void stopServerError(String errorMessage);

}
