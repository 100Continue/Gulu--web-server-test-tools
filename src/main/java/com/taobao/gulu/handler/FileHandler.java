package com.taobao.gulu.handler;

import java.util.List;

/**
 * <p>Title: FileHandler.java</p>
 * <p>Description: File Handler Interface</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public interface FileHandler 
{
	/**
	 * <p>Title: copyFile</p>
	 * <p>Description: copy file from source server to destination server</p>
	 * @param srcServer - source server IP
	 * @param srcFile - file path for the file you want to copy
	 * @param destServer - destination server IP
	 * @param destDir - the place to store copy file. empty means uses the source file path
	 * @return OperationResult
	 */
	public OperationResult copyFile(String srcServer, String srcFile, String destServer, String destDir);
	
	/**
	 * <p>Title: downloadFile</p>
	 * <p>Description: download file from source server to local server</p>
	 * @param srcServer - source server IP
	 * @param srcFile - file path for the file you want to download
	 * @param localDir - the place to store the download file
	 * @return OperationResult
	 */
	public OperationResult downloadFile(String srcServer, String srcFile, String localDir);
	
	
	/**
	 * <p>Title: uploadFile</p>
	 * <p>Description: upload file to destination server</p>
	 * @param destServer - destination server IP
	 * @param destDir - the place to store upload file in destination server
	 * @param localDir - file path for the file you want to upload
	 * @return OperationResult
	 */
	public OperationResult uploadFile(String destServer, String destDir, String localDir);
	
	/**
	 * <p>Title: copyFile</p>
	 * <p>Description: copy file from source server to destination server</p>
	 * @param srcUri - format like: 10.232.129.51:/home/admin/workspace/test.log
	 * @param destUri - format like: 10.232.129.51:/home/admin/workspace
	 * @return OperationResult
	 */
	public OperationResult copyFile(String srcUri, String destUri);
	
	/**
	 * <p>Title: copyRenameFile</p>
	 * <p>Description: copy file from source server to destination server and rename the copy file name</p>
	 * @param srcServer - source server IP
	 * @param srcFile - file path for the file you want to copy
	 * @param destServer - destination server
	 * @param destFile - the place to store the copy file and the new file name
	 * @return OperationResult
	 */
	public OperationResult copyRenameFile(String srcServer, String srcFile, String destServer, String destFile);

	/**
	 * <p>Title: copyDirectory</p>
	 * <p>Description: copy the folder from source server to destination server</p>
	 * @param srcServer - source server IP
	 * @param srcDir - folder path for the folder you want to copy
	 * @param destServer - destination server IP
	 * @param destDir - the place to store the copy folder. empty means uses the source folder path to store
	 * @return OperationResult
	 */
	public OperationResult copyDirectory(String srcServer, String srcDir, String destServer, String destDir);
	
	/**
	 * <p>Title: copyDirectory</p>
	 * <p>Description: copy the folder from source server to destination server</p>
	 * @param srcUri - format like: 10.232.129.51:/home/admin/workspace
	 * @param destUri - format like: 10.232.129.52:/home/admin/workspace
	 * @return OperationResult
	 */
	public OperationResult copyDirectory(String srcUri, String destUri);
	
	/**
	 * <p>Title: downloadDirectory</p>
	 * <p>Description: download the folder from source server to local server</p>
	 * @param srcServer - source server IP
	 * @param srcDir - folder path for the folder you want to download
	 * @param localDir - the place to store the download folder
	 * @return OperationResult
	 */
	public OperationResult downloadDirectory(String srcServer, String srcDir, String localDir); 
	
	/**
	 * <p>Title: uploadDirectory</p>
	 * <p>Description: upload local folder to  destination server</p>
	 * @param destServer - destination server IP
	 * @param destDir - the place to store the upload file in destination server
	 * @param localDir - the folder you want to upload in local server
	 * @return OperationResult
	 */
	public OperationResult uploadDirectory(String destServer, String destDir, String localDir);

	/**
	 * <p>Title: copyRenameDirectory</p>
	 * <p>Description: upload local folder to  destination server and rename the folder</p>
	 * @param srcServer - source server IP
	 * @param srcDir - folder path for the folder you want to copy
	 * @param destServer - destination server IP
	 * @param destPath - the place to store the copy folder and rename it
	 * @return OperationResult
	 */
	public OperationResult copyRenameDirectory(String srcServer, String srcDir, String destServer, String destPath);
	
	/**
	 * <p>Title: mkdir</p>
	 * <p>Description: create a directory in destination server</p>
	 * @param destServer - destination server IP
	 * @param dir - the directory you want to create
	 * @return OperationResult
	 */
	public OperationResult mkdir(String destServer, String dir);
	
	
	/**
	 * <p>Title: listDirectory</p>
	 * <p>Description: get the destination server folder file list</p>
	 * @param destServer - destination server IP
	 * @param dir - the directory you want to get file list
	 * @return List<String> file name list
	 */
	public List<String> listDirectory(String destServer, String dir);
	
	/**
	 * <p>Title: isEntryExisted</p>
	 * <p>Description: check the file or folder exist or not in destination server</p>
	 * @param destServer - destination server IP
	 * @param entryName - the file or folder you want to check
	 * @return ture / false
	 */
	public boolean isEntryExisted(String destServer, String entryName);
	
	/**
	 * <p>Title: deleteEntry</p>
	 * <p>Description: delete the file or directory in destination server</p>
	 * @param destServer - destination server IP
	 * @param entryName - the file or folder path you want to delete
	 * @param isKeepDir - if the entry name is directory, keep the directory folder or not
	 * @return OperationResult
	 */
	public OperationResult deleteEntry(String destServer, String entryName, boolean isKeepDir);

	/**
	 * <p>Title: deleteEntry</p>
	 * <p>Description: delete the file or directory in destination server</p>
	 * @param entryUri - format like: 10.232.129.51:/home/admin/workspace
	 * @param isKeepDir - if the entry uri is directory, keep the empty directory or not
	 * @return OperationResult
	 */
	public OperationResult deleteEntry(String entryUri, boolean isKeepDir);

	/**
	 * <p>Title: rename</p>
	 * <p>Description: rename the file or folder name in destination server</p>
	 * @param destServer - destination server IP
	 * @param entryName - the file or folder you want to rename
	 * @param newEntryName - the new file or folder name
	 * @return OperationResult
	 */
	public OperationResult rename(String destServer, String entryName, String newEntryName);
	
	/**
	 * <p>Title: getContent</p>
	 * <p>Description: get the file contain which match the key word or regular expression in destination server</p>
	 * @param destServer - destination server IP
	 * @param filePath - file path
	 * @param regexStr - key words or regular expression
	 * @return 
	 */
	public String getContent(String destServer, String filePath,  String regexStr);
	
	/**
	 * <p>Title: setMode</p>
	 * <p>Description: chmod file in destination server</p>
	 * @param destServer - destination server IP
	 * @param entryName - the file path you want to chmod
	 * @param mode - mode likes: 777, 742
	 * @param isRecursive - whether recursive change file and folder mode or not
	 * @return OperationResult
	 */
	public OperationResult setMode(String destServer, String entryName, int mode, boolean isRecursive);
	
	/**
	 * <p>Title: setOwner</p>
	 * <p>Description: set file or folder owner</p>
	 * @param destServer - destination server IP
	 * @param entryName - the file or folder to change owner
	 * @param owner - the owner for the file or folder
	 * @param group - the group for the file or folder
	 * @param isRecursive - whether recursive change file and folder owner or not
	 * @return OperationResult
	 */
	public OperationResult setOwner(String destServer, String entryName, String owner, String group, boolean isRecursive);

	/**
	 * <p>Title: IsDirectory</p>
	 * <p>Description: check is a directory or not</p>
	 * @param destServer - destination server IP
	 * @param entryName -  the directory path
	 * @return return ture / false
	 */
	public boolean IsDirectory(String destServer, String entryName);

}
