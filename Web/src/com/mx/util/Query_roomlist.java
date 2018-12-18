package com.mx.util;

import com.mx.start.Client1;

public class Query_roomlist implements Runnable{

	@Override
	public void run() {
		try {
	    	String ss=HttpClientUtil.doPostJson("http://118.31.42.0:8080/kugou/public/index.php/home/exeapi/selectStarPkByClanid","{\"mobile\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\",\"clanid\":"+Client1.clan+"}");
	    	String data="{\"cmd\":201801,\"data\":"+ss+"}";
	    	System.out.println("201801  ssssssssssssssssss"+ss);
	        ServerManager.serverManager.SendMessageToAll(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
