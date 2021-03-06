package com.example.bakingapp.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bakingapp.R;
import com.example.bakingapp.adapters.RecipeAdapter;
import com.example.bakingapp.models.FetchedRecipeList;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.widget.CibusAppWidget;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    JSONArray recipes;
    List<Recipe> recipeList;

    @BindView(R.id.rv_recipe_list)
    RecyclerView recipeRecyclerView;
    @BindView(R.id.loading_animation)
    LottieAnimationView animationView;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        recipeList = new ArrayList<Recipe>();
        sharedPreferences = getSharedPreferences("DataStorage", MODE_PRIVATE);
        requestData();
    }

    private void requestData(){
        animationView.playAnimation();
        animationView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        animationView.pauseAnimation();
                        animationView.setVisibility(View.GONE);
                        try {
                            recipes = new JSONArray(response);
                            convertJsonToRecipes();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                animationView.pauseAnimation();
                animationView.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void convertJsonToRecipes(){

        for (int i = 0; i < recipes.length(); i++){
            try {
                JSONObject object = recipes.getJSONObject(i);
                Gson gson = new Gson();
                Recipe recipe = gson.fromJson(object.toString(), Recipe.class);
                recipeList.add(recipe);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        populateUi();
    }

    private void populateUi(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        FetchedRecipeList fetchedRecipeList = new FetchedRecipeList(recipeList);
        String recipesString = gson.toJson(fetchedRecipeList);
        editor.putString("RecipeList", recipesString);
        editor.apply();
        RecipeAdapter adapter = new RecipeAdapter(recipeList);
        recipeRecyclerView.setAdapter(adapter);
        updateWidget();
    }

    void updateWidget(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SelectedRecipe", null);
        editor.apply();
        Intent intent = new Intent(this, CibusAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), CibusAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWidget();
    }
}
