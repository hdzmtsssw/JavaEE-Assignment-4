package io.github.medioqrity;

import java.util.*;

import io.github.medioqrity.TrieNode;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    /**
     * insert a string into the trie tree
     */
    public void insert(String name, int index, int start_index) {
        TrieNode currentNode = root;
        for (int i = start_index; i < name.length(); ++i) {
            if (!currentNode.hasNext(name.charAt(i))) // if there is no such node
                currentNode.setNext(name.charAt(i), new TrieNode());
            currentNode = currentNode.getNext(name.charAt(i));
        }
        currentNode.setFlag(true);
        currentNode.addIndex(index);
    }

    /**
     * returns a list of integer that contains the index that satisfies
     * the query
     */
    public void query(String name, Set<Integer> result) {
        TrieNode currentNode = root;
        for (int i = 0; i < name.length(); ++i) {
            if (!currentNode.hasNext(name.charAt(i)))
                return; // nothing found
            currentNode = currentNode.getNext(name.charAt(i));
        }
        dfs(currentNode, result);
    }

    /**
     * recursively get all possible results
     */
    private void dfs(TrieNode currentNode, Set<Integer> result) {
        if (currentNode.getFlag()) {
            for (int i : currentNode.getIndexes()) {
                result.add(i);
            }
        }
        for (TrieNode nextNode : currentNode.getMap().values()) {
            dfs(nextNode, result);
        }
    }

    private void print(Character c, int layer) {
        for (int i = 0; i < layer; ++i) System.out.print("  ");
        System.out.println(c);
    }

    private void debug(TrieNode currentNode, int layer) {
        for (Character c : currentNode.getMap().keySet()) {
            print(c, layer);
            debug(currentNode.getNext(c), layer + 1);
        }
    }

    public void debug() {
        debug(root, 0);
    }

    public TrieNode getRoot() {
        return root;
    }
}