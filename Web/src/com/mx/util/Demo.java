package com.mx.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import com.mx.start.Client1;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Demo implements Runnable{
	public static int clanid;
	public static String msg;
	public  Demo(int clanid) {
		// TODO Auto-generated constructor stub
		Demo.clanid=clanid;
	}
	@Override
	public void run() {
			try {
				long time=System.currentTimeMillis() / 1000;
		    	String time1=time+"";
		    	String time2=DateUtil.timestamp_HH(time1)+":68:00";
		    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	Date date = simpleDateFormat.parse(time2);
		    	long ts = date.getTime();
		    	long initDelay=ts-System.currentTimeMillis();
		      //分组收入
				Object data_group=HttpClientUtil.doPost("http://118.31.42.0:8080/kugou/public/index.php/home/mxhomeapi/groupStarInfo/clanid/"+Demo.clanid+"");
		    	JSONArray jsonArray_group = JSONArray.fromObject(data_group);
					if(jsonArray_group.size()>0){
					  for(int i=0;i<jsonArray_group.size();i++){
						  JSONObject job = jsonArray_group.getJSONObject(i); 
						  String groupname=job.getString("groupname");
						  String children=job.getString("children");
						  JSONArray jsonArray1 = JSONArray.fromObject(children);
						  for(int j=0;j<jsonArray1.size();j++){
							  int roomid=jsonArray1.getInt(j);
							  Client1.map_group.put(roomid, groupname);
						  }
						
					  }				
				}
				Client1.pool.scheduleAtFixedRate(new Update(),600,600,TimeUnit.SECONDS);
				Client1.pool.scheduleAtFixedRate(new NetState(),0,2,TimeUnit.SECONDS);
				Client1.pool.scheduleAtFixedRate(new ReturnMsg(clanid),0,59,TimeUnit.SECONDS);
				Client1.pool.scheduleAtFixedRate(new Database(),210,61,TimeUnit.SECONDS);
				Client1.pool.scheduleAtFixedRate(new Isonline(clanid),210, 61,TimeUnit.SECONDS);
				Client1.pool.scheduleAtFixedRate(new ShiData(),3*60*1000, 5*60*1000,TimeUnit.MILLISECONDS);
				Client1.pool.scheduleAtFixedRate(new Yestoday(),initDelay,60*60*1000,TimeUnit.MILLISECONDS);
				Client1.pool.scheduleWithFixedDelay(new OnMessage(), 0,1*1000,TimeUnit.MILLISECONDS);
				Client1.pool.scheduleWithFixedDelay(new Re_Start(), 6,6,TimeUnit.HOURS);
			    //主播状态
			   String msg =HttpClientUtil.doGetJson("http://visitor.fanxing.kugou.com/VServices/Clan.ClanServices.getClanStarListPaging/"+clanid+"-0-5000/", "");
			   int startIndex = msg.indexOf("(");  
		       int endIndex = msg.lastIndexOf(")");  
		       String msg1 = msg.substring(startIndex+1, endIndex);  
			   JSONObject json3 = JSONObject.fromObject(msg1);
			   String data=json3.getString("data");
			   JSONObject data1 = JSONObject.fromObject(data);
			   String list=data1.getString("list");
			   JSONArray jsonArray = JSONArray.fromObject(list);
			   int aa=0;
				if(jsonArray.size()>0){
				  for(int i=0;i<jsonArray.size();i++){
					  JSONObject job = jsonArray.getJSONObject(i); 
					  int liveStatus=job.getInt("liveStatus");
					  if (liveStatus==0) {
							break;
					  }
				    if (liveStatus==1) {
				    	String roomId= job.getString("roomId");
						String nickName=job.getString("nickName");
						Thread.sleep(1000);
						if (Client1.id==2) {
							if (Client1.list7.contains(roomId)) {
								aa=aa+1;
							}
						}
						Client1.list5.add(roomId);
						Client1.cachedThreadPool.execute(new Websocket(roomId,clanid,nickName));
					}
				  }				
			}
				System.out.println("开启完成");
				if (Client1.id==2) {
					ServerManager.serverManager.SendMessageToAll("{\"cmd\":10086,\"online\":"+aa+"}");
				}else {
					ServerManager.serverManager.SendMessageToAll("{\"cmd\":10086,\"online\":"+Client1.list5.size()+"}");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

