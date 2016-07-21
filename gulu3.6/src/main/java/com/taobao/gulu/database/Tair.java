//package com.taobao.gulu.database;
//
//import java.util.ArrayList;
//import java.util.List;
//import org.apache.log4j.Logger;
//
//import com.taobao.gulu.tools.FailedHandle;
//import com.taobao.tair.DataEntry;
//import com.taobao.tair.Result;
//import com.taobao.tair.ResultCode;
//import com.taobao.tair.impl.DefaultTairManager;
//
//
///**
// * <p>Title: Tair.java</p>
// * <p>Description: tair manange tools</p>
// * @author: gongyuan.cz
// * @email:  gongyuan.cz@taobao.com
// * @blog:   100continue.iteye.com
// */
//public class Tair {
//	private Logger logger = Logger.getLogger(Tair.class);
//
//	private DefaultTairManager tairManager = new DefaultTairManager();
//	private int area = 1;
//	private String master_address = "0.0.0.0:0000";
//	private String slave_address = "0.0.0.0:0000";
//	private String group_name = "group_1";
//	private int time_out = 2000;
//
//	public int getArea() {
//		return area;
//	}
//
//	public void setArea(int area) {
//		this.area = area;
//	}
//
//	public String getMaster_address() {
//		return master_address;
//	}
//
//	public void setMaster_address(String master_address) {
//		this.master_address = master_address;
//	}
//
//	public String getSlave_address() {
//		return slave_address;
//	}
//
//	public void setSlave_address(String slave_address) {
//		this.slave_address = slave_address;
//	}
//
//	public String getGroup_name() {
//		return group_name;
//	}
//
//	public void setGroup_name(String group_name) {
//		this.group_name = group_name;
//	}
//
//	public int getTime_out() {
//		return time_out;
//	}
//
//	public void setTime_out(int time_out) {
//		this.time_out = time_out;
//	}
//
//	public void init() {
//		logger.info("initialize tair configuration");
//
//		List<String> cs = new ArrayList<String>();
//		cs.add(master_address);
//		cs.add(slave_address);
//		tairManager.setConfigServerList(cs);
//		tairManager.setGroupName(group_name);
//		tairManager.setTimeout(time_out);
//		tairManager.init();
//	}
//
//	public void put(String key, String value) {
//		logger.info("put " + key + ":" + value + " into tair");
//		ResultCode rs = tairManager.put(area, key, value);
//		if (rs.isSuccess()) {
//			logger.info("put " + key + ":" + value + " into tair SUCCESS");
//		} else {
//			String errorInfo = "put " + key + ":" + value + " into tair FAILED";
//			logger.info(errorInfo);
//			throw new FailedHandle(errorInfo);
//		}
//	}
//
//	public void delete(String key) {
//		ResultCode rs = tairManager.delete(area, key);
//		if (rs.isSuccess()) {
//			logger.info("delete " + key + " from tair SUCCESS");
//		} else {
//			String errorInfo = "delete " + key + " from tair FAILED";
//			logger.info(errorInfo);
//			throw new FailedHandle(errorInfo);
//		}
//	}
//
//	public String get(String key) {
//		Result<DataEntry> rs = tairManager.get(area, key);
//		if (rs.isSuccess()) {
//			if (rs.getValue() == null) {
//				String errorInfo = "get " + key + " from tair FAIL value is NULL";
//				logger.info(errorInfo);
//				throw new FailedHandle(errorInfo);
//			} else {
//				String value = (String) rs.getValue().getValue();
//				logger.info("get " + key + ":" + value + " from tair SUCCESS");
//				return value;
//			}
//		} else {
//			String errorInfo = "get " + key + " from tair FAILED";
//			logger.info(errorInfo);
//			throw new FailedHandle(errorInfo);
//		}
//
//	}
//
//}
