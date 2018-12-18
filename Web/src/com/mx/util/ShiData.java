package com.mx.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mx.start.Client1;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ShiData implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (NetState.connect==true) {
		try {
		System.out.println("dadadadadad开始计算实时数据");
		Map< String,Collection<String>> map1 =new HashMap<String, Collection<String>>();
        Map< String, Long> map_totalincome=new HashMap<>();
		String user="{\"username\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\",\"clanid\":"+Demo.clanid+"}";
		List<String> list=new ArrayList<>();
		list.add(user);
	    map1.put("user", list);
	    String msg =HttpClientUtil.doGetJson("http://visitor.fanxing.kugou.com/VServices/Clan.ClanServices.getClanStarListPaging/"+Demo.clanid+"-0-5000/", "");
	    int startIndex = msg.indexOf("(");  
        int endIndex = msg.lastIndexOf(")");  
        String msg1 = msg.substring(startIndex+1, endIndex);  
	    JSONObject json3 = JSONObject.fromObject(msg1);
	    String data=json3.getString("data");
	    JSONObject data1 = JSONObject.fromObject(data);
	    String list1=data1.getString("list");
	    JSONArray jsonArray = JSONArray.fromObject(list1);
	    List<String> list_roomid=new ArrayList<>();
	    Map< String, Long> map_income=new HashMap<>();
		if(jsonArray.size()>0){
		  for(int i=0;i<jsonArray.size();i++){
			  JSONObject job = jsonArray.getJSONObject(i); 
			  String roomId=job.getString("roomId");
			  int userId=job.getInt("userId");
			  String bbb=HttpClientUtil.getJsonByInternet("http://visitor.fanxing.kugou.com/VServices/RoomService.RoomService.getStarInfo/"+userId+"/");
			  if (! bbb.equals("1")) {
				  int startIndex1 = bbb.indexOf("(");  
				  int endIndex1 = bbb.lastIndexOf(")");  
				  String msg11 = bbb.substring(startIndex1+1, endIndex1); 
				  JSONObject json31 = JSONObject.fromObject(msg11); 
				  String data11=json31.getString("data");
				  if (!data11.equals("[]")) {
					  long cointotal=JSONObject.fromObject(data11).getLong("coinTotal");
						 map_income.put(roomId, cointotal);
						 list_roomid.add(roomId);
					}
			}
		  }
	    }
		if (Client1.id==2) {
			map1.put("roomids", Client1.list7);
		}else{
			map1.put("roomids", list_roomid);
		}
		JSONObject jsonObject = JSONObject.fromObject(map1);
        String result = jsonObject.toString();
        String aaa=HttpClientUtil.doPostJson("http://47.96.103.64/public/api/datastore/getstarinfo", result);
        List<String> list2=new ArrayList<>();
        long total_income=0;
        if (! aaa.equals("1")) {
        	JSONObject msg2 = JSONObject.fromObject(aaa);
            JSONArray Datalist = msg2.getJSONArray("income");
            for(int i=0;i<Datalist.size();i++){
				 JSONObject job = Datalist.getJSONObject(i);
				 String roomid=job.getString("roomid");
				 int roomId=job.getInt("roomid");
				 long income1=job.getLong("cointotal");
				 long realincome=map_income.get(roomid)-income1;
				 total_income=realincome+total_income;
				 int id=-1;
				 try {
				 id= Data.map_id.get(roomid);
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
				 String data11="{\"id\":"+id+",\"income\":"+realincome+"}";
				 list2.add(data11);
				 if (map_totalincome.get(Client1.map_group.get(roomId))==null) {
		            	map_totalincome.put(Client1.map_group.get(roomId), realincome);
					}else {
						map_totalincome.put(Client1.map_group.get(roomId), map_totalincome.get(Client1.map_group.get(roomId))+realincome);
					}
			 }
            List<String> table_total=new ArrayList<>();
            for (Map.Entry<String, Long> entry : map_totalincome.entrySet()) { 
	  			  String msg_total="{\"group\":\""+entry.getKey()+"\",\"income\":"+entry.getValue()+"}";
	  			  table_total.add(msg_total);
	  			}
            String data22="{\"cmd\":10,\"data\":"+list2+",\"total_income\":\""+total_income+"\",\"group_data\":"+table_total+"}";
            ServerManager.serverManager.SendMessageToAll(data22);
            System.out.println("实时数据"+data22);
        }
        System.out.println("23131231313计算完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	}

}
