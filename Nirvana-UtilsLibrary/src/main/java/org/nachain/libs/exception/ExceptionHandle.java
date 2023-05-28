package org.nachain.libs.exception;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

@Slf4j
public class ExceptionHandle {


    public static String getException(Throwable t) {
        String returnValue = "";

        if (t != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            returnValue = sw.getBuffer().toString();
            try {
                sw.close();
            } catch (IOException e) {
                log.error("Error closing StringWriter after getting Exception information", e);
            }
            pw.close();
            returnValue = returnValue.replaceAll("\r\n", "\n").replaceAll("\r", "\n").replaceAll("\n", "\r\n");
        }

        return returnValue;
    }


    public static String getException(Exception e) {
        String returnValue = "";

        if (e != null) {
            VectorWriter vw = new VectorWriter();
            extractStringRep(e, vw);
            returnValue = vw.toString();
            returnValue = returnValue.replaceAll("\r\n", "\n").replaceAll("\r", "\n").replaceAll("\n", "\r\n");
        }

        return returnValue;
    }


    public static String getExceptionInfo(Exception e, String info) {
        String returnValue = info + "\r\n" + getException(e);

        return returnValue;
    }


    public static Exception getException(Exception e, String info) {
        Exception returnValue = new Exception(getExceptionInfo(e, info));

        return returnValue;
    }


    @SuppressWarnings("unchecked")
    public static void extractStringRep(Throwable t, VectorWriter vw) {
        t.printStackTrace(vw);

        try {
            Class<? extends Throwable> tC = t.getClass();
            Method[] mA = tC.getMethods();
            Method nextThrowableMethod = null;
            for (int i = 0; i < mA.length; i++) {
                if ("getCause".equals(mA[i].getName())
                        || "getRootCause".equals(mA[i].getName())
                        || "getNextException".equals(mA[i].getName())
                        || "getException".equals(mA[i].getName())) {

                    Class[] params = mA[i].getParameterTypes();
                    if ((params == null) || (params.length == 0)) {

                        nextThrowableMethod = mA[i];
                        break;
                    }
                }
            }

            if (nextThrowableMethod != null) {

                Throwable nextT = (Throwable) nextThrowableMethod.invoke(t,
                        new Object[0]);
                if (nextT != null) {
                    vw.print("Root cause follows.\r\n");
                    extractStringRep(nextT, vw);
                }
            }
        } catch (Exception e) {

        }
    }
}