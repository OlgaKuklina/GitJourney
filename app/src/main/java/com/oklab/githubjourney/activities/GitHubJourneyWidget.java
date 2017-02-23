package com.oklab.githubjourney.activities;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.oklab.githubjourney.adapters.StackWidgetService;
import com.oklab.githubjourney.asynctasks.FeedsAsyncTask;
import com.oklab.githubjourney.data.GitHubJourneyWidgetDataEntry;
import com.oklab.githubjourney.githubjourney.R;
import com.oklab.githubjourney.services.WidgetDataAtomParser;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link GitHubJourneyWidgetConfigureActivity GitHubJourneyWidgetConfigureActivity}
 */
public class GitHubJourneyWidget extends AppWidgetProvider implements FeedsAsyncTask.OnFeedLoadedListener<GitHubJourneyWidgetDataEntry>{

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = GitHubJourneyWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.git_hub_journey_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.widget_view, intent);
        views.setEmptyView(R.id.widget_view, R.id.empty_view);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        new FeedsAsyncTask<>(context, this, new WidgetDataAtomParser(), new State(appWidgetIds, context)).execute(1);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            GitHubJourneyWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
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

    @Override
    public void onFeedLoaded(List feedDataEntry, Object state) {
        if (feedDataEntry == null || feedDataEntry.isEmpty()) {
            return;
        }
        State st = (State)state;
        for (int appWidgetId : st.appWidgetIds) {
            updateAppWidget(st.context, AppWidgetManager.getInstance(st.context), appWidgetId);
        }
    }

    private static class State {
        public final int[] appWidgetIds;
        public final Context context;

        private State(int[] appWidgetIds, Context context) {
            this.appWidgetIds = appWidgetIds;
            this.context = context;
        }
    }
}

