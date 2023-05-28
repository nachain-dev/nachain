package org.nachain.core.chain.instance.npp;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.util.JsonUtils;

public class Core extends AbstractInstanceNpp {

    @Override
    public String toString() {
        return "Core{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", instanceType=" + instanceType +
                ", hash='" + hash + '\'' +
                '}';
    }

    @Override
    public String toHashString() {
        return "Core{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", instanceType=" + instanceType +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }


    public static Core toCore(String json) {
        return JsonUtils.jsonToPojo(json, Core.class);
    }
}
