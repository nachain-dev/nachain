package org.nachain.core.chain.index;


public class IndexId {

    private String indexId;

    private String indexValue;


    public String getIndexId() {
        return indexId;
    }

    public IndexId setIndexId(String indexId) {
        this.indexId = indexId;
        return this;
    }

    public String getIndexValue() {
        return indexValue;
    }

    public IndexId setIndexValue(String indexValue) {
        this.indexValue = indexValue;
        return this;
    }

    @Override
    public String toString() {
        return "IndexId{" +
                "indexId='" + indexId + '\'' +
                ", indexValue='" + indexValue + '\'' +
                '}';
    }
}
