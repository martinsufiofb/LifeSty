package com.example.lifestyle.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lifestyle.R;

public class SitupsCounterFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    int result_code = 11;
    CardView doneButton;
    CardView cancelButton;
    TextView numberOfSitups;
    Button situpsCounter;
    int noOfSitups = 0;

    public SitupsCounterFragment() {
        // Required empty public constructor
    }

    public static SitupsCounterFragment newInstance(String param1, String param2) {
        SitupsCounterFragment fragment = new SitupsCounterFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_situps_counter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doneButton = view.findViewById(R.id.situpsCounterDoneBtn);
        cancelButton = view.findViewById(R.id.situpsCounterCancelBtn);
        numberOfSitups = view.findViewById(R.id.tvNoOfSitups);
        situpsCounter = view.findViewById(R.id.btnSitupsCounter);
        Intent intent = getActivity().getIntent();
        String situps  = intent.getStringExtra("numberOfPushups");
        numberOfSitups.setText(situps);
        noOfSitups = Integer.parseInt(situps);

        situpsCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOfSitups++;
                numberOfSitups.setText(Integer.toString(noOfSitups));
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("situpsResult", noOfSitups);
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