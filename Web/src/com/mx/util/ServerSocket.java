package com.mx.util;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.mx.start.Client1;

import net.sf.json.JSONObject;



public class ServerSocket extends WebSocketServer {

    private ServerManager _serverManager;
    public ServerSocket(ServerManager serverManager,int port) throws UnknownHostException{
        super(new InetSocketAddress(port));     
        _serverManager=serverManager;
    }

    @Override
    public void onClose(WebSocket socket, int message,
            String reason, boolean remote) {
    	 _serverManager.UserLeave(socket);
    }

    @Override
    public void onError(WebSocket socket, Exception message) {
        System.out.println("Socket Exception:"+message.toString());
    }

    @Override
    public void onMessage(WebSocket socket, String message) {  
    	JSONObject json = JSONObject.fromObject(message);
    	int cmd=json.getInt("cmd");
    	if (cmd==140) {
    		String data=json.getString("data");
			String data1=data.substring(0, data.length()-1)+",\"mobile\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\",\"clanid\":"+Demo.clanid+"}";
			//延迟
			int time=json.getInt("time");
			Client1.cachedThreadPool.execute(new InsertStarPk(data1,time));
		}else if (cmd==141) {
			int id=json.getInt("id");
			String aaa=HttpClientUtil.doPostJson("http://118.31.42.0:8080/kugou/public/index.php/home/exeapi/deletepk","{\"id\":"+id+",\"mobile\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\"}");
			JSONObject json1 = JSONObject.fromObject(aaa);
			int code=json1.getInt("code");
			if (code==1) {
				_serverManager.SendMessageToAll("{\"cmd\":201803,\"msg\":1}");
				Client1.cachedThreadPool.execute(new Query_roomlist());
			}else{
				_serverManager.SendMessageToAll("{\"cmd\":201803,\"msg\":0}");
			}
		}else if(cmd==142){
			 System.out.println(message);
			 int id=json.getInt("id");
			 String a=HttpClientUtil.doPostJson("http://118.31.42.0:8080/kugou/public/index.php/home/exeapi/generatePkResult", "{\"id\":"+id+",\"mobile\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\"}");
			 JSONObject json1 = JSONObject.fromObject(a);
			 int code=json1.getInt("code");
			 if (code==1) {
				 _serverManager.SendMessageToAll("{\"cmd\":201804,\"msg\":1}");
				 Client1.cachedThreadPool.execute(new Query_roomlist());
			}else {
				_serverManager.SendMessageToAll("{\"cmd\":201804,\"msg\":0}");
			}
		}else if (cmd==143) {
			int id=json.getInt("id");
			String a=HttpClientUtil.doPostJson("http://118.31.42.0:8080/kugou/public/index.php/home/exeapi/getPkStatus", "{\"id\":"+id+",\"mobile\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\"}");
			 _serverManager.SendMessageToAll("{\"cmd\":201805,\"msg\":"+a+"}");
		}else if(cmd==144){
			Client1.cachedThreadPool.execute(new Query_roomlist());
		}else if (cmd==145) {
			 int id=json.getInt("id");
			 int page=json.getInt("page");
			 String a=HttpClientUtil.doPostJson("http://118.31.42.0:8080/kugou/public/index.php/home/exeapi/getPkGiftlist", "{\"id\":"+id+",\"page\":"+page+",\"mobile\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\"}");
			 JSONObject json1 = JSONObject.fromObject(a);
			 String redGift=json1.getString("redGift");
			 String blueGift=json1.getString("blueGift");
			 _serverManager.SendMessageToAll("{\"cmd\":201806,\"redGift\":"+redGift+",\"blueGift\":"+blueGift+"}");
		}else if(cmd==146){
			 int id=json.getInt("id");
			 String a=HttpClientUtil.doPostJson("http://118.31.42.0:8080/kugou/public/index.php/home/exeapi/getPkStatus", "{\"id\":"+id+",\"mobile\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\"}");
			 _serverManager.SendMessageToAll("{\"cmd\":201808,\"msg\":"+a+"}");
		}else if (cmd==147) {
			try {
			   String msg_data=Client1.msg1;
			   int startIndex = msg_data.indexOf("(");  
			   int endIndex = msg_data.lastIndexOf(")");  
			   String msg1 = msg_data.substring(startIndex+1, endIndex);  
			   JSONObject json3 = JSONObject.fromObject(msg1);
			   String data=json3.getString("data");
			   JSONObject data1 = JSONObject.fromObject(data);
			   String list=data1.getString("list");
				_serverManager.SendMessageToAll("{\"cmd\":201809,\"msg\":"+list+"}");
			} catch (Exception e) {
				e.printStackTrace();
				_serverManager.SendMessageToAll("{\"cmd\":201809,\"msg\":\"请求失败\"}");
			} 
		}
    }

    @Override
    public void onOpen(WebSocket socket, ClientHandshake handshake) {
    	try {
    	if (_serverManager.userMap.size()<10) {
    		System.out.println(socket+"有人连接了");
            _serverManager.UserLogin(socket);
            _serverManager.SendMessageToAll("{\"cmd\":"+110+",\"clanname\":\""+Client1.clanname+"\",\"sum\":"+Client1.sum+",\"img\":\""+Client1.img+"\"}");
            _serverManager.SendMessageToAll("{\"cmd\":201807,\"id\":"+Client1.id+"}");
            if (Client1.falg) {
            	Client1.cachedThreadPool.execute(new Data());
            	Client1.falg=false;
    		}
           Client1.cachedThreadPool.execute(new Query_roomlist());
           Client1.cachedThreadPool.execute(new GiftList());
           Client1.cachedThreadPool.execute(new Yestoday());
		}else{
			socket.close();
		}
    	} catch (Exception e) {
    		e.printStackTrace();
		}
        
    }
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

}