package com.oklab.githubjourney.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oklab.githubjourney.data.ContributionDataEntry;
import com.oklab.githubjourney.githubjourney.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContributionsByDateAdapter extends RecyclerView.Adapter<ContributionsByDateAdapter.ContributionsByDateViewHolder> {

    private static final String TAG = FeedListAdapter.class.getSimpleName();
    private final ArrayList<ContributionDataEntry> contributionDataEntryList = new ArrayList<>(1000);
    private final Context context;

    public ContributionsByDateAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ContributionsByDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contributions_list_item, parent, false);
        return new ContributionsByDateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContributionsByDateViewHolder holder, int position) {
        ContributionDataEntry entry = contributionDataEntryList.get(position);
        holder.populateContributionData(entry);
    }

    @Override
    public int getItemCount() {
        return contributionDataEntryList.size();
    }

    public void add(List<ContributionDataEntry> entryList) {
        contributionDataEntryList.addAll(entryList);
        notifyDataSetChanged();
    }

    public void resetAllData() {
        contributionDataEntryList.clear();
        notifyDataSetChanged();
    }

    public class ContributionsByDateViewHolder extends RecyclerView.ViewHolder {

        private ImageView contribTypeImage;
        private TextView title;
        private TextView description;

        public ContributionsByDateViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextView) v.findViewById(R.id.action_desc);
            contribTypeImage = (ImageView) v.findViewById(R.id.action_icon);
        }

        private void populateContributionData(ContributionDataEntry contribData) {
            title.setText(contribData.getTitle());
            CharSequence desc = Html.fromHtml(Html.fromHtml(contribData.getDescription()).toString());
            description.setText(desc);

            Picasso pic = Picasso.with(context);
            Log.v(TAG, "path" + contribData.getAvatarURL());
            if (contribData.getAvatarURL() == null || contribData.getAvatarURL().isEmpty()) {
                pic.load(R.drawable.octocat)
                        .fit().centerCrop()
                        .into(contribTypeImage);
            } else {
                pic.load(contribData.getAvatarURL())
                        .fit().centerCrop()
                        .error(R.drawable.octocat)
                        .into(contribTypeImage);
            }
        }

    }
}
