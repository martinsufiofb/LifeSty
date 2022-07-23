package com.lifestyle.utils;

import android.util.Log;

import com.parse.ParseUser;

public class Trie {
    static final int ALPHABET_SIZE = 26;
    Trie[] children = new Trie[ALPHABET_SIZE];
    boolean isEndOfWord;
    char letter;
    ParseUser user;

    public Trie() {
        isEndOfWord = false;
        for (int i = 0; i < ALPHABET_SIZE; i++)
            children[i] = null;
    }

    public static Trie root = new Trie();

    public static void insert(ParseUser user) {
        String key = user.getUsername();
        Log.i("TAG", key);
        int level;
        int length = key.length();
        int index;

        Trie pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';
            if (pCrawl.children[index] == null)
                pCrawl.children[index] = new Trie();

            pCrawl = pCrawl.children[index];
            pCrawl.letter = key.charAt(level);
        }
        pCrawl.isEndOfWord = true;
        pCrawl.user = user;
    }

    public static ParseUser search(String key) {
        int level;
        int length = key.length();
        int index;
        Trie pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';
            pCrawl = pCrawl.children[index];
        }
        return pCrawl.user;
    }

}
