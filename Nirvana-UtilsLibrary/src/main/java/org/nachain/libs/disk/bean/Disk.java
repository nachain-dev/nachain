package org.nachain.libs.disk.bean;

public class Disk {

    private String canonicalPath;

    private String diskDescription;

    private double usableSize;

    private double totalSize;

    public String getCanonicalPath() {
        return canonicalPath;
    }

    public Disk setCanonicalPath(String canonicalPath) {
        this.canonicalPath = canonicalPath;
        return this;
    }

    public String getDiskDescription() {
        return diskDescription;
    }

    public Disk setDiskDescription(String diskDescription) {
        this.diskDescription = diskDescription;
        return this;
    }

    public double getUsableSize() {
        return usableSize;
    }

    public Disk setUsableSize(double usableSize) {
        this.usableSize = usableSize;
        return this;
    }

    public double getTotalSize() {
        return totalSize;
    }

    public Disk setTotalSize(double totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    @Override
    public String toString() {
        return "Disk{" +
                "canonicalPath='" + canonicalPath + '\'' +
                ", diskDescription='" + diskDescription + '\'' +
                ", usableSize=" + usableSize +
                ", totalSize=" + totalSize +
                '}';
    }
}