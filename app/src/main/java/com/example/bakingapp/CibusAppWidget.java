package com.example.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.bakingapp.activities.DetailActivity;
import com.example.bakingapp.activities.HomeActivity;

/**
 * Implementation of App Widget functionality.
 */
public class CibusAppWidget extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cibus_app_widget);

        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.gv_recipes, intent);
        Intent homeIntent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, homeIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

        Intent recipeIntent = new Intent(context, DetailActivity.class);
        PendingIntent recipePendingIntent = PendingIntent.getActivity(context, 1, recipeIntent, 0);
        views.setPendingIntentTemplate(R.id.gv_recipes, recipePendingIntent);

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

