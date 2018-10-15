package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.activities.DetailActivity;
import com.example.bakingapp.activities.HomeActivity;
import com.example.bakingapp.models.Recipe;
import com.google.gson.Gson;

public class CibusAppWidget extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("DataStorage", Context.MODE_PRIVATE);
        String recipeString = sharedPreferences.getString("SelectedRecipe", null);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_cibus_app);

        if (recipeString == null) {

            views.setTextViewText(R.id.recipe_label, "Recipes");

            Intent intent = new Intent(context, RecipeGridWidgetService.class);
            views.setRemoteAdapter(R.id.gridview, intent);
            Intent homeIntent = new Intent(context, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, homeIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

            Intent recipeIntent = new Intent(context, DetailActivity.class);
            PendingIntent recipePendingIntent = PendingIntent.getActivity(context, 1, recipeIntent, 0);
            views.setPendingIntentTemplate(R.id.gridview, recipePendingIntent);

        } else {

            Gson gson = new Gson();
            Recipe recipe = gson.fromJson(recipeString, Recipe.class);

            views.setTextViewText(R.id.recipe_label, recipe.getName());
            Intent intent = new Intent(context, IngredientGridWidgetService.class);
            views.setRemoteAdapter(R.id.gridview, intent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

