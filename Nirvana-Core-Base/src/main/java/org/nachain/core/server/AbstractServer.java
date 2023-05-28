package org.nachain.core.server;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractServer extends Thread implements IServer {


    protected boolean isRun;


    protected boolean isEnd;


    protected boolean isError;


    protected boolean isReady;


    protected boolean isSleep;


    protected long sleepMillis;


    protected boolean destroyExecutes;

    public AbstractServer() {
        log.info("AbstractServer() -> {}", status());
    }

    public boolean isRun() {
        return isRun;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public boolean isError() {
        return isError;
    }

    public boolean isReady() {
        return isReady;
    }

    public boolean isSleep() {
        return isSleep;
    }

    public long getSleepMillis() {
        return sleepMillis;
    }

    public boolean isDestroyExecutes() {
        return destroyExecutes;
    }

    public void destroyExecutes() {
        destroyExecutes = true;
    }


    public AbstractServer executeSleepMillis(long millis) {
        this.sleepMillis = millis;
        return this;
    }


    public String status() {
        return String.format("ServerName=%s, isRun=%s, isEnd=%s, isError=%s, isReady=%s", getServerName(), isRun, isEnd, isError, isReady);
    }


    public void startServer() {

        ServerHolder.register(getServerName(), this);


        this.start();

        log.info("Server {} starting succeed", getServerName());
    }


    public void stopServer() {
        isRun = false;
        log.info("Server {} stopping", getServerName());
    }


    protected void doInit() {
        isRun = true;
        isEnd = false;
        isError = false;
        isReady = false;
        isSleep = false;
    }


    protected void doReady() {

        if (!isReady) {
            isReady = true;
            log.debug("Server {} is ready", getServerName());
        }
    }


    protected void doError() {
        isError = true;
    }


    protected void doFinally() {
        isRun = false;
        isEnd = true;
        isReady = false;
        isSleep = false;
        log.info("Server {} is finally", getServerName());
    }


    private void executeSleep() throws InterruptedException {

        isSleep = true;
        if (sleepMillis > 0) {
            Thread.sleep(sleepMillis);
        }

        isSleep = false;
    }


    public void run() {
        doInit();
        try {

            starting();
            while (isRun && !isDestroyExecutes()) {
                try {

                    executes();

                    doReady();

                    executeSleep();
                } catch (Exception e) {
                    log.error("Server {} executes error.", getServerName(), e);
                }
            }
        } catch (Exception e) {
            doError();
            log.error("Server {} error.", getServerName(), e);
        } finally {
            doFinally();
        }
    }
}
