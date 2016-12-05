package cs490.team_15.vibe.API;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

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
                UUID.randomUUID().toString().substring(0, 4),
                UUID.randomUUID().toString().substring(0, 4));
    }

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

    public static void createNewUser(User user, final Context currentActivityContext) throws Throwable {
        Call<User> call_user = Globals.userAPI.createNewUser(user);
        call_user.enqueue(new VibeCallback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User u = response.body();
                System.out.println("User id number is " + u.id);
                MainActivity.setCurrentUser(u);
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