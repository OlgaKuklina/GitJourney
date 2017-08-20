package com.oklab.gitjourney.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oklab.gitjourney.asynctasks.FollowersLoader;
import com.oklab.gitjourney.asynctasks.FollowingLoader;
import com.oklab.gitjourney.asynctasks.UserProfileAsyncTask;
import com.oklab.gitjourney.data.GitHubUserLocationDataEntry;
import com.oklab.gitjourney.data.GitHubUsersDataEntry;
import com.oklab.gitjourney.data.LocationConstants;
import com.oklab.gitjourney.parsers.LocationDataParser;
import com.oklab.gitjourney.parsers.Parser;
import com.oklab.gitjourney.services.FetchAddressIntentService;
import com.oklab.gitjourney.utils.TransformToCircle;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by olgakuklina on 2017-03-25.
 */

public class LocationsReadyCallback implements OnMapReadyCallback, UserProfileAsyncTask.OnProfilesLoadedListener<GitHubUserLocationDataEntry> {
    private static final String TAG = LocationsReadyCallback.class.getSimpleName();
    private final AppCompatActivity activity;
    private int count = 0;
    private List<GitHubUsersDataEntry> followersLocationsList;
    private List<GitHubUsersDataEntry> followingsLocationsList;
    private ArrayList<GitHubUserLocationDataEntry> locationsDataList;
    private LocationsReadyCallback.AddressResultReceiver mResultReceiver;
    private GoogleMap map;
    private ArrayList<Target> targets;

    public LocationsReadyCallback(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v(TAG, "onMapReady");
        Bundle bundle = new Bundle();
        bundle.putInt("page", 1);
        activity.getSupportLoaderManager().initLoader(0, bundle, new LocationsReadyCallback.FollowersListLoaderCallbacks());
        mResultReceiver = new LocationsReadyCallback.AddressResultReceiver(new Handler());
        map = googleMap;
    }

    protected void startIntentService() {
        Intent intent = new Intent(activity, FetchAddressIntentService.class);
        Log.v(TAG, "locationsDataList = " + locationsDataList.size());
        intent.putParcelableArrayListExtra(LocationConstants.LOCATION_DATA_EXTRA, locationsDataList);
        intent.putExtra(LocationConstants.RECEIVER, mResultReceiver);
        activity.startService(intent);
    }

    @Override
    public void OnProfilesLoaded(GitHubUserLocationDataEntry locationDataEntry) {
        Log.v(TAG, "OnProfilesLoaded " + count + " , " + locationDataEntry);
        count--;
        if (locationDataEntry != null && locationDataEntry.getLocation() != null && !locationDataEntry.getLocation().isEmpty()) {
            locationsDataList.add(locationDataEntry);
        }
        if (count == 0) {
            startIntentService();
        }
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.v(TAG, "onReceiveResult");
            targets = new ArrayList<>(resultData.size());
            ArrayList<GitHubUserLocationDataEntry> locationsList = resultData.getParcelableArrayList(LocationConstants.LOCATION_DATA_EXTRA);
            Log.v(TAG, "ArrayList.size() = " + locationsList.size());
            for (GitHubUserLocationDataEntry entry : locationsList) {
                if (entry.getLatitude() != 0.0 || entry.getLongitude() != 0.0) {
                    Log.v(TAG, "getLatitude = " + entry.getLatitude());
                    LatLng position = new LatLng(entry.getLatitude(), entry.getLongitude());
                    if (entry.getImageUri() == null) {
                        MarkerOptions options = new MarkerOptions().position(position).title(entry.getLogin());
                        map.addMarker(options);
                    } else {
                        Target target = new Target() {

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Log.v(TAG, "onBitmapLoaded");
                                BitmapDescriptor desc = BitmapDescriptorFactory.fromBitmap(bitmap);
                                MarkerOptions options = new MarkerOptions().position(position).title(entry.getLogin()).icon(desc);
                                map.addMarker(options);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Log.v(TAG, "error onBitmapFailed");
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }

                        };
                        targets.add(target);
                        Picasso.with(activity).load(entry.getImageUri()).resize(100, 100).transform(new TransformToCircle()).into(target);
                    }
                }
            }
        }
    }

    private class FollowersListLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<GitHubUsersDataEntry>> {

        @Override
        public Loader<List<GitHubUsersDataEntry>> onCreateLoader(int id, Bundle args) {
            Log.v(TAG, "onCreateLoader " + args);

            if (id == 0) {
                return new FollowingLoader(activity, args.getInt("page"));
            } else if (id == 1) {
                return new FollowersLoader(activity, args.getInt("page"));
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<List<GitHubUsersDataEntry>> loader, List<GitHubUsersDataEntry> DataEntryList) {
            if (DataEntryList != null && DataEntryList.isEmpty()) {
                activity.getSupportLoaderManager().destroyLoader(loader.getId());
                return;
            }
            if (loader.getId() == 0) {
                followersLocationsList = DataEntryList != null ? DataEntryList : Collections.emptyList();
                Log.v(TAG, "followersLocationsList = " + followersLocationsList.size());
                Bundle bundle = new Bundle();
                bundle.putInt("page", 1);
                activity.getSupportLoaderManager().initLoader(1, bundle, new LocationsReadyCallback.FollowersListLoaderCallbacks());
            } else {
                followingsLocationsList = DataEntryList != null ? DataEntryList : Collections.emptyList();
                HashSet<String> set = new HashSet<>();
                ArrayList<GitHubUsersDataEntry> list = new ArrayList<>(followingsLocationsList.size() + followersLocationsList.size());
                for (GitHubUsersDataEntry entry : followersLocationsList) {
                    set.add(entry.getLogin());
                    list.add(entry);
                }
                for (GitHubUsersDataEntry entry : followingsLocationsList) {
                    if (!set.contains(entry.getLogin())) {
                        set.add(entry.getLogin());
                        list.add(entry);
                    }
                }

                count = list.size();
                Log.v(TAG, "list = " + list.size());
                locationsDataList = new ArrayList<>(count);
                Parser<GitHubUserLocationDataEntry> parser = new LocationDataParser();
                for (GitHubUsersDataEntry entry : list) {
                    new UserProfileAsyncTask<GitHubUserLocationDataEntry>(activity, LocationsReadyCallback.this, parser).execute(entry.getLogin());
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<List<GitHubUsersDataEntry>> loader) {
            Log.v(TAG, "onLoaderReset");
        }
    }
}

