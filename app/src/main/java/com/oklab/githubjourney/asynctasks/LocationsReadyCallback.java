package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oklab.githubjourney.data.GitHubUserLocationDataEntry;
import com.oklab.githubjourney.data.GitHubUsersDataEntry;
import com.oklab.githubjourney.data.LocationConstants;
import com.oklab.githubjourney.services.FetchAddressIntentService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by olgakuklina on 2017-03-25.
 */

public class LocationsReadyCallback implements OnMapReadyCallback, FollowersAsyncTask.OnFollowersLoadedListener,FollowingAsyncTask.OnFollowingLoadedListener,  UserProfileAsyncTask.OnProfilesLoadedListener {
    private static final String TAG = LocationsReadyCallback.class.getSimpleName();
    private final Context context;
    private int currentPage = 1;
    private int count = 0;
    private int followersCount = 0;
    private List<GitHubUsersDataEntry> followersLocationsList;
    private List<GitHubUsersDataEntry> followingsLocationsList;
    private ArrayList<GitHubUserLocationDataEntry> locationsDataList;
    private AddressResultReceiver mResultReceiver;
    private GoogleMap map;

    public LocationsReadyCallback(Context context) {
        this.context = context;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        new FollowersAsyncTask(context, this).execute(1);
        mResultReceiver = new AddressResultReceiver(new Handler());
        map = googleMap;
    }

    protected void startIntentService() {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        Log.v(TAG, "locationsDataList = " + locationsDataList.size());
        intent.putParcelableArrayListExtra(LocationConstants.LOCATION_DATA_EXTRA, locationsDataList);
        intent.putExtra(LocationConstants.RECEIVER, mResultReceiver);
        context.startService(intent);
    }
    @Override
    public void OnFollowersLoaded(List<GitHubUsersDataEntry> followersDataEntry) {
        followersLocationsList = followersDataEntry !=null ? followersDataEntry: Collections.emptyList();
        Log.v(TAG, "followersLocationsList = " + followersLocationsList.size());
        new FollowingAsyncTask(context, this).execute(1);
    }
    @Override
    public void onFollowingLoaded(List<GitHubUsersDataEntry> followingDataEntry) {
        followingsLocationsList = followingDataEntry !=null ? followingDataEntry: Collections.emptyList();

        Log.v(TAG, "followingsLocationsList = " + followingsLocationsList.size());
        HashSet<String> set = new HashSet<>();
        ArrayList<GitHubUsersDataEntry> list = new ArrayList<>(followingsLocationsList.size() + followersLocationsList.size());

        for(GitHubUsersDataEntry entry: followersLocationsList) {
            set.add(entry.getName());
            list.add(entry);
        }
        for(GitHubUsersDataEntry entry: followingsLocationsList) {
            if(!set.contains(entry.getName())) {
                set.add(entry.getName());
                list.add(entry);
            }
        }
        count = list.size();
        Log.v(TAG, "list = " + list.size());
        locationsDataList = new ArrayList<>(count);
        for(GitHubUsersDataEntry entry: list) {
            new UserProfileAsyncTask(context, this).execute(entry.getName());
        }
    }
    @Override
    public void OnProfilesLoaded(GitHubUserLocationDataEntry locationDataEntry) {
        count--;
        if (locationDataEntry != null && locationDataEntry.getLocation()!=null && !locationDataEntry.getLocation().isEmpty()) {
            locationsDataList.add(locationDataEntry);
        }
        if(count == 0) {
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
            ArrayList<GitHubUserLocationDataEntry> locationsList = resultData.getParcelableArrayList(LocationConstants.LOCATION_DATA_EXTRA);
            Log.v(TAG, "ArrayList.size() = " + locationsList.size());
            for(GitHubUserLocationDataEntry entry: locationsList) {
                if(entry.getLatitude() != 0.0 || entry.getLongitude() != 0.0) {
                    Log.v(TAG, "getLatitude = " + entry.getLatitude());
                    LatLng position = new LatLng (entry.getLatitude(), entry.getLongitude());
                    if(entry.getImageUri() == null) {
                        MarkerOptions options = new MarkerOptions().position(position).title(entry.getName());
                        map.addMarker(options);
                    }
                    else {
                        Picasso.with(context).load(entry.getImageUri()).resize(100,100).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                BitmapDescriptor desc = BitmapDescriptorFactory.fromBitmap(bitmap);
                                MarkerOptions options = new MarkerOptions().position(position).title(entry.getName()).icon(desc);
                                map.addMarker(options);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Log.v(TAG, "error onBitmapFailed");
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }

                }
            }
        }
    }
}
