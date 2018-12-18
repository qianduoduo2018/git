package com.mx.util;

import java.awt.Desktop;
import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mx.start.Client1;

public class Re_Start implements Runnable{
	public static Logger logger = Logger.getLogger(Re_Start.class);

	@Override
	public void run() {
		try {
			String javaHome = System.getProperty("java.home"); 
			String javaHome1 = javaHome.substring(0, javaHome.length()-4);
			boolean stop=ServerManager.serverManager.Stop();
			if (stop) {
				Jxbrowser.browser.loadURL(javaHome1+"404\\404.html");	
			}
			Thread.sleep(2000);
			Client1.cachedThreadPool.execute(new Database());
			String programPath=javaHome1+"漫象数据采集助手更新器.exe";
			logger.info("启动应用程序：" + programPath);  
			if (StringUtils.isNotBlank(programPath)) {  
		        try {  
		            Desktop.getDesktop().open(new File(programPath));  
		        } catch (Exception e1) {  
		            e1.printStackTrace();  
		            logger.error("应用程序：" + programPath + "不存在！");  
		        }  
		    }  
			Jxbrowser.frame.dispose();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
