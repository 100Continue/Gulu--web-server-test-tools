package com.taobao.gulu.database;

import java.io.OutputStream;
import java.io.RandomAccessFile;
import org.apache.log4j.Logger;
import com.taobao.common.tfs.DefaultTfsManager;
import com.taobao.common.tfs.TfsException;
import com.taobao.common.tfs.impl.FSName;
import com.taobao.common.tfs.packet.FileInfo;
import com.taobao.gulu.tools.FailedHandle;

/**
 * <p>
 * Title: TFS.java
 * </p>
 * <p>
 * Description: TFS manage tools
 * </p>
 * 
 * @author: gongyuan.cz
 * @email: gongyuan.cz@taobao.com
 * @blog: 100continue.iteye.com
 */
public class TFS {
	private Logger logger = Logger.getLogger(TFS.class);

	private String name_server_address = "0.0.0.0:00000";
	private String tfs_rc_server_address = "0.0.0.0:0000";
	private String tfs_app_key = "";
	private long appid = 0;
	private long uid = 0;
	private int timeOut = 8000;
	private int batchCount = 2;
	private DefaultTfsManager tfsManager = new DefaultTfsManager();
	private RandomAccessFile file;

	// private TfsBaseCase tfsCom = new TfsBaseCase();

	public String getName_server_address() {
		return name_server_address;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public int getBatchCount() {
		return batchCount;
	}

	public void setBatchCount(int batchCount) {
		this.batchCount = batchCount;
	}

	public void setName_server_address(String name_server_address) {
		this.name_server_address = name_server_address;
	}

	public String getTfs_rc_server_address() {
		return tfs_rc_server_address;
	}

	public void setTfs_rc_server_address(String tfs_rc_server_address) {
		this.tfs_rc_server_address = tfs_rc_server_address;
	}

	public String getTfs_app_key() {
		return tfs_app_key;
	}

	public void setTfs_app_key(String tfs_app_key) {
		this.tfs_app_key = tfs_app_key;
	}

	public long getAppid() {
		return appid;
	}

	public void setAppid(long appid) {
		this.appid = appid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void init() {
		// init tfs
		logger.info("initialize TFS configuration");

		tfsManager.setRcAddr(tfs_rc_server_address);
		tfsManager.setAppKey(tfs_app_key);
		tfsManager.setTimeout(timeOut);
		tfsManager.setBatchCount(batchCount);
		tfsManager.setUseNameMeta(false);
		tfsManager.init();
	}

	public void createFile(String path, long fileSize) {
		logger.info("create a tmp file according to variable(file size and path)");
		try {
			file = new RandomAccessFile(path, "rw");
			file.setLength(fileSize);
			file.close();
		} catch (Exception e) {
			logger.info(e);
			throw new FailedHandle(e.getMessage());
		}
	}

	/**
	 * use a exit file to create a tfs file and save it to tfs file
	 * 
	 * @param path
	 * @param size
	 * @return tfs file name
	 */
	public String saveLargeFile(String path, long size) {
		logger.info("put a tmp file to TFS");
		String tfsFileName = null;
		createFile(path, size);

		tfsFileName = tfsManager.saveLargeFile(path, "", "");
		if (tfsFileName != null) {
			logger.info("put to TFS SUCCESS, TFS file name : " + tfsFileName
					+ ".");
			return tfsFileName;
		} else {
			String errorInfo = "put to TFS FAILED";
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	/**
	 * use a exit file to create a tfs file and save it to tfs file in large
	 * file mode
	 * 
	 * @param path
	 * @return tfs file name
	 */
	public String saveLargeFile(String path) {
		logger.info("put a large file to TFS, file path: " + path);
		String tfsFileName = null;
		tfsFileName = tfsManager.saveLargeFile(path, "", "");
		if (tfsFileName != null) {
			logger.info("put large file to TFS SUCCESS, TFS file name : "
					+ tfsFileName + ".");
			return tfsFileName;
		} else {
			String errorInfo = "put large file to TFS FAILED, file path: "
					+ path;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	public String saveLargeFile(String path, String suffix) {
		logger.info("put a large file to TFS, file path: " + path
				+ ", with suffix: " + suffix);
		String tfsFileName = null;
		tfsFileName = tfsManager.saveLargeFile(path, "", suffix);

		if (tfsFileName != null) {
			logger.info("put large file to TFS SUCCESS, TFS file name : "
					+ tfsFileName + ".");
			return tfsFileName;
		} else {
			String errorInfo = "put large file to TFS FAILED, file path: "
					+ path + ", with suffix: " + suffix;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	public String saveFile(String path) {
		logger.info("put a file to TFS, file path: " + path);
		String tfsFileName = null;
		tfsFileName = tfsManager.saveFile(path, null, null);

		if (tfsFileName != null) {
			logger.info("put file to TFS SUCCESS, TFS file name : "
					+ tfsFileName + ".");
			return tfsFileName;
		} else {
			String errorInfo = "put file to TFS FAILED, file path: " + path;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	public String saveFile(String path, String suffix) {
		logger.info("put a file to TFS, file path: " + path + ", with suffix: "
				+ suffix);

		String tfsFileName = null;
		tfsFileName = tfsManager.saveFile(path, "", suffix);

		if (tfsFileName != null) {
			logger.info("put file to TFS SUCCESS, TFS file name : "
					+ tfsFileName + ".");
			return tfsFileName;
		} else {
			String errorInfo = "put file to TFS FAILED, file path: " + path
					+ ", with suffix: " + suffix;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	public String saveFile(String path, boolean simpleName) {
		logger.info("put a file to TFS, file path: " + path
				+ ", with simple name: " + simpleName);

		String tfsFileName = null;
		tfsFileName = tfsManager.saveFile(path, null, null, simpleName);

		if (tfsFileName != null) {
			logger.info("put file to TFS SUCCESS, TFS file name : "
					+ tfsFileName + ".");
			return tfsFileName;
		} else {
			String errorInfo = "put file to TFS FAILED, file path: " + path
					+ ", with simple name: " + simpleName;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	public String saveFile(String path, String suffix, boolean simpleName) {
		logger.info("put a file to TFS, file path: " + path
				+ ", with simple name: " + simpleName + ", with suffix: "
				+ suffix);

		String tfsFileName = null;
		tfsFileName = tfsManager.saveFile(path, "", suffix, simpleName);

		if (tfsFileName != null) {
			logger.info("put file to TFS SUCCESS, TFS file name : "
					+ tfsFileName + ".");
			return tfsFileName;
		} else {
			String errorInfo = "put file to TFS FAILED, file path: " + path
					+ ", with simple name: " + simpleName + ", with suffix: "
					+ suffix;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	protected void saveFileWithNameMeta(String path, long size,
			String tfsFileName) {
		logger.info("put name meta to TFS, file path: " + path
				+ ", TFS file name: " + tfsFileName + ", size: " + size);

		createFile(path, size);

		if (tfsManager.saveFile(appid, uid, path, tfsFileName)) {
			logger.info("put name meta to TFS SUCCESS");
		} else {
			String errorInfo = "put name meta FAILED, file path: " + path
					+ ", TFS file name: " + tfsFileName + ", size: " + size;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	protected void saveFileWithNameMeta(String path, String tfsFileName) {
		logger.info("put name meta to TFS, file path: " + path
				+ ", TFS file name: " + tfsFileName);

		if (tfsManager.saveFile(appid, uid, path, tfsFileName)) {
			logger.info("put name meta to TFS SUCCESS");
		} else {
			String errorInfo = "put name meta FAILED, file path: " + path
					+ ", TFS file name: " + tfsFileName;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	/*
	 * save local file(byte[]) to tfs (weird interface, reserve for
	 * compatibility)
	 * 
	 * @param data data to save
	 * 
	 * @return tfsfilename if save successully, or null if fail
	 */
	protected String saveFile(byte[] data) {
		logger.info("put byte data to TFS, file data: " + data.toString());

		String tfsFileName = tfsManager.saveFile(data, null, null);

		if (tfsFileName != null) {
			logger.info("put byte data to TFS SUCCESS, TFS file name : "
					+ tfsFileName + ".");
			return tfsFileName;
		} else {
			String errorInfo = "put byte data to TFS FAILED, file data: "
					+ data.toString();
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	/*
	 * save local file(byte[]) to tfs (weird interface, reserve for
	 * compatibility)
	 * 
	 * @param data data to save
	 * 
	 * @param tfsFileName
	 * 
	 * @return tfsfilename if save successully, or null if fail
	 */
	protected String saveFile(byte[] data, String tfsFileName) {
		logger.info("put byte data to TFS, TFS file name: " + tfsFileName
				+ ", file data: " + data.toString());

		String name = tfsManager.saveFile(data, tfsFileName, null);
		if (name != null) {
			logger.info("put byte data to TFS SUCCESS, TFS file name : " + name
					+ ".");
			return name;
		} else {
			String errorInfo = "put byte data to TFS FAILED, TFS file name: "
					+ tfsFileName + ", file data: " + data.toString();
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	/*
	 * delete a tfs file
	 * 
	 * @param tfsFileName
	 * 
	 * @param tfsSuffix
	 * 
	 * @return true if delete successully, or false if fail
	 */
	public void delete(String tfsFileName, String suffix) {
		logger.info("delete file from TFS, TFS file name: " + tfsFileName
				+ ", with suffix: " + suffix);

		if (tfsManager.unlinkFile(tfsFileName, suffix)) {
			logger.info("delete file from TFS SUCCESS");
		} else {
			String errorInfo = "delete file from TFS FAILED, TFS file name: "
					+ tfsFileName + ", with suffix: " + suffix;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	public void delete(String tfsFileName) {
		logger.info("delete file from TFS, TFS file name: " + tfsFileName);

		if (tfsManager.unlinkFile(tfsFileName, null)) {
			logger.info("delete file from TFS SUCCESS");
		} else {
			String errorInfo = "delete file from TFS FAILED, TFS file name: "
					+ tfsFileName;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	/*
	 * hide file
	 * 
	 * @param tfsFileName
	 * 
	 * @param tfsSuffix
	 * 
	 * @param option 1 conceal 0 reveal
	 */
	protected void hide(String tfsFileName, String suffix) {
		logger.info("hide file from TFS, TFS file name: " + tfsFileName
				+ ", with suffix: " + suffix);

		if (tfsManager.hideFile(tfsFileName, null, 1)) {
			logger.info("hide file from TFS SUCCESS");
		} else {
			String errorInfo = "hide file from TFS FAILED, TFS file name: "
					+ tfsFileName + ", with suffix: " + suffix;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	/*
	 * fetch a tfsfile to output stream
	 * 
	 * @param tfsFileName
	 * 
	 * @param tfsSuffix
	 * 
	 * @param output
	 */
	protected void get(String tfsFileName, String suffix, OutputStream output) {
		logger.info("get file from TFS, TFS file name: " + tfsFileName
				+ ", with suffix: " + suffix);

		if (tfsManager.fetchFile(tfsFileName, suffix, output)) {
			logger.info("get file from TFS SUCCESS");
		} else {
			String errorInfo = "get file from TFS FAILED, TFS file name: "
					+ tfsFileName + ", with suffix: " + suffix;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	/*
	 * get a tfsfile's file info
	 */
	public FileInfo getTfsStat(String tfsFileName, String suffix) {
		logger.info("get file stat from TFS, TFS file name: " + tfsFileName
				+ ", with suffix: " + suffix);

		FileInfo fileInfo = tfsManager.statFile(tfsFileName, suffix);

		if (fileInfo != null) {
			logger.info("get file stat from TFS SUCCESS");
			return fileInfo;
		} else {
			String errorInfo = "get file stat from TFS FAILED, TFS file name: "
					+ tfsFileName + ", with suffix: " + suffix;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	/*
	 * get a tfsfile's file info
	 */
	public FileInfo getTfsStat(String tfsFileName) {
		logger.info("get file stat from TFS, TFS file name: " + tfsFileName);

		FileInfo fileInfo = tfsManager.statFile(tfsFileName, null);

		if (fileInfo != null) {
			logger.info("get file stat from TFS SUCCESS");
			return fileInfo;
		} else {
			String errorInfo = "get file stat from TFS FAILED, TFS file name: "
					+ tfsFileName;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	/*
	 * get a tfsfile's block id
	 */
	public int getTfsBlockID(String tfsFileName, String suffix)
			throws TfsException {
		logger.info("get block id from TFS, TFS file name: " + tfsFileName
				+ ", with suffix: " + suffix);

		FSName fsName = new FSName(tfsFileName, suffix);
		int id = fsName.getBlockId();

		if (id > 0) {
			logger.info("get block id from TFS SUCCESS");
			return id;
		} else {
			String errorInfo = "get block id from TFS FAILED, TFS file name: "
					+ tfsFileName + ", with suffix: " + suffix;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	/*
	 * get a tfsfile's block id
	 */
	public int getTfsBlockID(String tfsFileName) throws TfsException {
		logger.info("get block id from TFS, TFS file name: " + tfsFileName);

		FSName fsName = new FSName(tfsFileName, null);
		int id = fsName.getBlockId();

		if (id > 0) {
			logger.info("get block id from TFS SUCCESS");
			return id;
		} else {
			String errorInfo = "get block id from TFS FAILED, TFS file name: "
					+ tfsFileName;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

	/*
	 * get a tfsfile's file id
	 */
	public long getTfsFileID(String tfsFileName, String suffix)
			throws TfsException {
		logger.info("get file id from TFS, TFS file name: " + tfsFileName
				+ ", with suffix: " + suffix);

		FSName fsName = new FSName(tfsFileName, suffix);
		long id = fsName.getFileId();

		if (id > 0) {
			logger.info("get file id from TFS SUCCESS");
			return id;
		} else {
			String errorInfo = "get file id from TFS FAILED, TFS file name: "
					+ tfsFileName + ", with suffix: " + suffix;
			logger.info(errorInfo);
			throw new FailedHandle(errorInfo);
		}
	}

}
