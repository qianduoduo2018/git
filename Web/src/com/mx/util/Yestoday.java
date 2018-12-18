package com.mx.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mx.start.Client1;
import net.sf.json.JSONObject;

public class Yestoday implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (NetState.connect==true) {
		try {
			Map< String,Collection<String>> map1 =new HashMap<String, Collection<String>>();
			String user="{\"username\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\",\"clanid\":"+Client1.clan+"}";
			System.out.println(user);
			List<String> list=new ArrayList<>();
		    list.add(user);
			map1.put("user", list);
			JSONObject jsonObject = JSONObject.fromObject(map1);
	        String result = jsonObject.toString();
	        String  aaa="";
	        boolean flag=true;
	        while (flag) {
		         aaa=HttpClientUtil.doPostJson("http://47.96.103.64/public/api/datastore/getyesterdaydata", result);
		         if (aaa.equals("1")) {
		        	 Thread.sleep(5000);
				}else{
					flag=false;
				}
			}
	        JSONObject json = JSONObject.fromObject(aaa);
	        System.out.println("昨天"+json);
	        int vistorcount;
	        int songcount;
	        int income;
			try {
	        	 income=json.getInt("income");
			} catch (Exception e) {
				 income=0;
			}
	        try {
	        	  vistorcount=json.getInt("vistorcount");
			} catch (Exception e) {
				 vistorcount=0;
			}
	        try {
	        	 songcount=json.getInt("songcount");
			} catch (Exception e) {
				 songcount=0;
			}
	        System.out.println("{\"cmd\":"+111+",\"income\":"+income+",\"vistorcount\":"+vistorcount+",\"songcount\":"+songcount+"}");
	        ServerManager.serverManager.SendMessageToAll("{\"cmd\":"+111+",\"income\":"+income+",\"vistorcount\":"+vistorcount+",\"songcount\":"+songcount+"}");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	}

}
