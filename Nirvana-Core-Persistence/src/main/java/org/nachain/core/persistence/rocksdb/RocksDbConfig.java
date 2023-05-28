package org.nachain.core.persistence.rocksdb;

import org.rocksdb.CompactionStyle;
import org.rocksdb.CompressionType;

public class RocksDbConfig {
    public static final boolean createIfMissing = true;
    public static final int maxOpenFile = -1;
    public static final long writeBufferSize = 67108864;
    public static final int maxWriteBufferNumber = 16;
    public static final int writeBufferNumberToMerge = 1;
    public static final int levelZeroFileNumCompactionTrigger = 10;
    public static final int level0SlowdownWritesTrigger = 20;
    public static final int level0StopWritesTrigger = 40;
    public static final int maxBackgroundCompactions = 10;
    public static final int maxBackgroundFlushes = 1;
    public static final double memtablePrefixBloomSizeRatio = 0.125;
    public static final int boolmFilter = 10;
    public static final CompressionType compressionType = CompressionType.NO_COMPRESSION;
    public static final CompactionStyle compactionStyle = CompactionStyle.LEVEL;
    public static final boolean useFsync = false;
    public static final long targetFileSizeBase = 12582912;
    public static final long maxFileLevelBase = 10485760;
    public static final long maxLogFileSize = 5368709120L;
    public static final int maxBackgroundJob = 10;
}
