package com.mx.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mx.start.Client1;

import net.sf.json.JSONObject;

public class Update implements  Runnable {
	public static Logger logger = Logger.getLogger(Update.class);
	@Override
	public void run() {
		try {
		if (NetState.connect==true) {
			Client1.cachedThreadPool.execute(new Database());
			String version1=HttpClientUtil.doPost("http://47.99.65.217/public/portal/Index/check_latest_version");
			JSONObject json = JSONObject.fromObject(version1);
			double data=json.getDouble("data");
			String javaHome = System.getProperty("java.home"); 
			String javaHome1 = javaHome.substring(0, javaHome.length()-4); 				 
			//结果C:\Users\Administrator\Desktop\mx_tool\mx_tools\
			if (Client1.msg_version<data) {
				boolean stop=ServerManager.serverManager.Stop();
				if (stop) {
				Jxbrowser.browser.loadURL(javaHome1+"404\\404.html");	
				}
				String old_file=javaHome1+"漫象数据采集助手更新器.exe";
				java.net.URL l_url = new java.net.URL("http://xintan.oss-cn-beijing.aliyuncs.com/mxhome_new_images/version/update/3.44/%E6%BC%AB%E8%B1%A1%E6%95%B0%E6%8D%AE%E9%87%87%E9%9B%86%E5%8A%A9%E6%89%8B%E6%9B%B4%E6%96%B0%E5%99%A8.exe");
					//下载在根目录
				File file1=new File(old_file);
				if (file1.exists()) {
					file1.delete();
				}
				FileUtils.copyURLToFile(l_url, file1);
				System.out.println("更新成功");
				Jxbrowser.frame.dispose();
				logger.info("启动应用程序：" + old_file);  
				if (StringUtils.isNotBlank(old_file)) {  
			        try {  
			            Desktop.getDesktop().open(new File(old_file));  
			        } catch (Exception e1) {  
			            e1.printStackTrace();  
			            logger.error("应用程序：" + old_file + "不存在！");  
			        }  
			    }  
				System.exit(0);
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
