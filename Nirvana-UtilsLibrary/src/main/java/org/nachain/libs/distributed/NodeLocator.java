package org.nachain.libs.distributed;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public final class NodeLocator {


    private TreeMap<Long, Node> nodesMap;


    private HashAlgorithm hashAlg;


    private int virtualNodeTotal = 160;

    public NodeLocator(List<Node> nodes, HashAlgorithm alg) {


        int virtualNodeTotal = nodes.size() * 100;


        init(nodes, alg, virtualNodeTotal);
    }


    public NodeLocator(List<Node> nodes, HashAlgorithm alg, int virtualNodeTotal) {

        init(nodes, alg, virtualNodeTotal);
    }


    private void init(List<Node> nodes, HashAlgorithm alg, int virtualNodeTotal) {
        hashAlg = alg;
        nodesMap = new TreeMap<Long, Node>();


        if (virtualNodeTotal > 0) {
            this.virtualNodeTotal = virtualNodeTotal;

            if (this.virtualNodeTotal < 4) {
                this.virtualNodeTotal = 4;
            }
        }


        for (Node node : nodes) {
            for (int i = 0; i < this.virtualNodeTotal / 4; i++) {
                byte[] digest = hashAlg.computeMd5(node.getName() + i);
                for (int h = 0; h < 4; h++) {
                    long m = hashAlg.hash(digest, h);

                    nodesMap.put(m, node);
                }
            }
        }
    }


    public Node getNodeForKey(final String k) {
        byte[] digest = hashAlg.computeMd5(k);
        Node rv = getNodeForHash(hashAlg.hash(digest, 0));
        return rv;
    }


    public Node getNodeForHash(long hash) {
        final Node rv;
        Long key = hash;
        if (!nodesMap.containsKey(key)) {
            SortedMap<Long, Node> tailMap = nodesMap.tailMap(key);
            if (tailMap.isEmpty()) {
                key = nodesMap.firstKey();

            } else {
                key = tailMap.firstKey();
            }


        }

        rv = nodesMap.get(key);
        return rv;
    }
}