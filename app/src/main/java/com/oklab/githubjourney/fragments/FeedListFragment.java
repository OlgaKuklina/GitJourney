package com.oklab.githubjourney.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oklab.githubjourney.adapters.FeedListAdapter;
import com.oklab.githubjourney.asynctasks.FeedsAsyncTask;
import com.oklab.githubjourney.data.FeedDataEntry;
import com.oklab.githubjourney.githubjourney.R;

import java.util.List;

public class FeedListFragment extends Fragment implements FeedsAsyncTask.OnFeedLoadedListener {


    private static final String TAG = FeedListFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FeedListAdapter feedListAdapter;
    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager lineralLayoutManager;
    private int currentPage = 1;
    private boolean feedExhausted = false;
    private boolean loading = false;
    public FeedListFragment() {
    }

    public static FeedListFragment newInstance() {
        FeedListFragment fragment = new FeedListFragment();
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
        View v = inflater.inflate(R.layout.fragment_feed_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.items_list_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
        lineralLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lineralLayoutManager);
        feedListAdapter = new FeedListAdapter(this.getContext());
        recyclerView.setAdapter(feedListAdapter);
        recyclerView.addOnScrollListener(new FeedItemsListOnScrollListner());
        loading = true;
        new FeedsAsyncTask(getContext(), this).execute(currentPage++);
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
    public void onFeedLoaded(List<FeedDataEntry> feedDataEntry) {
        loading = false;
        if(feedDataEntry !=null && feedDataEntry.isEmpty()) {
            feedExhausted = true;
            return;
        }
        feedListAdapter.add(feedDataEntry);
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
            int lastScrollPosition = lineralLayoutManager.findLastCompletelyVisibleItemPosition();
            int itemsCount = feedListAdapter.getItemCount();
            Log.v(TAG, "onScrolled - imetsCount = " + itemsCount);
            Log.v(TAG, "onScrolled - lastScrollPosition = " + lastScrollPosition);
            if (lastScrollPosition == itemsCount - 1 && !feedExhausted && !loading) {
                loading = true;
                new FeedsAsyncTask(FeedListFragment.this.getContext(), FeedListFragment.this).execute(currentPage++);
            }
        }
    }


}
