package com.lifestyle.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lifestyle.R;

public class PushupsCounterFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int result_code = 10;
    private String mParam1;
    private String mParam2;
    CardView doneButton;
    CardView cancelButton;
    TextView numberOfPushups;
    ImageView addSitupsButton;
    int noOfPushups;

    public PushupsCounterFragment() {
        // Required empty public constructor
    }

    public static PushupsCounterFragment newInstance(String param1, String param2) {
        PushupsCounterFragment fragment = new PushupsCounterFragment();
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
        return inflater.inflate(R.layout.fragment_pushups_counter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doneButton = view.findViewById(R.id.pushupsCounterDoneBtn);
        cancelButton = view.findViewById(R.id.pushupsCounterCancelBtn);
        numberOfPushups = view.findViewById(R.id.tvPushupsNo);
        addSitupsButton = view.findViewById(R.id.pushupsAddButton);
        Intent intent = getActivity().getIntent();
        String pushups = intent.getStringExtra("numberOfPushups");
        numberOfPushups.setText(pushups);
        noOfPushups = Integer.parseInt(pushups);

        addSitupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOfPushups++;
                numberOfPushups.setText(String.valueOf(noOfPushups));
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", noOfPushups);
                getActivity().setResult(result_code, intent);
                getActivity().finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                getActivity().setResult(result_code, intent);
                getActivity().finish();
            }
        });
    }
}