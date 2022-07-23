package com.lifestyle.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lifestyle.R;
import com.lifestyle.adapters.SearchAdapter;
import com.lifestyle.utils.Trie;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String TAG = "SearchFragment";
    private String mParam1;
    private String mParam2;
    public static RecyclerView rvSearch;
    private SearchAdapter adapter;
    public static List<ParseUser> allUsers;
    EditText searchBar;
    ImageView searchButton;
    Trie trie;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queryUsers();
        rvSearch = view.findViewById(R.id.rvSearch);
        searchBar = view.findViewById(R.id.etSearchBar);
        searchButton = view.findViewById(R.id.ivSearchButton);
        allUsers = new ArrayList<>();
        adapter = new SearchAdapter(getContext(), allUsers);
        rvSearch.setAdapter(adapter);
        rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        trie = new Trie();


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allUsers.clear();
                String text = searchBar.getText().toString();
                ParseUser user = trie.search(text);
                if(user != null){
                    allUsers.add(user);
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), "No User found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void queryUsers() {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting exercises", e);
                    return;
                }
                for (int i = 0; i < users.size(); i++) {
                    trie.insert(users.get(i));
                }
            }
        });
    }
}