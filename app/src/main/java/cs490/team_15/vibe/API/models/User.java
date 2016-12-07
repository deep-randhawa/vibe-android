package cs490.team_15.vibe.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deep on 11/17/16.
 */

public class User {

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("spotify_id")
    @Expose
    public String spotifyID;

    @SerializedName("email")
    @Expose
    public String email;

    public User(Integer id, String name, String spotifyID, String email) {
        this.id = id;
        this.name = name;
        this.spotifyID = spotifyID;
        this.email = email;
    }

    public User(String name, String spotifyID, String email) {
        this(null, name, spotifyID, email);
    }

    @Override
    public String toString() {
        return "Name: " + name;
    }
}
