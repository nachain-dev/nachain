package org.nachain.libs.runtime.compile;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;


@Slf4j
public class CustomClassLoader extends java.lang.ClassLoader {


    Hashtable<String, Class<?>> loadedClasses;


    public CustomClassLoader() {
        loadedClasses = new Hashtable<String, Class<?>>();
    }


    @SuppressWarnings("unchecked")
    public synchronized Class loadClass(String className, boolean resolve) {
        Class newClass = null;
        byte[] classData = null;
        boolean nextRun = true;


        newClass = (Class) loadedClasses.get(className);
        if (newClass != null) {
            nextRun = false;
        }

        if (nextRun) {
            try {

                newClass = findSystemClass(className);
                return newClass;
            } catch (ClassNotFoundException e) {
                nextRun = true;

            }
        }


        if (nextRun) {

            classData = getClassData(className);

            newClass = defineClass(null, classData, 0, classData.length);
        }


        if (newClass != null) {
            loadedClasses.put(className, newClass);

            if (resolve) {
                resolveClass(newClass);
            }
        }
        return newClass;
    }


    protected byte[] getClassData(String fileName) {
        byte[] data = null;
        int length;
        try {

            if (fileName.startsWith("http://")) {

                URL url = new URL(fileName.endsWith(".class") ? fileName : fileName + ".class");
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                length = connection.getContentLength();
                data = new byte[length];
                inputStream.read(data);
                inputStream.close();
            } else {

                FileInputStream fis = new FileInputStream(new File(fileName));

                length = (int) new File(fileName).length();
                data = new byte[length];
                fis.read(data);
                fis.close();
            }

        } catch (Exception e) {
            log.error("Error loading class from URL:", e);
        }
        return data;
    }
} 