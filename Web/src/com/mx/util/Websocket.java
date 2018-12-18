package com.mx.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import com.mx.start.Client1;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class Websocket  implements Runnable{
	public String roomid;
	public int clanid;
	public String nickName;
    public WebSocketClient client;
    public int heartbeat = 0;// 0代表链接断开或者异常 1代表链接中.2代表正在连接	
    public String  heart="HEARTBEAT_REQUEST";
    public String url="ws://";//请求的路径地址包括端口
    public static Logger logger = Logger.getLogger(Websocket.class);
	public  Websocket(String roomid,int clanid,String nickname) {
		// TODO Auto-generated constructor stub
		this.roomid=roomid;
		this.clanid=clanid;
		this.nickName=nickname;
	}

	@Override
	public void run() {
		Timer timer = new Timer();
	    Task task = new Task(timer);
	    timer.schedule(task, new Date(), 20000);
}      
	
	public class Task extends TimerTask {
		private Timer timer;
		public Task(Timer timer) {
	        this.timer = timer;
	    }
		@Override
		public void run() {
			try {
				if (heartbeat ==1 && NetState.connect==true) {
				if (!(Client1.list5.contains(roomid))) {
					client.close();
					Client1.list5.remove(roomid);
					this.timer.cancel();
					Date curDate=new Date();
					SimpleDateFormat curDate1 = new SimpleDateFormat("HH:mm");
					String a1=curDate1.format(curDate);
					if (Data.map_id.get(roomid) !=null) {
					int id=Data.map_id.get(roomid);
					ServerManager.serverManager.SendMessageToAll("{\"id\":"+id+",\"cmd\":10000,\"content\":{\"time\":\""+a1+"\",\"anchor\":\""+nickName+"\",\"status\":\"下播了\"}}");
					}
					}else{
				    try {
				    	client.send(heart);
					} catch (Exception e) {
						System.out.println("网络连接超时，心跳发送失败");
					}
					Client1.map_online.put(roomid, System.currentTimeMillis());
				}
				}
				else if (heartbeat ==0 && NetState.connect==true) {
					if (!(Client1.list5.contains(roomid))) {
						Client1.list5.remove(roomid);
						this.timer.cancel();
					}else {
						String socket_data="chat1.fanxing.kugou.com:1314";
						client = new Client(new URI(url+socket_data), new Draft_6455(), null, 5000);
						client.connect();
					}
				}else if (NetState.connect==false) {
					heartbeat=0;
				}
			}catch (WebsocketNotConnectedException e) {
				heartbeat=0;
				logger.error("异常",e);
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error("异常",e);
			}		
		}
	}	   
	   public class Client extends WebSocketClient {
			public Client(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
				super(serverUri, protocolDraft, httpHeaders);
			}
		 
			@Override
			public void onOpen(ServerHandshake arg0) {
//				String  message = "{\"appid\":1010,\"clientid\":100,\"cmd\":201,\"roomid\":\""+roomid+"\",\"kugouid\":1297945058,\"token\":\"f41d7182b8a092cc8209235fc82da44de552d19e44057509bed3c2de6cebf129\"}";
				String  message = "{\"appid\":1010,\"clientid\":100,\"cmd\":201,\"roomid\":\""+roomid+"\"}";
				client.send(message);
				System.out.println(roomid+" "+nickName+"开启成功");
				heartbeat=1;
			}
			
			@Override
			public void onMessage(String arg0) {
				System.out.println(arg0);
			try {
			    	if(! arg0.equals("HEARTBEAT_RESPONSE")  && arg0!=null){
			    		JSONObject json = JSONObject.fromObject(arg0);
			    		int cmd=json.getInt("cmd");
			    			if (cmd==601) {
			    				JSONObject json2 = json.getJSONObject("content");
			    				int price=0;
			    				try {
			    					price=json.getInt("pvalue");
								} catch (Exception e) {
									price=json2.getInt("price");
								}
				    			String addtime=json.getString("__time__");
								String sendername1=json2.getString("sendername");
								String sendername = sendername1.replace("\"", "");
								String senderid=json2.getString("senderid");
								String giftimg=json2.getString("image");
								int giftnum=json2.getInt("num");
								String giftname=json2.getString("giftname");
								String anchorname1=json2.getString("receivername");
								String anchorname = anchorname1.replace("\"", "");
								int giftid=json2.getInt("giftid");
								int roomid1=Integer.parseInt(roomid);
								String group=Client1.map_group.get(roomid1);
								String gifttime=DateUtil.timestamp3Date(addtime);
								Client1.giftsum=Client1.giftsum+1;
								String sequenceid= String.valueOf(Demo.clanid)+String.valueOf(Client1.giftsum);
								String giftJson="{\"time\":"+addtime+",\"sequenceid\":\""+sequenceid+"\",\"senderName\":\""+sendername+"\",\"num\":"+giftnum+","
							+ "\"image\":\""+giftimg+"\","
									+ "\"roomId\":"+roomid1+",\"giftId\":"+giftid+",\"senderId\":"+senderid+"}";
								if (ControllerClient.heartbeat==1) {
									Client1.list1.add(giftJson);
								}
								if (ServerManager.userMap.size()>0 && Data.map_id.get(roomid) !=null) {
									int id=Data.map_id.get(roomid);
									if (anchorname.equals(nickName)) {
										if (Client1.id==2) {
											if (Client1.list7.contains(roomid)) {
												ServerManager.serverManager.SendMessageToAll("{\"id\":"+id+",\"cmd\":"+cmd+",\"senderName\":\""+sendername+"\",\"time\":\""+gifttime+"\",\"num\":"+giftnum+",\"giftname\":\""+giftname+"\",\"anchorname\":\""+anchorname+"\",\"gift_id\":"+giftid+",\"group\":\""+group+"\",\"roomid\":\""+roomid+"\",\"giftimg\":\""+giftimg+"\",\"price\":"+price+"}");
											}
										}else {
											ServerManager.serverManager.SendMessageToAll("{\"id\":"+id+",\"cmd\":"+cmd+",\"senderName\":\""+sendername+"\",\"time\":\""+gifttime+"\",\"num\":"+giftnum+",\"giftname\":\""+giftname+"\",\"anchorname\":\""+anchorname+"\",\"gift_id\":"+giftid+",\"group\":\""+group+"\",\"roomid\":\""+roomid+"\",\"giftimg\":\""+giftimg+"\",\"price\":"+price+"}");
										}
									}
								}
				    		}else if(cmd==1901){
				    			int actionId=json.getInt("actionId");
				    			if (actionId==1805 ) {
				    				JSONObject json2 = json.getJSONObject("content");
				    				String level=json2.getString("level");
				    				if (level.equals("SSS") ||level.equals("SS")||level.equals("S")) {
				    					String nickName1=json2.getString("nickName");
				    					String nickName = nickName1.replace("\"", "");
						    			int userId=json2.getInt("userId");
						    			int roomid1=Integer.parseInt(roomid);
						    			int kugouId=json2.getInt("kugouId");
						    			String songName=json2.getString("songName");
					    				String songJson="{\"cmd\":"+cmd+",\"roomid\":"+roomid1+",\"userId\":"+userId+","
					    						+ "\"kugouId\":"+kugouId+","
												+ "\"nickName\":\""+nickName+"\",\"songName\":\""+songName+"\",\"level\":\""+level+"\"}";
										if (ControllerClient.heartbeat==1) {
											Client1.list2.add(songJson);
										}
					    				if (ServerManager.userMap.size()>0 && Data.map_id.get(roomid) !=null) {
										int id=Data.map_id.get(roomid);
										if (Client1.id==2) {
											if (Client1.list7.contains(roomid)) {
												ServerManager.serverManager.SendMessageToAll("{\"id\":"+id+",\"cmd\":"+cmd+","
														+ "\"nickName\":\""+nickName+"\",\"songName\":\""+songName+"\",\"level\":\""+level+"\"}");
											}
										}else {
											ServerManager.serverManager.SendMessageToAll("{\"id\":"+id+",\"cmd\":"+cmd+","
													+ "\"nickName\":\""+nickName+"\",\"songName\":\""+songName+"\",\"level\":\""+level+"\"}");
										}
												}
									}
								}
				    		}else if(cmd==201){
				    			JSONObject content1  = json.getJSONObject("content");
				    			int roomid1=Integer.parseInt(roomid);
				    			int time=json.getInt("time");
				    			String time1=json.getString("time");
				    			String cometime=DateUtil.timestamp3Date(time1);
				    			int userid=content1.getInt("userid");
				    			int kugouid=content1.getInt("kugouid");
				    			String nickname1=content1.getString("nickname");
				    			String nickname = nickname1.replace("\"", "");
				    			int richlevel=content1.getInt("richlevel");
				    			String wellcomes1=content1.getString("wellcomes");
				    			String reg = "%nick";
				    			String wellcomes=wellcomes1.replace(reg, "");
				    			String fangkJson="{\"cmd\":"+cmd+",\"roomid\":"+roomid1+",\"userid\":"+userid+","
			    						+ "\"kugouid\":"+kugouid+","
										+ "\"nickname\":\""+nickname+"\",\"richlevel\":"+richlevel+",\"wellcomes\":\""+wellcomes+"\",\"time\":"+time+"}";
				    			if (ControllerClient.heartbeat==1) {
				    				Client1.list3.add(fangkJson);
								}
				    			Client1.vistorsum=Client1.vistorsum+1;
				    			if (Data.map_id.get(roomid) !=null && kugouid!=1297945058) {
			    					int id=Data.map_id.get(roomid);
			    					String message="{\"summ\":"+Client1.vistorsum+",\"id\":"+id+",\"abc\":"+cmd+",\"nickname\":\""+nickname+"\",\"wellcomes\":\""+wellcomes+"\",\"time\":\""+cometime+"\",\"anchor\":\""+nickName+"\"}";
			    					if (Client1.id==2) {
										if (Client1.list7.contains(roomid)) {
											Client1.list6.add(message);
										}
									}else {
										Client1.list6.add(message);
									}
			    				}
				    		}
						}				
				} catch (JSONException e) {
					logger.error("异常",e);
					logger.error(arg0);
					e.printStackTrace();
					System.out.println("数据解析错误");
				}
			}
			
			@Override
			public void onClose(int arg0, String arg1, boolean arg2) {
				if (arg0==1000) {
					System.out.println(nickName+"下线关闭");
				}else{
					heartbeat=0;
					System.out.println(nickName+"异常关闭，正在重连");
				}
			}
			@Override
		    public void onMessage(ByteBuffer bytes) {
		        try {
		            System.out.println(new String(bytes.array(),"utf-8"));
		        } catch (UnsupportedEncodingException e) {
		        	logger.error("异常",e);
		            e.printStackTrace();
		        }
		    }

			@Override
			public void onError(Exception e){
				try {
					heartbeat=0;
				} catch (Exception e2) {
					e.printStackTrace();
					logger.error("异常",e);
				}
				
			}
		}

}
