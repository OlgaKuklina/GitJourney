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
import com.oklab.githubjourney.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-02-05.
 */

public class FollowersListAdapter extends RecyclerView.Adapter<FollowersListAdapter.FollowersListViewHolder> {

    private static final String TAG = FollowersListAdapter.class.getSimpleName();

    private final ArrayList<GitHubUsersDataEntry> followersDataEntrylist = new ArrayList<>(1000);
    private final Context context;

    public FollowersListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FollowersListAdapter.FollowersListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.followers_list_item, parent, false);
        return new FollowersListAdapter.FollowersListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FollowersListAdapter.FollowersListViewHolder holder, int position) {
        GitHubUsersDataEntry entry = followersDataEntrylist.get(position);
        holder.populateFollowersViewData(entry);
    }


    @Override
    public int getItemCount() {
        return followersDataEntrylist.size();
    }

    public void add(List<GitHubUsersDataEntry> entryList) {
        followersDataEntrylist.addAll(entryList);
        notifyDataSetChanged();
    }

    public void resetAllData() {
        followersDataEntrylist.clear();
        notifyDataSetChanged();
    }

    public class FollowersListViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView avatar;

        public FollowersListViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.followers_user_name);
            avatar = (ImageView) v.findViewById(R.id.followers_avatar_image);
        }

        private void populateFollowersViewData(GitHubUsersDataEntry followersDataEntry) {
            Log.v(TAG, "followersDataEntry.getName() - " + followersDataEntry.getName());
            name.setText(followersDataEntry.getName());

            Picasso pic = Picasso.with(context);
            Log.v(TAG, "path" + followersDataEntry.getImageUri());
            pic.load(followersDataEntry.getImageUri())
                    .fit().centerCrop()
                    .error(R.drawable.octocat)
                    .into(avatar);
        }

    }
}
