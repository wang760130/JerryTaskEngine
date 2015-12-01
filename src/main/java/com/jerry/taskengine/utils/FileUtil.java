package com.jerry.taskengine.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public class FileUtil {
	
	/**
	 * Get all file which in dir
	 * @param dir
	 * @param extension
	 * @return
	 */
	public static List<File> getFiles(String dir, String...extension) {
		File file = new File(dir);
		if(!file.isDirectory()) {
			return null;
		}
		
		List<File> fileList = new ArrayList<File>();
		getFiles(file, fileList, extension);
		
		return fileList;
	}

	private static void getFiles(File file, List<File> fileList,
			String... extension) {
		File[] files = file.listFiles();
		for(int i = 0; i < files.length; i++) {
			if(files[i].isDirectory()) {
				getFiles(files[i], fileList, extension);
			} else if (files[i].isFile()) {
				String fileName = files[i].getName().toLowerCase();
				boolean isAdd = false;
				if(extension != null) {
					for(String ext : extension) {
						if(fileName.lastIndexOf(ext) == fileName.length() - ext.length()) {
							isAdd = true;
							break;
						}
					}
				}
				
				if(isAdd) {
					fileList.add(files[i]);
				}
			}
		}
	}

	/**
	 * Get all file which in dir 
	 * @param dir
	 * @param extension
	 * @return
	 */
	public static List<File> getCurrentPathFiles(String dir, String...extension) {
		File file = new File(dir);
		if(!file.isDirectory()) {
			return null;
		}
		List<File> fileList = new ArrayList<File>();
		getCurrentFiles(file, fileList, extension);
		return fileList;
	}

	private static void getCurrentFiles(File file, List<File> fileList,
			String[] extension) {
		File[] files = file.listFiles();
		for(int i = 0; i < files.length; i++) {
			if(files[i].isFile()) {
				String fileName = files[i].getName().toLowerCase();
				boolean isAdd = false;
				if(extension != null) {
					for(String ext : extension) {
						if(fileName.lastIndexOf(ext) == fileName.length() - ext.length()) {
							isAdd = true;
							break;
						}
					}
				}
				if(isAdd) {
					fileList.add(files[i]);
				}
			}
		}
	}
	
	/**
	 * Get all jar/war/ear which in dir 
	 * @param dirs
	 * @return
	 * @throws IOException 
	 */
	public static List<String> getUniqueLibPath(String... dirs) throws IOException {
		
		List<String> jarList = new ArrayList<String>();
		List<String> fileNameList = new ArrayList<String>();
		
		for(String dir : dirs) {
			List<File> fileList = getFiles(dir, "rar", "jar", "war", "ear");
			if(fileList != null) {
				for(File file : fileList) {
					if(!fileNameList.contains(file.getName())) {
						jarList.add(file.getCanonicalPath());
						fileNameList.add(file.getName());
					}
				}
			}
		}
		
		return jarList;
	}
}
