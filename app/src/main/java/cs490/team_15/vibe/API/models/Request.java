package cs490.team_15.vibe.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deep on 11/24/16.
 */
public class Request {
    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("user_id")
    @Expose
    public Integer userID;

    @SerializedName("song_id")
    @Expose
    public String songID;

    @SerializedName("num_votes")
    @Expose
    public Integer numVotes;

    @SerializedName("song_name")
    @Expose
    public String songName;

    @SerializedName("artist_name")
    @Expose
    public String artistName;

    @SerializedName("album_name")
    @Expose
    public String albumName;

    public Request(Integer id, Integer userID, String songID, Integer numVotes,
                   String songName, String artistName, String albumName) {
        this.id = id;
        this.userID = userID;
        this.songID = songID;
        this.numVotes = numVotes;
        this.songName = songName;
        this.artistName = artistName;
        this.albumName = albumName;
    }

    public Request(Integer userID, String songID, Integer numVotes,
                   String songName, String artistName, String albumName) {
        this(null, userID, songID, numVotes, songName, artistName, albumName);
    }

    public String getResult() {
        return "Song: " + songName + "\n" +
                "Artist: " + artistName + "\n" +
                "Album: " + albumName;
    }

    @Override
    public String toString() {
        return numVotes +
                " - " + songName +
                " - " + artistName +
                " - " + albumName;
    }
}
