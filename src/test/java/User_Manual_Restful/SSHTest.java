package User_Manual_Restful;
import java.util.List;

import org.junit.Test;

import Base.BaseCase;

import com.jcraft.jsch.SftpException;
import com.taobao.gulu.handler.OperationResult;
import com.taobao.gulu.handler.jsch.authorization.PasswordAuthorization;
import com.taobao.gulu.handler.jsch.filehandler.FileHandlerSFTPImpl;
import com.taobao.gulu.handler.jsch.processhandler.ProcessHandlerExecImpl;
import com.taobao.gulu.tools.OperationException;

public class SSHTest extends BaseCase {

	
//	@Test
//	public void test_getPasswordAuthorization() {
//		System.out.println(Util.getPasswordAuthorization("password"));
//	}

	@Test
	public void test_Process() throws OperationException {
		
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		ProcessHandlerExecImpl process2 = new ProcessHandlerExecImpl(
				passwords);
		OperationResult result = process2.executeCmd("xx.xxx.x.29",
				"./sleep.sh ", true);
		System.out.println(result);
				
		int[] id = process2.getPidByProcName("username", "xx.xxx.x.29",
				"./sleep.sh");
		if (id != null)
			for (int i = 0; i < id.length; i++)
				System.out.println("id : " + id[i]);

		id = process2.getPidByProcName("admin", "xx.xxx.x.29", "./sleep.sh");
		if (id != null)
			for (int i = 0; i < id.length; i++)
				System.out.println("id : " + id[i]);

		result = process2.killProcess("xx.xxx.x.29", "sleep.sh");
		System.out.println("msg : " + result.getMsg());
		System.out.println("exit code : " + result.getReturnCode());
		System.out.println("is success : " + result.isSuccess());
	}
	
