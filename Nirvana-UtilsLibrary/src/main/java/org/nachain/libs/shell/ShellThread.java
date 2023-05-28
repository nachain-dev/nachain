package org.nachain.libs.shell;


public class ShellThread extends Thread {

    private String command;

    private String charset;

    private ShellUtil shellUtil;

    private ShellThread() {
    }

    public ShellThread(String command, String charset) {
        this.command = command;
        this.charset = charset;
        shellUtil = new ShellUtil();
    }

    public String getResponse() {
        return shellUtil.getResponse().toString();
    }

    @Override
    public void run() {
        shellUtil.run(command, charset);
    }


}