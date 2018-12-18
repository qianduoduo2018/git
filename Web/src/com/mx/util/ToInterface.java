package com.mx.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class ToInterface {
    /**
     * 调用对方接口方法
     * @param path 对方或第三方提供的路径
     * @param data 向对方或第三方发送的数据，大多数情况下给对方发送JSON数据让对方解析
     */
    public static String interfaceUtil(String path,String data) {
    	String data1="";
        try {
            URL url = new URL(path);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            PrintWriter out = null;
            //请求方式
            conn.setRequestMethod("POST");
//           //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("Cookie", "PHPSESSID=495cdafc853e86d14805b17e34704426");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "GB18030");  
            conn.setRequestProperty("User-agent", "	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");
            //设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
            //post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //发送请求参数即数据
            out.print(data);
            //缓冲数据
            out.flush();
            //获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            //构造一个字符流缓存
            String str = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((str = br.readLine()) != null) {
                /*JSONObject json = JSONObject.fromObject(str);*/
                data1=str;
                
            }
            //关闭流
            is.close();
            //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return data1;
    }

    public static String testUrlConnection(){
        String urlStr="http://www.maxxiang.com/kugou/public/index.php/home/login/index?mobile=15366626662&password=djlmain123";
        OutputStream out=null;
        InputStream in=null;
        try {
            URL url=new URL(urlStr);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            
            //打印请求头信息
            Map hfs=con.getHeaderFields();
            Set<String> keys=hfs.keySet();
            for(String str:keys){
                List<String> vs=(List)hfs.get(str);
                System.out.print(str+":");
                for(String v:vs){
                    System.out.print(v+"\t");
                }
                System.out.println();
            }
            System.out.println("-----------------------");
            String cookieValue=con.getHeaderField("Set-Cookie");
            System.out.println("cookie value:"+cookieValue);
            String sessionId=cookieValue.substring(0, cookieValue.indexOf(";"));
            return sessionId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            try {
                if(in!=null)
                    in.close();
                if(out!=null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    

}

