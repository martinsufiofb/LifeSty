package com.lifestyle.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lifestyle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SquatCounterFragment extends Fragment {
    int result_code = 12;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static double thresholdProbability = 0.989;
    private static double bodyThresholdProbability = 0.9899;
    private static double squatThreshold = 230;
    private String mParam1;
    private String mParam2;
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageView flipCameraLens;
    private TextView numberOfSquats;
    private Integer noOfSquats = 0;
    List<Float> noseData = new ArrayList<>();
    private TextView warningText;
    private FrameLayout warningBackground;
    private int lensFacing = CameraSelector.LENS_FACING_FRONT;
    PoseDetector poseDetector;
    CardView squatsDoneButton;
    CardView squatsCancelButton;

    public SquatCounterFragment() {
        // Required empty public constructor
    }

    public static SquatCounterFragment newInstance(String param1, String param2) {
        SquatCounterFragment fragment = new SquatCounterFragment();
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
        return inflater.inflate(R.layout.fragment_squat_counter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flipCameraLens = view.findViewById(R.id.ivSwitchlens);
        previewView = view.findViewById(R.id.previewView);
        numberOfSquats = view.findViewById(R.id.tvNoOfSquats);
        warningBackground = view.findViewById(R.id.warningBackground);
        warningText = view.findViewById(R.id.warningText);
        squatsCancelButton = view.findViewById(R.id.squatsCounterCancelBtn);
        squatsDoneButton = view.findViewById(R.id.squatsCounterDoneBtn);
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        Intent intent = getActivity().getIntent();
        String squats = intent.getStringExtra("numberOfSquats");
        numberOfSquats.setText(squats);
        noOfSquats = Integer.parseInt(squats);

        flipCameraLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                    lensFacing = CameraSelector.LENS_FACING_BACK;
                } else {
                    lensFacing = CameraSelector.LENS_FACING_FRONT;
                }
                cameraProviderFuture.addListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                            bindImageAnalysis(cameraProvider);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, ContextCompat.getMainExecutor(getContext()));
            }
        });

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindImageAnalysis(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(getContext()));

        AccuratePoseDetectorOptions options =
                new AccuratePoseDetectorOptions.Builder()
                        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                        .build();
        poseDetector = PoseDetection.getClient(options);

        squatsDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("squatsResult", noOfSquats);
                getActivity().setResult(result_code, intent);
                getActivity().finish();
            }
        });

        squatsCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                getActivity().setResult(result_code, intent);
                getActivity().finish();
            }
        });
    }

    private void bindImageAnalysis(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(previewView.getWidth(), previewView.getHeight())).setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(getContext()), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                @SuppressLint("UnsafeOptInUsageError") Image mediaImage = imageProxy.getImage();
                if (mediaImage != null) {
                    InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                    Task<Pose> pose = poseDetector.process(image)
                            .addOnSuccessListener(
                                    new OnSuccessListener<Pose>() {
                                        @Override
                                        public void onSuccess(Pose pose) {
                                            List<PoseLandmark> allPoseLandmarks = pose.getAllPoseLandmarks();
                                            PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
                                            PoseLandmark leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER);
                                            PoseLandmark leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE);
                                            PoseLandmark leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER);
                                            PoseLandmark rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER);
                                            PoseLandmark rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE);
                                            PoseLandmark rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER);

                                            if (allPoseLandmarks != null && nose != null) {
                                                float noseInFrameLikelihood = nose.getInFrameLikelihood();
                                                float leftEyeInFrameLikelihood = leftEye.getInFrameLikelihood();
                                                float leftEyeInnerInFrameLikelihood = leftEyeInner.getInFrameLikelihood();
                                                float leftEyeOuterInFrameLikelihood = leftEyeOuter.getInFrameLikelihood();
                                                float rightEyeInnerInFrameLikelihood = rightEyeInner.getInFrameLikelihood();
                                                float rightEyeInFrameLikelihood = rightEye.getInFrameLikelihood();
                                                float rightEyeOuterInFrameLikelihood = rightEyeOuter.getInFrameLikelihood();

                                                if (personInFrameLikelyHood(allPoseLandmarks) > bodyThresholdProbability && noseInFrameLikelihood > thresholdProbability && leftEyeInFrameLikelihood > thresholdProbability
                                                        && leftEyeOuterInFrameLikelihood > thresholdProbability && rightEyeOuterInFrameLikelihood > thresholdProbability && rightEyeInFrameLikelihood > thresholdProbability
                                                        && rightEyeInnerInFrameLikelihood > thresholdProbability && leftEyeInnerInFrameLikelihood > thresholdProbability) {
                                                    warningText.setVisibility(View.INVISIBLE);
                                                    warningBackground.setVisibility(View.INVISIBLE);
                                                    noseData.add(nose.getPosition().y);

                                                    if (noseData.size() > 1 && noseData.get(noseData.size() - 1) - noseData.get(noseData.size() - 2) > squatThreshold) {
                                                        noOfSquats++;
                                                        numberOfSquats.setText(noOfSquats.toString());
                                                        noseData.clear();
                                                    }
                                                } else {
                                                    warningText.setVisibility(View.VISIBLE);
                                                    warningBackground.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("Check", "Not detected");
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Pose>() {
                                @Override
                                public void onComplete(@NonNull Task<Pose> task) {
                                    mediaImage.close();
                                    imageProxy.close();
                                }
                            });
                }
            }
        });
        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }

    private float personInFrameLikelyHood(List<PoseLandmark> allPoseLandmarks) {
        float percent = 0;
        float size = allPoseLandmarks.size();
        for (int i = 0; i < size; i++) {
            percent += allPoseLandmarks.get(i).getInFrameLikelihood();
        }
        percent /= size;
        return percent;
    }
}