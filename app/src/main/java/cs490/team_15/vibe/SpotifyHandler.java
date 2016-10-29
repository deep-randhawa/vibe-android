package cs490.team_15.vibe;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Austin Dewey on 10/28/2016.
 */

public class SpotifyHandler extends AsyncTask<String, String, String> {

    /*
        Strings[0] - Indicates which functionality to perform
        Strings[1] - The accessToken to use to get information from the server
     */
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
        DJLoginActivity.asyncGetPlaylists(s);
        return null;
    }
}
