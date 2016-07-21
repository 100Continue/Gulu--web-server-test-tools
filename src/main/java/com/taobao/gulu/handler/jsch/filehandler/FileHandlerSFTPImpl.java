package com.taobao.gulu.handler.jsch.filehandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpProgressMonitor;
import com.taobao.gulu.handler.FileHandler;
import com.taobao.gulu.handler.OperationResult;
import com.taobao.gulu.handler.jsch.authorization.AuthorizationInterface;
import com.taobao.gulu.handler.jsch.processhandler.ProcessHandlerExecImpl;
import com.taobao.gulu.tools.OperationException;

/**
 * <p>
 * Title: FileHandlerSFTPImpl.java
 * </p>
 * <p>
 * Description: file handler implement in SFTP
 * </p>
 * 
 * @author: gongyuan.cz
 * @email: gongyuan.cz@taobao.com
 * @blog: 100continue.iteye.com
 */
public class FileHandlerSFTPImpl implements FileHandler {
	private static Logger log = Logger.getLogger(FileHandlerSFTPImpl.class);
	private String tmpFile = "src/main/resources/fileHandlerTmpFile";
	private String tmpFolder = "src/main/resources/fileHandlerTmpFolder/";
	private AuthorizationInterface authorization;
	private ChannelSftp channel = null;

	public FileHandlerSFTPImpl(AuthorizationInterface authorization) {
		this.authorization = authorization;
	}

	private void setExecChannelProvider(AuthorizationInterface authorization)
			throws OperationException {
		this.channel = new SFTPChannelProvider(authorization).getChannel();
	}

	public OperationResult downloadFile(String srcServer, String srcFile,
			String localDir) {
		if (srcFile == null || "".equals(srcServer) || "".equals(srcFile))
			return new OperationResult(
					"The transfered file is null or the source IP is empty!",
					false);
		try {
			// set channel
			this.authorization.setHost(srcServer);
			setExecChannelProvider(authorization);

			// download file to local tmp file
			@SuppressWarnings("unchecked")
			Vector<ChannelSftp.LsEntry> list = channel.ls(srcFile);
			channel.get(srcFile, localDir, new FileProgressMonitor(list.get(0)
					.getAttrs().getSize()));

			disconnect();
		} catch (Exception e) {
			return new OperationResult(e.toString(), false);
		}

		return new OperationResult("download file " + srcFile + " from "
				+ srcServer + "to local file " + localDir + " success.", true);

	}

	public OperationResult uploadFile(String destServer, String destDir,
			String localDir) {
		if (localDir == null || "".equals(destServer) || "".equals(localDir)
				|| "".equals(destDir))
			return new OperationResult(
					"The transfered file is null or the dest dir or the dest IP is empty!",
					false);
		try {
			// set channel
			this.authorization.setHost(destServer);
			setExecChannelProvider(authorization);

			// upload local tmp file to dest dir
			File file = new File(localDir);
			channel.put(localDir, destDir,
					new FileProgressMonitor(file.length()),
					ChannelSftp.OVERWRITE);

			disconnect();
		} catch (Exception e) {
			return new OperationResult(e.toString(), false);
		}

		return new OperationResult("upload local file " + localDir + " to "
				+ destServer + " in dir " + destDir + " success.", true);

	}

	@Override
	public OperationResult copyFile(String srcServer, String srcFile,
			String destServer, String destDir) {
		if (srcFile == null || "".equals(srcServer) || "".equals(destServer))
			return new OperationResult(
					"The transfered file is null or the source or destination IP is empty!",
					false);

		try {
			// delete local tmp file
			deleteFile(tmpFile);

			// download file to local tmp file
			OperationResult downloadResult = downloadFile(srcServer, srcFile,
					tmpFile);
			if (!downloadResult.isSuccess()) {
				throw new OperationException(downloadResult.getMsg());
			}

			// upload local tmp file to dest dir
			OperationResult uploadResult;
			if ("".equals(destDir))
				uploadResult = uploadFile(destServer, srcFile, tmpFile);
			else
				uploadResult = uploadFile(destServer, destDir, tmpFile);

			if (!uploadResult.isSuccess()) {
				throw new OperationException(uploadResult.getMsg());
			}

			OperationResult result = new OperationResult();
			result.setMsg("copy file from " + srcFile + " from " + srcServer
					+ " to " + destServer + " in " + destDir + " success");
			result.setReturnCode(0);
			result.setSuccess(true);

			// delete local tmp file
			deleteFile(tmpFile);

			return result;

		} catch (Exception e) {
			log.error(e.toString());
			log.error("sorry can not put " + srcFile + " from " + srcServer
					+ " to " + destServer + " in " + destDir);
			disconnect();
			// delete local tmp file
			deleteFile(tmpFile);
			return new OperationResult(e.toString(), false);
		}

	}

