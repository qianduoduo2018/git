package com.mx.util;

import com.mx.start.Client1;

import net.sf.json.JSONObject;

public class InsertStarPk implements Runnable{
	private String data;
	private int time;
	public  InsertStarPk(String data,int time) {
		// TODO Auto-generated constructor stub
		this.data=data;
		this.time=time;
	}
	@Override
	public void run() {
		try {
			String aa=HttpClientUtil.doPostJson("http://118.31.42.0:8080/kugou/public/index.php/home/exeapi/insertStarPk", data);
			JSONObject json1 = JSONObject.fromObject(aa);
			int code=json1.getInt("code");
			String data11 =json1.getString("data");
			JSONObject json2 = JSONObject.fromObject(data11);
			if (code==1) {
				ServerManager.serverManager.SendMessageToAll("{\"cmd\":201802,\"msg\":1,\"data\":"+data11+"}");
				Client1.cachedThreadPool.execute(new Query_roomlist());
				int id=json2.getInt("id");
				Thread.sleep(time*1000);
				String a=HttpClientUtil.doPostJson("http://118.31.42.0:8080/kugou/public/index.php/home/mxhomestarpk/updatePkStart", "{\"updatePkStart\":2,\"pkid\":"+id+",\"mobile\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\"}");
				ServerManager.serverManager.SendMessageToAll("{\"cmd\":201805,\"msg\":"+a+"}");
				Client1.cachedThreadPool.execute(new Query_roomlist());
			}else {
				System.out.println("创建失败");
				ServerManager.serverManager.SendMessageToAll("{\"cmd\":201802,\"msg\":0}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
