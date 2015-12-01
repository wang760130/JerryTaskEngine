package com.jerry.taskengine.utils;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import com.jerry.taskengine.AppTest;


public class ClassUtilTest {
	
	@Test
	public void scanPackageFileTest() {
//		List<String> list = new ArrayList<String>();
//		list.add("ClassUtil");
//		ClassUtil.setFilters(list);
		
		Set<Class<?>> files = ClassUtil.scanPackageFile("com", true);
		for(Class<?> file : files) {
			System.out.println(file.getName());
		}
	}
	
	@Test
	public void getShortNameAsPropertyTest() {
		String shortName = ClassUtil.getShortNameAsProperty(AppTest.class);
		System.out.println(shortName);
	}
	
	@Test
	public void getAllInterfacesForClassTest() {
		Class<?>[] classes = ClassUtil.getAllInterfacesForClass(AppTest.class);
		for(Class<?> clazz : classes) {
			System.out.println(clazz.getName());
		}
	}
	
	@Test
	public void getClassFromJarTest() throws ClassNotFoundException {
		try {
			Set<Class<?>> classes = ClassUtil.getClassFromJar("D:\\My Workspace\\JerryTaskEngine\\target\\com.jerry.task.engine-0.0.1-SNAPSHOT.jar");
//			Set<Class<?>> classes = ClassUtil.getClassFromJar("D:\\My Workspace\\JerryTaskEngineTest\\target\\com.jerry.task.engine.test-0.0.1-SNAPSHOT.jar");
//			Set<Class<?>> classes = ClassUtil.getClassFromJar("D:\\My Workspace\\JerryTaskEngine\\target\\lib\\log4j-1.2.8.jar");
			
		
			for(Class<?> clazz : classes) {
				System.out.println(clazz.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
