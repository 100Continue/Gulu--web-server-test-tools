package com.taobao.gulu.perf;

/**
 * <p>Title: PerfInfo.java</p>
 * <p>Description: perf test infomation</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class PerfInfo {

	private String host = "0.0.0.0";
	private String username = null;
	private String password = null;
	private String ABSenderPath = null;
	private String xlsxFilePath = null;
	private String concurrencyList = null;
	private String perfCommand = null;
	private String perfTestTitle = "no title";

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPerfCommand() {
		return perfCommand;
	}

	public void setPerfCommand(String perfCommand) {
		this.perfCommand = perfCommand;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getABSenderPath() {
		return ABSenderPath;
	}

	public void setABSenderPath(String ABSenderPath) {
		this.ABSenderPath = ABSenderPath;
	}

	public String getXlsxFilePath() {
		return xlsxFilePath;
	}

	public void setXlsxFilePath(String xlsxFilePath) {
		this.xlsxFilePath = xlsxFilePath;
	}

	public String getConcurrencyList() {
		return concurrencyList;
	}

	
	/**
	 * <p>Title: setConcurrencyList</p>
	 * <p>Description: set the concurrency param</p>
	 * @param concurrencyList pattern: "1 10 20 30 40 50"  means concurrency 1, 10, 20, 30, 40 and 50
	 */
	public void setConcurrencyList(String concurrencyList) {
		this.concurrencyList = concurrencyList;
	}

	public String getPerfTestTitle() {
		return perfTestTitle;
	}

	public void setPerfTestTitle(String perfTestTitle) {
		this.perfTestTitle = perfTestTitle;
	}
}
