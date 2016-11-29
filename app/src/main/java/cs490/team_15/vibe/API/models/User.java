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

    @SerializedName("first_name")
    @Expose
    public String firstName;

    @SerializedName("last_name")
    @Expose
    public String lastName;

    @SerializedName("spotify_id")
    @Expose
    public String spotifyID;

    @SerializedName("email")
    @Expose
    public String email;

    public User(Integer id, String firstName, String lastName, String spotifyID, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.spotifyID = spotifyID;
        this.email = email;
    }

    public User(String firstName, String lastName, String spotifyID, String email) {
        this(null, firstName, lastName, spotifyID, email);
    }
}
