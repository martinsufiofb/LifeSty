package com.lifestyle.utils;

import com.parse.ParseUser;

public class Trie {
    static final int ALPHABET_SIZE = 26;

    static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];

        boolean isEndOfWord;
        char letter;
        ParseUser user;

        TrieNode() {
            isEndOfWord = false;
            for (int i = 0; i < ALPHABET_SIZE; i++)
                children[i] = null;
        }
    }

    static TrieNode root;

    static void insert(ParseUser user) {
        String key = user.getUsername();
        int level;
        int length = key.length();
        int index;

        TrieNode pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';
            if (pCrawl.children[index] == null)
                pCrawl.children[index] = new TrieNode();

            pCrawl = pCrawl.children[index];
            pCrawl.letter = key.charAt(level);
        }

        pCrawl.isEndOfWord = true;
        pCrawl.user = user;
    }

    static String search(String key) {
        int level;
        int length = key.length();
        int index;
        String result = "";
        TrieNode pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';

            if (pCrawl.children[index] == null) {
                return result;
            }


            pCrawl = pCrawl.children[index];
            result += pCrawl.letter;
        }
        return result;
    }

}
