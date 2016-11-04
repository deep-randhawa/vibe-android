package cs490.team_15.vibe.models;

/**
 * Created by Austin Dewey on 10/29/2016.
 */

public class Playlist {
    private String name;
    private String id;
    private String img;

    public Playlist(String name, String id, String img) {
        this.name = name;
        this.id = id;
        this.img = img;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
    }
}
