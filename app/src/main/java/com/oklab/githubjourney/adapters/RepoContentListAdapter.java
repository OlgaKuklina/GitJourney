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
import com.oklab.githubjourney.data.RepositoryContentDataEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by olgakuklina on 2017-04-26.
 */

public class RepoContentListAdapter extends RecyclerView.Adapter<RepoContentListAdapter.RepoContentListViewHolder> {
    private static final String TAG = RepoContentListAdapter.class.getSimpleName();
    private static final Comparator<RepositoryContentDataEntry> COMPARATOR = new ContentTypeComparator();
    private final Context context;
    private final RepoContentOnClickListener repoContentOnClickListener;
    private final ArrayList<RepositoryContentDataEntry> repoContentDataEntrylist = new ArrayList<>(1000);

    public RepoContentListAdapter(Context context, RepoContentOnClickListener repoContentOnClickListener) {
        Log.v(TAG, "RepoContentListAdapter");
        this.context = context;
        this.repoContentOnClickListener = repoContentOnClickListener;
    }

    @Override
    public RepoContentListAdapter.RepoContentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.repository_content_list_item, parent, false);
        return new RepoContentListAdapter.RepoContentListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepoContentListAdapter.RepoContentListViewHolder holder, int position) {
        RepositoryContentDataEntry entry = repoContentDataEntrylist.get(position);
        holder.populateRepoContentViewData(entry);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, " entry = " + entry);
                repoContentOnClickListener.onRepoItemClicked(entry);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repoContentDataEntrylist.size();
    }

    public void add(List<RepositoryContentDataEntry> entryList) {
        Log.v(TAG, "entryList " + entryList.size());
        Collections.sort(entryList, COMPARATOR);
        repoContentDataEntrylist.addAll(entryList);
        notifyDataSetChanged();
    }

    public void resetAllData() {
        repoContentDataEntrylist.clear();
        notifyDataSetChanged();
    }

    public interface RepoContentOnClickListener {
        void onRepoItemClicked(RepositoryContentDataEntry entry);
    }
    private static class ContentTypeComparator implements Comparator<RepositoryContentDataEntry>
    {
        @Override
        public int compare (RepositoryContentDataEntry e1,  RepositoryContentDataEntry e2)
        {
            if(e1.getType() != e2.getType()) {
                return Integer.compare(e1.getType().getPriority(), e2.getType().getPriority());
            }
            return e1.getName().compareTo(e2.getName());
        }
    }
    public class RepoContentListViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView path;
        private ImageView contentIcon;


        public RepoContentListViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.content_name);
            path = (TextView) v.findViewById(R.id.content_path);
            contentIcon = (ImageView) v.findViewById(R.id.content_icon_image);
        }

        private void populateRepoContentViewData(RepositoryContentDataEntry dataEntry) {
            Log.v(TAG, "dataEntry" + dataEntry);
            name.setText(dataEntry.getName());
            path.setText(dataEntry.getUri());
            switch (dataEntry.getType()) {
                case DIR:
                    contentIcon.setVisibility(View.VISIBLE);
                    contentIcon.setBackground(context.getDrawable(R.drawable.dir));
                    break;
                case FILE:
                    contentIcon.setVisibility(View.VISIBLE);
                    contentIcon.setBackground(context.getDrawable(R.drawable.doc));
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
