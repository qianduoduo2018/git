package com.mx.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnZip {
	public static void unZip(File srcFile, String destDirPath) throws RuntimeException {
		long start = System.currentTimeMillis();
		if (!srcFile.exists()) {
			throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
		 }
		 ZipFile zipFile = null;
		 try {
			 zipFile = new ZipFile(srcFile);
			 Enumeration<?> entries = zipFile.entries();
			 while (entries.hasMoreElements()) {
				 ZipEntry entry = (ZipEntry) entries.nextElement();
				 System.out.println("解压" + entry.getName());
				 if (entry.isDirectory()) {
					 String dirPath = destDirPath + "/" + entry.getName();
					 File dir = new File(dirPath);
					 dir.mkdirs();
				 } else {
					 File targetFile = new File(destDirPath + "/" + entry.getName());
					 if(!targetFile.getParentFile().exists()){
						 targetFile.getParentFile().mkdirs();
					 }
					 targetFile.createNewFile();
					 InputStream is = zipFile.getInputStream(entry);
					 FileOutputStream fos = new FileOutputStream(targetFile);
					 int len;
					 byte[] buf = new byte[4096];
					 while ((len = is.read(buf)) != -1) {
						 fos.write(buf, 0, len);
					 }
					 fos.close();
					 is.close();
					 }
				 }
			 long end = System.currentTimeMillis();
			 System.out.println("解压完成，耗时：" + (end - start) +" ms");
		 } catch (Exception e) {
			 throw new RuntimeException("unzip error from ZipUtils", e);
		 } finally {
			 if(zipFile != null){
				 try {
					 zipFile.close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		}
	}

}
