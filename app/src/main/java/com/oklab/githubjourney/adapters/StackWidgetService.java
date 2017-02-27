package com.oklab.githubjourney.adapters;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.oklab.githubjourney.activities.GeneralActivity;
import com.oklab.githubjourney.data.GitHubJourneyWidgetDataEntry;
import com.oklab.githubjourney.githubjourney.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import static android.R.style.Widget;

public class StackWidgetService extends RemoteViewsService {
    private static final String TAG = StackWidgetService.class.getSimpleName();

    private static final String EXTRA_LIST_VIEW_ROW_NUMBER = "row_number";
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private static class StackRemoteViewsFactory implements RemoteViewsFactory {
        private static final String TAG = StackRemoteViewsFactory.class.getSimpleName();
        private Context context;
        private ArrayList<GitHubJourneyWidgetDataEntry> widgetDatas;
        private int appWidgetId;

        public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
            Log.v(TAG, "StackRemoteViewsFactory");
            intent.setExtrasClassLoader(GitHubJourneyWidgetDataEntry.class.getClassLoader());
            this.context = applicationContext;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 1);
            Bundle bundle = intent.getBundleExtra("bundle");
            Parcelable[] items = bundle.getParcelableArray("parcelables");
            Log.v(TAG, "items size = " + items.length);

            if (items.length != 0) {
                widgetDatas = new ArrayList<>(items.length);
                for (Parcelable item : items) {
                    widgetDatas.add((GitHubJourneyWidgetDataEntry) item);
                }
            } else {
                widgetDatas = new ArrayList<>(0);
            }
            Log.v(TAG, "widgetDatas" + widgetDatas.size());
        }

        @Override
        public void onCreate() {
            //   widgetDatas = getData();
            Log.v(TAG, "onCreate");
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            Log.v(TAG, "getCount " + widgetDatas.size());
            return widgetDatas.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.v(TAG, "getViewAt: position = " + position);

            // Construct a remote views item based on the app widget item XML file,
            // and set the text based on the position.
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(EXTRA_LIST_VIEW_ROW_NUMBER, position);
            rv.setOnClickFillInIntent(R.id.main_list_item, fillInIntent);

            rv.setTextViewText(R.id.w_author_name, widgetDatas.get(position).getAuthorName());
            rv.setTextViewText(R.id.w_title, widgetDatas.get(position).getTitle());
            Picasso pic = Picasso.with(context);
            try {
                Bitmap map = pic.load(widgetDatas.get(position).getAvatar()).get();
                rv.setImageViewBitmap(R.id.w_github_user_image, map);
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }
            return rv;
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
            return false;
        }

    }
}
