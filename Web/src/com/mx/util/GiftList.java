package com.mx.util;

public class GiftList implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String  aaa=HttpClientUtil.doPostJson("http://118.31.42.0:8080/kugou/public/index.php/home/mxhomeapi/giftAndCategory", "");
	           String data="{\"cmd\":201800,\"data\":"+aaa+"}";
	           ServerManager.serverManager.SendMessageToAll(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	       
	}

}
