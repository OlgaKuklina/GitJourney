package com.oklab.githubjourney.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oklab.githubjourney.data.GitHubUsersDataEntry;
import com.oklab.githubjourney.data.StarsDataEntry;
import com.oklab.githubjourney.githubjourney.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-02-05.
 */

public class FollowingListAdapter extends RecyclerView.Adapter<FollowingListAdapter.FollowingListViewHolder> {
    private static final String TAG = FeedListAdapter.class.getSimpleName();
    private final ArrayList<GitHubUsersDataEntry> followingDataEntrylist = new ArrayList<>(1000);
    private final Context context;
    public FollowingListAdapter(Context context) {
        this.context = context;
    }
    @Override
    public FollowingListAdapter.FollowingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.stars_list_item, parent, false);
        return new FollowingListAdapter.FollowingListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FollowingListAdapter.FollowingListViewHolder holder, int position) {
        GitHubUsersDataEntry entry = followingDataEntrylist.get(position);
        holder.populateFollowingViewData(entry);
    }

    @Override
    public int getItemCount() {
        return followingDataEntrylist.size();
    }

    public void add(List<GitHubUsersDataEntry> entryList) {
        followingDataEntrylist.addAll(entryList);
        notifyDataSetChanged();
    }

    public void resetAllData() {
        followingDataEntrylist.clear();
        notifyDataSetChanged();
    }

    public class FollowingListViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView avatar;
        private TextView profileUri;


        public FollowingListViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.following_user_name);
            avatar = (ImageView) v.findViewById(R.id.following_avatar_image);
            profileUri = (TextView) v.findViewById(R.id.following_user_profile_uri);
        }

        private void populateFollowingViewData(GitHubUsersDataEntry followingDataEntrylist) {
            name.setText(followingDataEntrylist.getName());
            profileUri.setText(followingDataEntrylist.getImageUri());

            Picasso pic = Picasso.with(context);
            Log.v(TAG, "path" + followingDataEntrylist.getImageUri());
            if (followingDataEntrylist.getImageUri()!=null) {
                pic.load(R.drawable.octocat)
                        .fit().centerCrop()
                        .into(avatar);
            } else if (followingDataEntrylist.getImageUri()==null) {
                pic.load(R.drawable.octocat)
                        .fit().centerCrop()
                        .error(R.drawable.octocat)
                        .into(avatar);
            }

        }
    }
}
