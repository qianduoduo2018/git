package com.mx.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String timestamp2Date(String str_num) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    if (str_num.length() == 13) {
	        String date = sdf.format(new Date(Long.parseLong(str_num)));
	        return date;
	    } else {
	        String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
	        return date;
	    }
	}
	public static String timestamp3Date(String str_num) {
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    if (str_num.length() == 13) {
	        String date = sdf.format(new Date(Long.parseLong(str_num)));
	        return date;
	    } else {
	        String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
	        return date;
	    }
	}
	public static String timestamp_HH(String str_num) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
	    if (str_num.length() == 13) {
	        String date = sdf.format(new Date(Long.parseLong(str_num)));
	        return date;
	    } else {
	        String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
	        return date;
	    }
	}
	public static String timestamp_DD(String str_num) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    if (str_num.length() == 13) {
	        String date = sdf.format(new Date(Long.parseLong(str_num)));
	        return date;
	    } else {
	        String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
	        return date;
	    }
	}
}
