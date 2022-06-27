package com.example.lifestyle.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifestyle.Exercise;
import com.example.lifestyle.ExercisesAdapter;
import com.example.lifestyle.History;
import com.example.lifestyle.HistoryAdapter;
import com.example.lifestyle.LoginActivity;
import com.example.lifestyle.Pushups;
import com.example.lifestyle.R;
import com.example.lifestyle.Situps;
import com.example.lifestyle.Squats;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static String TAG = "ProfileFragment";
    LinearLayout logout;
    String currentUserUsername;
    TextView profileUsername;
    TextView secondCircle;
    TextView firstCircle;
    TextView thirdCircle;
    int pushupsTotal;
    int situpsTotal;
    int squatsTotal;

    public static RecyclerView rvHistory;
    private HistoryAdapter adapter;
    private List<History> historyList;
    private List<Pushups> pushupsList;
    private List<Situps> situpsList;
    private List<Squats> squatsList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUserUsername = ParseUser.getCurrentUser().getUsername();
        profileUsername = view.findViewById(R.id.tvProfileUsername);
        firstCircle = view.findViewById(R.id.tv1);
        secondCircle = view.findViewById(R.id.tv2);
        thirdCircle = view.findViewById(R.id.tv3);

        profileUsername.setText(currentUserUsername);

        logout =  view.findViewById(R.id.LLlogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }

        });

        rvHistory = view.findViewById(R.id.rvHistory);

        historyList = new ArrayList<>();
        pushupsList = new ArrayList<>();
        situpsList = new ArrayList<>();
        squatsList = new ArrayList<>();

        adapter = new HistoryAdapter(getContext(), historyList);

        rvHistory.setAdapter(adapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        queryHistory();
        queryPushups();
        querySitups();
        querySquats();


    }


    private void querySquats() {
        ParseQuery<Squats> query = ParseQuery.getQuery(Squats.class);
        query.include(Squats.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());

        query.findInBackground(new FindCallback<Squats>() {
            @Override
            public void done(List<Squats> squats, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue getting squats", e);
                    return;
                }

                squatsList.addAll(squats);
                for(int i = 0; i<squatsList.size(); i++){
                    Log.i(TAG,"TOTALRUN: "+squatsTotal);
                    squatsTotal+= Integer.parseInt(squatsList.get(i).getCount());
                }
                Log.i(TAG,"TOTAL: "+squatsTotal);
                secondCircle.setText(String.valueOf(squatsTotal));
            }
        });

    }

    private void querySitups() {
        ParseQuery<Situps> query = ParseQuery.getQuery(Situps.class);
        query.include(Situps.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());

        query.findInBackground(new FindCallback<Situps>() {
            @Override
            public void done(List<Situps> situps, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue getting situps", e);
                    return;
                }

                situpsList.addAll(situps);

                for(int i = 0; i<situpsList.size(); i++){
                    situpsTotal+= Integer.parseInt(situpsList.get(i).getCount());
                }
                firstCircle.setText(String.valueOf(situpsTotal));

            }
        });

    }

    private void queryPushups() {
        ParseQuery<Pushups> query = ParseQuery.getQuery(Pushups.class);
        query.include(Pushups.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());

        query.findInBackground(new FindCallback<Pushups>() {
            @Override
            public void done(List<Pushups> pushups, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue getting pushups", e);
                    return;
                }
                pushupsList.addAll(pushups);

                for(int i = 0; i<pushupsList.size(); i++){
                    pushupsTotal+= Integer.parseInt(pushupsList.get(i).getCount());
                }

                thirdCircle.setText(String.valueOf(pushupsTotal));

            }
        });

    }


    private void queryHistory() {
        ParseQuery<History> query = ParseQuery.getQuery(History.class);
        query.include(History.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<History>() {
            @Override
            public void done(List<History> history, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue getting exercises", e);
                    return;
                }

                historyList.addAll(history);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void logout() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Error signing out", e);
                    Toast.makeText(getContext(), "Error signing out", Toast.LENGTH_SHORT).show();
                    return;
                }
                goToLoginActivity();
            }
        });
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}