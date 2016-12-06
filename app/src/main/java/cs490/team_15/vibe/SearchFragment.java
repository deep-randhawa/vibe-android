package cs490.team_15.vibe;


import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import cs490.team_15.vibe.API.models.Request;
import cs490.team_15.vibe.API.models.SearchResult;


/**
 * Created by Austin Dewey on 12/5/2016.
 */

public class SearchFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ArrayAdapter<SearchResult> mSongArrayAdapter;
    Request request;

    public SearchFragment() {}

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mSongArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        setListAdapter(this.mSongArrayAdapter);
        getListView().setOnItemClickListener(this);
        //Call function to populate with songs

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
