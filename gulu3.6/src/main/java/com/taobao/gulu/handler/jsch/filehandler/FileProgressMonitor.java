package com.taobao.gulu.handler.jsch.filehandler;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import com.jcraft.jsch.SftpProgressMonitor;

/**
 * <p>Title: FileProgressMonitor.java</p>
 * <p>Description: file progress monitor</p>
 * @author: gongyuan.cz
 * @email:  gongyuan.cz@taobao.com
 * @blog:   100continue.iteye.com
 */
public class FileProgressMonitor extends TimerTask implements SftpProgressMonitor {
	private static Logger log = Logger.getLogger(FileProgressMonitor.class);
	
    private long progressInterval = 5 * 1000; // default interval 5 seconds
    
    private boolean isEnd = false; // record transfer finish
    
    private long transfered; // record total transfer size
    
    private long fileSize; // record file size
    
    private Timer timer; // timer object
    
    private boolean isScheduled = false; // record execute timer status
    
    public FileProgressMonitor(long fileSize) {
        this.fileSize = fileSize;
    }
    
    public FileProgressMonitor() {
        this.fileSize = 0;
    }
    
    @Override
    public void run() {
        if (!isEnd()) { // verify transfer finish or not
            log.info("Transfering is in progress.");
            long transfered = getTransfered();
            if (transfered != fileSize) { 
                log.info("Current transfered: " + transfered + " bytes");
                sendProgressMessage(transfered);
            } else {
                log.info("File transfering is done.");
                setEnd(true); 
            }
        } else {
            log.info("Transfering done. Cancel timer.");
            stop();
            return;
        }
    }
    
    public void stop() {
        log.info("Try to stop progress monitor.");
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
            isScheduled = false;
        }
        log.info("Progress monitor stoped.");
    }
    
    public void start() {
        log.info("Try to start progress monitor.");
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(this, 1000, progressInterval);
        isScheduled = true;
        log.info("Progress monitor started.");
    }
    
    /**
     * print process info
     * @param transfered
     */
    private void sendProgressMessage(long transfered) {
        if (fileSize != 0) {
            double d = ((double)transfered * 100)/(double)fileSize;
            DecimalFormat df = new DecimalFormat( "#.##"); 
            log.info("Sending progress message: " + df.format(d) + "%");
        } else {
            log.info("Sending progress message: " + transfered);
        }
    }

   
    public boolean count(long count) {
        if (isEnd()) return false;
        if (!isScheduled) {
            start();
        }
        add(count);
        return true;
    }

    
    public void end() {
        setEnd(true);
        log.info("transfering end.");
    }
    
    private synchronized void add(long count) {
        transfered = transfered + count;
    }
    
    private synchronized long getTransfered() {
        return transfered;
    }
    
    public synchronized void setTransfered(long transfered) {
        this.transfered = transfered;
    }
    
    private synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }
    
    private synchronized boolean isEnd() {
        return isEnd;
    }

    public void init(int op, String src, String dest, long max) {
        // Not used for putting InputStream
    }
}
