package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oklab.githubjourney.data.GitHubUserLocationDataEntry;
import com.oklab.githubjourney.data.GitHubUsersDataEntry;
import com.oklab.githubjourney.data.LocationConstants;
import com.oklab.githubjourney.services.FetchAddressIntentService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-03-25.
 */

public class LocationsReadyCallback implements OnMapReadyCallback, FollowersAsyncTask.OnFollowersLoadedListener, UserProfileAsyncTask.OnProfilesLoadedListener {
    private static final String TAG = LocationsReadyCallback.class.getSimpleName();
    private final Context context;
    private int currentPage = 1;
    private int count = 0;
    private ArrayList<GitHubUserLocationDataEntry> locationsList;
    private AddressResultReceiver mResultReceiver;
    private GoogleMap map;

    public LocationsReadyCallback(Context context) {
        this.context = context;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        new FollowersAsyncTask(context, this).execute(currentPage++);
        mResultReceiver = new AddressResultReceiver(new Handler());
        map = googleMap;
    }

    protected void startIntentService() {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putParcelableArrayListExtra(LocationConstants.LOCATION_DATA_EXTRA, locationsList);
        intent.putExtra(LocationConstants.RECEIVER, mResultReceiver);
        context.startService(intent);
    }
    @Override
    public void OnFollowersLoaded(List<GitHubUsersDataEntry> followersDataEntry) {
        if (followersDataEntry != null && followersDataEntry.isEmpty()) {
            return;
        }
        locationsList = new ArrayList<>(followersDataEntry.size());
        count = followersDataEntry.size();
        for(GitHubUsersDataEntry entry: followersDataEntry) {
            new UserProfileAsyncTask(context, this).execute(entry.getName());
        }
    }

    @Override
    public void OnProfilesLoaded(GitHubUserLocationDataEntry locationDataEntry) {
        count--;
        if (locationDataEntry != null && locationDataEntry.getLocation()!=null && !locationDataEntry.getLocation().isEmpty()) {
            locationsList.add(locationDataEntry);
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
                    MarkerOptions options= new MarkerOptions().position(position).title(entry.getName());
                    map.addMarker(options);
                }
            }
        }
    }
}
