package org.nachain.libs.disk;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.disk.bean.Disk;
import org.nachain.libs.util.ByteUtil;
import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.RandomUtil;

import javax.swing.filechooser.FileSystemView;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Slf4j
public class DiskUtil {

    private static final byte[] blockData32K = newBlockData(32);
    private static final byte[] blockData64K = newBlockData(64);
    private static final byte[] blockData128K = newBlockData(128);
    private static final byte[] blockData512K = newBlockData(512);
    private static final byte[] blockData1024K = newBlockData(1024);


    public static List<Disk> getDisks() {
        List<Disk> list = new ArrayList<Disk>();

        FileSystemView fileSystemView = FileSystemView.getFileSystemView();

        double unit = 1024 * 1024 * 1024;
        File[] roots = File.listRoots();
        try {
            for (File file : roots) {

                String canonicalPath = file.getCanonicalPath();

                String diskDescription = fileSystemView.getSystemTypeDescription(file);


                long usableSpace = file.getUsableSpace();

                long totalSpace = file.getTotalSpace();


                Disk disk = new Disk();
                disk.setCanonicalPath(canonicalPath);
                disk.setDiskDescription(diskDescription);
                disk.setUsableSize((int) (usableSpace / unit * 10) / 10d);
                disk.setTotalSize((int) (totalSpace / unit * 10) / 10d);

                list.add(disk);
            }
        } catch (IOException e) {
            log.error("Error getting drive letter information", e);
        }

        return list;
    }


    private static byte[] newBlockData(int blockSize) {

        byte[] writeData = new byte[0];

        byte[] dataUnit = CommUtil.reduplicate("1", blockSize).getBytes();

        for (int i = 1; i <= 1024; i++) {

            writeData = ByteUtil.byteMerger(writeData, dataUnit);
        }
        return writeData;
    }


    public static long writeSpeedTest(String diskName, long testKBSize) throws Exception {

        long writeTotal = 0;
        long startTime = System.currentTimeMillis();

        String fileName = RandomUtil.getRandomDateNum(3);

        File tmpFile = File.createTempFile(fileName, ".writeSpeed", new File(diskName));

        tmpFile.deleteOnExit();
        log.debug("Disk write test file:" + tmpFile.getPath());
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(tmpFile);
            bos = new BufferedOutputStream(fos);
            Random rad = new Random();

            long testTotal = 1024 * testKBSize;
            log.debug("testTotal:" + testTotal);
            byte[] writeData = blockData512K;
            while (writeTotal < testTotal) {
                bos.write(writeData);

                writeTotal += writeData.length;
            }
        } catch (Exception e) {
            log.error("The disk write performance test failed:" + diskName, e);
        } finally {
            bos.flush();
            bos.close();

            tmpFile.delete();
        }


        long usrTime = System.currentTimeMillis() - startTime;

        long writeSpeedByMillisecond = writeTotal / usrTime;

        long writeSpeedBySecond = writeSpeedByMillisecond * 1000;

        return writeSpeedBySecond;
    }


}