package org.nachain.ftp.impl;

import org.nachain.ftp.Utils;
import org.nachain.ftp.api.IFileSystem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;


public class NativeFileSystem implements IFileSystem<File> {

    private final File rootDir;


    public NativeFileSystem(File rootDir) {
        this.rootDir = rootDir;

        if (!rootDir.exists()) rootDir.mkdirs();
    }

    @Override
    public File getRoot() {
        return rootDir;
    }

    @Override
    public String getPath(File file) {
        return rootDir.toURI().relativize(file.toURI()).getPath();
    }

    @Override
    public boolean exists(File file) {
        return file.exists();
    }

    @Override
    public boolean isDirectory(File file) {
        return file.isDirectory();
    }

    @Override
    public int getPermissions(File file) {
        int perms = 0;
        perms = Utils.setPermission(perms, Utils.CAT_OWNER + Utils.TYPE_READ, file.canRead());
        perms = Utils.setPermission(perms, Utils.CAT_OWNER + Utils.TYPE_WRITE, file.canWrite());
        perms = Utils.setPermission(perms, Utils.CAT_OWNER + Utils.TYPE_EXECUTE, file.canExecute());
        return perms;
    }

    @Override
    public long getSize(File file) {
        return file.length();
    }

    @Override
    public long getLastModified(File file) {
        return file.lastModified();
    }

    @Override
    public int getHardLinks(File file) {
        return file.isDirectory() ? 3 : 1;
    }

    @Override
    public String getName(File file) {
        return file.getName();
    }

    @Override
    public String getOwner(File file) {
        return "-";
    }

    @Override
    public String getGroup(File file) {
        return "-";
    }

    @Override
    public File getParent(File file) throws IOException {
        if (file.equals(rootDir)) {
            throw new FileNotFoundException("No permission to access this file");
        }

        return file.getParentFile();
    }

    @Override
    public File[] listFiles(File dir) throws IOException {
        if (!dir.isDirectory()) throw new IOException("Not a directory");

        return dir.listFiles();
    }

    @Override
    public File findFile(String path) throws IOException {
        File file = new File(rootDir, path);

        if (!isInside(rootDir, file)) {
            throw new FileNotFoundException("No permission to access this file");
        }

        return file;
    }

    @Override
    public File findFile(File cwd, String path) throws IOException {
        File file = new File(cwd, path);

        if (!isInside(rootDir, file)) {
            throw new FileNotFoundException("No permission to access this file");
        }

        return file;
    }

    @Override
    public InputStream readFile(File file, long start) throws IOException {

        if (start <= 0) {
            return new FileInputStream(file);
        }


        final RandomAccessFile raf = new RandomAccessFile(file, "r");
        raf.seek(start);


        return new FileInputStream(raf.getFD()) {
            @Override
            public void close() throws IOException {
                super.close();
                raf.close();
            }
        };
    }

    @Override
    public OutputStream writeFile(File file, long start) throws IOException {

        if (start <= 0) {
            return new FileOutputStream(file, false);
        } else if (start == file.length()) {
            return new FileOutputStream(file, true);
        }


        final RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(start);


        return new FileOutputStream(raf.getFD()) {
            @Override
            public void close() throws IOException {
                super.close();
                raf.close();
            }
        };
    }

    @Override
    public void mkdirs(File file) throws IOException {
        if (!file.mkdirs()) throw new IOException("Couldn't create the directory");
    }

    @Override
    public void delete(File file) throws IOException {
        if (file.isDirectory()) {
            Files.walk(file.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } else {

            if (!file.delete()) throw new IOException("Couldn't delete the file");
        }
    }

    @Override
    public void rename(File from, File to) throws IOException {
        if (!from.renameTo(to)) throw new IOException("Couldn't rename the file");
    }

    @Override
    public void chmod(File file, int perms) throws IOException {
        boolean read = Utils.hasPermission(perms, Utils.CAT_OWNER + Utils.TYPE_READ);
        boolean write = Utils.hasPermission(perms, Utils.CAT_OWNER + Utils.TYPE_WRITE);
        boolean execute = Utils.hasPermission(perms, Utils.CAT_OWNER + Utils.TYPE_EXECUTE);

        if (!file.setReadable(read, true)) throw new IOException("Couldn't update the readable permission");
        if (!file.setWritable(write, true)) throw new IOException("Couldn't update the writable permission");
        if (!file.setExecutable(execute, true)) throw new IOException("Couldn't update the executable permission");
    }

    @Override
    public void touch(File file, long time) throws IOException {
        if (!file.setLastModified(time)) throw new IOException("Couldn't touch the file");
    }

    private boolean isInside(File dir, File file) {
        if (file.equals(dir)) return true;

        try {
            return file.getCanonicalPath().startsWith(dir.getCanonicalPath() + File.separator);
        } catch (IOException ex) {
            return false;
        }
    }

}
