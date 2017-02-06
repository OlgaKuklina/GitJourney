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

import com.oklab.githubjourney.adapters.StarsListAdapter;
import com.oklab.githubjourney.asynctasks.StarsAsyncTask;
import com.oklab.githubjourney.data.StarsDataEntry;
import com.oklab.githubjourney.githubjourney.R;

import java.util.List;

/**
 * Created by olgakuklina on 2017-02-06.
 */

public class StarsListFragment extends Fragment implements StarsAsyncTask.OnStarsLoadedListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = StarsListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private StarsListAdapter starsListAdapter;
    private StarsListFragment.OnFragmentInteractionListener mListener;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = 1;
    private boolean starsExhausted = false;
    private boolean loading = false;

    public StarsListFragment() {
    }

    public static StarsListFragment newInstance() {
        StarsListFragment fragment = new StarsListFragment();
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
        starsListAdapter = new StarsListAdapter(this.getContext());
        recyclerView.setAdapter(starsListAdapter);
        recyclerView.addOnScrollListener(new StarsListFragment.StarsItemsListOnScrollListner());
        swipeRefreshLayout.setOnRefreshListener(this);
        loading = true;
        new StarsAsyncTask(getContext(), this).execute(currentPage++);
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
        if (context instanceof StarsListFragment.OnFragmentInteractionListener) {
            mListener = (StarsListFragment.OnFragmentInteractionListener) context;
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
        starsListAdapter.resetAllData();
        starsExhausted = false;
        loading = true;
        currentPage = 1;
        new StarsAsyncTask(getContext(), this).execute(currentPage++);
    }


    @Override
    public void onStarsLoaded(List<StarsDataEntry> starsDataEntry) {
        loading = false;
        if (starsDataEntry != null && starsDataEntry.isEmpty()) {
            starsExhausted = true;
            return;
        }
        starsListAdapter.add(starsDataEntry);
        swipeRefreshLayout.setRefreshing(false);
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

    private class StarsItemsListOnScrollListner extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastScrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            int itemsCount = starsListAdapter.getItemCount();
            Log.v(TAG, "onScrolled - imetsCount = " + itemsCount);
            Log.v(TAG, "onScrolled - lastScrollPosition = " + lastScrollPosition);
            Log.v(TAG, "onScrolled - currentPage = " + currentPage);
            if (lastScrollPosition == itemsCount - 1 && !starsExhausted && !loading) {
                loading = true;
                new StarsAsyncTask(StarsListFragment.this.getContext(), StarsListFragment.this).execute(currentPage++);
            }
        }
    }
}
