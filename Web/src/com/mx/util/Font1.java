package com.mx.util;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;

public class Font1 {
	 public static void setUIFont(Font font1)
	    {
	        Enumeration<Object> keys = UIManager.getDefaults().keys();
	        while(keys.hasMoreElements())
	        {
	            Object key = keys.nextElement();
	            Object value = UIManager.get(key);
	            if(value instanceof javax.swing.plaf.FontUIResource)
	            {
	                UIManager.put(key,font1);
	            }
	        }
	    }

}
