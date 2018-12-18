package com.mx.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mx.start.Client1;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Data implements Runnable{
	public static Logger logger = Logger.getLogger(Data.class);
	public static Map< String, Integer> map_id=new HashMap<>();
	@Override
	public void run() {
		    if (NetState.connect==true) {
				try {
					map_id.clear();
					Map< String,Collection<String>> map1 =new HashMap<String, Collection<String>>();
					String user="{\"username\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\",\"clanid\":"+Client1.clan+"}";
					List<String> list=new ArrayList<>();
				    list.add(user);
					map1.put("user", list);
					List<String> list_roomid=new ArrayList<>();
					 //主播状态
					   String msg="";
					   boolean flag=true;
					   while (flag) {
						   msg =HttpClientUtil.doGetJson("http://visitor.fanxing.kugou.com/VServices/Clan.ClanServices.getClanStarListPaging/"+Client1.clan+"-0-5000/", "");
                           if (msg.equals("")) {
							Thread.sleep(2000);
						}else {
							flag=false;
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
							  list_roomid.add(roomId);
							  
						  }				
					}
						if (Client1.id==2) {
							map1.put("roomids", Client1.list7);
						}else{
							map1.put("roomids", list_roomid);
						}
						JSONObject jsonObject = JSONObject.fromObject(map1);
				        String result = jsonObject.toString();
				        String	aaa="";
				        for (int i = 0; i < 5; i++) {
					        aaa=HttpClientUtil.doPostJson("http://47.96.103.64/public/api/datastore/gethourstarinfo", result);
				        	i++;
					        if (aaa.equals("1")) {
					        	Thread.sleep(10000);
				        		continue;
							}else {
								break;
							}
						}
			            JSONObject msg2 = JSONObject.fromObject(aaa);
			            //收入
			            String income=msg2.getString("income");
			            JSONArray Datalist = JSONArray.fromObject(income);
			            Map< Integer, String> map_income=new HashMap<>();
			            for(int i=0;i<Datalist.size();i++){
							 JSONObject job = Datalist.getJSONObject(i);
							 int roomid=job.getInt("roomid");
							 String income1=job.getString("income");
							 map_income.put(roomid, income1);
						 }
			            //访客
			            String vistorcount=msg2.getString("vistorcount");
			            JSONArray Datalist1 = JSONArray.fromObject(vistorcount);
			            Map< Integer, String> map_vistorcount=new HashMap<>();
			            for(int i=0;i<Datalist1.size();i++){
							 JSONObject job = Datalist1.getJSONObject(i);
							 int roomid=job.getInt("roomid");
							 String vistorcount1=job.getString("vistorcount");
							 map_vistorcount.put(roomid, vistorcount1);
						 }
			            //真唱
			            String songcount=msg2.getString("songcount");
			            JSONArray Datalist2 = JSONArray.fromObject(songcount);
			            Map< Integer, String> map_songcount=new HashMap<>();
			            for(int i=0;i<Datalist2.size();i++){
							 JSONObject job = Datalist2.getJSONObject(i);
							 int roomid=job.getInt("roomid");
							 String songcount1=job.getString("songcount");
							 map_songcount.put(roomid, songcount1);
						 }
			          //礼物数量
			            String giftcount=msg2.getString("giftcount");
			            JSONArray Datalist3 = JSONArray.fromObject(giftcount);
			            Map< Integer, String> map_giftcount=new HashMap<>();
			            for(int i=0;i<Datalist3.size();i++){
							 JSONObject job = Datalist3.getJSONObject(i);
							 int roomid=job.getInt("roomid");
							 String songcount1=job.getString("giftcount");
							 map_giftcount.put(roomid,songcount1);
						 }
			            int sum=0;
			            int n=0;
			            Map< String, Integer> map_totalincome=new HashMap<>();
			            List<String> table=new ArrayList<>();
			  			  for(int i=0;i<jsonArray.size();i++){
			  				  JSONObject job = jsonArray.getJSONObject(i); 
			  				  String roomid=job.getString("roomId");
			  				  int roomId;
			  				  if (Client1.id==2) {
			  					  if (Client1.list7.contains(roomid)) {
			  						roomId=job.getInt("roomId");
								}else {
									continue;
								}
							  }else {
								 roomId=job.getInt("roomId");
							 }
			  				  int userId=job.getInt("userId");
			  				  String userLogo=job.getString("userLogo");
			  				  String nickName=job.getString("nickName");
			  				  int liveStatus=job.getInt("liveStatus");
			  				  String Status="";
			  				  if (liveStatus==1) {
								 Status="在线";
							}else{
								 Status="下线";
							}
			  				  String income1=map_income.get(roomId);//收入
			  				  if (income1==null) {
			  					income1="0";
							}
			  				  int income2=0;
			  				try {
			  				     income2 = Integer.parseInt(income1);
			  				} catch (NumberFormatException e) {
			  				    e.printStackTrace();
			  				}
			  				
			  				sum=sum+income2;
			  				String vistorcount1=map_vistorcount.get(roomId);//访客
							  if (vistorcount1==null) {
								  vistorcount1="0"; 
							} 
							  int vistorcount2=0;
							  try {
				  				    vistorcount2 = Integer.parseInt(vistorcount1);
				  				} catch (NumberFormatException e) {
				  				    e.printStackTrace();
				  				}
							  String songcount11=map_songcount.get(roomId);//真唱
			  				  if (songcount11==null) {
			  					songcount11="0";
							}
			  				 int songcount2=0;
			  				try {
			  				    songcount2 = Integer.parseInt(songcount11);
			  				} catch (NumberFormatException e) {
			  				    e.printStackTrace();
			  				}
			  				String giftcount1=map_giftcount.get(roomId);//礼物数量
							  if (giftcount1==null) {
								  giftcount1="0";
							}
							  int giftcount2=0;
							  try {
				  				    giftcount2 = Integer.parseInt(giftcount1);
				  				} catch (NumberFormatException e) {
				  				    e.printStackTrace();
				  				}
					            if (map_totalincome.get(Client1.map_group.get(roomId))==null) {
					            	map_totalincome.put(Client1.map_group.get(roomId), income2);
								}else {
									map_totalincome.put(Client1.map_group.get(roomId), map_totalincome.get(Client1.map_group.get(roomId))+income2);
								}
					            n=n+1;
					            map_id.put(roomid, n);
			  				  String msg_data="{\"id\":"+n+",\"roomId\":"+roomId+",\"nickName\":\""+nickName+"\",\"Status\":\""+Status+"\",\"income\":"+income2+""
			  				  		+ ",\"vistorcount\":"+vistorcount2+",\"songcount\":"+songcount2+",\"giftcount\":"+giftcount2+",\"userId\":"+userId+",\"userLogo\":\""+userLogo+"\"}";
			  				table.add(msg_data);
			  		 }
			  			List<String> table_total=new ArrayList<>();
			  			for (Map.Entry<String, Integer> entry : map_totalincome.entrySet()) { 
			  			  String msg_total="{\"group\":\""+entry.getKey()+"\",\"income\":"+entry.getValue()+"}";
			  			  table_total.add(msg_total);
			  			}
				        String data4="{\"cmd\":221,\"data\":"+table+",\"sum\":\""+sum+"\",\"group_data\":"+table_total+"}";
				        ServerManager.serverManager.SendMessageToAll(data4);
			  			  }catch (NullPointerException  e) {
					logger.error("异常",e);
					e.printStackTrace();
				}catch (Exception  e) {
					e.printStackTrace();
					logger.error("异常",e);
				}
			}else{
				System.out.println("网络连接失败");
			}
			
	}

}
