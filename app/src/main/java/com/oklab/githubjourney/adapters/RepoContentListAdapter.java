package com.oklab.githubjourney.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.GitHubRepoContentType;
import com.oklab.githubjourney.data.GitHubUserProfileDataEntry;
import com.oklab.githubjourney.data.RepositoryContentDataEntry;
import com.squareup.picasso.Picasso;

/**
 * Created by olgakuklina on 2017-04-26.
 */

public class RepoContentListAdapter extends RecyclerView.Adapter<RepoContentListAdapter.RepoContentListViewHolder> {
    private static final String TAG = RepoContentListAdapter.class.getSimpleName();
    private final Context context;

    public RepoContentListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RepoContentListAdapter.RepoContentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RepoContentListAdapter.RepoContentListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RepoContentListViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView contentIcon;


        public RepoContentListViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.content_name);
            contentIcon = (ImageView) v.findViewById(R.id.content_icon_image);
        }

        private void populateRepoContentViewData(RepositoryContentDataEntry dataEntry) {
            name.setText(dataEntry.getName());
            switch (dataEntry.getType()) {
                case DIR:
                    contentIcon.setVisibility(View.VISIBLE);
                    contentIcon.setBackground(context.getDrawable(R.drawable.repository));
                    break;
                case FILE:
                    contentIcon.setVisibility(View.VISIBLE);
                    contentIcon.setBackground(context.getDrawable(R.drawable.octocat));
                    break;
                case SYMLINK:
                    contentIcon.setVisibility(View.VISIBLE);
                    contentIcon.setBackground(context.getDrawable(R.drawable.octocat));
                    break;
                case SUBMODULE:
                    contentIcon.setVisibility(View.VISIBLE);
                    contentIcon.setBackground(context.getDrawable(R.drawable.octocat));
                    break;
            }
        }
    }
}
