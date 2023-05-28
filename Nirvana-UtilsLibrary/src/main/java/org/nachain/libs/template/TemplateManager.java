package org.nachain.libs.template;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.template.bean.Template;
import org.nachain.libs.util.ClassUtil;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class TemplateManager {


    protected final static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected final static Lock readLock = readWriteLock.readLock();
    protected final static Lock writeLock = readWriteLock.writeLock();

    private String[] telNames;
    private String[] tleContents;

    protected TemplateManager(Class<?> objClass, List<Template> templateList) {
        int tleSize = templateList.size();


        this.telNames = new String[tleSize];
        this.tleContents = new String[tleSize];

        setClassValue(objClass, templateList);
    }


    protected void templateList2Array(List<Template> templateList) {

        int i = 0;
        for (Object obj : templateList) {
            Template t = (Template) obj;
            int tleID = i;

            addTemplate(tleID, t);

            i++;
        }
    }


    protected void setClassValue(Class<?> objClass, List<Template> templateList) {

        String ClassName = objClass.getName();
        String valueName = null;
        try {

            int i = 0;
            for (Object obj : templateList) {
                Template t = (Template) obj;
                int tleID = i;
                valueName = t.getTleName();

                ClassUtil.setStaticProperty(ClassName, valueName, tleID);
                i++;
            }
        } catch (Exception e) {
            log.error("There was an error setting static properties,variable name=" + valueName, e);
        }
    }


    protected void addTemplate(int templateID, Template t) {

        this.telNames[templateID] = t.getTleName();


        this.tleContents[templateID] = t.getTleContent();

    }


    protected String merge(int TempletID, String[][] data) {

        String tleContent = tleContents[TempletID];
        return TemplateUtil.merge(tleContent, data);
    }

}