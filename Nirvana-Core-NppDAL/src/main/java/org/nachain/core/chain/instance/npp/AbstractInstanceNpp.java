package org.nachain.core.chain.instance.npp;

import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;

public abstract class AbstractInstanceNpp implements IInstanceNpp {


    protected long id;


    protected long instanceId;


    protected String name;


    protected String version;


    protected String author;


    protected InstanceType instanceType;


    protected String hash;

    public long getId() {
        return id;
    }

    public AbstractInstanceNpp setId(long id) {
        this.id = id;
        return this;
    }

    public long getInstanceId() {
        return instanceId;
    }

    public AbstractInstanceNpp setInstanceId(long instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public String getName() {
        return name;
    }

    public AbstractInstanceNpp setName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public AbstractInstanceNpp setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public AbstractInstanceNpp setAuthor(String author) {
        this.author = author;
        return this;
    }


    public InstanceType getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(InstanceType instanceType) {
        this.instanceType = instanceType;
    }

    public String getHash() {
        return hash;
    }

    public AbstractInstanceNpp setHash(String hash) {
        this.hash = hash;
        return this;
    }


    public byte[] encodeHash() {

        return Hash.h256(this.toHashString().getBytes());
    }


    public String encodeHashString() {
        return Hex.encode0x(encodeHash());
    }
}
