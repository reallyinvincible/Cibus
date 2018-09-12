package com.example.bakingapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.interfaces.StepNavigationInterface;
import com.example.bakingapp.interfaces.StepSelectInterface;
import com.example.bakingapp.fragments.RecipeDetailFragment;
import com.example.bakingapp.fragments.StepDetailFragment;
import com.example.bakingapp.models.Recipe;
import com.google.gson.Gson;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    Recipe recipe;
    static StepSelectInterface mStepSelectInterface;
    StepNavigationInterface mStepNavigationInterface;
    int mPosition = 0;
    boolean recipeLoaded = true;
    static boolean twoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (savedInstanceState != null){
            mPosition = savedInstanceState.getInt("Position");
            recipe = savedInstanceState.getParcelable("Recipe");
            recipeLoaded = savedInstanceState.getBoolean("RecipeLoaded");
        }

        mStepSelectInterface = new StepSelectInterface() {
            @Override
            public void onStepSelect(int position) {
                mPosition = position;
                loadStepsWithBackStack();
            }
        };

        mStepNavigationInterface = new StepNavigationInterface() {
            @Override
            public void onNextStepNavigated() {
                if (mPosition < recipe.getSteps().size() - 1) {
                    setmPosition(mPosition + 1);
                }
            }

            @Override
            public void onPreviousStepNavigated() {
                if (mPosition > 0) {
                    setmPosition(mPosition - 1);
                }
            }
        };

        if (findViewById(R.id.two_pane_container) != null){
            twoPane = true;
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_back_button);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAfterTransition();
                }
            });
        }

        if (!recipeLoaded) {
            loadStepsWithoutBackStack();
        } else if (getIntent() != null) {
            Intent intent = getIntent();
            String recipeString = intent.getStringExtra("SelectedRecipe");
            Gson gson = new Gson();
            recipe = gson.fromJson(recipeString, Recipe.class);
            recipeLoaded = true;
            if (findViewById(R.id.toolbar) != null){
                ((TextView)findViewById(R.id.tv_recipe_name)).setText(recipe.getName());
                Toolbar toolbar = findViewById(R.id.toolbar);
                toolbar.setNavigationIcon(R.drawable.ic_back_button);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishAfterTransition();
                    }
                });
            }
            loadRecipes();
        }
    }

    public void loadRecipes() {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setRecipe(recipe);
        recipeDetailFragment.setmStepSelectInterface(mStepSelectInterface);
        recipeLoaded = true;
        if (!twoPane) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, recipeDetailFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_recipe_detail_pane, recipeDetailFragment).commit();
        }
    }

    public void loadStepsWithBackStack() {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setRecipe(recipe);
        stepDetailFragment.setPosition(mPosition);
        stepDetailFragment.setmStepNavigationInterface(mStepNavigationInterface);
        recipeLoaded = false;
        if (!twoPane) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, stepDetailFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_step_detail_pane, stepDetailFragment).commit();
        }
    }

    public void loadStepsWithoutBackStack() {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setRecipe(recipe);
        stepDetailFragment.setPosition(mPosition);
        stepDetailFragment.setmStepNavigationInterface(mStepNavigationInterface);
        recipeLoaded = false;
        if (!twoPane) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, stepDetailFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_step_detail_pane, stepDetailFragment).commit();
        }
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
        loadStepsWithoutBackStack();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("Position", mPosition);
        outState.putParcelable("Recipe", recipe);
        outState.putBoolean("RecipeLoaded", recipeLoaded);
        super.onSaveInstanceState(outState);
    }

    public static boolean isTwoPane() {
        return twoPane;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        loadRecipes();
    }

    public static StepSelectInterface getmStepSelectInterface() {
        return mStepSelectInterface;
    }
}

