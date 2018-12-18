package com.mx.util;
import com.mx.start.Client1;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.ba;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import net.sf.json.JSONObject;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
public class Jxbrowser{
	/**
	 * 
	 */
	public static Browser browser;
	public static JFrame frame;
	/**
	 * @wbp.parser.entryPoint
	 */
	static void useSkin() throws Exception {		
		JFrame.setDefaultLookAndFeelDecorated(true);		
		JDialog.setDefaultLookAndFeelDecorated(true); 
		UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel");
		}
	static {
        try {
            Field e = ba.class.getDeclaredField("e");
            e.setAccessible(true);
            Field f = ba.class.getDeclaredField("f");
            f.setAccessible(true);
            Field modifersField = Field.class.getDeclaredField("modifiers");
            modifersField.setAccessible(true);
            modifersField.setInt(e, e.getModifiers() & ~Modifier.FINAL);
            modifersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            e.set(null, new BigInteger("1"));
            f.set(null, new BigInteger("1"));
            modifersField.setAccessible(false);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
	public static void webfram() throws Exception {
	        useSkin();
	        String javaHome = System.getProperty("java.home"); 
			String javaHome1 = javaHome.substring(0, javaHome.length()-4);  
//			String javaHome1="C:/Users/Administrator/Desktop/mx_tool/mx_tools/";
	    	browser = new Browser();
	    	BrowserView view = new BrowserView(browser);
	    	frame = new JFrame();
	        frame.getContentPane().add(view, BorderLayout.CENTER);
	        frame.setUndecorated(true); 
	        Dimension screen = frame.getToolkit().getScreenSize();  //得到屏幕尺寸
	        frame.setSize(screen.width,screen.height);
	        frame.setVisible(true);
			String path=javaHome1+"index\\index.html";
	        browser.loadURL(path);
	        Font font =new Font("微软雅黑", Font.PLAIN, 16);
	        Font1.setUIFont(font);
	        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Jxbrowser.class.getResource("/com/mx/start/300.png")));
	        frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
	        frame.setTitle("漫象数据采集助手V3.43");
	      	ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Jxbrowser.class.getResource
	    ("/com/mx/start/16.png")));
	      	PopupMenu pop = new PopupMenu(); 
	      	MenuItem download = new MenuItem("播放控件下载");
	      	MenuItem restart = new MenuItem("软件重启");
	      	MenuItem qupdate = new MenuItem("强制修复");
	      	MenuItem update = new MenuItem("检查更新");
	      	MenuItem exit = new MenuItem("退出");
	      	download.addActionListener(e ->{
	      		try { 
	      		   JOptionPane.showMessageDialog(null, "安装完成后请重启软件即可进入主播房间观看");
                   Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler https://www.flash.cn/cdm/latest/flashplayerpp_install_cn.exe");   
	      		} catch (Exception e1) {  
                    e1.printStackTrace() ;  
                } 
	      	});
	      	restart.addActionListener(e ->{
	      		
	      		Client1.cachedThreadPool.execute(new Re_Start());	
	     	  	});
	      	qupdate.addActionListener(e ->{
	      		Jxbrowser.browser.loadURL(javaHome1+"404\\404.html");	
	      		ServerManager.serverManager.Stop();
	      		String old_file=javaHome1+"漫象数据采集助手更新器.exe";
	      		frame.dispose();
	      		if (StringUtils.isNotBlank(old_file)) {  
			        try {  
			            Desktop.getDesktop().open(new File(old_file));  
			        } catch (Exception e1) {  
			            e1.printStackTrace();  
			        }  
			    }  
				System.exit(0);
	     	  	});
	      	update.addActionListener(e ->{
	      		String version1=HttpClientUtil.doPost("http://47.99.65.217/public/portal/Index/check_latest_version");
	    		JSONObject json = JSONObject.fromObject(version1);
	    		double data=json.getDouble("data");
	    		if (Client1.msg_version>=data) {
	    			JOptionPane.showMessageDialog(null, "已是最新版本");
	    		}else{
	    			Client1.cachedThreadPool.execute(new Update());	
	    		}
	     	  	});
	      	exit.addActionListener(e ->{
	      		  int n;
	      	  	  n=JOptionPane.showConfirmDialog(null, "退出采集器，会影响送礼明细。请问是否要退出。","警告",JOptionPane.YES_NO_OPTION);
	      	  		if (n==0) {
	      	  		Client1.cachedThreadPool.execute(new Database());
	      	  		frame.dispose(); 
	      	  		System.exit(0);
	      			}
	      	  	});
	      	pop.add(download);
	      	pop.add(restart);
	      	pop.add(qupdate);
	      	pop.add(update);
	      	pop.add(exit);
	      	TrayIcon tri = new TrayIcon(icon.getImage(),"漫象数据采集助手",pop);
	      	tri.addActionListener(e -> {
	      		frame.setVisible(true);
	          });
	      	try {
	    		SystemTray.getSystemTray().add(tri);
	    	} catch (AWTException e1) {
	    		// TODO Auto-generated catch block
	    		e1.printStackTrace();
	    	}
	      	
	      	frame.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	            	frame.setVisible(false);
	              
	            }
	          });
	
	}

}
