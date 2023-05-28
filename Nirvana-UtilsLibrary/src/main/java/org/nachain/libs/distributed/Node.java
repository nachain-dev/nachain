package org.nachain.libs.distributed;

public class Node implements java.io.Serializable, Cloneable {

    private String Name;

    public Node(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}