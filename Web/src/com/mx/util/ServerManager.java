package com.mx.util;

import java.util.ArrayList;
import java.util.List;


import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;

public class ServerManager {

    private ServerSocket serverSocket=null;
    public static ServerManager serverManager=new ServerManager();;
    public static List<WebSocket> userMap=new ArrayList<WebSocket>();

    public ServerManager(){

    }

    public void UserLogin(WebSocket socket){
        if (socket!=null) {
        	userMap.add(socket);
        }
        System.out.println(userMap);
    }
    public void UserLeave(WebSocket socket){
        if (userMap.contains(socket)) {
            userMap.remove(socket); 
            System.out.println(socket+"离开了");
        }
    }


    public void SendMessageToAll(String message){
    	if (userMap.size()>0) {
            for (int i =0;i< userMap.size();i++){
            	userMap.get(i).send(message); 
            }
		}
    }

    public boolean Start(int port){

        if (port<0) {
            System.out.println("Port error...");
            return false;
        }

        System.out.println("Start ServerSocket...");

        WebSocketImpl.DEBUG=false;  
        try {
            serverSocket=new ServerSocket(this,port);
            serverSocket.start();
            System.out.println("Start ServerSocket Success...");
            return true;
        } catch (Exception e) {
            System.out.println("Start Failed...");
            e.printStackTrace();
            return false;
        }
    }

    public boolean Stop(){
        try {
            serverSocket.stop();
            System.out.println("Stop ServerSocket Success...");
            return true;
        } catch (Exception e) {
            System.out.println("Stop ServerSocket Failed...");
            e.printStackTrace();
            return false;
        }
    }

}