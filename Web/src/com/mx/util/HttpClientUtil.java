package com.mx.util;
 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
 
public class HttpClientUtil {
	public static Logger logger = Logger.getLogger(HttpClientUtil.class);
	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,"utf-8");
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
 
		return resultString;
	}
 
	public static String doPost(String url) {
		return doPost(url, null);
	}
	
		public static String doPostJson(String url, String json) {
			String returnValue ="1";
			CloseableHttpClient httpClient = null;
			
			RequestConfig requestConfig = 
			RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(10000)
			.setConnectionRequestTimeout(30000).setRedirectsEnabled(true).build();//设置请求和
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpPost httpPost = null;
			try{
				//第一步：创建HttpClient对象
			    httpClient = HttpClients.createDefault();
			 	
			 	//第二步：创建httpPost对象
		        httpPost = new HttpPost(url);
		        httpPost.setConfig(requestConfig);
		        //第三步：给httpPost设置JSON格式的参数
		        StringEntity requestEntity = new StringEntity(json,"utf-8");
		        requestEntity.setContentEncoding("UTF-8");    	        
		        httpPost.setHeader("Content-type", "application/json");
		        httpPost.setEntity(requestEntity);
		       
		       //第四步：发送HttpPost请求，获取返回值
		       returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法
		      
			}catch(HttpResponseException e)
			{
				 e.printStackTrace();
			}
			 catch(Exception e)
			{
				 e.printStackTrace();
			}
			
			finally {
				if (httpPost != null && !httpPost.isAborted()) {
					httpPost.releaseConnection();
					httpPost.abort();
	        	 }
	             if (httpClient != null) {
	            	 try {
	            		 httpClient.close();
	                 }catch (IOException e) {
	                     e.printStackTrace();
	                 }
	        	 }
		    }
			 //第五步：处理返回值
		     return returnValue;
		}


	
	public static String doGetJson(String url, String json)throws UnknownHostException,SocketException{
		CloseableHttpClient httpCilent2 = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom()
				 .setConnectTimeout(5000)   //设置连接超时时间
				 .setConnectionRequestTimeout(5000) // 设置请求超时时间
				 .setSocketTimeout(5000)
				 .setRedirectsEnabled(true)//默认允许自动重定向
				 .build();
		HttpGet httpGet2 = new HttpGet(url);
		httpGet2.setConfig(requestConfig);
		String srtResult = "";
		List<Header> headers = new ArrayList<Header>();  
        headers.add(new Header("Host","food.39.net"));
        headers.add(new Header("User-Agent","Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0)"));
        headers.add(new Header("Accept","*/*"));
        headers.add(new Header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8"));
        headers.add(new Header("Connection","keep-alive"));
		 CloseableHttpResponse httpResponse=null;
		 try {
	             httpResponse = httpCilent2.execute(httpGet2);
	            if(httpResponse.getStatusLine().getStatusCode() == 200){
	                srtResult = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
	            }else {
	            	srtResult="1";
	            }
	        }catch (UnknownHostException e) {
	            e.printStackTrace();
	            srtResult="1";
	        }catch (SocketException e) {
	            e.printStackTrace();
	            srtResult="1";
	        }catch (SocketTimeoutException e) {
	            e.printStackTrace();
	            srtResult="1";
	        }catch (Exception e) {
	            e.printStackTrace();
	            srtResult="1";
	        }finally {
	        	 try {
	        		 if (httpResponse != null) {
	        			 EntityUtils.consume(httpResponse.getEntity());
	        			 httpResponse.close();
	        		 }
	        		 }catch (IOException e) {
	                     e.printStackTrace();
	                 }
	        	 if (httpGet2 != null && !httpGet2.isAborted()) {
	        		 httpGet2.releaseConnection();
	        		 httpGet2.abort();
	        	 }
	             if (httpCilent2 != null) {
	            	 try {
	            		 httpCilent2.close();
	                 }catch (IOException e) {
	                     e.printStackTrace();
	                 }
	        	 }
	        	 }
	        
		 return srtResult;
	}
	
	public static String getJsonByInternet(String path){
        try {
            URL url = new URL(path.trim());
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            if(200 == urlConnection.getResponseCode()){
                //得到输入流
                InputStream is =urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while(-1 != (len = is.read(buffer))){
                    baos.write(buffer,0,len);
                    baos.flush();
                }
                return baos.toString("utf-8");
            }
        } catch (SocketTimeoutException e) {
        	return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }

        return null;
    }
	
	public static String doPost1(String url, String params) throws Exception {				
		CloseableHttpClient httpclient = HttpClients.createDefault();		
		HttpPost httpPost = new HttpPost(url);// 创建httpPost       	
		httpPost.setHeader("Accept", "application/json");     	
		httpPost.setHeader("Content-Type", "application/json");    	
		String charSet = "UTF-8";    	
		StringEntity entity = new StringEntity(params, charSet);    	
		httpPost.setEntity(entity);                
		CloseableHttpResponse response = null;                
		try {        	        	
			response = httpclient.execute(httpPost);            
			StatusLine status = response.getStatusLine();            
			int state = status.getStatusCode();            
			if (state == HttpStatus.SC_OK) {            	
				HttpEntity responseEntity = response.getEntity();            	
				String jsonString = EntityUtils.toString(responseEntity);            	
				return jsonString;            
				}            
			else{				 
				logger.error("请求返回:"+state+"("+url+")");
				System.out.println("请求返回:"+state+"("+url+")");
				}        
			}        
		finally {            
			if (response != null) {                
				try {                    
					response.close();                
					} catch (IOException e) {                    
						e.printStackTrace();                
						}           

			}            
			try {				
	
				httpclient.close();			
			} catch (IOException e) {				
		e.printStackTrace();			
		}        
		}        
		return null;	
	
}
}
