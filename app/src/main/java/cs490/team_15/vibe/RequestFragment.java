package cs490.team_15.vibe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
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

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cs490.team_15.vibe.API.RequestAPI;
import cs490.team_15.vibe.API.models.Playlist;
import cs490.team_15.vibe.API.models.Request;

/**
 * Created by Austin Dewey on 11/29/2016.
 */

public class RequestFragment extends ListFragment implements AdapterView.OnItemClickListener {

    static ArrayAdapter<Request> mRequestArrayAdapter;
    static RequestFragment mCurrentInstance;
    static HashSet <String> songSet;
    Timer timer;

    private View hiddenPanel;
    private ListView playlistLV;

    private Request selRequest;

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
        songSet = new HashSet<String>();
        RequestAPI.getAllRequests(MainActivity.getCurrentUser(), this.mRequestArrayAdapter);

        setTimerForAdvertise();
    }

    void setTimerForAdvertise() {
        timer = new Timer();
        TimerTask updateProfile = new CustomTimerTask();
        timer.scheduleAtFixedRate(updateProfile, 1000, 1000);
    }

    public class CustomTimerTask extends TimerTask
    {

        private Handler mHandler = new Handler();

        @Override
        public void run()
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            RequestAPI.getAllRequests(MainActivity.getCurrentUser(), mRequestArrayAdapter);
                        }
                    });
                }
            }).start();

        }

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
        playlistLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Playlist temp = playlists.get(i);
                // TODO: Add selected song to the selected playlist
                String uri = "spotify:track:" + selRequest.songID;
                String ownerID = MainActivity.getCurrentUser().spotifyID;
                String playlistID = temp.getId();
                /*System.out.println("uri: " + uri);
                System.out.println("ownerID: " + ownerID);
                System.out.println("playlistID: " + playlistID);*/
                new AddSongToPlaylistTask().execute(MainActivity.getAccessToken(), ownerID, playlistID, uri);
            }
        });
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
            adapterView.setSelection(i);
            selRequest = (Request)adapterView.getItemAtPosition(i);
            slideUpDown();
        }
        else {                              // Is a partier
            adapterView.setSelection(i);
            Request r = (Request)adapterView.getItemAtPosition(i);
            selRequest = r;
            Request newR = new Request(r.userID, r.songID, r.numVotes, r.songName, r.artistName, r.albumName);
            try {
                if (songSet.contains(r.songID)) {
                    return;
                }
                RequestAPI.voteOnSong(r.userID, r.songID, getContext());

                songSet.add(r.songID);

                RequestAPI.getAllRequests(MainActivity.getCurrentUser(), this.mRequestArrayAdapter);
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

    private class AddSongToPlaylistTask extends AsyncTask<String, Void, String> {

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }

        @Override
        protected String doInBackground(String... strings) {
            String s = "";
            URL url;
            HttpURLConnection urlConnection = null;
            System.out.println("Entering");
            try {
                String userID = strings[1];
                String playlistID = strings[2];
                String trackID = strings[3];
                url = new URL("https://api.spotify.com/v1/users/" + userID + "/playlists/" + playlistID + "/tracks?uris="+trackID);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + strings[0]);
                int statusCode = urlConnection.getResponseCode();
                System.out.println(statusCode);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return s;
        }
    }
}
