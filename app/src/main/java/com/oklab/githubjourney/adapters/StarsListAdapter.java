package com.oklab.githubjourney.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oklab.githubjourney.data.StarsDataEntry;
import com.oklab.githubjourney.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-02-05.
 */

public class StarsListAdapter extends RecyclerView.Adapter<StarsListAdapter.StarsListViewHolder> {

    private static final String TAG = FeedListAdapter.class.getSimpleName();
    private final ArrayList<StarsDataEntry> starsDataEntrylist = new ArrayList<>(1000);
    private final Context context;

    public StarsListAdapter(Context context) {
        this.context = context;
    }


    @Override
    public StarsListAdapter.StarsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.stars_list_item, parent, false);
        return new StarsListAdapter.StarsListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StarsListViewHolder holder, int position) {
        StarsDataEntry entry = starsDataEntrylist.get(position);
        holder.populateStarsViewData(entry);
    }


    @Override
    public int getItemCount() {
        return starsDataEntrylist.size();
    }

    public void add(List<StarsDataEntry> entryList) {
        starsDataEntrylist.addAll(entryList);
        notifyDataSetChanged();
    }

    public void resetAllData() {
        starsDataEntrylist.clear();
        notifyDataSetChanged();
    }

    public class StarsListViewHolder extends RecyclerView.ViewHolder {

        private ImageView type;
        private TextView title;
        private TextView description;
        private TextView repoShortUri;
        private TextView language;
        private TextView stars;
        private TextView forks;
        private TextView watchers;

        public StarsListViewHolder(View v) {
            super(v);
            type = (ImageView) v.findViewById(R.id.stars_repo_status_image);
            title = (TextView) v.findViewById(R.id.stars_repo_title);
            description = (TextView) v.findViewById(R.id.stars_repo_desc);
            language = (TextView) v.findViewById(R.id.stars_repo_language);
            stars = (TextView) v.findViewById(R.id.stars_count);
            forks = (TextView) v.findViewById(R.id.stars_forks);
            watchers = (TextView) v.findViewById(R.id.stars_watchers);
            repoShortUri = (TextView) v.findViewById(R.id.stars_repo_short_uri);

        }

        private void populateStarsViewData(StarsDataEntry starsDataEntry) {
            title.setText(starsDataEntry.getTitle());
            description.setText(starsDataEntry.getDescription());
            language.setText(starsDataEntry.getLanguage());
            stars.setText(Integer.toString(starsDataEntry.getStars()));
            forks.setText(Integer.toString(starsDataEntry.getForks()));
            watchers.setText(Integer.toString(starsDataEntry.getWatchers()));
            repoShortUri.setText(starsDataEntry.getFullName());

            Picasso pic = Picasso.with(context);
            Log.v(TAG, "path" + starsDataEntry.isPrivate());
            if (starsDataEntry.getForks() != 0) {
                pic.load(R.drawable.octocat)
                        .fit().centerCrop()
                        .into(type);
            } else if (starsDataEntry.isPrivate()) {
                pic.load(R.drawable.octocat)
                        .fit().centerCrop()
                        .error(R.drawable.octocat)
                        .into(type);
            } else {
                pic.load(R.drawable.octocat)
                        .fit().centerCrop()
                        .error(R.drawable.octocat)
                        .into(type);
            }


        }
    }

}
