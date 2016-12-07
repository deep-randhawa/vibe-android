package cs490.team_15.vibe.API;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import cs490.team_15.vibe.API.models.User;
import cs490.team_15.vibe.MainActivity;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deep on 11/17/16.
 */

public class UserAPI {

    public static User generateRandomUser() {
        return new User(UUID.randomUUID().toString().substring(0, 4),
                UUID.randomUUID().toString().substring(0, 4),
                UUID.randomUUID().toString().substring(0, 4));
    }

    public static User generateLoggedInUser(String accessToken) {
        try {
            User u = new GenerateLoggedInUserTask().execute(accessToken).get();
            return u;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void getAllUsers(final ArrayAdapter<User> userArrayAdapter) throws Throwable {
        Call<List<User>> call_users = Globals.userAPI.getAllUsers();
        call_users.enqueue(new VibeCallback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                userArrayAdapter.clear();
                userArrayAdapter.addAll(response.body());
                userArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public static void createNewUser(User user, final Context currentActivityContext) throws Throwable {
        Call<User> call_user = Globals.userAPI.createNewUser(user);
        call_user.enqueue(new VibeCallback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                MainActivity.setCurrentUser(response.body());
                Toast.makeText(currentActivityContext, "New User created", Toast.LENGTH_SHORT);
            }
        });
    }

    public static void deleteUser(User user, final Context currentActivityContext) throws Throwable {
        if (user == null)
            return;
        Call<String> call_string = Globals.userAPI.deleteUser(user.id);
        call_string.enqueue(new VibeCallback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                MainActivity.setCurrentUser(null);
                Toast.makeText(currentActivityContext, "Deleted User", Toast.LENGTH_SHORT);
            }
        });
    }

    private static class GenerateLoggedInUserTask extends AsyncTask<String, User, User> {

        @Override
        protected User doInBackground(String... strings) {
            String s = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("https://api.spotify.com/v1/me");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + strings[0]);
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    s += Character.toString((char) data);
                    data = isw.read();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            String firstName = "";
            String lastName = "";
            String spotifyID = "";
            String email = "";
            try {
                JSONObject json = new JSONObject(s);
                spotifyID = json.getString("id");
                // TODO: Change first and last name to just name in schema
                firstName = json.getString("display_name");
                lastName = "asdf";
                email = "asdf";
                // TODO: Get rid of email in schema
            } catch (JSONException e) {
                e.printStackTrace();
            }
            User u = new User(firstName, spotifyID, email);
            return u;
        }
    }
////    public static User getUser(Integer id) throws Throwable {
////        Call<User> call_user = Globals.userAPI.getUser(id);
////
////        VibeCallback<User> callback = new VibeCallback<User>() {
////            @Override
////            public void onResponse(Call<User> call, Response<User> response) {
////                futureUser.complete(response.body());
////            }
////        };
////
////        call_user.enqueue(callback);
////        futureUser.join();
////        if (!callback.didErrorOccur())
////            return futureUser.get();
////        throw callback.getErrorThrowable();
////    }
//
//    public static User createNewUser(User user) throws Throwable {
//        Call<User> call_user = Globals.userAPI.createNewUser(user);
//        final BlockingQueue<User> newUser = new ArrayBlockingQueue<User>(1);
//
//        VibeCallback<User> callback = new VibeCallback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                Log.d("NEW USER FROM CLASS", response.body().toString());
//                newUser.add(response.body());
//            }
//        };
//
//        call_user.enqueue(callback);
////        futureUser.join();
//        return null;
////        while (newUser.peek() == null)
////            continue;
////        if (!callback.didErrorOccur()) {
////            return newUser.peek();
////        } else
////            throw callback.getErrorThrowable();
//    }
//
////    public static String modifyUser(Integer id, User user) throws Throwable {
////        Call<String> call_response = Globals.userAPI.modifyUser(id, user);
////
////        VibeCallback<String> callback = new VibeCallback<String>() {
////            @Override
////            public void onResponse(Call<String> call, Response<String> response) {
////                futureResponse.complete(response.body());
////            }
////        };
////
////        call_response.enqueue(callback);
////        futureResponse.join();
////        if (!callback.didErrorOccur())
////            return futureResponse.get();
////        throw callback.getErrorThrowable();
////    }
////
////    public static String deleteUser(Integer id) throws Throwable {
////        Call<String> call_response = Globals.userAPI.deleteUser(id);
//
//        VibeCallback<String> callback = new VibeCallback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                futureResponse.complete(response.body());
//            }
//        };
//
//        call_response.enqueue(callback);
//        futureResponse.join();
//        if (!callback.didErrorOccur())
//            return futureResponse.get();
//        throw callback.getErrorThrowable();
//    }
}