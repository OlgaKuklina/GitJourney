package com.oklab.githubjourney.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.FeedDataEntry;
import com.oklab.githubjourney.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by olgakuklina on 2017-01-15.
 */

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.FeedListViewHolder> {

    private static final String TAG = FeedListAdapter.class.getSimpleName();
    private final ArrayList<FeedDataEntry> feedDataEntryList = new ArrayList<>(1000);
    private final Context context;

    public FeedListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FeedListAdapter.FeedListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.feeds_list_item, parent, false);
        return new FeedListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FeedListAdapter.FeedListViewHolder holder, int position) {
        FeedDataEntry entry = feedDataEntryList.get(position);
        holder.populateFeedViewData(entry);
    }

    @Override
    public int getItemCount() {
        return feedDataEntryList.size();
    }

    public void add(List<FeedDataEntry> entryList) {
        feedDataEntryList.addAll(entryList);
        notifyDataSetChanged();
    }

    public void resetAllData() {
        feedDataEntryList.clear();
        notifyDataSetChanged();
    }

    public class FeedListViewHolder extends RecyclerView.ViewHolder {

        private ImageView personalProfImage;
        private TextView action_date;
        private TextView title;
        private TextView authorName;
        private TextView description;

        public FeedListViewHolder(View v) {
            super(v);
            personalProfImage = (ImageView) v.findViewById(R.id.github_user_image);
            authorName = (TextView) v.findViewById(R.id.author_name);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextView) v.findViewById(R.id.action_desc);
            action_date = (TextView) v.findViewById(R.id.action_date);
        }

        private void populateFeedViewData(FeedDataEntry feedData) {
            authorName.setText(feedData.getAuthorName());
            title.setText(feedData.getTitle());
            CharSequence desc = Html.fromHtml(Html.fromHtml(feedData.getDescription()).toString());
            description.setText(desc);
            DateFormat formatter = Utils.createDateFormatterWithTimeZone(context, Utils.DMY_DATE_FORMAT_PATTERN);
            Calendar date = feedData.getDate();
            action_date.setText(formatter.format(date.getTime()));

            Picasso pic = Picasso.with(context);
            if (feedData.getAvatarURL() == null || feedData.getAvatarURL().isEmpty()) {
                pic.load(R.drawable.octocat)
                        .fit().centerCrop()
                        .into(personalProfImage);
            } else {
                pic.load(feedData.getAvatarURL())
                        .fit().centerCrop()
                        .error(R.drawable.octocat)
                        .into(personalProfImage);
            }
        }
    }
}



