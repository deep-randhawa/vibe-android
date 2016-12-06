package cs490.team_15.vibe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Austin Dewey on 12/5/2016.
 */

public class SearchFragment extends ListFragment {

    public SearchFragment() {}

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.searchText);

        SearchView sv = (SearchView) rootView.findViewById(R.id.search);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // TODO: Create async task to get Spotify search results
                new GetSearchResultsTask().execute(s);
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

        return rootView;
    }

    private static class GetSearchResultsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String temp = strings[0];
            String withoutSpaces = "";
            for (int i = 0; i < temp.length(); i++) {
                if (temp.charAt(i) == ' ') {
                    withoutSpaces += "%20";
                }
                else {
                    withoutSpaces += temp.charAt(i);
                }
            }

            String s = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("https://api.spotify.com/v1/search?q=" + withoutSpaces + "&type=track&limit=10");
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

        }
    }
}
