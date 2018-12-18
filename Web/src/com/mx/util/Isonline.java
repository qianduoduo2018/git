package com.mx.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mx.start.Client1;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Isonline implements Runnable{
	public static Logger logger = Logger.getLogger(Isonline.class);
	public int clanid;
	public  Isonline(int clanid) {
		// TODO Auto-generated constructor stub
		this.clanid=clanid;
	}
	@Override
	public void run() {
		try {  
			   String msg_data=Client1.msg1;
			   if (msg_data.equals("1")) {
				   System.out.println("暂未获取到数据");
			   }else{
			   System.out.println("检查上线");
			   int startIndex = msg_data.indexOf("(");  
			   int endIndex = msg_data.lastIndexOf(")");  
			   String msg1 = msg_data.substring(startIndex+1, endIndex);  
			   JSONObject json3 = JSONObject.fromObject(msg1);
			   String data=json3.getString("data");
			   JSONObject data1 = JSONObject.fromObject(data);
			   String list=data1.getString("list");
			   JSONArray jsonArray = JSONArray.fromObject(list);
					if(jsonArray.size()>0 && NetState.connect==true){
						ArrayList<String> list_online=new ArrayList<String>();
						ArrayList<String> list_zz=new ArrayList<String>();
					  for(int i=0;i<jsonArray.size();i++){
					    JSONObject job = jsonArray.getJSONObject(i); 
					    String roomId= job.getString("roomId");
					    int liveStatus=job.getInt("liveStatus");
					    String nickName=job.getString("nickName");
					    if (liveStatus==1 ) {
					    	if (Client1.id==2 && Client1.list7.contains(roomId)) {
									list_zz.add(roomId);
							}
							list_online.add(roomId);
					    	if ( (!Client1.list5.contains(roomId))&& NetState.connect==true) {
					    		Client1.list5.add(roomId);
					    		Client1.cachedThreadPool.execute(new Websocket(roomId,Client1.clan,nickName));
					    		System.out.println(nickName+"上线了");
					    		Thread.sleep(1000);
					    		Date curDate=new Date();
								SimpleDateFormat curDate1 = new SimpleDateFormat("HH:mm");
								String a=curDate1.format(curDate);
								if (Data.map_id.get(roomId) !=null) {
									int id=Data.map_id.get(roomId);
									if (Client1.id==2) {
										if (Client1.list7.contains(roomId)) {
											System.out.println(nickName+"上线了");
											ServerManager.serverManager.SendMessageToAll("{\"id\":"+id+",\"cmd\":10000,\"content\":{\"time\":\""+a+"\",\"anchor\":\""+nickName+"\",\"status\":\"开播了\"}}");
										}
									}else {
										    System.out.println(nickName+"上线了");
										    ServerManager.serverManager.SendMessageToAll("{\"id\":"+id+",\"cmd\":10000,\"content\":{\"time\":\""+a+"\",\"anchor\":\""+nickName+"\",\"status\":\"开播了\"}}");
									}
								}
							}
						   }else{
							  continue; 
						   }
					  }	
					  System.out.println("在线人数"+Client1.list5.size());
					  Client1.list5.clear();
					  Client1.list5.addAll(list_online);
					  Set<Entry<String, Long>> set=Client1.map_online.entrySet();
					  Iterator<Entry<String, Long>> iterator=set.iterator();
					  while(iterator.hasNext()){
						  Entry<String, Long> entry=iterator.next();
						  if (System.currentTimeMillis()-entry.getValue()>60*1000 && Client1.list5.contains(entry.getKey())) {
								Client1.list5.remove(entry.getKey());
								iterator.remove();
							}
					  }
					  if (Client1.id==2) {
						  ServerManager.serverManager.SendMessageToAll("{\"cmd\":10086,\"online\":"+list_zz.size()+"}");
					}else {
						  ServerManager.serverManager.SendMessageToAll("{\"cmd\":10086,\"online\":"+Client1.list5.size()+"}");
					}
				}
			   }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常",e);
		}
	}

}
