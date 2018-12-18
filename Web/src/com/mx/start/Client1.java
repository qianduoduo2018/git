package com.mx.start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.JOptionPane;

import com.mx.util.ControllerClient;
import com.mx.util.Demo;
import com.mx.util.HttpClientUtil;
import com.mx.util.Jxbrowser;
import com.mx.util.ServerManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Client1 {
	public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	public static ScheduledExecutorService pool = Executors.newScheduledThreadPool(15);
	public static List<String> list1 = new CopyOnWriteArrayList<String>();//礼物
	public static List<String> list2 = new CopyOnWriteArrayList<String>();//真唱
	public static List<String> list3 = new CopyOnWriteArrayList<String>();//访客
	public static List<String> list4 = new CopyOnWriteArrayList<String>();//时间
	public static List<String> list5 = new CopyOnWriteArrayList<String>();//在线主播
	public static List<String> list6 = new CopyOnWriteArrayList<String>();//访客
	public static List<String> list7 = new CopyOnWriteArrayList<String>();//子账号主播
	public static Map< String, Long> map_online=new HashMap<>();  //在线心跳
	public static Map< Integer, String> map_group=new HashMap<>();
	public static String msg1;
	public static boolean falg=true;
    public static double msg_version=3.43;
    public static String username;
    public static String password;
    public static int clan;
	public static int giftsum = 0;
	public static String clanname;
	public static int sum;
	public static String img;
	public static int n;
	public static int vistorsum;
	public static int id;



	/**
	 * Launch the application.
	 * @throws Exception 
	 */
    public static void main(String[] args){
    	ServerManager.serverManager.Start(8888);
		//记住密码
		String javaHome = System.getProperty("java.home"); 
        String javaHome1 = javaHome.substring(0, javaHome.length()-4);  
//		String javaHome1="C:/Users/Administrator/Desktop/mx_tool/mx_tools/";
		String path=javaHome1+"config\\pas.txt";
		String path1=javaHome1+"config\\zhanghao.txt";
		File file = new File(path);
		File file1 = new File(path1);
		if(!file1.exists()){
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
		        String str = br.readLine();
		        System.out.println(str);
				JSONObject json = JSONObject.fromObject(str);
				username=json.getString("username");
				password=json.getString("password");
				clan=json.getInt("clan");
				id=0;
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String data11 = HttpClientUtil.doPostJson(
					"http://www.maxxiang.com/kugou/public/index.php/home/caiji/caijilogin7",
					"{\"mobile\":\""+ Client1.username+"\",\"password\":\""+ Client1.password +"\"}");
			JSONObject object1 = JSONObject.fromObject(data11);
			String groups = object1.getString("groups");
			JSONArray jsonArray11 = JSONArray.fromObject(groups);
			 for(int i=0;i<jsonArray11.size();i++){
				 JSONObject job = jsonArray11.getJSONObject(i); 
				 int clan2=(int) job.get("clanid");
				 if (clan2==clan) {
					 clanname=job.getString("clanname");
					 sum=job.getInt("starnum");
					 img=job.getString("clanimg");
				}
			 }
        }else {
        	FileReader fr1;
        	try {
				fr1 = new FileReader(file1);
				BufferedReader br1 = new BufferedReader(fr1);
		        String str = br1.readLine();
		        System.out.println(str);
				JSONObject json = JSONObject.fromObject(str);
				id=json.getInt("id");
				username=json.getString("username");
				password=json.getString("password");
				clan=json.getInt("clan");
				img=json.getString("clanimg");
				if (id==1) {
					sum = json.getInt("starnum");
					clanname=json.getString("clanname");
				}else {
					clanname=json.getString("groupname");
					String roomids=json.getString("roomids");
					String[] strs=roomids.split(",");
					list7=Arrays.asList(strs);
					sum=list7.size();
				}
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
		Jxbrowser.webfram();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "程序已运行，请勿重复开启！");
			System.exit(0);
		}
		Client1.cachedThreadPool.execute(new ControllerClient());
		System.out.println(clan);
		Client1.cachedThreadPool.execute(new Demo(clan));
	}
    
}
