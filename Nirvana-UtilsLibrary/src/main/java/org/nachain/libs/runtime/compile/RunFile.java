package org.nachain.libs.runtime.compile;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;


@Slf4j
public class RunFile {


    public static synchronized void run(String className, boolean resolve) {
        try {

            CustomClassLoader mc = new CustomClassLoader();
            Class<?> cls = mc.loadClass(className, true);

            Method main = cls.getMethod("main", new Class[]{String[].class});

            main.invoke(null, new Object[]{new String[0]});
        } catch (Exception e) {
            log.error("Error running class:", e);
        }
    }


}