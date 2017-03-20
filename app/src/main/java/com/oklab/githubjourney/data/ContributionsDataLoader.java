package com.oklab.githubjourney.data;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by olgakuklina on 2017-03-19.
 */

public class ContributionsDataLoader extends CursorLoader {
    private  ContributionsDataLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, ActivityItemsContract.Items.DEFAULT_SORT);
    }

    public static ContributionsDataLoader newAllItemsLoader(Context context) {
        return new ContributionsDataLoader(context, ActivityItemsContract.Items.buildDirUri());

    }

    public static final class Query {
        private static final String[] PROJECTION = {
                ActivityItemsContract.ItemsColumns._ID,
                ActivityItemsContract.ItemsColumns.TITLE,
                ActivityItemsContract.ItemsColumns.ENTRY_URL,
                ActivityItemsContract.ItemsColumns.AUTHOR_ID,
                ActivityItemsContract.ItemsColumns.ACTION_TYPE,
                ActivityItemsContract.ItemsColumns.DESCRIPTION,
                ActivityItemsContract.ItemsColumns.PUBLISHED_DATE,
        };
        public static final int _ID = 0;
        public static final int TITLE = 1;
        public static final int ENTRY_URL = 2;
        public static final int AUTHOR_ID = 3;
        public static final int ACTION_TYPE = 4;
        public static final int DESCRIPTION = 5;
        public static final int PUBLISHED_DATE = 6;

    }
}