	@Test
	public void test_LocalProcess() throws OperationException {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"", "");
		ProcessHandlerExecImpl process2 = new ProcessHandlerExecImpl(
				passwords);
		OperationResult result = process2.executeCmd("",
				"ipconfig /all", false);
		System.out.println(result);
	}

	@Test
	public void test_Sudo() throws OperationException {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		ProcessHandlerExecImpl process2 = new ProcessHandlerExecImpl(
				passwords);
		OperationResult result = process2.executeCmdByRoot("xx.xxx.x.29", "ls",
				false);

		System.out.println(result);

		result = process2.executeCmd("xx.xxx.x.29", "./sleep.sh ", true);

		System.out.println(result);
	}

	@Test
	public void test_localhost() throws OperationException {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		ProcessHandlerExecImpl process2 = new ProcessHandlerExecImpl(
				passwords);
		OperationResult result = process2.executeCmdByRoot("xx.xxx.x.29", "ls",
				false);
		System.out.println(result);
	}

	@Test
	public void test_error() throws OperationException {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		ProcessHandlerExecImpl process2 = new ProcessHandlerExecImpl(
				passwords);
		OperationResult result = process2.executeCmdByRoot("10.232.4.110",
				"ls", false);
		System.out.println(result);
	}
	
	@Test
	public void test_errorInfo() throws OperationException {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		ProcessHandlerExecImpl process2 = new ProcessHandlerExecImpl(
				passwords);
		System.out.println(process2.executeCmdByRoot("xx.xxx.x.29",
				"/home/admin/nginx/sbin/nginx -c xx -s start", false));
	}

	@Test
	public void test_copyFile() throws OperationException {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		FileHandlerSFTPImpl fileHandler = new FileHandlerSFTPImpl(
				passwords);
		OperationResult result = fileHandler.copyFile("xx.xxx.x.29",
				"/home/admin/log.log", "xx.xxx.x.31",
				"/home/admin/copyfile_log");
		System.out.println(result);
	}

	@Test
	public void test_copyFolder() throws OperationException {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		FileHandlerSFTPImpl fileHandler = new FileHandlerSFTPImpl(
				passwords);
		OperationResult result = fileHandler.copyDirectory("xx.xxx.x.29",
				"/home/admin/sql/", "xx.xxx.x.31",
				"/home/admin/copyfolder");
		System.out.println(result);
	}

	@Test
	public void test_mkdir() {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		FileHandlerSFTPImpl fileHandler = new FileHandlerSFTPImpl(
				passwords);
		if (fileHandler.mkdir("xx.xxx.x.29", "/home/admin/ttttdd").isSuccess()) {
			System.out.println("true");
		} else {
			System.out.println("false");
		}

	}

	@Test
	public void test_listDirectory() throws OperationException {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		FileHandlerSFTPImpl fileHandler = new FileHandlerSFTPImpl(
				passwords);
		@SuppressWarnings("rawtypes")
		List list = fileHandler.listDirectory("xx.xxx.x.29",
				"/home/admin/sql/");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}
		}
	}

	@Test
	public void test_isEntryExisted() throws OperationException {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		FileHandlerSFTPImpl fileHandler = new FileHandlerSFTPImpl(
				passwords);
		if (fileHandler.isEntryExisted("xx.xxx.x.29",
				"/home/admin/ttttdd/t")) {
			System.out.println("true");
		} else {
			System.out.println("false");
		}
	}

	@Test
	public void test_deleteEntry() throws SftpException, OperationException {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		FileHandlerSFTPImpl fileHandler = new FileHandlerSFTPImpl(
				passwords);
		System.out.println(fileHandler.copyDirectory("xx.xxx.x.29",
				"/home/admin/sql/", "xx.xxx.x.29",
				"/home/admin/copyfolder"));
		System.out.println(fileHandler.copyDirectory("xx.xxx.x.29",
				"/home/admin/sql/", "xx.xxx.x.29",
				"/home/admin/copyfolder2"));

		System.out.println(fileHandler.deleteEntry("xx.xxx.x.29",
				"/home/admin/sql/t", false));
		System.out.println(fileHandler.deleteEntry("xx.xxx.x.29",
				"/home/admin/copyfolder/", false));
		System.out.println(fileHandler.deleteEntry("xx.xxx.x.29",
				"/home/admin/copyfolder2/", true));

		System.out.println(fileHandler.deleteEntry("xx.xxx.x.29",
				"/home/admin/test.log", true));
	}

	@Test
	public void test_rename1() {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		FileHandlerSFTPImpl fileHandler = new FileHandlerSFTPImpl(
				passwords);
		System.out.println(fileHandler.rename("xx.xxx.x.29",
				"/home/admin/ttttdd/", "/home/admin/copyfolder"));
		System.out.println(fileHandler.rename("xx.xxx.x.29",
				"/home/admin/update_logic_a_read.sql",
				"/home/admin/sql.sql.sql"));

	}

	@Test
	public void test_rename2() {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		FileHandlerSFTPImpl fileHandler = new FileHandlerSFTPImpl(
				passwords);
		System.out.println(fileHandler.rename("xx.xxx.x.29",
				"/home/admin/copyfolder", "/home/admin/ttttdd/"));
		System.out.println(fileHandler.rename("xx.xxx.x.29",
				"/home/admin/sql.sql.sql",
				"/home/admin/update_logic_a_read.sql"));

	}

	@Test
	public void test_setMode() {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		FileHandlerSFTPImpl fileHandler = new FileHandlerSFTPImpl(
				passwords);
		System.out.println(fileHandler.setMode("xx.xxx.x.29",
				"/home/admin/xx.jpg", 7777, true));
		System.out.println(fileHandler.setMode("xx.xxx.x.29",
				"/home/admin/ttttdd", 7000, true));

	}

	@Test
	public void test_setOwner() {
		PasswordAuthorization passwords = new PasswordAuthorization(
				"username", "XXXXX7Eo7InqWEnnnFFqw788FvvOCZ7H");
		FileHandlerSFTPImpl fileHandler = new FileHandlerSFTPImpl(
				passwords);
		System.out.println(fileHandler.setOwner("xx.xxx.x.29",
				"/home/admin/xx.jpg", 1, true));
		System.out.println(fileHandler.setOwner("xx.xxx.x.29",
				"/home/admin/ttttdd", 0, true));

	}
}
