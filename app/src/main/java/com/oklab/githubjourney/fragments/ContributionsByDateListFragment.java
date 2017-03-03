package com.oklab.githubjourney.fragments;

import android.content.Context;
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

import com.oklab.githubjourney.adapters.ContributionsByDateAdapter;
import com.oklab.githubjourney.asynctasks.ContributionsAsyncTask;
import com.oklab.githubjourney.githubjourney.R;

import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ContributionsByDateListFragment extends Fragment implements ContributionsAsyncTask.OnContributionsLoadedListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = ContributionsByDateListFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ContributionsByDateAdapter contributionsListAdapter;
    private OnListFragmentInteractionListener mListener;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = 1;
    private boolean feedExhausted = false;
    private boolean loading = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContributionsByDateListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ContributionsByDateListFragment newInstance(int columnCount) {
        Log.v(TAG, " ContributionsByDateListFragment newInstance ");
        ContributionsByDateListFragment fragment = new ContributionsByDateListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView savedInstanceState = " + savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_contributions, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.c_list);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.c_swipe_refresh_layout);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        contributionsListAdapter = new ContributionsByDateAdapter(this.getContext());
        recyclerView.setAdapter(contributionsListAdapter);
        recyclerView.addOnScrollListener(new ContributionsByDateListFragment.ContributionItemsListOnScrollListner());
        swipeRefreshLayout.setOnRefreshListener(this);

        loading = true;
        new ContributionsAsyncTask(ContributionsByDateListFragment.this.getContext(), ContributionsByDateListFragment.this).execute(currentPage++);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {

    }


    @Override
    public void OnContributionsLoaded(List contributionsDataEntry) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        //  void onListFragmentInteraction(DummyItem item);
    }

    private class ContributionItemsListOnScrollListner extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastScrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            int itemsCount = contributionsListAdapter.getItemCount();
            Log.v(TAG, "onScrolled - imetsCount = " + itemsCount);
            Log.v(TAG, "onScrolled - lastScrollPosition = " + lastScrollPosition);
            if (lastScrollPosition == itemsCount - 1 && !feedExhausted && !loading) {
                loading = true;
                new ContributionsAsyncTask(ContributionsByDateListFragment.this.getContext(), ContributionsByDateListFragment.this).execute(currentPage++);
            }
        }
    }
}
