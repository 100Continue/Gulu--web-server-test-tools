package com.taobao.gulu.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.jasypt.util.TextEncryptor;


public class Util {
	private static Logger logger = Logger.getLogger(Util.class);

	public static byte[] readFile(String filePath, int offset, int size)
			throws Exception {
		File file = new File(filePath);
		long length = file.length();
		byte[] bytes = new byte[(int) length];

		int count = 0;
		int numRead;

		InputStream is = new FileInputStream(filePath);
		while (true) {
			numRead = is.read(bytes, count, bytes.length - count);
			if (numRead == 0) {
				break;
			}
			count += numRead;
		}
		is.close();

		if (offset > 0 || size > 0) {
			byte[] readBytes = new byte[size];

			if (size > 0) {
				System.arraycopy(bytes, offset, readBytes, 0, size);
			} else {
				System.arraycopy(bytes, offset, readBytes, 0, bytes.length + 1);
			}

			return readBytes;
		} else {
			return bytes;
		}
	}
	
	public static void appendtoFile(String filepath, String content) throws IOException{
		File file = new File(filepath);
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter writer = new FileWriter(file, true); 
		
        writer.write(content);  
        writer.close();  
	}

	public static void createFile(String path, long fileSize) {
		RandomAccessFile file;
		try {
			file = new RandomAccessFile(path, "rw");
			file.setLength(fileSize);
			file.close();
		} catch (Exception e) {
			throw new FailedHandle(e.getMessage());
		}
	}
	
	// ------------------------ MD5 ------------------------------
	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	// ----------------------- Use MD5 to Verify --------------------------

	// ------ MD5 help method --------------
	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	// ------ MD5 help method ---------------

	/*
	 * convert String to MD5
	 */
	public static String getMD5(byte[] content) {
		MessageDigest messagedigest = null;

		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.info(Util.class.getName()
					+ "initialize MD5Util fail!");
			logger.info(e);
			throw new FailedHandle(e.getMessage());
		}

		// reset messagedigest
		messagedigest.reset();
		messagedigest.update(content);
		return bufferToHex(messagedigest.digest());
	}
	
	public static String getMD5(InputStream inputStream) throws Exception{
		return getMD5(inputStream, 0, 0);
	}
	
	public static String getMD5(InputStream inputStream, int offset, int size) throws Exception{
		int length = 0;
		int offsetCount = 0;
		int sizeCount = 0;
		byte[] dataArray = new byte[10240];

		MessageDigest messagedigest = null;
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}

		// reset messagedigest
		messagedigest.reset();

		while ((length = inputStream.read(dataArray)) != -1) {
			offsetCount += length;
			if(offsetCount < offset){
				continue;
			}
			if(size > 0){
				sizeCount += length;
				if(sizeCount <= size)
					messagedigest.update(dataArray, 0, length);
				else
					break;
			}else{
				messagedigest.update(dataArray, 0, length);
			}
		}

		return bufferToHex(messagedigest.digest());
		
	}
	
	public static String getEncryptedPasswords(String rawPasswords) {
		TextEncryptor textEncryptor = new TextEncryptor();
		textEncryptor.setPassword("password");
		return textEncryptor.encrypt(rawPasswords);
	}
}
