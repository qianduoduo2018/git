package com.mx.util;




import org.apache.log4j.Logger;

import com.mx.start.Client1;



public class ReturnMsg implements Runnable{
	static Logger logger = Logger.getLogger(ReturnMsg.class);
	public int clanid;
	
	public  ReturnMsg(int clanid) {
		// TODO Auto-generated constructor stub
		this.clanid=clanid;
	}

	@Override
	public void run() {
			boolean flag=NetState.connect;
			if (flag) {
				try {
				boolean flag1=true;
				while (flag1 && NetState.connect) {
				   Client1.msg1 = HttpClientUtil.getJsonByInternet("http://visitor.fanxing.kugou.com/VServices/Clan.ClanServices.getClanStarListPaging/"+clanid+"-0-500/");
                   if (Client1.msg1.equals("1") ) {
                	   System.out.println("2222222222222222222请求失败");
				}else {
					System.out.println("111111请求成功");
					flag1=false;
				}
				}
				}	  
				catch (Exception e) {
					logger.error("ReturnMsg异常",e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
}

