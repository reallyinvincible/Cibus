package com.example.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.models.FetchedRecipeList;
import com.example.bakingapp.models.Recipe;
import com.google.gson.Gson;

import java.util.List;


public class RecipeGridWidgetService extends RemoteViewsService{


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeGridRemoteViewsFactory(this.getApplicationContext());
    }
}

class RecipeGridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private final List<Recipe> recipeList;

    RecipeGridRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("DataStorage", Context.MODE_PRIVATE);
        String recipesString = sharedPreferences.getString("RecipeList", null);
        Gson gson = new Gson();
        FetchedRecipeList fetchedRecipeList = gson.fromJson(recipesString, FetchedRecipeList.class);
        recipeList = fetchedRecipeList.getRecipes();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Recipe recipe = recipeList.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.grid_item_recipe);
        views.setTextViewText(R.id.tv_dish_name, recipe.getName());
        views.setTextViewText(R.id.tv_servings, recipe.getServings().toString());

        Intent fillInIntent = new Intent();
        Gson gson = new Gson();
        String recipeString = gson.toJson(recipe);
        fillInIntent.putExtra("SelectedRecipe", recipeString);
        views.setOnClickFillInIntent(R.id.grid_container, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
