package org.nachain.libs.speedrecord;

import org.nachain.libs.log.Log;

import java.util.List;
import java.util.Vector;

public class SpeedRecord {

    private Log log;

    private String RecordName;

    private long StartTiem;

    private long LastRecordTime;

    private long FlagTime;

    private boolean isEnable;

    private long ValveValue;


    private List<String> logList = new Vector<>();


    private boolean isFlushAuto;

    public SpeedRecord() {
        this("", null, true);
    }


    public SpeedRecord(String RecordName, Log log) {
        this(RecordName, log, true);
    }


    public SpeedRecord(String RecordName, Log log, boolean isEnable) {
        this(RecordName, log, isEnable, true);
    }


    public SpeedRecord(String RecordName, Log log, boolean isEnable, boolean isFlushAuto) {

        this.RecordName = RecordName;
        this.log = log;
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


    public SpeedRecord(String RecordName, long ValveValue, Log log) {
        this.RecordName = RecordName;
        this.StartTiem = System.currentTimeMillis();
        this.LastRecordTime = StartTiem;
        this.ValveValue = ValveValue;
        this.log = log;
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