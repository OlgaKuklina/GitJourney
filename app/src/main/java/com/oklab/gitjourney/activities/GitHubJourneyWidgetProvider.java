package com.oklab.gitjourney.activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.adapters.StackWidgetService;
import com.oklab.gitjourney.asynctasks.FeedsAsyncTask;
import com.oklab.gitjourney.data.GitHubJourneyWidgetDataEntry;
import com.oklab.gitjourney.parsers.WidgetDataAtomParser;
import com.oklab.gitjourney.utils.Utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link GitHubJourneyWidgetConfigureActivity GitHubJourneyWidgetConfigureActivity}
 */
public class GitHubJourneyWidgetProvider extends AppWidgetProvider implements FeedsAsyncTask.OnFeedLoadedListener<GitHubJourneyWidgetDataEntry> {
    private static final String TAG = GitHubJourneyWidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Parcelable[] parcelables) {
        Log.v(TAG, "updateAppWidget parcelables = " + parcelables.length);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.git_hub_journey_widget);

        Intent startActivityIntent = new Intent(context, GeneralActivity.class);
        PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_data_view, startActivityPendingIntent);

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = Utils.createDateFormatterWithTimeZone(context, context.getString(R.string.add_widget_date_format));
        views.setTextViewText(R.id.widget_date, dateFormat.format(calendar.getTime()));
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        Bundle bundle = new Bundle();
        bundle.putParcelableArray("parcelables", parcelables);
        intent.putExtra("bundle", bundle);
        views.setRemoteAdapter(R.id.widget_data_view, intent);
        views.setEmptyView(R.id.widget_data_view, R.id.empty_view);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(TAG, "onUpdate");
        SharedPreferences prefs = context.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
        String sessionDataStr = prefs.getString("userSessionData", null);
        if(sessionDataStr == null || sessionDataStr.isEmpty()) {
            Toast.makeText(context, "Please login to GitJourney", Toast.LENGTH_SHORT).show();
        }
        else {
            new FeedsAsyncTask<>(context, this, new WidgetDataAtomParser(), new State(appWidgetIds, context)).execute(1);
        }
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
    public void onFeedLoaded(List<GitHubJourneyWidgetDataEntry> widgetDataEntries, Object state) {
        Log.v(TAG, "onFeedLoaded " + widgetDataEntries.size());
        if (widgetDataEntries == null || widgetDataEntries.isEmpty()) {
            return;
        }
        Parcelable[] arrayOfWidgetDataEntries = new Parcelable[widgetDataEntries.size()];
        int i = 0;
        for (Parcelable a : widgetDataEntries) {
            arrayOfWidgetDataEntries[i++] = a;
        }
        State st = (State) state;
        for (int appWidgetId : st.appWidgetIds) {
            updateAppWidget(st.context, AppWidgetManager.getInstance(st.context), appWidgetId, arrayOfWidgetDataEntries);
        }
        Log.v(TAG, "onFeedLoaded end");
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

