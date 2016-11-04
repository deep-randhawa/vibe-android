package cs490.team_15.vibe.models;

/**
 * Created by drandhaw on 11/4/16.
 */

public class Globals {

    public final static String BASE_URL = "http://vibe-server.herokuapp.com/";
    public final static String USER_API = "/user";

    public static String getUserApiPath() {
        return BASE_URL + USER_API;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

}
