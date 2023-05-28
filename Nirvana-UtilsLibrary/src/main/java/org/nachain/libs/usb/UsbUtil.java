package org.nachain.libs.usb;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.shell.ShellUtil;
import org.nachain.libs.usb.bean.Usb;
import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.FileUtil;
import org.nachain.libs.util.RandomUtil;
import org.nachain.libs.util.ValueUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class UsbUtil {

    private static StringBuffer vbsCode;

    private static File vbsFile;

    static {

        vbsCode = new StringBuffer();
        vbsCode.append("Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n");
        vbsCode.append("Set colItems = objWMIService.ExecQuery(\"Select * From Win32_USBHub\")\n");
        vbsCode.append("\n");

        vbsCode.append("Dim response\n");
        vbsCode.append("\n");
        vbsCode.append("For Each objItem In colItems\n");

        vbsCode.append("	Dim DeviceID\n");
        vbsCode.append("	DeviceID = objItem.DeviceID \n");

        vbsCode.append("	Dim Name\n");
        vbsCode.append("	Name=objItem.Name\n");

        vbsCode.append("	Dim Status\n");
        vbsCode.append("	Status=objItem.Status\n");

        vbsCode.append("	Dim VidPid\n");
        vbsCode.append("	\n");

        vbsCode.append("	If InStr(DeviceID, \"VID\")<>0 And InStr(DeviceID, \"PID\")<>0 Then\n");

        vbsCode.append("		parameterArray = Split(DeviceID, \"\\\") \n");

        vbsCode.append("		VidPid = parameterArray(1)\n");

        vbsCode.append("		response = response & \"DeviceID=\" & DeviceID & \";VidPid=\"&VidPid & \";Name=\" & Name & \";Status=\" & Status & Chr(10)\n");
        vbsCode.append("	End If\n");
        vbsCode.append("Next\n");
        vbsCode.append("\n");

        vbsCode.append("wscript.Echo response\n");


        try {
            vbsFile = File.createTempFile(RandomUtil.getRandomStringNum(5), ".vbs");
            FileUtil.saveFile(vbsCode.toString(), vbsFile.getPath());
        } catch (IOException e) {
            log.error("Error creating temporary file", e);
        }


        buildVBSFile();
    }


    private static void buildVBSFile() {
        if (!vbsFile.exists()) {
            FileUtil.saveFile(vbsCode.toString(), vbsFile.getPath());
        }
    }


    public static Usb getUsb(String VidPid) {

        Usb returnValue = null;

        ArrayList<Usb> usbList = getUsbs();
        for (Object obj : usbList) {
            Usb usb = (Usb) obj;

            if (VidPid.equals(usb.getVidPid())) {
                returnValue = usb;
            }
        }

        return returnValue;
    }


    public static ArrayList<Usb> getUsbs() {
        ArrayList<Usb> returnValue = new ArrayList<Usb>();


        buildVBSFile();


        ShellUtil su = new ShellUtil();
        String response = su.runScript(vbsFile.getPath());
        response = CommUtil.replace(response, "\r\n", "\n");

        String[] lines = response.split("\n");
        for (Object obj : lines) {
            String line = (String) obj;

            HashMap<String, String> pMap = ValueUtil.analyzeParameter(line, ";");

            Usb usb = new Usb();
            usb.setDeviceID(pMap.get("DeviceID"));
            usb.setName(pMap.get("Name"));
            usb.setStatus(pMap.get("Status"));
            usb.setVidPid(pMap.get("VidPid"));
            returnValue.add(usb);
        }

        return returnValue;
    }


    public static void main(String[] args) {
        Usb usb = UsbUtil.getUsb("VID_090C&PID_1000");
        if (usb != null) {
            log.debug(usb.getName());
        }
    }

}