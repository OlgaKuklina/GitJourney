package com.oklab.githubjourney.data;

import android.net.Uri;

import java.util.Calendar;

/**
 * Created by olgakuklina on 2017-03-18.
 */
public class ActivityItemsContract {

        public static final String CONTENT_AUTHORITY = "com.oklab.githubjourney";
        public static final Uri BASE_URI = Uri.parse("content://com.oklab.githubjourney");

        interface ItemsColumns {
            /** Type: INTEGER PRIMARY KEY AUTOINCREMENT */
            String _ID = "_id";
            /** Type: TEXT */
            String ENTRY_URL = "entry_url";
            /** Type: TEXT NOT NULL */
            String TITLE = "title";
            /** Type: TEXT NOT NULL */
            String AUTHOR_ID = "author_id";
            /** Type: TEXT NOT NULL */
            String DESCRIPTION = "description";
            /** Type: TEXT NOT NULL */
            String ACTION_TYPE = "actionType";
            /** Type: INTEGER NOT NULL DEFAULT 0 */
            String PUBLISHED_DATE = "published_date";
        }

        public static class Items implements ItemsColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.oklab.githubjourney.items";
            public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.oklab.githubjourney.items";

            public static final String DEFAULT_SORT = PUBLISHED_DATE + " DESC";

            /** Matches: /items/ */
            public static Uri buildDirUri() {
                return BASE_URI.buildUpon().appendPath("items").build();
            }

            /** Matches: /items/[_id]/ */
            public static Uri buildItemUri(long _id) {
                return BASE_URI.buildUpon().appendPath("items").appendPath(Long.toString(_id)).build();
            }

            /** Read item ID item detail URI. */
            public static long getItemId(Uri itemUri) {
                return Long.parseLong(itemUri.getPathSegments().get(1));
            }
        }

        private ActivityItemsContract() {
        }
    }

