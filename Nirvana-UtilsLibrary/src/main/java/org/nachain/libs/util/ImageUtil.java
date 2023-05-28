package org.nachain.libs.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.*;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class ImageUtil {


    public static boolean createMark(String filePath, String markContent, Color markContentColor, float qualNum) {

        ImageIcon imgIcon = new ImageIcon(filePath);
        Image theImg = imgIcon.getImage();
        int width = theImg.getWidth(null);
        int height = theImg.getHeight(null);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();

        bi = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = bi.createGraphics();

        g2d.setColor(markContentColor);
        g2d.setBackground(Color.white);
        g2d.drawImage(theImg, 0, 0, null);
        g2d.drawString(markContent, width / 5, height / 5);
        g2d.dispose();
        try {


            ImageIO.write(bi, "jpeg", new File(filePath));
            bi.flush();

        } catch (Exception e) {
            return false;
        }

        return true;
    }


    static void saveImage(BufferedImage dstImage, String dstName) throws IOException {
        String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);


        ImageIO.write(dstImage, formatName, new File(dstName));
    }


    public static void watermarkImage(String watermarkFile, String sourceFile, int x, int y) {
        try {

            File _file = new File(sourceFile);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage bi = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);


            File _filebiao = new File(watermarkFile);
            Image src_biao = ImageIO.read(_filebiao);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao, null);

            g.dispose();


            ImageIO.write(bi, "jpeg", new File(sourceFile));
            bi.flush();

        } catch (Exception e) {
            log.error("watermarkImage error", e);
        }
    }


    public static void pressText(String pressText, String targetImg, String fontName, int fontStyle, int color, int fontSize, int x, int y) {
        try {
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            g.setColor(Color.RED);
            g.setFont(new Font(fontName, fontStyle, fontSize));

            g.drawString(pressText, wideth - fontSize - x, height - fontSize / 2 - y);
            g.dispose();


            ImageIO.write(image, "jpeg", new File(targetImg));
            image.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void createPng(String content, String fontName, int fontSize, Color fontColor, boolean isAntialias, int width, int height, File file) throws FileNotFoundException, IOException {
        createPng(content, fontName, fontSize, fontColor, isAntialias, width, height, new FileOutputStream(file));
    }


    public static void createPng(String content, String fontName, int fontSize, Color fontColor, boolean isAntialias, int width, int height, OutputStream os) throws IOException {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = image.createGraphics();


        image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();


        Font font = new Font(fontName, Font.PLAIN, fontSize);
        g2d.setFont(font);


        if (isAntialias) {
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }


        g2d.setColor(new Color(62, 62, 62));
        g2d.drawString(content, 0, fontSize - 2);

        g2d.dispose();

        ImageIO.write(image, "png", os);
    }


    public static void createImage(OutputStream out) {
        int width = 100;
        int height = 100;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();


        g2d.clearRect(0, 0, width, height);

        g2d.setColor(Color.RED);

        g2d.drawLine(0, 0, 99, 199);

        g2d.dispose();
        bi.flush();


        try {
            ImageIO.write(bi, "jpeg", out);
            bi.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void write(ImageOutputStream out, IIOWriteProgressListener listener, String formatName, BufferedImage image, List<? extends BufferedImage> thumbnails, float quality) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("OutputStream must be non null");
        }

        if (formatName == null) {
            throw new IllegalArgumentException("FormatName must be non null");
        }

        if (image == null) {
            throw new IllegalArgumentException("Image must be non null");
        }


        Iterator<?> writers = ImageIO.getImageWritersByFormatName(formatName);
        if (writers == null || !writers.hasNext()) {
            throw new IllegalStateException("No " + formatName + " writers!");
        }
        ImageWriter writer = (ImageWriter) writers.next();

        ImageTypeSpecifier imageType = ImageTypeSpecifier.createFromRenderedImage(image);
        IIOMetadata metadata = writer.getDefaultImageMetadata(imageType, null);

        IIOImage iioImage = new IIOImage(image, thumbnails, metadata);

        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        writer.setOutput(out);
        writer.addIIOWriteProgressListener(listener);
        writer.write(null, iioImage, param);
        writer.dispose();
    }


    public static BufferedImage zoomImage(String src, float resize) throws IOException {

        BufferedImage result = null;

        try {
            File srcfile = new File(src);
            if (!srcfile.exists()) {
                throw new IOException("zoomImage error , file = " + src);
            }
            BufferedImage im = ImageIO.read(srcfile);


            result = zoomImage(im, resize);

        } catch (Exception e) {
            throw new IOException("zoomImage error: " + src);
        }

        return result;

    }


    public static BufferedImage zoomImage(BufferedImage im, float resize) {

        BufferedImage result = null;

        try {

            int width = im.getWidth();
            int height = im.getHeight();


            int toWidth = (int) (width * resize);
            int toHeight = (int) (height * resize);


            result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);

            result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, Image.SCALE_SMOOTH), 0, 0, null);

        } catch (Exception e) {
            log.error("zoom image error", e);
        }

        return result;

    }


    public static boolean writeHighQuality(BufferedImage im, String fileFullPath) {
        try {


            ImageIO.write(im, "jpeg", new File(fileFullPath));
            im.flush();

            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static String zipImageFile(String oldFile, int width, int height,
                                      float quality, String smallIcon) {
        if (oldFile == null) {
            return null;
        }
        String newImage = null;
        try {

            Image srcFile = ImageIO.read(new File(oldFile));

            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bi.getGraphics().drawImage(srcFile, 0, 0, width, height, null);
            String filePrex = oldFile.substring(0, oldFile.indexOf('.'));

            newImage = filePrex + smallIcon + oldFile.substring(filePrex.length());


            ImageIO.write(bi, "jpeg", new File(newImage));
            bi.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newImage;
    }


    public static String writeFile(String fileName, InputStream is) {
        if (fileName == null || fileName.trim().length() == 0) {
            return null;
        }
        try {

            FileOutputStream fos = new FileOutputStream(fileName);
            byte[] readBytes = new byte[512];
            int readed = 0;
            while ((readed = is.read(readBytes)) > 0) {
                fos.write(readBytes, 0, readed);
            }
            fos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }


    public static void saveMinPhoto(String srcURL, String deskURL, double comBase,
                                    double scale) throws Exception {
        File srcFile = new File(srcURL);
        Image src = ImageIO.read(srcFile);
        int srcHeight = src.getHeight(null);
        int srcWidth = src.getWidth(null);
        int deskHeight = 0;
        int deskWidth = 0;
        double srcScale = (double) srcHeight / srcWidth;

        if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
            if (srcScale >= scale || 1 / srcScale > scale) {
                if (srcScale >= scale) {
                    deskHeight = (int) comBase;
                    deskWidth = srcWidth * deskHeight / srcHeight;
                } else {
                    deskWidth = (int) comBase;
                    deskHeight = srcHeight * deskWidth / srcWidth;
                }
            } else {
                if ((double) srcHeight > comBase) {
                    deskHeight = (int) comBase;
                    deskWidth = srcWidth * deskHeight / srcHeight;
                } else {
                    deskWidth = (int) comBase;
                    deskHeight = srcHeight * deskWidth / srcWidth;
                }
            }
        } else {
            deskHeight = srcHeight;
            deskWidth = srcWidth;
        }
        BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
        tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null);


        ImageIO.write(tag, "jpeg", new File(deskURL));
        tag.flush();

    }


}