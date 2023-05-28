package org.nachain.core.persistence.leveldb;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

public class LevelDB {

    private DB db;

    @Deprecated
    public LevelDB(String pathname) {
        Options options = new Options();
        options.createIfMissing(true);

        options.cacheSize(128L * 1024L * 1024L);
        options.compressionType(CompressionType.NONE);

        File f = new File("database/testnet/index");

        try (DB db = JniDBFactory.factory.open(f, options)) {
            DBIterator itr = db.iterator();
            itr.seekToFirst();
            while (itr.hasNext()) {
                Entry<byte[], byte[]> entry = itr.next();

            }
            itr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
