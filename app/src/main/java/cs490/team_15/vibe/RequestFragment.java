package cs490.team_15.vibe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cs490.team_15.vibe.API.RequestAPI;
import cs490.team_15.vibe.API.models.Playlist;
import cs490.team_15.vibe.API.models.Request;

/**
 * Created by Austin Dewey on 11/29/2016.
 */

public class RequestFragment extends ListFragment implements AdapterView.OnItemClickListener {

    static ArrayAdapter<Request> mRequestArrayAdapter;
    static RequestFragment mCurrentInstance;

    private View hiddenPanel;
    private ListView playlistLV;

    ArrayList<Playlist> playlists = new ArrayList<Playlist>();

    public RequestFragment() {
    }

    public static RequestFragment getInstance() {
        if (mCurrentInstance == null)
            mCurrentInstance = new RequestFragment();
        return mCurrentInstance;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestAPI.getAllRequests(MainActivity.getCurrentUser(), this.mRequestArrayAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        hiddenPanel = view.findViewById(R.id.hidden_panel);
        Button temp = (Button) view.findViewById(R.id.cancelButton);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUpDown();
            }
        });
        playlistLV = (ListView) view.findViewById(R.id.playlistListView);
        playlists.add(new Playlist("asdf", "url", "id"));
        playlistLV.setAdapter(new PlaylistAdapter(getContext(), playlists));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mRequestArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        setListAdapter(this.mRequestArrayAdapter);
        getListView().setOnItemClickListener(this);
        //slideUpDown();  // TODO: Delete this after playlist implementation
    }

    public void onLoggedIn(int id) {
        try {
            RequestAPI.getAllRequests(MainActivity.getCurrentUser(), this.mRequestArrayAdapter);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //slideUpDown();
        if (MainActivity.isLoggedIn()) {    // Is a DJ - Add song to playlist
            slideUpDown();
        }
        else {                              // Is a partier
            adapterView.setSelection(i);
            Request r = (Request)adapterView.getItemAtPosition(i);
            Request newR = new Request(r.userID, r.songID, r.numVotes, r.songName, r.artistName, r.albumName);
            try {
                RequestAPI.voteOnSong(r.userID, r.songID, getContext());
                // RequestAPI.createNewRequest(newR, getContext());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public void slideUpDown() {
        if (!isPanelShown()) {
            new GetPlaylistsTask().execute(MainActivity.getAccessToken());

            Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
        }
        else {
            Animation bottomDown = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }

    private class GetPlaylistsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String s = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("https://api.spotify.com/v1/me/playlists");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + strings[0]);
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
            playlists.clear();
            try {
                JSONObject json = new JSONObject(result);
                int total = json.getInt("total");
                for (int i = 0; i < total; i++) {
                    String name = json.getJSONArray("items").getJSONObject(i).getString("name");
                    String id = json.getJSONArray("items").getJSONObject(i).getString("id");
                    String url = json.getJSONArray("items").getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url");
                    playlists.add(new Playlist(name, url, id));
                }
                ((PlaylistAdapter)playlistLV.getAdapter()).notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
