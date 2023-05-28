package org.nachain.core.util;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Vector;

@Slf4j
public class SpeedRecord {

    private String RecordName;

    private long StartTiem;

    private long LastRecordTime;

    private long FlagTime;

    private boolean isEnable;

    private long ValveValue;


    private List<String> logList = new Vector<>();


    private boolean isFlushAuto;

    public SpeedRecord() {
        this("", true);
    }


    public SpeedRecord(String RecordName) {
        this(RecordName, true);
    }


    public SpeedRecord(String RecordName, boolean isEnable) {
        this(RecordName, isEnable, true);
    }


    public SpeedRecord(String RecordName, boolean isEnable, boolean isFlushAuto) {
        this.RecordName = RecordName;
        this.StartTiem = System.currentTimeMillis();
        this.LastRecordTime = StartTiem;
        this.isEnable = isEnable;
        this.isFlushAuto = isFlushAuto;
    }


    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isFlushAuto() {
        return isFlushAuto;
    }

    public SpeedRecord setFlushAuto(boolean flushAuto) {
        isFlushAuto = flushAuto;
        return this;
    }


    public SpeedRecord(String RecordName, long ValveValue) {
        this.RecordName = RecordName;
        this.StartTiem = System.currentTimeMillis();
        this.LastRecordTime = StartTiem;
        this.ValveValue = ValveValue;
    }


    public void addFlag(String flagInfo) {
        if (!isEnable) {
            return;
        }

        FlagTime = System.currentTimeMillis();
        if (FlagTime - LastRecordTime >= ValveValue) {
            String msg = "Speed record:" + RecordName + "(" + flagInfo + "); Always use:" + (FlagTime - StartTiem) + " ms; use:" + (FlagTime - LastRecordTime) + " ms";
            if (log != null) {
                if (isFlushAuto) {
                    log.debug(msg);
                } else {
                    logList.add(msg);
                }
            } else {
                System.out.println(msg);
            }
        }
        LastRecordTime = FlagTime;
    }


    public void flush() {
        if (!isEnable) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        while (logList.size() > 0) {
            String remove = logList.remove(0);
            sb.append(remove).append("\n");
        }

        log.debug(sb.toString());
    }


    public void addFlag(double index) {
        addFlag(String.valueOf(index));
    }

}