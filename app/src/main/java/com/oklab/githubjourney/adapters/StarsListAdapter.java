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
import com.oklab.githubjourney.customui.CircleView;
import com.oklab.githubjourney.data.StarsDataEntry;
import com.oklab.githubjourney.utils.GithubLanguageColorsMatcher;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        private CircleView languageCircle;

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
            languageCircle = (CircleView) v.findViewById(R.id.language_circle_star);


        }

        private void populateStarsViewData(StarsDataEntry starsDataEntry) {
            title.setText(starsDataEntry.getTitle());
            description.setText(starsDataEntry.getDescription());
            if (starsDataEntry.getLanguage() != null && !starsDataEntry.getLanguage().isEmpty() && !starsDataEntry.getLanguage().equals("null")) {
                Log.v(TAG, " data.getLanguage() = " + starsDataEntry.getLanguage());
                int colorId = GithubLanguageColorsMatcher.findMatchedColor(context, starsDataEntry.getLanguage());
                Log.v(TAG, " colorId = " + colorId);
                if (colorId != 0) {
                    languageCircle.setColor(context.getResources().getColor(colorId));
                } else {
                    languageCircle.setColor(context.getResources().getColor(R.color.colorred));
                }
                language.setVisibility(View.VISIBLE);
                language.setText(starsDataEntry.getLanguage());
            } else {
                language.setVisibility(View.INVISIBLE);
            }
            language.setText(starsDataEntry.getLanguage());
            stars.setText(String.format(Locale.getDefault(), "%d", starsDataEntry.getStars()));
            forks.setText(String.format(Locale.getDefault(), "%d", starsDataEntry.getForks()));
            watchers.setText(String.format(Locale.getDefault(), "%d", starsDataEntry.getWatchers()));
            repoShortUri.setText(starsDataEntry.getFullName());

            Picasso pic = Picasso.with(context);
            pic.load(R.drawable.repository)
                    .fit().centerCrop()
                    .error(R.drawable.octocat)
                    .into(type);
        }

    }
}
