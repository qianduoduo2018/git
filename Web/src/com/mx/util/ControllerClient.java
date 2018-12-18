package com.mx.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import com.mx.start.Client1;

import net.sf.json.JSONObject;


public class ControllerClient  implements Runnable{
	public static Logger logger = Logger.getLogger(ControllerClient.class);
    public static WebSocketClient client;
    public static int zt=0;
    public static int heartbeat = 0;// 0代表链接断开或者异常 1代表链接中.2代表正在连接	
    public String url="ws://47.99.65.217:8282";//请求的路径地址包括端口
    public int status_id=0;//检查登录状态
    

	@Override
	public void run() {
		Timer timer = new Timer();
	    Task task = new Task(timer);
	    int a=(int)(1+Math.random()*(1000-1+1));
	    timer.schedule(task,0, 8999+a);
}      
	
	public class Task extends TimerTask {
		private Timer timer;
		public Task(Timer timer) {
	        this.timer = timer;
	    }
		@Override
		public void run() {
			try {
				if (heartbeat ==1) {
				    String  heart="{\"action\":\"websocket_heart\",\"data\":{\"user_name\":\""+Client1.username+"\",\"clan_id\":\""+Demo.clanid+"\",\"collecting_clan\":\""+Client1.clan+"\",\"version\":"+Client1.msg_version+"}}";
					client.send(heart);
				}
				else if (heartbeat ==0 && NetState.connect==true) {
					client = new Client(new URI(url), new Draft_6455(), null, 5000);
					client.connect();
				} else {
					String msg =HttpClientUtil.doPost("http://47.99.65.217/public/portal/Index/check_clan_collecting/clan_id/"+Client1.clan+"");
				   	JSONObject json = JSONObject.fromObject(msg);
					String status=json.getString("status");
					if (status.equals("clan_no_collecting")) {
						heartbeat =0;
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
	 
			}		
		}
	}	   
	   public class Client extends WebSocketClient {
			public Client(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
				super(serverUri, protocolDraft, httpHeaders);
			}
		 
			@Override
			public void onOpen(ServerHandshake arg0) {
				try {
				System.out.println("控制端连接成功");
				String msg =HttpClientUtil.doPost("http://47.99.65.217/public/portal/Index/check_clan_collecting/clan_id/"+Client1.clan+"");
			   	JSONObject json = JSONObject.fromObject(msg);
				String status=json.getString("status");
				if (status.equals("clan_is_collecting")) {
					client.close();
				}else if(status.equals("clan_no_collecting")){
					heartbeat=1;
					String  heart="{\"action\":\"websocket_heart\",\"data\":{\"user_name\":\""+Client1.username+"\",\"clan_id\":\""+Demo.clanid+"\",\"collecting_clan\":\""+Client1.clan+"\",\"version\":"+Client1.msg_version+"}}";
					client.send(heart);
				}
				} catch (Exception e) {
					heartbeat=6;
					e.printStackTrace();
				}
			}
		 
			@Override
			public void onMessage(String arg0) {
				System.out.println(arg0);
			}
			
			@Override
			public void onClose(int arg0, String arg1, boolean arg2) {
				System.out.println("控制端链接已关闭");
				heartbeat=6;
			}
			@Override
		    public void onMessage(ByteBuffer bytes) {
		        try {
		            System.out.println(new String(bytes.array(),"utf-8"));
		        } catch (UnsupportedEncodingException e) {
		            e.printStackTrace();
		        }
		    }

			@Override
			public void onError(Exception e){
				logger.error("异常",e);
				heartbeat=0;		
			}
		}

}
