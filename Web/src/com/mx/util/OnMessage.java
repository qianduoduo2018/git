package com.mx.util;

import com.mx.start.Client1;

public class OnMessage implements Runnable{

	@Override
	public void run() {
		try {
			for(int i = 0;i<Client1.list6.size();i++){
				if (i<10) {
					ServerManager.serverManager.SendMessageToAll(Client1.list6.get(i));
					Client1.list6.remove(i);
				}else {
					break;
				}
			}
			Client1.n=Client1.n+1;
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
