package org.nachain.core.token.nft;

import org.nachain.core.util.JsonUtils;

import java.util.List;


public class NftItemDetail {


    long token;


    long nftItemId;


    String name;


    String description;


    NftContentTypeEnum ContentType;


    String preview;


    String original;


    List<NftItemAttr> properties;

    public long getToken() {
        return token;
    }

    public NftItemDetail setToken(long token) {
        this.token = token;
        return this;
    }

    public long getNftItemId() {
        return nftItemId;
    }

    public NftItemDetail setNftItemId(long nftItemId) {
        this.nftItemId = nftItemId;
        return this;
    }

    public String getName() {
        return name;
    }

    public NftItemDetail setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public NftItemDetail setDescription(String description) {
        this.description = description;
        return this;
    }

    public NftContentTypeEnum getContentType() {
        return ContentType;
    }

    public NftItemDetail setContentType(NftContentTypeEnum contentType) {
        ContentType = contentType;
        return this;
    }

    public String getPreview() {
        return preview;
    }

    public NftItemDetail setPreview(String preview) {
        this.preview = preview;
        return this;
    }

    public String getOriginal() {
        return original;
    }

    public NftItemDetail setOriginal(String original) {
        this.original = original;
        return this;
    }

    public List<NftItemAttr> getProperties() {
        return properties;
    }

    public NftItemDetail setProperties(List<NftItemAttr> properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public String toString() {
        return "NftItemDetail{" +
                "token=" + token +
                ", nftItemId=" + nftItemId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ContentType=" + ContentType +
                ", preview='" + preview + '\'' +
                ", original='" + original + '\'' +
                ", properties=" + properties +
                '}';
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }

}
