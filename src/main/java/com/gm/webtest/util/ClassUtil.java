package com.gm.webtest.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> cls;

        try {
            cls = Class.forName(className,isInitialized,getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure",e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    public static Class<?> loadClass(String className){
        Class<?> cls;

        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure",e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<Class<?>>();

        Enumeration<URL> urls = null;
        try {
            urls = getClassLoader()
                    .getResources(packageName.replace(".","/"));
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url != null){
                    String protocol = url.getProtocol();
                    if(protocol.equals("file")){
                        String packagePath = url.getPath().replaceAll("%20","");
                        addClass(classSet,packagePath,packageName);
                    }else if (protocol.equals("jar")){
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if(jarURLConnection!=null){
                            JarFile jarFile = jarURLConnection.getJarFile();
                            Enumeration<JarEntry> jarEntires = jarFile.entries();
                            while(jarEntires.hasMoreElements()){
                                JarEntry jarEntry = jarEntires.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if(jarEntryName.endsWith(".class")){
                                    String className = jarEntryName.substring(0,
                                            jarEntryName.lastIndexOf(".")).replaceAll("/",".");
                                    doAddClass(classSet,className);

                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            LOGGER.error(" load Class Set failure",e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet,String packagePath,String packageName){
            File[] files = new File(packagePath).listFiles(new FileFilter(){

                @Override
                public boolean accept(File f) {
                    return (f.isFile() && f.getName().endsWith(".class"))||
                            f.isDirectory();
                }
            });

            for(File file : files){
                String fileName = file.getName();
                if(file.isFile()){
                    String className = fileName.substring(0,fileName.lastIndexOf("."));
                    if(StringUtils.isNotEmpty(packageName)){
                        className = packageName +"." + className;
                    }
                    doAddClass(classSet,className);
                }else{
                    String subPackagePath =fileName;
                    if(StringUtils.isNotEmpty(packagePath)){
                        subPackagePath = packagePath +"/"+subPackagePath;
                    }
                    String subPackageName = fileName;
                    if(StringUtils.isNotEmpty(packageName)){
                        subPackageName = packageName +"."+subPackageName;
                    }
                    addClass(classSet,subPackagePath,subPackageName);
                }
            }
    }

    private static void doAddClass(Set<Class<?>> classSet,String className){
        Class<?> cls = loadClass(className,false);
        classSet.add(cls);
    }
}
