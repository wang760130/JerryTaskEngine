package com.jerry.taskengine.utils;

import java.beans.Introspector;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public class ClassUtil {
	private static final char PACKAGE_SEPARATOR = '.';

	private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static final char INNER_CLASS_SEPARATOR = '$';

    private static final String ARRAY_SUFFIX = "[]";
	
	private static List<String> classFilters = null;
	
	/**
	 * 扫描文件类型的class
	 * @param packageName 基础包
	 * @param recursive 是否递归搜索子包
	 * @return
	 */
	public static Set<Class<?>> scanPackageFile(String packageName, boolean recursive) {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		String packageToPath = packageName.replace('.', '/');
		
		try {
			Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageToPath);
			while(dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					doScanPackageClassesByFile(classes, packageName, filePath, recursive);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return classes;
	}
	
	/**
	 * 以文件的方式扫描包下的所有Class文件
	 * @param classes
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 */
	private static void doScanPackageClassesByFile(Set<Class<?>> classes, String packageName, String packagePath, final boolean recursive) {
		File dir = new File(packagePath);
		if(!dir.exists() || !dir.isDirectory()) {
			return ;
		}
		
		// 过滤当前文件夹下的所有文件
		File[] dirFiles = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return filterFile(file, recursive);
			}
		});
		
		for(File file : dirFiles) {
			if(file.isDirectory()) {
				doScanPackageClassesByFile(classes, packageName + "." + file.getName() , file.getAbsolutePath(), recursive);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 根据过滤规则判断类名
	 * @param file
	 * @param recursive
	 * @return true 不过滤  	false 过滤
	 */
	private static boolean filterFile(File file, boolean recursive) {
		if(file.isDirectory()) {
			return recursive;
		}
		String fileName = file.getName();
		if(fileName.indexOf('$') != -1) {
			return false;
		}
		
		if(!fileName.endsWith(".class")) {
			return false;	
		}
		if(null == classFilters || classFilters.isEmpty()) {
			return true;
		}
		String name = fileName.substring(0, fileName.length() - 6);
		boolean flag = true;
		for(String str : classFilters) {
			String reg = "^" + str.replace("*", ".*") + "$";
            Pattern p = Pattern.compile(reg);
            if (p.matcher(name).find()) {
                flag = false;
                break;
            }
		}
		return flag;
	}

	
	public static void setFilters(List<String> classFilters) {
		ClassUtil.classFilters = classFilters;
	}
	
	public static String getShortNameAsProperty(Class<?> clazz) {
		String shortName = getShortName(clazz);
		int dotIndex = shortName.lastIndexOf('.');
		shortName = (dotIndex != -1 ? shortName.substring(dotIndex + 1) : shortName);
		return Introspector.decapitalize(shortName);
	}

	public static String getShortName(Class<?> clazz) {
		return getShortName(getQualifiedName(clazz));
	}
	
	public static String getShortName(String className) {
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
		if(nameEndIndex == -1) {
			nameEndIndex = className.length();
		}
		String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
		shortName = shortName.replace(INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
		return shortName;
	}
	
	private static String getQualifiedName(Class<?> clazz) {
		if(clazz.isArray()) {
			return getQualifiedNameForArray(clazz);
		} else {
			return clazz.getName();
		}
	}
	
	private static String getQualifiedNameForArray(Class<?> clazz) {
		StringBuffer sb = new StringBuffer();
		while(clazz.isArray()) {
			clazz = clazz.getComponentType();
			sb.append(ARRAY_SUFFIX);
		}
		sb.insert(0, clazz.getName());
		return sb.toString();
	}
	
	 /**
     * Return all interfaces that the given class implements as array,
     * including ones implemented by superclasses.
     * <p>If the class itself is an interface, it gets returned as sole interface.
     * @param clazz the class to analyze for interfaces
     * @return all interfaces that the given object implements as array
     */
	public static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {
		return getAllInterfacesForClass(clazz, null);
	}
	
	 /**
     * Return all interfaces that the given class implements as array,
     * including ones implemented by superclasses.
     * <p>If the class itself is an interface, it gets returned as sole interface.
     * @param clazz the class to analyze for interfaces
     * @param classLoader the ClassLoader that the interfaces need to be visible in
     * (may be <code>null</code> when accepting all declared interfaces)
     * @return all interfaces that the given object implements as array
     */
	public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, ClassLoader classLoader) {
		Set<Class> interfacesSet = getAllInterfacesForClassAsSet(clazz, classLoader);
		return interfacesSet.toArray(new Class[interfacesSet.size()]);
	}
	
	 /**
     * Return all interfaces that the given class implements as Set,
     * including ones implemented by superclasses.
     * <p>If the class itself is an interface, it gets returned as sole interface.
     * @param clazz the class to analyze for interfaces
     * @param classLoader the ClassLoader that the interfaces need to be visible in
     * (may be <code>null</code> when accepting all declared interfaces)
     * @return all interfaces that the given object implements as Set
     */
	private static Set<Class> getAllInterfacesForClassAsSet(Class clazz, ClassLoader classLoader) {
		if(clazz.isInterface() && isVisible(clazz, classLoader)) {
			return Collections.singleton(clazz);
		}
		
		Set<Class> interfacesSet = new LinkedHashSet<Class>();
		while(clazz != null) {
			Class<?>[] interfaces = clazz.getInterfaces();
			for(Class<?> ifs : interfaces) {
				interfacesSet.addAll(getAllInterfacesForClassAsSet(ifs, classLoader));
			}
			clazz = clazz.getSuperclass();
		}
		return interfacesSet;
	}
	
	/**
     * Check whether the given class is visible in the given ClassLoader.
     * @param clazz the class to check (typically an interface)
     * @param classLoader the ClassLoader to check against (may be <code>null</code>,
     * in which case this method will always return <code>true</code>)
     */
	private static boolean isVisible(Class<?> clazz, ClassLoader classLoader) {
		if(classLoader == null) {
			return true;
		}
		Class<?> actualClass;
		try {
			actualClass = classLoader.loadClass(clazz.getName());
			// Else: different interface class found...
			return clazz == actualClass;
		} catch (ClassNotFoundException e) {
			// No interface class found...
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
     * Check if the right-hand side type may be assigned to the left-hand side
     * type, assuming setting by reflection. Considers primitive wrapper
     * classes as assignable to the corresponding primitive types.
     * @param lhsType the target type
     * @param rhsType the value type that should be assigned to the target type
     * @return if the target type is assignable from the value type
     * @see TypeUtils#isAssignable
     */
	public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
		return lhsType.isAssignableFrom(rhsType);
	}
	
	/**
     * Determine the name of the package of the given class:
     * e.g. "java.lang" for the <code>java.lang.String</code> class.
     * @param clazz the class
     * @return the package name, or the empty String if the class
     * is defined in the default package
     */
	private static String getPackageName(Class<?> clazz) {
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		return (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
	}
	
	public static Set<Class<?>> getClassFromJar(String jarPath) throws IOException{
		// read jar file
		JarFile jarFile = new JarFile(jarPath);
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		while(jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			String name = jarEntry.getName();
			if(name.endsWith(".class")) {
				String className = name.replaceAll(".class", "").replaceAll("/", ".");
				Class<?> type = null;
				
				try {
					type = ClassLoader.getSystemClassLoader().loadClass(className);
				} catch (Error e) {
				} catch (ClassNotFoundException e) {
				}
				
				if(type != null) {
					classes.add(type);
				}
			}
		}
		jarFile.close();
		return classes;
	}
} 
