package org.nachain.libs.shell;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class ShellUtil {


    private StringBuffer response = new StringBuffer();

    public ShellUtil() {
    }


    public StringBuffer getResponse() {
        return response;
    }


    class InputStreamDrainer implements Runnable {

        private InputStream is;
        private String charset;
        private IOutputCallback outputCallback;

        public InputStreamDrainer(InputStream is, String charset, IOutputCallback outputCallback) {
            this.is = is;
            this.charset = charset;
            this.outputCallback = outputCallback;
        }

        @Override
        public void run() {
            try {
                BufferedInputStream in = new BufferedInputStream(is);
                BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
                String line = null;
                while ((line = br.readLine()) != null) {
                    response.append(line + "\n");

                    if (outputCallback != null) {
                        outputCallback.processReadLine(line);
                    }
                }
            } catch (Exception e) {
                log.error("Handle different outputs through threads", e);
            }
        }
    }


    public void run(String command) throws IOException {
        run(command, false, 0, "utf-8");
    }


    public String run(String command, String charset) {
        return run(command, false, 0, charset);
    }


    public String runResponse(String command) {
        return run(command, true, 0, "utf-8");
    }


    public String runResponse(String command, String charset) {
        return run(command, true, 0, charset);
    }


    public String runCmd(String command) {
        if (!command.startsWith("cmd") || command.indexOf("cmd.exe") == -1) {
            command = "cmd /c " + command;
        }
        return run(command, true, 0, "gbk");
    }


    public String runScript(String command) {
        if (!command.startsWith("cscript.exe") || command.indexOf("cscript.exe") == -1) {
            command = "cscript.exe " + command;
        }

        return run(command, true, 3, "gbk");
    }


    public String run(String command, boolean isResponse, int skipLine, String charset) {
        ShellResult shellResult = runResult(command, isResponse, skipLine, charset);

        return shellResult.getResponse();
    }


    public ShellResult runResult(String command, boolean isResponse, int skipLine, String charset) {

        ShellResult shellResult = new ShellResult();


        Process process = null;
        try {

            process = Runtime.getRuntime().exec(command);
            if (isResponse) {


                new Thread(new InputStreamDrainer(process.getInputStream(), charset, null)).start();
                new Thread(new InputStreamDrainer(process.getErrorStream(), charset, null)).start();

                process.waitFor();


                log.debug("command:" + command + ", response:" + response);


                String[] data = response.toString().split("\n");
                StringBuffer newResponse = new StringBuffer();


                if (skipLine >= data.length) {
                    response = newResponse;
                } else {
                    int startNum = 0;
                    if (skipLine <= 0) {
                        startNum = 0;
                    } else {
                        startNum = skipLine;
                    }
                    response = new StringBuffer();
                    for (int i = startNum; i < data.length; i++) {
                        response.append(data[i] + "\n");
                    }
                }
            }
            shellResult.setResponse(response.toString());
        } catch (IOException e) {
            log.error("Error calling external program with Runtime:", e);
        } catch (InterruptedException e) {
            log.error("Error calling external program with Runtime:", e);
        } finally {
            try {
                process.getInputStream().close();
                process.getErrorStream().close();

            } catch (IOException e) {
                log.error("Disable I/O egress error", e);
            }

        }

        return shellResult;
    }


}