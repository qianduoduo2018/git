package com.mx.util;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mx.start.Client1;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




		

public class Test1 {
	static Logger logger = Logger.getLogger(Test1.class);

    public static void main(String[] args) throws ParseException, IOException, InterruptedException {
    	String data11 = HttpClientUtil.doPostJson(
				"http://www.maxxiang.com/kugou/public/index.php/home/caiji/caijilogin7",
				"{\"mobile\":18262815943,\"password\":\"hd123456\"}");
    	System.out.println(data11);
		JSONObject object1 = JSONObject.fromObject(data11);
		String groups = object1.getString("groups");

    	 
    	
		
    }
}

