package com.example.bakingapp.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.activities.DetailActivity;
import com.example.bakingapp.interfaces.StepNavigationInterface;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    @BindView(R.id.tv_recipe_name)
    private
    TextView recipeName;
    @BindView(R.id.exoplayer)
    private
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.tv_step_full_description)
    private
    TextView descriptionText;
    @BindView(R.id.btn_next_step)
    private
    Button nextStepButton;
    @BindView(R.id.btn_previous_step)
    private
    Button previousStepButton;
    @BindView(R.id.toolbar)
    private
    Toolbar toolbar;

    private SimpleExoPlayer mExoPlayer;
    private StepNavigationInterface mStepNavigationInterface;
    private int position = 0;
    private Recipe recipe;
    private int orientation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);

        if (!DetailActivity.isTwoPane()) {
            toolbar.setNavigationIcon(R.drawable.ic_back_button);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        } else {
            toolbar.setVisibility(View.GONE);
        }

        orientation = getResources().getConfiguration().orientation;
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepNavigationInterface.onNextStepNavigated();
            }
        });
        previousStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepNavigationInterface.onPreviousStepNavigated();
            }
        });
        populateUi();
        return view;
    }

    private void populateUi(){
        try {
            recipeName.setText(recipe.getName());
            List<Step> stepList = recipe.getSteps();
            Step step = stepList.get(position);
            String description = step.getDescription();
            if (position > 0)
            description = "Step " + String.valueOf(position+1) + '\n' + description.substring(3);
            descriptionText.setText(description);
            if (!step.getVideoURL().equals("")) {
                initializePlayer(Uri.parse(step.getVideoURL()));
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (getActivity().findViewById(R.id.toolbar) != null){
                        ((TextView)getActivity().findViewById(R.id.tv_recipe_name)).setText(recipe.getName());
                        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
                        toolbar.setNavigationIcon(R.drawable.ic_back_button);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().finishAfterTransition();
                            }
                        });
                    }
                    nextStepButton.setVisibility(View.GONE);
                    previousStepButton.setVisibility(View.GONE);
                    descriptionText.setVisibility(View.GONE);
                }

            } else {
                exoPlayerView.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setPosition(int position) {
        this.position = position;
        populateUi();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        populateUi();
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            exoPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    public void setmStepNavigationInterface(StepNavigationInterface mStepNavigationInterface) {
        this.mStepNavigationInterface = mStepNavigationInterface;
    }
}
