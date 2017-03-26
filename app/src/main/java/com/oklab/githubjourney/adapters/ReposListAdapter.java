package com.oklab.githubjourney.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.ReposDataEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-01-20.
 */

public class ReposListAdapter extends RecyclerView.Adapter<ReposListAdapter.ReposListViewHolder> {

    private static final String TAG = FeedListAdapter.class.getSimpleName();
    private final ArrayList<ReposDataEntry> reposDataEntrylist = new ArrayList<>(1000);
    private final Context context;

    public ReposListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ReposListAdapter.ReposListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.repositories_list_item, parent, false);
        return new ReposListAdapter.ReposListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReposListViewHolder holder, int position) {
        ReposDataEntry entry = reposDataEntrylist.get(position);
        holder.populateReposViewData(entry);
    }

    @Override
    public int getItemCount() {
        return reposDataEntrylist.size();
    }

    public void add(List<ReposDataEntry> entryList) {
        reposDataEntrylist.addAll(entryList);
        notifyDataSetChanged();
    }

    public void resetAllData() {
        reposDataEntrylist.clear();
        notifyDataSetChanged();
    }

    public class ReposListViewHolder extends RecyclerView.ViewHolder {

        private ImageView type;
        private TextView title;
        private TextView description;
        private TextView language;
        private TextView stars;
        private TextView forks;


        public ReposListViewHolder(View v) {
            super(v);
            type = (ImageView) v.findViewById(R.id.status_image);
            title = (TextView) v.findViewById(R.id.repo_name);
            description = (TextView) v.findViewById(R.id.desc);
            language = (TextView) v.findViewById(R.id.language);
            stars = (TextView) v.findViewById(R.id.stars);
            forks = (TextView) v.findViewById(R.id.forks);
        }

        private void populateReposViewData(ReposDataEntry reposDataEntry) {
            title.setText(reposDataEntry.getTitle());
            if (reposDataEntry.getDescription() != null && !reposDataEntry.getDescription().equals("null")) {
                description.setText(reposDataEntry.getDescription());
            }

            language.setText(reposDataEntry.getLanguage());
            stars.setText(Integer.toString(reposDataEntry.getStars()));
            forks.setText(Integer.toString(reposDataEntry.getForks()));

            Picasso pic = Picasso.with(context);
            Log.v(TAG, "path" + reposDataEntry.isPrivate());
            if (reposDataEntry.isForked()) {
                pic.load(R.drawable.octocat)
                        .fit().centerCrop()
                        .into(type);
            } else if (reposDataEntry.isPrivate()) {
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
