package com.oklab.gitjourney.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.data.GitHubUserLocationDataEntry;
import com.oklab.gitjourney.data.LocationConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by olgakuklina on 2017-03-24.
 */

public class FetchAddressIntentService extends IntentService {
    private static final String TAG = FetchAddressIntentService.class.getSimpleName();

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.v(TAG, "onHandleIntent");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";
        // Get the location passed to this service through an extra.
        ArrayList<GitHubUserLocationDataEntry> locationsList = intent.getParcelableArrayListExtra(
                LocationConstants.LOCATION_DATA_EXTRA);
        ResultReceiver mReceiver = intent.getParcelableExtra(
                LocationConstants.RECEIVER);
        try {
            for (int i = 0; i < locationsList.size(); i++) {
                GitHubUserLocationDataEntry entry = locationsList.get(i);
                List<Address> addressList = geocoder.getFromLocationName(entry.getLocation(), 1);
                if (!addressList.isEmpty()) {
                    Address address = addressList.get(0);
                    entry.setLatitude(address.getLatitude());
                    entry.setLongitude(address.getLongitude());
                }
            }
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage, illegalArgumentException);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LocationConstants.LOCATION_DATA_EXTRA, locationsList);
        mReceiver.send(0, bundle);
    }
}
