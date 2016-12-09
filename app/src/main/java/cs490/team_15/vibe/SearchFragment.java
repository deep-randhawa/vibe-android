package cs490.team_15.vibe;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import cs490.team_15.vibe.API.RequestAPI;
import cs490.team_15.vibe.API.models.Request;
import cs490.team_15.vibe.API.models.SearchResult;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Austin Dewey on 12/5/2016.
 */

public class SearchFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private static ProgressBar mProgressBar;
    private static View mView;

    static ArrayAdapter<Request> mSongArrayAdapter;
    Request request;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //TextView textView = (TextView) rootView.findViewById(R.id.searchText);

        SearchView sv = (SearchView) rootView.findViewById(R.id.search);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // TODO: Create async task to get Spotify search results
                new GetSearchResultsTask().execute(s);
                mProgressBar.setVisibility(View.VISIBLE);
                return false;
            }

            // Can use this to update in real-time
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        sv.setSubmitButtonEnabled(true);
        sv.setIconifiedByDefault(false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.INVISIBLE);

        mView = rootView;
        return rootView;
    }

    private static class GetSearchResultsTask extends AsyncTask<String, Void, String> {

        private final int LIMIT = 5;

        @Override
        protected String doInBackground(String... strings) {
            String temp = strings[0];
            String withoutSpaces = "";
            for (int i = 0; i < temp.length(); i++) {
                if (temp.charAt(i) == ' ') {
                    withoutSpaces += "%20";
                } else {
                    withoutSpaces += temp.charAt(i);
                }
            }

            String s = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("https://api.spotify.com/v1/search?q=" + withoutSpaces + "&type=track&limit=" + LIMIT);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    s += Character.toString((char) data);
                    data = isw.read();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return s;
        }

        @Override
        protected void onPostExecute(String result) {
            SearchFragment.mProgressBar.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    SearchFragment.mProgressBar.setVisibility(View.INVISIBLE);
                }
            });
            mSongArrayAdapter.clear();
            try {
                JSONObject json = new JSONObject(result);
                for (int i = 0; i < LIMIT; i++) {
                    String songID = json.getJSONObject("tracks").getJSONArray("items").getJSONObject(i).getString("id");
                    int numVotes = 0;
                    String songName = json.getJSONObject("tracks").getJSONArray("items").getJSONObject(i).getString("name");
                    String artistName = json.getJSONObject("tracks").getJSONArray("items").getJSONObject(i).getJSONArray("artists").getJSONObject(0).getString("name");
                    String albumName = json.getJSONObject("tracks").getJSONArray("items").getJSONObject(i).getJSONObject("album").getString("name");
                    mSongArrayAdapter.add(new Request(null, null, songID, numVotes, songName, artistName, albumName));
                    //mSongArrayAdapter.add(new SearchResult(id, name, artist, album));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mSongArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        setListAdapter(this.mSongArrayAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.setSelection(i);
        Request sr = (Request) adapterView.getItemAtPosition(i);
        if (MainActivity.getCurrentUser() == null) {
            System.out.println("NULL!!!");
        } else {
            try {
                sr.userID = MainActivity.getCurrentUser().id;
                RequestAPI.createNewRequest(sr, getContext());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
