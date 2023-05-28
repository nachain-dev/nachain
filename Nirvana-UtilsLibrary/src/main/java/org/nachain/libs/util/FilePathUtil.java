package org.nachain.libs.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;


public class FilePathUtil {

    private Class<?> objClass;


    public static final String SEP = File.separator;

    @SuppressWarnings("unused")
    private FilePathUtil() {
    }


    public FilePathUtil(Class<?> objClass) throws Exception {
        this.objClass = objClass;
    }


    public FilePathUtil(Object objClass) throws Exception {
        this.objClass = objClass.getClass();
    }


    public String getClassName() {
        return objClass.getName();
    }


    public String getPackageName() {
        return objClass.getPackage().getName();
    }


    public String getClassFileName() {
        String classFileName = "";

        String className = getClassName();
        String packageName = getPackageName();
        if (packageName.equals(""))
            className.substring(packageName.length() + 1, className.length());
        else
            classFileName = className;

        return classFileName;
    }


    private String getPath(URL resURL) throws UnsupportedEncodingException {

        String returnValue = "";


        if (resURL == null) {
            URL url = objClass.getProtectionDomain().getCodeSource().getLocation();

            returnValue = url.getPath();


            returnValue = returnValue.replace("+", "%2B");


            returnValue = URLDecoder.decode(returnValue, "utf-8");


            if (returnValue.endsWith(".jar"))
                returnValue = returnValue.substring(0, returnValue.lastIndexOf("/") + 1);


            File file = new File(returnValue);
            returnValue = file.getAbsolutePath();

        } else {
            returnValue = resURL.getFile();

            File file = new File(returnValue);
            returnValue = file.getAbsolutePath();


            returnValue = returnValue.replace("+", "%2B");


            returnValue = URLDecoder.decode(returnValue, "utf-8");

        }


        returnValue = returnValue.replaceAll("%20", " ");


        if (!returnValue.endsWith(File.separator))
            returnValue = returnValue.concat(File.separator);


        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            if (!returnValue.startsWith(SEP)) {
                returnValue = SEP.concat(returnValue);
            }
        }

        return returnValue;
    }


    public String getRealPath() throws UnsupportedEncodingException {
        String returnValue = "";


        URL resURL = objClass.getClassLoader().getResource("");


        returnValue = getPath(resURL);

        return returnValue;
    }


    public String getRootPath() throws UnsupportedEncodingException {
        String returnValue = "";


        URL resURL = objClass.getResource("/");

        returnValue = getPath(resURL);

        return returnValue;
    }


    public String getProjectRootPath() throws UnsupportedEncodingException {

        String rootPath = getRootPath();


        if (rootPath.endsWith(SEP + "bin" + SEP)) {
            rootPath = rootPath.substring(0, rootPath.lastIndexOf(SEP + "bin" + SEP));
        } else if (rootPath.endsWith(SEP + "lib" + SEP)) {
            rootPath = rootPath.substring(0, rootPath.lastIndexOf(SEP + "lib" + SEP));
        } else if (rootPath.endsWith(SEP + "target" + SEP + "classes" + SEP)) {
            rootPath = rootPath.substring(0, rootPath.lastIndexOf(SEP + "target" + SEP + "classes" + SEP));
        } else if (rootPath.endsWith(SEP + "target" + SEP + "test-classes" + SEP)) {
            rootPath = rootPath.substring(0, rootPath.lastIndexOf(SEP + "target" + SEP + "test-classes" + SEP));
        } else if (rootPath.endsWith(SEP + "classes" + SEP)) {
            rootPath = rootPath.substring(0, rootPath.lastIndexOf(SEP + "classes" + SEP));
        }


        if (!rootPath.endsWith(SEP))
            rootPath = rootPath.concat(SEP);

        return rootPath;
    }

    public String debug() {

        String rtv = "";

        try {
            FilePathUtil fp = new FilePathUtil(objClass);
            rtv += "getClassName: " + fp.getClassName();
            rtv += "\r\n";
            rtv += "getPackageName: " + fp.getPackageName();
            rtv += "\r\n";
            rtv += "getClassFileName: " + fp.getClassFileName();
            rtv += "\r\n";
            rtv += "getRealPath: " + fp.getRealPath();
            rtv += "\r\n";
            rtv += "getRootPath: " + fp.getRootPath();
            rtv += "\r\n";
            rtv += "getProjectRootPath: " + fp.getProjectRootPath();
            rtv += "\r\n";
            rtv += "OS: " + System.getProperty("os.name");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtv;
    }


}