	@Override
	public OperationResult copyFile(String srcUri, String destUri) {
		String fromServer = srcUri.substring(0, srcUri.indexOf(":"));
		String fromFile = srcUri.substring(srcUri.indexOf(":") + 1);
		String destServer = destUri.substring(0, destUri.indexOf(":"));
		String destFile = destUri.substring(destUri.indexOf(":") + 1);

		return copyFile(fromServer, fromFile, destServer, destFile);
	}

	@Override
	public OperationResult copyRenameFile(String srcServer, String srcFile,
			String destServer, String destFile) {
		return copyFile(srcServer, srcFile, destServer, destFile);
	}

	public OperationResult downloadDirectory(String srcServer, String srcDir,
			String localDir) {
		if (srcDir == null || "".equals(srcServer) || "".equals(srcDir))
			return new OperationResult(
					"The transfered dir is null or the source IP is empty!",
					false);

		try {
			// set channel
			this.authorization.setHost(srcServer);
			setExecChannelProvider(authorization);

			// download directory to local tmp folder
			channel.cd(srcDir);
			File dir = new File(localDir);
			if (!dir.exists())
				dir.mkdir();

			@SuppressWarnings("unchecked")
			Vector<ChannelSftp.LsEntry> list = channel.ls("*");
			downloadDirectory(list, localDir);

			disconnect();
		} catch (Exception e) {
			return new OperationResult(e.toString(), false);
		}

		return new OperationResult("download dir " + srcDir + " from "
				+ srcServer + " to local dir " + localDir + " success.", true);

	}

	public OperationResult uploadDirectory(String destServer, String destDir,
			String localDir) {
		if (destDir == null || "".equals(destServer) || "".equals(destDir)
				|| "".equals(localDir))
			return new OperationResult(
					"The local file dir or the dest dir or the dest IP is empty!",
					false);
		try {
			// set channel
			this.authorization.setHost(destServer);
			setExecChannelProvider(authorization);

			// upload local tmp folder to remote server directory
			try {
				channel.ls(destDir);
			} catch (Exception e) {
				channel.mkdir(destDir);
			}

			File dir = new File(localDir);
			File file[] = dir.listFiles();
			uploadDirectory(file, destDir, new FileProgressMonitor(),
					ChannelSftp.OVERWRITE);

			// session and channel disconnect
			disconnect();

		} catch (Exception e) {
			return new OperationResult(e.toString(), false);
		}

		return new OperationResult("upload local file directory " + localDir
				+ " to " + destServer + " in dir " + destDir + " success.",
				true);

	}

	@Override
	public OperationResult copyDirectory(String srcServer, String srcDir,
			String destServer, String destDir) {
		try {
			// delete local tmp folder
			deleteDirectory(tmpFolder);

			// download directory to local tmp folder
			OperationResult downloadResult = downloadDirectory(srcServer,
					srcDir, tmpFolder);
			if (!downloadResult.isSuccess()) {
				throw new OperationException(downloadResult.getMsg());
			}

			// upload local tmp folder to remote server directory
			OperationResult uploadResult;
			if ("".equals(destDir))
				uploadResult = uploadDirectory(destServer, srcDir, tmpFolder);
			else
				uploadResult = uploadDirectory(destServer, destDir, tmpFolder);

			if (!uploadResult.isSuccess()) {
				throw new OperationException(uploadResult.getMsg());
			}

			OperationResult result = new OperationResult();
			result.setMsg("copy file from " + srcDir + " from " + srcServer
					+ " to " + destServer + "in " + destDir + " success");
			result.setReturnCode(0);
			result.setSuccess(true);

			// delete local tmp folder
			deleteDirectory(tmpFolder);

			return result;

		} catch (Exception e) {
			log.error(e.toString());
			log.error("sorry can not put " + srcDir + " from " + srcServer
					+ " to " + destServer + " in " + destDir);
			disconnect();
			// delete local tmp folder
			deleteDirectory(tmpFolder);
			return new OperationResult(e.toString(), false);
		}
	}

