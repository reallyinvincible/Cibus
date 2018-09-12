package com.example.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.activities.DetailActivity;
import com.example.bakingapp.interfaces.StepSelectInterface;
import com.example.bakingapp.adapters.IngredientAdapter;
import com.example.bakingapp.adapters.StepAdapter;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.rv_ingredients_list)
    RecyclerView ingredientsRecyclerView;
    @BindView(R.id.rv_step_details)
    RecyclerView stepRecyclerView;
    @BindView(R.id.tv_recipe_name)
    TextView recipeName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Recipe recipe;
    static StepSelectInterface mStepSelectInterface;

    public RecipeDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
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

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable("Recipe");
        }

        recipeName.setText(recipe.getName());
        IngredientAdapter ingredientAdapter = new IngredientAdapter(recipe.getIngredients());
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
        StepAdapter stepAdapter = new StepAdapter(recipe.getSteps(), DetailActivity.getmStepSelectInterface());
        stepRecyclerView.setAdapter(stepAdapter);
        return view;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setmStepSelectInterface(StepSelectInterface mStepSelectInterface) {
        RecipeDetailFragment.mStepSelectInterface = mStepSelectInterface;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("Recipe", recipe);
        super.onSaveInstanceState(outState);
    }
}
