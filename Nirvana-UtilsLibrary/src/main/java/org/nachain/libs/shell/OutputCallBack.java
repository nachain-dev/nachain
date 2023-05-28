package org.nachain.libs.shell;


public abstract class OutputCallBack implements IOutputCallback {

    private Process process;

    public OutputCallBack(Process process) {
        this.process = process;
    }


    public Process getProcess() {
        return process;
    }


}