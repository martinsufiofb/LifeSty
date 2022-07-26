package com.lifestyle.utils;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trie {
    static final int ALPHABET_SIZE = 26;
    Trie[] children = new Trie[ALPHABET_SIZE];
    boolean isEndOfWord;
    char letter;
    ParseUser user;
    Trie previous;
    String word;
    List<Integer> nexts;

    public Trie() {
        isEndOfWord = false;
        nexts = new ArrayList<>();
        for (int i = 0; i < ALPHABET_SIZE; i++)
            children[i] = null;
    }

    public static Trie root = new Trie();

    public static void insert(ParseUser user) {
        String key = user.getUsername();
        int level;
        int length = key.length();
        int index;
        Trie pPrevious;
        Trie pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';
            if (pCrawl.children[index] == null) {
                pCrawl.children[index] = new Trie();
                pCrawl.nexts.add(index);
            }
            Collections.sort(pCrawl.nexts);
            pPrevious = pCrawl;
            pCrawl = pCrawl.children[index];
            pCrawl.previous = pPrevious;
            pCrawl.letter = key.charAt(level);
        }
        pCrawl.isEndOfWord = true;
        pCrawl.user = user;
        pCrawl.word = key;
    }

    private static List<ParseUser> display(Trie root, List<ParseUser> results) {
        if (root.isEndOfWord) {
            results.add(root.user);
        }
        for (int i = 0; i < root.nexts.size(); i++) {
            display(root.children[root.nexts.get(i)], results);
        }
        return results;
    }

    // This function goes to the last node of the what the user is typing.
    // If the user types 'fran' the function goes to node n and calls display function on node n
    // When display function is called on node n, we get back all the children node of n that are end of words
    public static List<ParseUser> suggestedSearch(String key) {
        List<ParseUser> results = new ArrayList<>();
        int level;
        int length = key.length();
        int index;
        Trie pCrawl = root;
        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';
            if (pCrawl.children[index] == null) {
                return results;
            }
            pCrawl = pCrawl.children[index];
        }
        return display(pCrawl, results);
    }
}
