package org.nachain.core.chain.instance.npp;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.util.JsonUtils;

import java.util.Arrays;

public class DApp extends AbstractInstanceNpp {


    private String[] domains;


    private String nppStore;

    public String[] getDomains() {
        return domains;
    }

    public DApp setDomains(String[] domains) {
        this.domains = domains;
        return this;
    }

    public String getNppStore() {
        return nppStore;
    }

    public DApp setNppStore(String nppStore) {
        this.nppStore = nppStore;
        return this;
    }

    @Override
    public String toString() {
        return "DApp{" +
                "domains=" + Arrays.toString(domains) +
                ", nppStore='" + nppStore + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", instanceType=" + instanceType +
                ", hash='" + hash + '\'' +
                '}';
    }

    @Override
    public String toHashString() {
        return "DApp{" +
                "domains=" + Arrays.toString(domains) +
                ", nppStore='" + nppStore + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", instanceType=" + instanceType +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }


    public static DApp toDApp(String json) {
        return JsonUtils.jsonToPojo(json, DApp.class);
    }
}
