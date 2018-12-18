package com.mx.util;
 
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

 
/**
 * 判断网络连接状况.
 * @author nagsh
 *
 */
public class NetState implements Runnable{
	public static Logger logger = Logger.getLogger(NetState.class);
	public static boolean connect = true;
	@Override
	public void run() {
		// TODO Auto-generated method stub
    	Runtime runtime = Runtime.getRuntime();
    	Process process;
		try {
			process = runtime.exec("ping " + "www.baidu.com");
			InputStream is = process.getInputStream(); 
	        InputStreamReader isr = new InputStreamReader(is); 
	        BufferedReader br = new BufferedReader(isr); 
	        String line = null; 
	        StringBuffer sb = new StringBuffer(); 
	        while ((line = br.readLine()) != null) { 
	            sb.append(line); 
	        } 
	        is.close(); 
	        isr.close(); 
	        br.close(); 
 
	        if (null != sb && !sb.toString().equals("")) { 
	            String logString = ""; 
	            if (sb.toString().indexOf("TTL") > 0) { 
	                // 网络畅通  
	            	connect = true;
	            } else { 
	                // 网络不畅通  
	            	connect = false;
	            	System.out.println(connect);
	            } 
	        } 
		} catch (Exception e) {
			logger.error("异常",e);
			e.printStackTrace();
		} 
	}
 
}