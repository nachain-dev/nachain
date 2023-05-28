package org.nachain.ftp.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public interface IFileSystem<F extends Object> {


    F getRoot();


    String getPath(F file);


    boolean exists(F file);


    boolean isDirectory(F file);


    int getPermissions(F file);


    long getSize(F file);


    long getLastModified(F file);


    int getHardLinks(F file);


    String getName(F file);


    String getOwner(F file);


    String getGroup(F file);


    default byte[] getDigest(F file, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest d = MessageDigest.getInstance(algorithm);
        InputStream in = readFile(file, 0);
        byte[] bytes = new byte[1024];
        int length;

        while ((length = in.read(bytes)) != -1) {
            d.update(bytes, 0, length);
        }

        return d.digest();
    }


    F getParent(F file) throws IOException;


    F[] listFiles(F dir) throws IOException;


    F findFile(String path) throws IOException;


    F findFile(F cwd, String path) throws IOException;


    InputStream readFile(F file, long start) throws IOException;


    OutputStream writeFile(F file, long start) throws IOException;


    void mkdirs(F file) throws IOException;


    void delete(F file) throws IOException;


    void rename(F from, F to) throws IOException;


    void chmod(F file, int perms) throws IOException;


    void touch(F file, long time) throws IOException;

}
