package org.nachain.libs.ftp;

import java.util.Calendar;

public class FfpFileInfo {

    private String Name;

    private long Size;

    private Calendar Timestamp;

    private boolean Type;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }

    public Calendar getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        Timestamp = timestamp;
    }

    public boolean isType() {
        return Type;
    }

    public void setType(boolean type) {
        Type = type;
    }

}