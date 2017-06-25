package com.oklab.gitjourney.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.activities.RepositoryActivity;
import com.oklab.gitjourney.customui.CircleView;
import com.oklab.gitjourney.data.ReposDataEntry;
import com.oklab.gitjourney.utils.GithubLanguageColorsMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by olgakuklina on 2017-04-01.
 */

public class UserProfileDetailAdapter extends RecyclerView.Adapter<UserProfileDetailAdapter.UserProfileDetailViewHolder> {
    private static final String TAG = UserProfileDetailAdapter.class.getSimpleName();
    private final ArrayList<ReposDataEntry> reposDataEntries = new ArrayList<>(1000);
    private final Context context;


    public UserProfileDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public UserProfileDetailAdapter.UserProfileDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_profile_list_item, parent, false);
        return new UserProfileDetailAdapter.UserProfileDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserProfileDetailAdapter.UserProfileDetailViewHolder holder, int position) {
        ReposDataEntry entry = reposDataEntries.get(position);
        holder.populateUserProfileDetailViewData(entry);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, " onClick");
                Intent intent = new Intent(context, RepositoryActivity.class);
                intent.putExtra("repo", entry);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reposDataEntries.size();
    }

    public void addAll(List<ReposDataEntry> entryList) {
        reposDataEntries.addAll(entryList);
        notifyDataSetChanged();
    }

    public void resetAllData() {
        reposDataEntries.clear();
        notifyDataSetChanged();
    }


    public class UserProfileDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView repoTitle;
        private TextView repoSubTitle;
        private TextView language;
        private TextView stars;
        private TextView forks;
        private CircleView languageCircle;

        public UserProfileDetailViewHolder(View view) {
            super(view);
            repoSubTitle = (TextView) view.findViewById(R.id.repo_subtitle);
            repoTitle = (TextView) view.findViewById(R.id.repo_title);
            language = (TextView) view.findViewById(R.id.repo_detail_language);
            languageCircle = (CircleView) view.findViewById(R.id.language_circle);
            stars = (TextView) view.findViewById(R.id.repo_detail_stars);
            forks = (TextView) view.findViewById(R.id.repo_detail_forks);

        }

        private void populateUserProfileDetailViewData(ReposDataEntry data) {
            Log.v(TAG, "ReposDataEntry" + data.getLanguage());
            if (data.getDescription() != null && !data.getDescription().isEmpty() && !data.getDescription().equals("null")) {
                repoSubTitle.setVisibility(View.VISIBLE);
                repoSubTitle.setText(data.getDescription());
            } else {
                repoSubTitle.setVisibility(View.INVISIBLE);
            }
            repoTitle.setText(data.getTitle());
            if (data.getLanguage() != null && !data.getLanguage().isEmpty() && !data.getLanguage().equals("null")) {
                Log.v(TAG, " data.getLanguage() = " + data.getLanguage());
                int colorId = GithubLanguageColorsMatcher.findMatchedColor(context, data.getLanguage());
                Log.v(TAG, " colorId = " + colorId);
                if (colorId != 0) {
                    languageCircle.setColor(context.getResources().getColor(colorId));
                } else {
                    languageCircle.setColor(context.getResources().getColor(R.color.colorred));
                }
                language.setVisibility(View.VISIBLE);
                language.setText(data.getLanguage());
            } else {
                language.setText(R.string.unknown_text);
            }
            forks.setTextColor(context.getResources().getColor(R.color.color_text_primary));
            forks.setText(String.format(Locale.getDefault(), "%d", data.getForks()));
            stars.setTextColor(context.getResources().getColor(R.color.color_text_primary));
            stars.setText(String.format(Locale.getDefault(), "%d", data.getStars()));
        }
    }
}





