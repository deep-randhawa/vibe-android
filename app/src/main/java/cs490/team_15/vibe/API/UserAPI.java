package cs490.team_15.vibe.API;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import cs490.team_15.vibe.API.models.User;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deep on 11/17/16.
 */

public class UserAPI {

    public static void getAllUsers(final ArrayAdapter<User> userArrayAdapter) throws Throwable {
        Call<List<User>> call_users = Globals.userAPI.getAllUsers();
        call_users.enqueue(new VibeCallback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                userArrayAdapter.clear();
                userArrayAdapter.addAll(response.body());
            }
        });
    }

    public static void createNewUser(User user, final Activity currentActivity) throws Throwable {
        Call<User> call_user = Globals.userAPI.createNewUser(user);
        call_user.enqueue(new VibeCallback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(currentActivity.getApplicationContext(), "New User created", Toast.LENGTH_SHORT);
            }
        });
    }

    public static void deleteUser(Integer id, final Activity activity) throws Throwable {
        Call<String> call_string = Globals.userAPI.deleteUser(id);
        call_string.enqueue(new VibeCallback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(activity.getApplicationContext(), "Deleted User", Toast.LENGTH_SHORT);
            }
        });
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