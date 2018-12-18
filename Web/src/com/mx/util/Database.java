package com.mx.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.mx.start.Client1;

import net.sf.json.JSONObject;




public class Database implements Runnable{

	public static Logger logger = Logger.getLogger(Database.class);
	@Override
	public void run() {
		try {   
			    if (NetState.connect==true && Client1.list1.size()!=0 && ControllerClient.heartbeat==1) {
			    	Map< String,Collection<String>> map =new HashMap<String, Collection<String>>();
			    	String first=Client1.list1.get(0);
			    	JSONObject firstjson = JSONObject.fromObject(first);
					String first_time1=firstjson.getString("time");//第一个礼物时间
					String yestoday=DateUtil.timestamp_DD(first_time1);//昨天的时间日期
					String first_date=yestoday+" 00:00:00";//第一个礼物时间
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	Date date1 = simpleDateFormat.parse(first_date);//第一个礼物时间
			    	long ts_first = date1.getTime()/1000L;//昨天零点时间戳
			    	String last=Client1.list1.get(Client1.list1.size()-1);
			    	JSONObject lastjson = JSONObject.fromObject(last);
					String last_time1=lastjson.getString("time");//最后一个礼物时间
					String today=DateUtil.timestamp_DD(last_time1);//今天的时间日期
					String last_date=today+" 00:00:00";//最后一个礼物时间
			    	Date date2 = simpleDateFormat.parse(last_date);//最后一个礼物时间
			    	long ts_last = date2.getTime()/1000L;//今天零点时间戳
					List<String> list_user=new ArrayList<String>();
					String user="{\"username\":\""+Client1.username+"\",\"password\":\""+Client1.password+"\",\"clanid\":"+Demo.clanid+"}";
					list_user.add(user);
					Client1.list4.add(today);
					
					if (Client1.list1 !=null  && Client1.list1.size() > 0) {
						if (ts_first==ts_last) {
							map.put("gift",Client1.list1);//当天的数据
						}else{
							Iterator<String> iterator = Client1.list1.iterator();
							List<String> list_gift=new CopyOnWriteArrayList<String>();
							while (iterator.hasNext()) {
								String data=iterator.next();
								JSONObject json3 = JSONObject.fromObject(data);
								long time4=json3.getInt("time");
								if (time4<ts_last) {
									list_gift.add(data);
									Client1.list1.remove(data);
								}
							}
							map.put("gift",list_gift);//前一天的数据
							List<String> list4 = new CopyOnWriteArrayList<String>();//时间
							list4.add(yestoday);
							map.put("date", list4);
							map.put("songLike",new ArrayList<>());
							map.put("fangk",new ArrayList<>());
							map.put("user", list_user);
							JSONObject jsonObject = JSONObject.fromObject(map);
					        String result = jsonObject.toString();
							HttpClientUtil.doPostJson("http://47.96.103.64/public/api/datastore/store", result);
							map.clear();
							map.put("gift",new ArrayList<>());
						}
					}else{
						map.put("gift",new ArrayList<>());
					}
					
					
					if (Client1.list3 !=null  && Client1.list3.size() > 0) {
						if (ts_first==ts_last) {
							map.put("fangk",Client1.list3);
						}else {
							Iterator<String> iterator = Client1.list3.iterator();
							List<String> list_gift1=new CopyOnWriteArrayList<String>();
							while (iterator.hasNext()) {
								String data=iterator.next();
								JSONObject json3 = JSONObject.fromObject(data);
								long time4=json3.getInt("time");
								if (time4<ts_last) {
									list_gift1.add(data);
									Client1.list3.remove(data);
								}
							}
							map.put("fangk",list_gift1);  //前一天的数据
							List<String> list4 = new CopyOnWriteArrayList<String>();//时间
							list4.add(yestoday);
							map.put("date", list4);
							map.put("songLike",new ArrayList<>());
							map.put("gift",new ArrayList<>());
							map.put("user", list_user);
							JSONObject jsonObject = JSONObject.fromObject(map);
					        String result = jsonObject.toString();
							HttpClientUtil.doPostJson("http://47.96.103.64/public/api/datastore/store", result);
							map.clear();
							map.put("fangk",new ArrayList<>());
						}
					}else{
						map.put("fangk",new ArrayList<>());
					}
					
					map.put("user", list_user);
					map.put("songLike",Client1.list2);
					map.put("date", Client1.list4);
					JSONObject jsonObject = JSONObject.fromObject(map);
			        String result = jsonObject.toString();
					String msg =HttpClientUtil.doPost1("http://47.96.103.64/public/api/datastore/store", result);
					System.out.println(msg);
					logger.error(first_time1+"   "+msg);
					}
		} catch (Exception e) {
			 e.printStackTrace();
			 logger.error("发生异常 msg={}",e);
		}finally {
			Client1.list1.clear();
			Client1.list2.clear();
			Client1.list3.clear();
			Client1.list4.clear();
		}
	}
	}

