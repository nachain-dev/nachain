package org.nachain.libs.template;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.template.bean.Template;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TleHandle extends TemplateManager {


    private static TleHandle instance;


    public static int CKEditor;
    public static int Pagination;

    private static List<Template> templateList;

    private TleHandle() {
        super(TleHandle.class, templateList);
    }


    static {
        templateList = new ArrayList<Template>();
        try {
            templateList.add(new Template("CKEditor", ""));
        } catch (Exception e) {
            log.error("Error creating template:", e);
        }
        getInstance();
    }


    public static TleHandle getInstance() {
        if (instance == null) {
            writeLock.lock();
            try {
                instance = new TleHandle();
            } finally {
                writeLock.unlock();
            }
        }

        return instance;
    }


    public static String ckEditor(String InstanceName, String CategoryName, String ToolbarName, String EditorHeight) {
        String returnValue = "";

        String[][] data = {{"InstanceName", InstanceName}, {"CategoryName", CategoryName}, {"ToolbarName", ToolbarName}, {"EditorHeight", EditorHeight}};

        returnValue = TleHandle.getInstance().merge(CKEditor, data);

        return returnValue;
    }

    public static String ckEditor(String InstanceName, String CategoryName) {
        String returnValue = "";

        String[][] data = {{"InstanceName", InstanceName}, {"CategoryName", CategoryName}, {"ToolbarName", "Custom"}, {"EditorHeight", "480"}};

        returnValue = TleHandle.getInstance().merge(CKEditor, data);

        return returnValue;
    }

}