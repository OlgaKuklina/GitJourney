package com.oklab.githubjourney.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.oklab.githubjourney.activities.GitHubJourneyWidgetProvider;
import com.oklab.githubjourney.data.GitHubJourneyWidgetDataEntry;
import com.oklab.githubjourney.githubjourney.R;

import java.util.ArrayList;
import java.util.Arrays;

public class StackWidgetService extends RemoteViewsService {
    private static final String TAG = StackWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private static class StackRemoteViewsFactory implements RemoteViewsFactory {
        private static final String TAG = StackRemoteViewsFactory.class.getSimpleName();
        private Context mContext;
        private ArrayList<GitHubJourneyWidgetDataEntry> widgetDatas;

        public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
            Log.v(TAG, "StackRemoteViewsFactory" );
            intent.setExtrasClassLoader(GitHubJourneyWidgetDataEntry.class.getClassLoader());
            this.mContext = applicationContext;
            Bundle bundle = intent.getBundleExtra("bundle");
            Parcelable[] items = bundle.getParcelableArray("parcelables");
            Log.v(TAG, "items size = "+  items.length);
            if(items.length!=0) {
                GitHubJourneyWidgetDataEntry[] array = (GitHubJourneyWidgetDataEntry[])intent.getParcelableArrayExtra("parcelables");
                widgetDatas = new ArrayList<>(Arrays.asList(array));

            }
            else {
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
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
            rv.setTextViewText(R.id.author_name, widgetDatas.get(position).getAuthorName());
            rv.setTextViewText(R.id.action_icon, widgetDatas.get(position).getAvatar());
            rv.setTextViewText(R.id.title, widgetDatas.get(position).getTitle());
            rv.setTextViewText(R.id.action_desc, widgetDatas.get(position).getDescription());
            Log.v(TAG, "getViewAt end" );
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