	@Override
	public OperationResult copyDirectory(String srcUri, String destUri) {
		String fromServer = srcUri.substring(0, srcUri.indexOf(":"));
		String fromDir = srcUri.substring(srcUri.indexOf(":") + 1);

		String destServer = destUri.substring(0, destUri.indexOf(":"));
		String destDir = destUri.substring(destUri.indexOf(":") + 1);

		return copyDirectory(fromServer, fromDir, destServer, destDir);
	}

	@Override
	public OperationResult copyRenameDirectory(String srcServer, String srcDir,
			String destServer, String destPath) {
		return copyDirectory(srcServer, srcDir, destServer, destPath);
	}

	@Override
	public OperationResult mkdir(String destServer, String dir) {
		if ("".equals(dir) || "".equals(destServer))
			return new OperationResult(
					"create directory false. error message: should have directory or destserver",
					false);

		try {
			// set channel
			this.authorization.setHost(destServer);
			setExecChannelProvider(authorization);

			channel.mkdir(dir);
			return new OperationResult("create directory: " + dir
					+ " at server: " + destServer + " success.", true);
		} catch (Exception e) {
			log.error(e.toString());
			return new OperationResult("create directory: " + dir
					+ " at server: " + destServer + " false. error message: "
					+ e.toString(), false);
		}
	}

