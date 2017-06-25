package com.oklab.gitjourney.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.adapters.RepoContentListAdapter;
import com.oklab.gitjourney.asynctasks.RepoContentLoader;
import com.oklab.gitjourney.asynctasks.RepositoryFileContentLoader;
import com.oklab.gitjourney.data.RepositoryContentDataEntry;

import java.util.List;
import java.util.Stack;

import io.github.kbiakov.codeview.CodeView;

/**
 * Created by olgakuklina on 2017-04-26.
 */
public class RepositoryContentListFragment extends Fragment implements RepoContentListAdapter.RepoContentOnClickListener {
    private static final String TAG = RepositoryContentListFragment.class.getSimpleName();
    private static Stack<String> pathStack = new Stack<>();
    private final RepoContentLoaderCallbacks callbacks = new RepoContentLoaderCallbacks();
    private final RepoFileContentLoaderCallbacks fileContentLoadedCallbacks = new RepoFileContentLoaderCallbacks();
    private RecyclerView recyclerView;
    private CodeView codeView;
    private RepoContentListAdapter repoContentListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RepoContentFragmentInteractionListener repoContentChangedlistner;

    public RepositoryContentListFragment() {
    }

    public static RepositoryContentListFragment newInstance(RepoContentFragmentInteractionListener repoContentChangedListener, String repoName, String loginName) {
        RepositoryContentListFragment fragment = new RepositoryContentListFragment();
        Bundle args = new Bundle();
        args.putString("path", "");
        args.putString("repoName", repoName);
        args.putString("login", loginName);
        fragment.setArguments(args);
        fragment.repoContentChangedlistner = repoContentChangedListener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_repository_content, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.repo_content_recycler_view);
        codeView = (CodeView) v.findViewById(R.id.code_view);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        repoContentListAdapter = new RepoContentListAdapter(this.getContext(), this);
        recyclerView.setAdapter(repoContentListAdapter);
        getLoaderManager().initLoader(0, getArguments(), callbacks);
    }

    @Override
    public void onRepoItemClicked(RepositoryContentDataEntry entry) {
        Log.v(TAG, "entry.getPath() = " + entry.getPath());
        Bundle args = getArguments();
        switch (entry.getType()) {
            case DIR:
            case SUBMODULE:
                repoContentChangedlistner.onPathChanged(entry.getPath());
                repoContentListAdapter.resetAllData();
                pathStack.push(args.getString("path"));
                args.putString("path", entry.getPath());
                getLoaderManager().initLoader(0, args, callbacks);
                break;
            case EMPTY:
                Log.v(TAG, "EMPTY");
                String newPath = pathStack.pop();
                repoContentChangedlistner.onPathChanged(newPath);
                repoContentListAdapter.resetAllData();
                args.putString("path", newPath);
                getLoaderManager().initLoader(0, args, callbacks);
                break;
            case README:
            case FILE:
                repoContentChangedlistner.onPathChanged(entry.getPath());
                Log.v(TAG, "download_uri = " + entry.getUri());
                repoContentListAdapter.resetAllData();
                pathStack.push(args.getString("path"));
                Bundle argsFileContent = new Bundle();
                argsFileContent.putString("download_uri", entry.getUri());
                getLoaderManager().initLoader(1, argsFileContent, fileContentLoadedCallbacks);
                break;
            default:
                break;
        }
    }

    public boolean repoContentOnBackPressed() {
        if (pathStack.isEmpty()) {
            return false;
        }
        Log.v(TAG, "repoContentOnBackPressed [" + pathStack.peek() + "]");
        String newPath = pathStack.pop();
        repoContentChangedlistner.onPathChanged(newPath);
        repoContentListAdapter.resetAllData();
        Bundle args = getArguments();
        args.putString("path", newPath);
        getLoaderManager().initLoader(0, args, callbacks);
        return true;
    }

    public interface RepoContentFragmentInteractionListener {
        void onPathChanged(String newPath);
    }

    private class RepoContentLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<RepositoryContentDataEntry>> {

        @Override
        public Loader<List<RepositoryContentDataEntry>> onCreateLoader(int id, Bundle args) {
            return new RepoContentLoader(getContext(), args.getString("path"), args.getString("repoName"), args.getString("login"));
        }

        @Override
        public void onLoadFinished(Loader<List<RepositoryContentDataEntry>> loader, List<RepositoryContentDataEntry> repoContentDataEntryList) {
            Log.v(TAG, "onLoadFinished " + repoContentDataEntryList);
            if (repoContentDataEntryList != null && !repoContentDataEntryList.isEmpty()) {
                repoContentListAdapter.add(repoContentDataEntryList);
                recyclerView.setVisibility(View.VISIBLE);
                codeView.setVisibility(View.GONE);
            }
            getLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<List<RepositoryContentDataEntry>> loader) {
        }
    }

    private class RepoFileContentLoaderCallbacks implements LoaderManager.LoaderCallbacks<String> {

        @Override
        public Loader<String> onCreateLoader(int id, Bundle argsFileContent) {
            return new RepositoryFileContentLoader(getContext(), argsFileContent.getString("download_uri"));
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String fileContent) {
            Log.v(TAG, "onLoadFinished " + fileContent);
            if (fileContent != null && !fileContent.isEmpty()) {
                codeView.setCode(fileContent);
                codeView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            getLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
        }
    }
}
