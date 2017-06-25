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
import com.oklab.gitjourney.adapters.FeedListAdapter;
import com.oklab.gitjourney.asynctasks.FeedListLoader;
import com.oklab.gitjourney.data.FeedDataEntry;
import com.oklab.gitjourney.parsers.FeedListAtomParser;

import java.util.List;

public class FeedListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = FeedListFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FeedListAdapter feedListAdapter;
    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = 1;
    private boolean feedExhausted = false;
    private boolean loading = false;

    public FeedListFragment() {
    }

    public static FeedListFragment newInstance() {
        Log.v(TAG, " FeedListFragment newInstance ");
        FeedListFragment fragment = new FeedListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, " FeedListFragment onStart ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView savedInstanceState = " + savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_general_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.items_list_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        feedListAdapter = new FeedListAdapter(this.getContext());
        Log.v(TAG, "recyclerView getAdapter = " + recyclerView.getAdapter());
        recyclerView.setAdapter(feedListAdapter);
        recyclerView.addOnScrollListener(new FeedItemsListOnScrollListner());
        swipeRefreshLayout.setOnRefreshListener(this);

        loading = true;
        Bundle bundle = new Bundle();
        bundle.putInt("page", currentPage++);
        getLoaderManager().initLoader(0, bundle, new FeedListFragment.FeedLoaderCallbacks());
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        Log.v(TAG, "onRefresh");
        if (loading) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        feedListAdapter.resetAllData();
        feedExhausted = false;
        loading = true;
        currentPage = 1;
        Bundle bundle = new Bundle();
        bundle.putInt("page", currentPage++);
        getLoaderManager().initLoader(0, bundle, new FeedListFragment.FeedLoaderCallbacks());
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

    private class FeedItemsListOnScrollListner extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastScrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            int itemsCount = feedListAdapter.getItemCount();
            Log.v(TAG, "onScrolled - imetsCount = " + itemsCount);
            Log.v(TAG, "onScrolled - lastScrollPosition = " + lastScrollPosition);
            if (lastScrollPosition == itemsCount - 1 && !feedExhausted && !loading) {
                loading = true;
                Bundle bundle = new Bundle();
                bundle.putInt("page", currentPage++);
                getLoaderManager().initLoader(0, bundle, new FeedListFragment.FeedLoaderCallbacks());
            }
        }
    }

    private class FeedLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<FeedDataEntry>> {

        @Override
        public Loader<List<FeedDataEntry>> onCreateLoader(int id, Bundle args) {
            Log.v(TAG, "onCreateLoader " + args);
            return new FeedListLoader(getContext(), args.getInt("page"), new FeedListAtomParser());
        }

        @Override
        public void onLoadFinished(Loader<List<FeedDataEntry>> loader, List<FeedDataEntry> feedDataEntry) {
            loading = false;
            if (feedDataEntry != null && feedDataEntry.isEmpty()) {
                feedExhausted = true;
                getLoaderManager().destroyLoader(loader.getId());
                return;
            }
            feedListAdapter.add(feedDataEntry);
            swipeRefreshLayout.setRefreshing(false);
            getLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<List<FeedDataEntry>> loader) {
            loading = false;
        }
    }
}