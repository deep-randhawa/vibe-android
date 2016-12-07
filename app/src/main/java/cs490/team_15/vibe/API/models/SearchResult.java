package cs490.team_15.vibe.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Joseph on 12/5/16.
 */

public class SearchResult {
    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("song_name")
    @Expose
    public String song_name;

    @SerializedName("artist_name")
    @Expose
    public String artist_name;

    public String album_name;

    public SearchResult(String id, String songName, String artistName,
                        String albumName) {
        this.id = id;
        this.song_name = songName;
        this.artist_name = artistName;
        this.album_name = albumName;
    }

    @Override
    public String toString() {
        return "Song: " + song_name + "\n" +
                "Artist: " + artist_name + "\n" +
                "Album: " + album_name;
    }
}