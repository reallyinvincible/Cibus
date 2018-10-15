package com.example.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.google.gson.Gson;

import java.util.List;

public class IngredientGridWidgetService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientGridRemoteViewsFactory(this.getApplicationContext());
    }
}

class IngredientGridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> ingredientList;

    IngredientGridRemoteViewsFactory(Context context) {
        this.mContext = context;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("DataStorage", Context.MODE_PRIVATE);
        String recipeString = sharedPreferences.getString("SelectedRecipe", null);
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(recipeString, Recipe.class);
        ingredientList = recipe.getIngredients();
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
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = ingredientList.get(position);
        String str = ingredient.getIngredient();
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.grid_item_ingredients);
        views.setTextViewText(R.id.tv_ingredient, builder.toString());
        views.setTextViewText(R.id.tv_quantity, ingredient.getQuantity().toString());
        views.setTextViewText(R.id.tv_apparatus, ingredient.getMeasure());
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
