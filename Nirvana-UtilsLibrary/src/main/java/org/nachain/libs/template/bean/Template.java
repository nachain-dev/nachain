package org.nachain.libs.template.bean;

import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.FilePathUtil;
import org.nachain.libs.util.IoUtil;

import java.io.File;

public class Template {

    private String TleName;

    private String TleContent;

    private String TleFilePath;

    public Template() {
    }

    public Template(String TleName, String TleFilePath) throws Exception {
        this.TleName = TleName;
        this.TleFilePath = TleFilePath;

        loadFile();
    }


    public void init(String TleName, String TleContent) {
        this.TleName = TleName;
        this.TleContent = TleContent;
    }


    public void loadFile() throws Exception {

        if (!CommUtil.isEmpty(this.TleFilePath)) {
            File tleFile = new File(this.TleFilePath);

            if (!tleFile.exists()) {

                String newPath = TleFilePath;

                String tleFolderPath = new FilePathUtil(Template.class).getProjectRootPath();
                tleFolderPath = tleFolderPath.concat("template/");

                if (newPath.startsWith(File.pathSeparator)) {
                    newPath = newPath.substring(1);
                }


                tleFile = new File(tleFolderPath + newPath);
                if (tleFile.exists()) {
                    String fileContent = "";
                    byte[] contentByte = IoUtil.fileToBytes(tleFile);


                    fileContent = new String(contentByte, "utf-8");

                    String regex = "";
                    regex = "[\t| ]*?<!--/\\*" + "[^\\*-->]*" + "\\*/-->\r\n";
                    fileContent = fileContent.replaceAll(regex, "");
                } else {
                    throw new Exception("file does not exist:" + newPath);
                }
            }
        }
    }

    public String getTleName() {
        return TleName;
    }

    public void setTleName(String tleName) {
        TleName = tleName;
    }

    public String getTleContent() {
        return TleContent;
    }

    public void setTleContent(String tleContent) {
        TleContent = tleContent;
    }

    public String getTleFilePath() {
        return TleFilePath;
    }

    public void setTleFilePath(String tleFilePath) {
        TleFilePath = tleFilePath;
    }

}