package com.mx.util;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SxStuts {

	private static Map<String, String> map_stuts;

	public static Map<String, String> getMap(){
		if (NetState.connect==true) {
		try {
		   map_stuts=new HashMap<>();
		   String	msg;
		   while (true) {
			   	msg = HttpClientUtil.doGetJson("http://visitor.fanxing.kugou.com/VServices/Clan.ClanServices.getClanStarListPaging/"+Demo.clanid+"-0-5000/", "");
	            if (msg.equals("1")) {
					Thread.sleep(10000);
				}else {
					break;
				}
			}
			   int startIndex = msg.indexOf("(");  
		       int endIndex = msg.lastIndexOf(")");  
		       String msg1 = msg.substring(startIndex+1, endIndex);  
			   JSONObject json3 = JSONObject.fromObject(msg1);
			   String data=json3.getString("data");
			   JSONObject data1 = JSONObject.fromObject(data);
			   String list1=data1.getString("list");
			   JSONArray jsonArray = JSONArray.fromObject(list1);
				if(jsonArray.size()>0){
				  for(int i=0;i<jsonArray.size();i++){
					  JSONObject job = jsonArray.getJSONObject(i); 
					  String roomId=job.getString("roomId");
					  int liveStatus=job.getInt("liveStatus");
					  String stuts="";
					  if (liveStatus==1) {
						  stuts ="在线";
					  }else{
						  stuts="下线";
					  }
					  map_stuts.put(roomId, stuts);
				  }				
			   } 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		}
		return map_stuts;
		
	}
}
