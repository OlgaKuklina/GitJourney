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
import com.oklab.githubjourney.activities.RepositoryActivity;
import com.oklab.githubjourney.activities.UserProfileActivity;
import com.oklab.githubjourney.customui.CircleView;
import com.oklab.githubjourney.data.ReposDataEntry;
import com.oklab.githubjourney.utils.GithubLanguageColorsMatcher;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, " entry = " + entry);
                Intent intent = new Intent(context, RepositoryActivity.class);
                intent.putExtra("repo", entry);
                context.startActivity(intent);
            }
        });
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
        private CircleView languageCircle;


        public ReposListViewHolder(View v) {
            super(v);
            type = (ImageView) v.findViewById(R.id.status_image);
            title = (TextView) v.findViewById(R.id.repo_name);
            description = (TextView) v.findViewById(R.id.desc);
            language = (TextView) v.findViewById(R.id.language);
            stars = (TextView) v.findViewById(R.id.stars);
            forks = (TextView) v.findViewById(R.id.forks);
            languageCircle = (CircleView) v.findViewById(R.id.language_circle_repo);
        }

        private void populateReposViewData(ReposDataEntry reposDataEntry) {
            title.setText(reposDataEntry.getTitle());
            if (reposDataEntry.getDescription() != null && !reposDataEntry.getDescription().equals("null")) {
                description.setText(reposDataEntry.getDescription());
            }

            if (reposDataEntry.getLanguage() != null && !reposDataEntry.getLanguage().isEmpty() && !reposDataEntry.getLanguage().equals("null")) {
                int colorId = GithubLanguageColorsMatcher.findMatchedColor(context, reposDataEntry.getLanguage());
                Log.v(TAG, " colorId = " + colorId);
                if (colorId != 0) {
                    languageCircle.setColor(context.getResources().getColor(colorId));
                } else {
                    languageCircle.setColor(context.getResources().getColor(R.color.colorred));
                }
                language.setVisibility(View.VISIBLE);
                language.setText(reposDataEntry.getLanguage());
            } else {
                language.setVisibility(View.INVISIBLE);
            }
            stars.setText(String.format(Locale.getDefault(),"%d", reposDataEntry.getStars()));
            forks.setText(String.format(Locale.getDefault(),"%d",reposDataEntry.getForks()));

            Picasso pic = Picasso.with(context);
            pic.load(R.drawable.repository)
                    .fit().centerCrop()
                    .error(R.drawable.octocat)
                    .into(type);
        }
    }
}


