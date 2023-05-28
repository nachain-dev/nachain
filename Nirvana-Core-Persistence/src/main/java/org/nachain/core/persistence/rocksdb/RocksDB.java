package org.nachain.core.persistence.rocksdb;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.nachain.core.persistence.IDB;
import org.nachain.core.persistence.rocksdb.page.SortEnum;
import org.nachain.core.util.FileUtils;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class RocksDB implements IDB {
    private static final Logger logger = LoggerFactory.getLogger(RocksDB.class);

    static {

        org.rocksdb.RocksDB.loadLibrary();
    }


    private final String dbName;

    private final long groupId;

    private final String dbPath;

    private org.rocksdb.RocksDB rocksDB;


    public RocksDB(String dbName, String kvPath) throws RocksDBException, IOException {
        this.dbName = dbName;
        this.groupId = 0;
        this.dbPath = kvPath + File.separator + groupId;
        init();
    }


    public RocksDB(String dbName, long groupId, String kvPath) throws RocksDBException, IOException {
        this.dbName = dbName;
        this.groupId = groupId;
        this.dbPath = kvPath + File.separator + groupId;
        init();
    }


    public void init() throws RocksDBException, IOException {
        Path path = Paths.get(dbPath);

        if (!Files.isSymbolicLink(path)) {
            Files.createDirectories(path);
        }

        Options options = new Options();
        options.setCreateIfMissing(true);
        options.setInfoLogLevel(InfoLogLevel.WARN_LEVEL);
        options.setKeepLogFileNum(10);


        rocksDB = org.rocksdb.RocksDB.open(options, dbPath);
    }


    public org.rocksdb.RocksDB getRocksDB() {
        return rocksDB;
    }


    public void setRocksDB(org.rocksdb.RocksDB rocksDB) {
        this.rocksDB = rocksDB;
    }


    public byte[] get(byte[] key) throws RocksDBException {
        long start = System.currentTimeMillis();
        byte[] retBuf = this.rocksDB.get(key);
        long cost = System.currentTimeMillis() - start;
        if (cost > 200) {
            logger.warn("rocksdb get cost {}", cost);
        }
        return retBuf;
    }


    public String get(String key) throws RocksDBException {
        byte[] bytes = get(key.getBytes());
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }


    public String get(long key) throws RocksDBException {
        return get(String.valueOf(key));
    }


    public <T> T get(long key, Class<T> beanType) throws RocksDBException {
        String data = get(String.valueOf(key));
        if (data != null) {
            return JsonUtils.jsonToPojo(data, beanType);
        }

        return null;
    }


    public List<byte[]> gets(long startId, long endId) throws RocksDBException {
        List<byte[]> dataList = new ArrayList<>();


        for (long i = startId; i <= endId; i++) {
            byte[] data = get(String.valueOf(i).getBytes());
            dataList.add(data);
        }

        return dataList;
    }


    public <T> List<T> gets(long startId, long endId, Class<T> beanType) throws RocksDBException {
        List<T> dataList = new ArrayList<>();


        if (endId >= startId) {
            for (long i = startId; i <= endId; i++) {
                byte[] data = get(String.valueOf(i).getBytes());
                if (data != null) {
                    dataList.add(JsonUtils.jsonToPojo(new String(data), beanType));
                } else {
                    dataList.add(null);
                }
            }
        } else {
            for (long i = startId; i >= endId; i--) {
                byte[] data = get(String.valueOf(i).getBytes());
                if (data != null) {
                    dataList.add(JsonUtils.jsonToPojo(new String(data), beanType));
                } else {
                    dataList.add(null);
                }
            }
        }

        return dataList;
    }


    public List<byte[]> gets(List<String> keys) throws RocksDBException {
        List<byte[]> dataList = new ArrayList<>();


        for (String key : keys) {
            byte[] data = get(key.getBytes());
            dataList.add(data);
        }

        return dataList;
    }


    public <T> List<T> gets(List<String> keys, Class<T> beanType) throws RocksDBException {
        List<T> dataList = new ArrayList<>();


        for (String key : keys) {
            byte[] data = get(key.getBytes());
            if (data != null) {
                dataList.add(JsonUtils.jsonToPojo(new String(data), beanType));
            } else {
                dataList.add(null);
            }
        }

        return dataList;
    }


    public boolean put(byte[] key, byte[] value) throws RocksDBException {
        long start = System.currentTimeMillis();
        this.rocksDB.put(key, value);
        long cost = System.currentTimeMillis() - start;
        if (cost > 200) {
            logger.warn("rocksdb put cost {}", cost);
        }

        TpsCounter.addCounter(groupId, dbName);

        return true;
    }


    public boolean put(String key, String value) throws RocksDBException {
        return put(key.getBytes(), value.getBytes());
    }


    public boolean put(long key, String value) throws RocksDBException {
        return put(String.valueOf(key), value);
    }


    public void putData(byte[] key, byte[] value, WriteOptions writeOptions) {
        try {
            rocksDB.put(writeOptions, key, value);
        } catch (RocksDBException e) {
            logger.error(e.getMessage(), e);
        }
    }


    public boolean delete(byte[] key) throws RocksDBException {
        long start = System.currentTimeMillis();
        this.rocksDB.delete(key);
        long cost = System.currentTimeMillis() - start;
        if (cost > 200) {
            logger.warn("rocksdb delete cost {}", cost);
        }
        return true;
    }


    public void deleteData(byte[] key, WriteOptions writeOptions) {
        try {
            rocksDB.delete(writeOptions, key);
        } catch (RocksDBException e) {
            logger.error(e.getMessage(), e);
        }
    }


    public boolean delete(String key) throws RocksDBException {
        return delete(key.getBytes());
    }


    public boolean delete(long key) throws RocksDBException {
        return delete(String.valueOf(key));
    }


    private void updateByBatchInner(Map<byte[], byte[]> rows) throws Exception {
        try (WriteBatch batch = new WriteBatch()) {
            for (Map.Entry<byte[], byte[]> entry : rows.entrySet()) {
                if (entry.getValue() == null) {
                    batch.delete(entry.getKey());
                } else {
                    batch.put(entry.getKey(), entry.getValue());
                }
            }
            rocksDB.write(new WriteOptions(), batch);
        }
    }


    private void updateByBatchInner(Map<byte[], byte[]> rows, WriteOptions options)
            throws Exception {
        try (WriteBatch batch = new WriteBatch()) {
            for (Map.Entry<byte[], byte[]> entry : rows.entrySet()) {
                if (entry.getValue() == null) {
                    batch.delete(entry.getKey());
                } else {
                    batch.put(entry.getKey(), entry.getValue());
                }
            }
            rocksDB.write(options, batch);
        }
    }


    public void updateByBatch(Map<byte[], byte[]> rows) {
        try {
            updateByBatchInner(rows);
        } catch (Exception e) {
            try {
                updateByBatchInner(rows);
            } catch (Exception e1) {
                throw new RuntimeException(e);
            }
        }
    }


    public void updateByBatch(Map<byte[], byte[]> rows, WriteOptions writeOptions) {
        try {
            updateByBatchInner(rows, writeOptions);
        } catch (Exception e) {
            try {
                updateByBatchInner(rows);
            } catch (Exception e1) {
                throw new RuntimeException(e);
            }
        }
    }


    public Map<byte[], byte[]> getNext(byte[] key, long limit) {
        if (limit <= 0) {
            return Collections.emptyMap();
        }
        try (RocksIterator iter = rocksDB.newIterator()) {
            Map<byte[], byte[]> result = Maps.newLinkedHashMap();
            long i = 0;

            iter.seek(key);
            for (iter.next(); iter.isValid() && i < limit; iter.next(), i++) {
                result.put(iter.key(), iter.value());
            }
            return result;
        }
    }


    public Set<byte[]> getLatestValues(long limit) {

        if (limit <= 0) {
            return Sets.newHashSet();
        }
        try (RocksIterator iter = rocksDB.newIterator()) {
            Set<byte[]> result = Sets.newHashSet();
            long i = 0;
            for (iter.seekToLast(); iter.isValid() && i < limit; iter.prev(), i++) {
                result.add(iter.value());
            }
            return result;
        }
    }


    public Map<byte[], byte[]> getLatest(long limit) {
        Map<byte[], byte[]> result = Maps.newLinkedHashMap();
        if (limit <= 0) {
            return result;
        }
        try (RocksIterator iter = rocksDB.newIterator()) {
            long i = 0;
            for (iter.seekToLast(); iter.isValid() && i < limit; iter.prev(), i++) {
                result.put(iter.key(), iter.value());
            }
            return result;
        }
    }


    public Map<byte[], byte[]> getList(long limit, SortEnum sortEnum) {
        Map<byte[], byte[]> result = Maps.newLinkedHashMap();
        if (limit <= 0) {
            return result;
        }
        try (RocksIterator iter = rocksDB.newIterator()) {
            long i = 0;


            if (sortEnum == SortEnum.ASC) {
                for (iter.seekToFirst(); iter.isValid() && i < limit; iter.next(), i++) {
                    result.put(iter.key(), iter.value());
                }
            } else if (sortEnum == SortEnum.DESC) {
                for (iter.seekToLast(); iter.isValid() && i < limit; iter.prev(), i++) {
                    result.put(iter.key(), iter.value());
                }
            }

            return result;
        }
    }


    public Map<byte[], byte[]> getList(byte[] key, long limit, SortEnum sortEnum) {
        Map<byte[], byte[]> result = Maps.newLinkedHashMap();
        if (limit <= 0) {
            return result;
        }
        try (RocksIterator iter = rocksDB.newIterator()) {

            iter.seek(key);


            long i = 0;


            if (sortEnum == SortEnum.ASC) {

                for (iter.next(); iter.isValid() && i < limit; iter.next(), i++) {
                    result.put(iter.key(), iter.value());
                }
            } else if (sortEnum == SortEnum.DESC) {

                for (iter.prev(); iter.isValid() && i < limit; iter.prev(), i++) {
                    result.put(iter.key(), iter.value());
                }
            }

            return result;
        }
    }


    public Map<byte[], byte[]> getLatest(byte[] key, long limit) {
        Map<byte[], byte[]> result = Maps.newLinkedHashMap();
        if (limit <= 0) {
            return result;
        }
        try (RocksIterator iter = rocksDB.newIterator()) {
            long i = 0;

            iter.seek(key);

            return result;
        }
    }


    public Set<byte[]> getValuesPrev(byte[] key, long limit) {
        if (limit <= 0) {
            return Sets.newHashSet();
        }
        try (RocksIterator iter = rocksDB.newIterator()) {
            Set<byte[]> result = Sets.newHashSet();
            long i = 0;

            iter.seekForPrev(key);
            for (iter.prev(); iter.isValid() && i < limit; iter.prev(), i++) {
                result.add(iter.value());
            }
            return result;
        }
    }


    public Set<byte[]> getValuesNext(byte[] key, long limit) {
        if (limit <= 0) {
            return Sets.newHashSet();
        }
        try (RocksIterator iter = rocksDB.newIterator()) {
            Set<byte[]> result = Sets.newHashSet();
            long i = 0;

            iter.seek(key);
            for (iter.next(); iter.isValid() && i < limit; iter.next(), i++) {
                result.add(iter.value());
            }
            return result;
        }
    }


    public Map<byte[], byte[]> getFirstToPrevious(byte[] key, long limit, int precision) {
        if (limit <= 0 || key.length < precision) {
            return Collections.emptyMap();
        }
        try (RocksIterator iterator = rocksDB.newIterator()) {
            Map<byte[], byte[]> result = Maps.newLinkedHashMap();
            long i = 0;
            for (iterator.seekToFirst(); iterator.isValid() && i++ < limit; iterator.next()) {
                if (iterator.key().length >= precision) {

                    if (ByteUtil.less(ByteUtil.parseBytes(key, 0, precision), ByteUtil.parseBytes(iterator.key(), 0, precision))) {
                        break;
                    }
                    result.put(iterator.key(), iterator.value());
                }
            }
            return result;
        }
    }


    public List<byte[]> findAll() {
        List<byte[]> values = new ArrayList<>();
        try (final RocksIterator iterator = rocksDB.newIterator()) {
            for (iterator.seekToLast(); iterator.isValid(); iterator.prev()) {
                byte[] v = iterator.value();
                values.add(v);
            }
        }
        return values;
    }


    public List findAll(Class clazz) {
        List values = new ArrayList<>();
        try (final RocksIterator iterator = rocksDB.newIterator()) {
            for (iterator.seekToLast(); iterator.isValid(); iterator.prev()) {
                byte[] v = iterator.value();
                values.add(JsonUtils.jsonToPojo(new String(v), clazz));
            }
        }
        return values;
    }


    public List<String> findAllString() {
        List<String> values = new ArrayList<>();
        try (final RocksIterator iterator = rocksDB.newIterator()) {
            for (iterator.seekToLast(); iterator.isValid(); iterator.prev()) {
                byte[] v = iterator.value();
                values.add(new String(v));
            }
        }
        return values;
    }


    public Map<String, String> findAllMap() {
        Map<String, String> map = Maps.newLinkedHashMap();
        try (final RocksIterator iterator = rocksDB.newIterator()) {
            for (iterator.seekToLast(); iterator.isValid(); iterator.prev()) {
                byte[] value = iterator.value();
                byte[] key = iterator.key();
                map.put(new String(key), new String(value));
            }
        }
        return map;
    }


    public Map<String, Object> findAllMap(Class clazz) {
        Map<String, Object> map = Maps.newLinkedHashMap();
        try (final RocksIterator iterator = rocksDB.newIterator()) {
            for (iterator.seekToLast(); iterator.isValid(); iterator.prev()) {
                byte[] value = iterator.value();
                byte[] key = iterator.key();
                map.put(new String(key), JsonUtils.jsonToPojo(new String(value), clazz));
            }
        }
        return map;
    }


    public long count() {
        long count = 0;
        try (final RocksIterator iterator = rocksDB.newIterator()) {


            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                count++;
            }
        }
        return count;
    }


    public long countEstimate() {
        try {
            return rocksDB.getLongProperty("rocksdb.estimate-num-keys");
        } catch (RocksDBException e) {
            throw new RuntimeException("Error in getting records count", e);
        }
    }


    public Set<byte[]> allKeys() throws RuntimeException {
        Set<byte[]> result = Sets.newHashSet();
        try (final RocksIterator iter = rocksDB.newIterator()) {
            for (iter.seekToFirst(); iter.isValid(); iter.next()) {
                result.add(iter.key());
            }
            return result;
        }
    }


    public boolean keyMayExist(byte[] key) {
        final Holder<byte[]> holder = new Holder<>();
        return rocksDB.keyMayExist(key, holder);
    }


    public boolean keyMayExist(String key) {
        final Holder<byte[]> holder = new Holder<>();
        return rocksDB.keyMayExist(key.getBytes(StandardCharsets.UTF_8), holder);
    }


    public boolean keyMayExist(long key) {
        return keyMayExist(String.valueOf(key));
    }


    public void close() {
        this.rocksDB.close();
        this.rocksDB = null;
        logger.info("rocksdb close.groupid {}", groupId);
    }


    public void makeCheckPoint(String path) throws RocksDBException {
        Checkpoint checkpoint = Checkpoint.create(rocksDB);
        checkpoint.createCheckpoint(path);
    }


    public boolean recoverCheckpoint(String checkPointPath, List<String> fileList) throws RocksDBException, IOException {
        if (rocksDB != null) {
            logger.info("rocksdb close");
            rocksDB.close();
            rocksDB = null;
        }
        File file = new File(dbPath);
        FileUtils.deleteDir(dbPath + ".bak");
        if (!file.renameTo(new File(dbPath + ".bak"))) {
            logger.error("rename file error");
            return false;
        }
        for (String s : fileList) {
            logger.info("checkpoint file {}", s);
        }
        File checkpoint = new File(checkPointPath);
        boolean b = checkpoint.renameTo(new File(dbPath));
        if (!b) {
            logger.error("rename chaeckpoint error");
            return false;
        }
        init();
        return true;
    }


    public boolean isEmpty() {
        RocksIterator it = null;
        try {
            it = rocksDB.newIterator();
            it.seekToFirst();
            return !it.isValid();
        } finally {
            if (it != null) {
                it.close();
            }
        }

    }


}