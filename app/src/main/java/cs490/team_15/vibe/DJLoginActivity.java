package cs490.team_15.vibe;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

public class DJLoginActivity extends ListActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

    private static final String CLIENT_ID = "ff502d57cc2a464fbece5c9511763cea";
    private static final String REDIRECT_URI = "localhost://callback";
    private static final int REQUEST_CODE = 1337;

    public static final int PLAYLIST = 0;
    public static final int SONG = 1;

    private int page = PLAYLIST;

    private Player mPlayer;
    private String accessToken;
    private String ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_djlogin);
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[] {"user-read-private", "streaming"}); // Will need to change this scope to collaborative
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        Button logout = (Button) findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.logout();
            }
        });
        Button playback = (Button) findViewById(R.id.playbackBtn);
        playback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer.getPlaybackState().isPlaying) {
                    System.out.println(mPlayer.getMetadata().contextUri);
                    mPlayer.pause(null);
                }
                else {
                    mPlayer.playUri(null, mPlayer.getMetadata().contextUri, 0, (int)mPlayer.getPlaybackState().positionMs);
                }
            }
        });
    }

    private class OwnerIdTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String s = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("https://api.spotify.com/v1/me");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + strings[0]);
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    s += Character.toString((char)data);
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
            getOwnerId(result);
        }
    }

    public void getOwnerId(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String id = json.getString("id");
            this.ownerId = id;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            // Get oauth access token for DJ functions
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            this.accessToken = response.getAccessToken();
            new OwnerIdTask().execute(this.accessToken);

            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(DJLoginActivity.this);
                        mPlayer.addNotificationCallback(DJLoginActivity.this);

                        printAccessToken(); // For debugging purposes
                        getPlaylists();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
        //mPlayer.playUri(null, "spotify:artist:5K4W6rqBFWDnAN6FQUkS6x", 0, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(int i) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    public void printAccessToken() {
        System.out.println("Access token: " + this.accessToken);
    }

    // Use this.accessToken to get user information
    public void getPlaylists() {
        SpotifyHandler2 spotifyHandler = new SpotifyHandler2();
        spotifyHandler.execute("", this.accessToken);
    }

    private class SpotifyHandler2 extends AsyncTask<String, Void, String> {
        // https://vibe-server.herokuapp.com
        @Override
        protected String doInBackground(String... strings) {
            String s = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("https://api.spotify.com/v1/me/playlists");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + strings[1]);
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    s += Character.toString((char)data);
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
            asyncGetPlaylists(result);
        }
    }

    // Use this to return the playlists from the SpotifyHandler
    public void asyncGetPlaylists(String s) {
        this.page = PLAYLIST;
        try {
            JSONObject json = new JSONObject(s);
            int total = json.getInt("total");
            String[] arr = new String[total];
            Playlist[] pArr = new Playlist[total];
            for (int i = 0; i < total; i++) {
                String name = json.getJSONArray("items").getJSONObject(i).getString("name");
                String id = json.getJSONArray("items").getJSONObject(i).getString("id");
                String img = json.getJSONArray("items").getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url");
                arr[i] = name;
                pArr[i] = new Playlist(name, id, img);
            }
            setListAdapter(new MobileArrayAdapter(this, pArr, PLAYLIST));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Playlist p = (Playlist)getListAdapter().getItem(position);
        if (this.page == PLAYLIST) {
            System.out.println(p.getName());
            System.out.println(p.getId());
            System.out.println(p.getImg());
            new GetSongsTask().execute(this.ownerId, p.getId(), this.accessToken);
        }
        else if (this.page == SONG) {
            System.out.println(p.getName());
            System.out.println(p.getId());
            mPlayer.playUri(null, p.getId(), 0, 0);
        }
    }

    private class GetSongsTask extends AsyncTask<String, Void, String> {
        // https://api.spotify.com/v1/users/{user_id}/playlists/{playlist_id}/tracks
        @Override
        protected String doInBackground(String... strings) {
            String s = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String urlS = "https://api.spotify.com/v1/users/" + strings[0] + "/playlists/" + strings[1] + "/tracks";
                url = new URL(urlS);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + strings[2]);
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                // This while loop is really, really slow
                while (data != -1) {
                    s += Character.toString((char)data);
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
            getSongs(result);
        }
    }

    public void getSongs(String result) {
        this.page = SONG;
        try {
            JSONObject json = new JSONObject(result);
            int total = json.getInt("total");
            String[] arr = new String[total];
            Playlist[] pArr = new Playlist[total];
            for (int i = 0; i < total; i++) {
                String name = json.getJSONArray("items").getJSONObject(i).getJSONObject("track").getString("name");
                String id = json.getJSONArray("items").getJSONObject(i).getJSONObject("track").getString("uri");
                arr[i] = name;
                pArr[i] = new Playlist(name, id, null);
            }
            for (int i = 0; i < total; i++) {
                System.out.println(arr[i]);
            }
            setListAdapter(new MobileArrayAdapter(this, pArr, SONG));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // TODO: Try to change activities and see if the music will still play
}
