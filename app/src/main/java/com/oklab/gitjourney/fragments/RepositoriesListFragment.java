package com.oklab.gitjourney.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.adapters.ReposListAdapter;
import com.oklab.gitjourney.asynctasks.RepositoriesLoader;
import com.oklab.gitjourney.data.ReposDataEntry;

import java.util.List;

/**
 * Created by olgakuklina on 2017-01-16.
 */

public class RepositoriesListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = RepositoriesListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ReposListAdapter reposListAdapter;
    private RepositoriesListFragment.OnFragmentInteractionListener mListener;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = 1;
    private boolean reposExhausted = false;
    private boolean loading = false;

    public RepositoriesListFragment() {
    }

    public static RepositoriesListFragment newInstance() {
        RepositoriesListFragment fragment = new RepositoriesListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View v = inflater.inflate(R.layout.fragment_general_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.items_list_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        reposListAdapter = new ReposListAdapter(this.getContext());
        recyclerView.setAdapter(reposListAdapter);
        recyclerView.addOnScrollListener(new RepositoriesListFragment.ReposItemsListOnScrollListner());
        swipeRefreshLayout.setOnRefreshListener(this);
        loading = true;
        Bundle bundle = new Bundle();
        bundle.putInt("page", currentPage++);
        getLoaderManager().initLoader(0, bundle, new RepositoriesListFragment.ReposLoaderCallbacks());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RepositoriesListFragment.OnFragmentInteractionListener) {
            mListener = (RepositoriesListFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        if (loading) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        reposListAdapter.resetAllData();
        reposExhausted = false;
        loading = true;
        currentPage = 1;
        Bundle bundle = new Bundle();
        bundle.putInt("page", currentPage++);
        getLoaderManager().initLoader(0, bundle, new RepositoriesListFragment.ReposLoaderCallbacks());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class ReposItemsListOnScrollListner extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastScrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            int itemsCount = reposListAdapter.getItemCount();
            Log.v(TAG, "onScrolled - imetsCount = " + itemsCount);
            Log.v(TAG, "onScrolled - lastScrollPosition = " + lastScrollPosition);
            if (lastScrollPosition == itemsCount - 1 && !reposExhausted && !loading) {
                loading = true;
                Bundle bundle = new Bundle();
                bundle.putInt("page", currentPage++);
                getLoaderManager().initLoader(0, bundle, new RepositoriesListFragment.ReposLoaderCallbacks());
            }
        }
    }

    private class ReposLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<ReposDataEntry>> {

        @Override
        public Loader<List<ReposDataEntry>> onCreateLoader(int id, Bundle args) {
            Log.v(TAG, "onCreateLoader " + args);
            return new RepositoriesLoader(getContext(), args.getInt("page"));
        }

        @Override
        public void onLoadFinished(Loader<List<ReposDataEntry>> loader, List<ReposDataEntry> reposDataEntry) {
            loading = false;
            if (reposDataEntry != null && reposDataEntry.isEmpty()) {
                reposExhausted = true;
                getLoaderManager().destroyLoader(loader.getId());
                return;
            }
            reposListAdapter.add(reposDataEntry);
            swipeRefreshLayout.setRefreshing(false);
            getLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<List<ReposDataEntry>> loader) {
            loading = false;
        }
    }
}