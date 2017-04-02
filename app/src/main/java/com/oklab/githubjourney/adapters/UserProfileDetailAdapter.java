package com.oklab.githubjourney.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.ReposDataEntry;
import com.oklab.githubjourney.utils.GithubLanguageColorsMatcher;

import java.util.ArrayList;
import java.util.List;

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

        public UserProfileDetailViewHolder(View view) {
            super(view);
            repoSubTitle = (TextView) view.findViewById(R.id.repo_subtitle);
            repoTitle = (TextView) view.findViewById(R.id.repo_title);
            language = (TextView) view.findViewById(R.id.repo_detail_language);
            stars = (TextView) view.findViewById(R.id.repo_detail_stars);
            forks = (TextView) view.findViewById(R.id.repo_detail_forks);
        }

        private void populateUserProfileDetailViewData(ReposDataEntry data) {
            Log.v(TAG, "ReposDataEntry" + data.getLanguage());
            if(data.getDescription() !=null && !data.getDescription().isEmpty() && !data.getDescription().equals("null")) {
                repoSubTitle.setText(data.getDescription());
            }
            else {
                repoSubTitle.setText(R.string.empty_text);
            }
            repoTitle.setText(data.getTitle());
            if(data.getLanguage() !=null && !data.getLanguage().isEmpty() && !data.getLanguage().equals("null")) {
                Log.v(TAG, " data.getLanguage() = " + data.getLanguage());
                int colorId = GithubLanguageColorsMatcher.findMatchedColor(context, data.getLanguage());
                Log.v(TAG, " colorId = " + colorId);
                if(colorId!=0) {
                    language.setTextColor(context.getResources().getColor(colorId));
                }

                language.setText(data.getLanguage());
            }
            else {
                language.setText(R.string.empty_value);
            }
            if(data.getForks() != 0) {
                forks.setText(Integer.toString(data.getForks()));
            }
            else {
                forks.setText(R.string.empty_value);
            }
            if(data.getStars() != 0) {
                stars.setText(Integer.toString(data.getStars()));
            }
            else {
                stars.setText(R.string.empty_value);
            }

        }
    }
}




