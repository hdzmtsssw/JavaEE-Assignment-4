package io.github.medioqrity;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

public class TrieNode {
    private Map<Character, TrieNode> next;
    private boolean flag = false;
    private List<Integer> indexes = null; // there can be more than one indexes

    public TrieNode() {
        next = new HashMap<>();
    }

    public void setNext(Character key, TrieNode nextNode) {
        next.put(key, nextNode);
    }

    public TrieNode getNext(Character key) {
        return next.get(key);
    }

    public boolean hasNext(Character key) {
        return next.containsKey(key);
    }

    public void setFlag(boolean value) {
        flag = value;
    }

    public void addIndex(int i) {
        if (indexes == null) indexes = new LinkedList<>();
        indexes.add(i);
    }

    public boolean getFlag() {
        return flag;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public Map<Character, TrieNode> getMap() {
        return next;
    }
}