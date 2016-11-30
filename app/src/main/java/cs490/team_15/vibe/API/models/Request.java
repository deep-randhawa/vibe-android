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
    public Integer songID;

    public Request(Integer id, Integer userID, Integer songID) {
        this.id = id;
        this.userID = userID;
        this.songID = songID;
    }

    public Request(Integer userID, Integer songID) {
        this(null, userID, songID);
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", userID=" + userID +
                ", songID=" + songID +
                '}';
    }
}
