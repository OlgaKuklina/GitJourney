package com.oklab.githubjourney.fragments;

import android.database.Cursor;
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

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.adapters.ContributionsByDateAdapter;
import com.oklab.githubjourney.data.ContributionsDataLoader;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ContributionsByDateListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = ContributionsByDateListFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ContributionsByDateAdapter contributionsListAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContributionsByDateListFragment() {
    }

    public static ContributionsByDateListFragment newInstance() {
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
        View v = inflater.inflate(R.layout.fragment_contributions_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.c_items_list_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.c_swipe_refresh_layout);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout.setOnRefreshListener(this);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onRefresh() {
        recyclerView.setAdapter(null);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return ContributionsDataLoader.newAllItemsLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(TAG, "loader finished " + data.getCount());
        contributionsListAdapter = new ContributionsByDateAdapter(this.getContext(), data);
        contributionsListAdapter.setHasStableIds(true);
        recyclerView.setAdapter(contributionsListAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
}
