package cs490.team_15.vibe.API.models;

/**
 * Created by Austin Dewey on 12/8/2016.
 */

public class Playlist {
    private String name;
    private String url;
    private String id;

    public Playlist(String name, String url, String id) {
        this.name = name;
        this.url = url;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }
}
