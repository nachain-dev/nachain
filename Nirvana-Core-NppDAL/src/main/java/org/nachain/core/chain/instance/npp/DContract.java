package org.nachain.core.chain.instance.npp;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.util.JsonUtils;

public class DContract extends AbstractInstanceNpp {


    private String nppStore;

    public String getNppStore() {
        return nppStore;
    }

    public DContract setNppStore(String nppStore) {
        this.nppStore = nppStore;
        return this;
    }

    @Override
    public String toString() {
        return "DContract{" +
                "nppStore='" + nppStore + '\'' +
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
        return "DContract{" +
                "nppStore='" + nppStore + '\'' +
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


    public static DContract toDContract(String json) {
        return JsonUtils.jsonToPojo(json, DContract.class);
    }
}
