package cs490.team_15.vibe.models;

/**
 * Created by drandhaw on 11/4/16.
 */

public class User {
    String FirstName;
    String LastName;
    String SpotifyID;
    UserType Type;
}

class Users {

    public static User[] getAllUsers() {
        // TODO
        return null;
    }

    public static User[] getAllDJs() {
        // TODO
        return null;
    }

    public static User getUserWithId(String SpotifyId) {
        // TODO
        return null;
    }
}

enum UserType {
    DJ, PARTYER
}
