package com.oklab.githubjourney.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.activities.UserProfileActivity;
import com.oklab.githubjourney.data.GitHubUserProfileDataEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-02-05.
 */

public class FollowingListAdapter extends RecyclerView.Adapter<FollowingListAdapter.FollowingListViewHolder> {
    private static final String TAG = FeedListAdapter.class.getSimpleName();
    private final ArrayList<GitHubUserProfileDataEntry> followingDataEntrylist = new ArrayList<>(1000);
    private final Context context;

    public FollowingListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FollowingListAdapter.FollowingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.following_list_item, parent, false);
        return new FollowingListAdapter.FollowingListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FollowingListAdapter.FollowingListViewHolder holder, int position) {
        GitHubUserProfileDataEntry entry = followingDataEntrylist.get(position);
        holder.populateFollowingViewData(entry);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, " view.getId()" + view.getId());
                String login = followingDataEntrylist.get(position).getLogin();
                String name = followingDataEntrylist.get(position).getName();
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("login_id", login);
                intent.putExtra("name", name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return followingDataEntrylist.size();
    }

    public void add(List<GitHubUserProfileDataEntry> entryList) {
        followingDataEntrylist.addAll(entryList);
        notifyDataSetChanged();
    }

    public void resetAllData() {
        followingDataEntrylist.clear();
        notifyDataSetChanged();
    }

    public class FollowingListViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView login;
        private TextView email;
        private TextView location;
        private ImageView avatar;


        public FollowingListViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.following_user_name);
            avatar = (ImageView) v.findViewById(R.id.following_avatar_image);
            login = (TextView) v.findViewById(R.id.following_login);
            email = (TextView) v.findViewById(R.id.following_email);
            location = (TextView) v.findViewById(R.id.following_location);
            avatar = (ImageView) v.findViewById(R.id.following_avatar_image);
        }

        private void populateFollowingViewData(GitHubUserProfileDataEntry followingDataEntry) {
            name.setText(followingDataEntry.getName());
            login.setText(followingDataEntry.getLogin());
            email.setText(followingDataEntry.getEmail());
            location.setText(followingDataEntry.getLocation());
            Picasso pic = Picasso.with(context);
            Log.v(TAG, "path" + followingDataEntry.getImageUri());
            pic.load(followingDataEntry.getImageUri())
                    .fit().centerCrop()
                    .error(R.drawable.octocat)
                    .into(avatar);
        }
    }
}