	@Override
	public List<String> listDirectory(String destServer, String dir) {
		if ("".equals(dir) || "".equals(destServer))
			return null;

		try {
			// set channel
			this.authorization.setHost(destServer);
			setExecChannelProvider(authorization);

			List<String> list = listDir(dir);

			disconnect();

			return list;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean isEntryExisted(String destServer, String entryName) {
		if ("".equals(entryName) || "".equals(destServer))
			return false;

		try {
			// set channel
			this.authorization.setHost(destServer);
			setExecChannelProvider(authorization);

			return isFileExit(entryName);
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public OperationResult deleteEntry(String destServer, String entryName,
			boolean isKeepDir) {

		if ("".equals(entryName) || "".equals(destServer))
			return new OperationResult(
					"The dirtory speicified is empty or the destination IP is empty",
					false);
		else {
			try {
				// set channel
				this.authorization.setHost(destServer);
				setExecChannelProvider(authorization);

				if (channel.stat(entryName).isDir()) {
					rmDirectory(entryName);
					if (isKeepDir) {
						channel.mkdir(entryName);
					}
				} else {
					channel.rm(entryName);
				}
				return new OperationResult("delete entry success.", true);

			} catch (Exception e) {
				return new OperationResult(e.toString(), false);
			}

		}
	}

	@Override
	public OperationResult deleteEntry(String entryUri, boolean isKeepDir) {
		String[] content = entryUri.split(":");
		if (content.length != 2) {
			String msg = "wrong format of fielUri: " + entryUri;
			log.error(msg);
			return new OperationResult(msg, false);
		}
		String destServer = content[0];
		String destFile = content[1];

		return deleteEntry(destServer, destFile, isKeepDir);
	}

	@Override
	public OperationResult rename(String destServer, String entryName,
			String newEntryName) {
		if (entryName == null || "".equals(destServer)
				|| "".equals(newEntryName))
			return new OperationResult(
					"The specified file is null or the target IP or new file name is empty!",
					false);

		try {
			// set channel
			this.authorization.setHost(destServer);
			setExecChannelProvider(authorization);

			channel.rename(entryName, newEntryName);

			return new OperationResult("rename new entry name success.", true);

		} catch (Exception e) {
			return new OperationResult(e.toString(), false);
		}

	}

	@Override
	public String getContent(String destServer, String filePath, String regexStr) {
		String strCmd = "cat " + filePath + " | grep -P '" + regexStr + "'";
		String content = "";
		try {
			this.authorization.setHost(destServer);
			ProcessHandlerExecImpl processHandler = new ProcessHandlerExecImpl(
					authorization);
			content = processHandler.executeCmd(destServer, strCmd, false).getMsg();
		} catch (OperationException e) {
			e.printStackTrace();
		}
		
		return content;
	}

	@Override
	public OperationResult setMode(String destServer, String entryName,
			int mode, boolean isRecursive) {
		if (entryName == null || "".equals(destServer))
			return new OperationResult(
					"The specified file is null or the target IP is empty!",
					false);

		try {
			// set channel
			this.authorization.setHost(destServer);
			setExecChannelProvider(authorization);

			if (channel.stat(entryName).isDir() && isRecursive) {
				channel.chmod(mode, entryName);
				chmodDir(mode, entryName);
			} else {
				channel.chmod(mode, entryName);
			}

			return new OperationResult("chmod " + entryName + " to mode "
					+ mode + " success.", true);

		} catch (Exception e) {
			return new OperationResult(e.toString(), false);
		}
	}

	@Override
	public OperationResult setOwner(String destServer, String entryName,
			String owner, String group, boolean isRecursive) {
		String cmd = "";

		if (isRecursive)
			cmd = "chown -R " + owner + ":" + group + " " + entryName;
		else
			cmd = "chown " + owner + ":" + group + " " + entryName;

		OperationResult result = null;
		try {
			this.authorization.setHost(destServer);
			ProcessHandlerExecImpl processHandler = new ProcessHandlerExecImpl(
					authorization);
			result = processHandler.executeCmd(destServer, cmd, false);
		} catch (OperationException e) {
			e.printStackTrace();
		}

		return result;
	}

	public OperationResult setOwner(String destServer, String entryName,
			int uid, boolean isRecursive) {
		if (entryName == null || "".equals(destServer))
			return new OperationResult(
					"The specified file is null or the target IP is empty!",
					false);

		try {
			// set channel
			this.authorization.setHost(destServer);
			setExecChannelProvider(authorization);

			if (channel.stat(entryName).isDir() && isRecursive) {
				channel.chown(uid, entryName);
				chownDir(uid, entryName);
			} else {
				channel.chown(uid, entryName);
			}

			return new OperationResult("chmod " + entryName + " to uid " + uid
					+ " success.", true);

		} catch (Exception e) {
			return new OperationResult(e.toString(), false);
		}
	}

	@Override
	public boolean IsDirectory(String destServer, String entryName) {
		try {
			// set channel
			this.authorization.setHost(destServer);
			setExecChannelProvider(authorization);

			return isDir(entryName);

		} catch (Exception e) {
			return false;
		}
	}

	private boolean isDir(String entryName) {
		try {
			if (channel.stat(entryName).isDir()) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private void downloadDirectory(Vector<ChannelSftp.LsEntry> list, String dst)
			throws OperationException {
		try {
			for (ChannelSftp.LsEntry file : list) {
				if (file.getAttrs().isDir()) {
					String newDir = file.getFilename();
					channel.cd(file.getFilename());
					File localfile = new File(dst + "/" + newDir);
					localfile.mkdir();
					downloadDirectory(channel.ls("*"),
							localfile.getAbsolutePath());
					channel.cd("..");
				} else {
					channel.get(file.getFilename(), dst,
							new FileProgressMonitor(file.getAttrs().getSize()));
				}

			}

		} catch (Exception e) {
			log.error(e.toString());
			throw new OperationException(e.toString());
		}
	}

	private void uploadDirectory(File[] file, String dst,
			SftpProgressMonitor monitor, int mode) throws OperationException {
		try {
			for (int i = 0; i < file.length; i++) {
				if (file[i].isDirectory()) {
					File newfile[] = file[i].listFiles();
					String newDst = dst + "/" + file[i].getName();
					try {
						channel.ls(newDst);
					} catch (Exception e) {
						channel.mkdir(newDst);
					}
					uploadDirectory(newfile, newDst, monitor, mode);
				} else {
					channel.put(file[i].getAbsolutePath(), dst, monitor, mode);

				}
			}
		} catch (Exception e) {
			if (file == null) {
				log.error("you should use directory to upload.");
			}
			log.error(e.toString());
			throw new OperationException(e);
		}
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

	// delete the directory
	private void deleteDirectory(String folderPath) {
		try {
			deleteAllFile(folderPath); // delete the file in directory
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // delete the empty directory
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// delete the file in directory, path is a directory
	private boolean deleteAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		String filename;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				filename = path + tempList[i];
			} else {
				filename = path + File.separator + tempList[i];
			}
			deleteFile(filename);

			if (new File(filename).isDirectory()) {
				deleteAllFile(path + File.separator + tempList[i]);// delete the
																	// file in
																	// directory
				deleteDirectory(path + File.separator + tempList[i]);// delete
																		// the
																		// empty
																		// directory
				flag = true;
			}
		}
		return flag;
	}

	private void deleteFile(String filepath) {
		File file = new File(filepath);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
	}

	// use sftp to remove directory
	private void rmDirectory(String folderPath) {
		try {
			rmAllFile(folderPath); // remove the file in directory
			channel.rmdir(folderPath); // remove the empty directory
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// use sftp to remove the file in directory, path is the directory
	private boolean rmAllFile(String path) {
		try {
			boolean flag = false;
			if (!isFileExit(path)) {
				return flag;
			}
			if (!channel.stat(path).isDir()) {
				return flag;
			}
			List<String> tempList = listDir(path);
			String filename;
			for (int i = 0; i < tempList.size(); i++) {
				if (!tempList.get(i).equals(".")
						&& !tempList.get(i).equals("..")) {
					if (path.endsWith("/")) {
						filename = path + tempList.get(i);
					} else {
						filename = path + "/" + tempList.get(i);
					}

					if (isFileExit(filename)) {
						if (channel.stat(filename).isDir()) {
							rmAllFile(path + "/" + tempList.get(i));// remove
																	// the file
																	// in
																	// directory
							rmDirectory(path + "/" + tempList.get(i));// remove
																		// the
																		// empty
																		// directory
							flag = true;
						} else {
							rmFile(filename);
						}
					}
				}
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void rmFile(String filepath) {
		try {
			if (isFileExit(filepath))
				channel.rm(filepath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void chmodDir(int mode, String path) throws OperationException {
		try {
			if (!isFileExit(path)) {
				return;
			}
			if (!channel.stat(path).isDir()) {
				return;
			}
			List<String> tempList = listDir(path);
			String filename;
			for (int i = 0; i < tempList.size(); i++) {
				if (!tempList.get(i).equals(".")
						&& !tempList.get(i).equals("..")) {
					if (path.endsWith("/")) {
						filename = path + tempList.get(i);
					} else {
						filename = path + "/" + tempList.get(i);
					}

					if (isFileExit(filename)) {
						if (channel.stat(filename).isDir()) {
							channel.chmod(mode, path + "/" + tempList.get(i));
							chmodDir(mode, path + "/" + tempList.get(i));
						} else {
							channel.chmod(mode, filename);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OperationException(e.toString());
		}
	}

	private void chownDir(int uid, String path) throws OperationException {
		try {
			if (!isFileExit(path)) {
				return;
			}
			if (!channel.stat(path).isDir()) {
				return;
			}
			List<String> tempList = listDir(path);
			String filename;
			for (int i = 0; i < tempList.size(); i++) {
				if (!tempList.get(i).equals(".")
						&& !tempList.get(i).equals("..")) {
					if (path.endsWith("/")) {
						filename = path + tempList.get(i);
					} else {
						filename = path + "/" + tempList.get(i);
					}

					if (isFileExit(filename)) {
						if (channel.stat(filename).isDir()) {
							channel.chown(uid, path + "/" + tempList.get(i));
							chownDir(uid, path + "/" + tempList.get(i));
						} else {
							channel.chown(uid, filename);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OperationException(e.toString());
		}
	}

	private boolean isFileExit(String path) {
		try {
			channel.ls(path);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private List<String> listDir(String dir) {
		try {
			List<String> list = new ArrayList<String>();
			@SuppressWarnings("unchecked")
			Vector<ChannelSftp.LsEntry> vector = channel.ls(dir);
			for (ChannelSftp.LsEntry file : vector) {
				list.add(file.getFilename());
			}
			return list;
		} catch (Exception e) {
			return null;
		}
	}
}
