package cs490.team_15.vibe.API;

import java.util.List;

import cs490.team_15.vibe.API.models.User;
import retrofit2.Call;
import retrofit2.Response;

import java.util.concurrent.CompletableFuture;

/**
 * Created by deep on 11/17/16.
 */

public class UserAPI {

    public synchronized static List<User> getAllUsers() throws Throwable {
        Call<List<User>> call_users = Globals.userAPI.getAllUsers();
        CompletableFuture<List<User>> futureUsers = new CompletableFuture<>();

        VibeCallback<List<User>> callback = new VibeCallback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                futureUsers.complete(response.body());
            }
        };

        call_users.enqueue(callback);
        futureUsers.join();
        if (!callback.didErrorOccur())
            return futureUsers.get();
        throw callback.getErrorThrowable();
    }

    public synchronized static User getUser(Integer id) throws Throwable {
        Call<User> call_user = Globals.userAPI.getUser(id);
        CompletableFuture<User> futureUser = new CompletableFuture<>();

        VibeCallback<User> callback = new VibeCallback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                futureUser.complete(response.body());
            }
        };

        call_user.enqueue(callback);
        futureUser.join();
        if (!callback.didErrorOccur())
            return futureUser.get();
        throw callback.getErrorThrowable();
    }

    public synchronized static User createNewUser(User user) throws Throwable {
        Call<User> call_user = Globals.userAPI.createNewUser(user);
        CompletableFuture<User> futureUser = new CompletableFuture<>();

        VibeCallback<User> callback = new VibeCallback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                futureUser.complete(response.body());
            }
        };

        call_user.enqueue(callback);
        futureUser.join();
        if (!callback.didErrorOccur())
            return futureUser.get();
        throw callback.getErrorThrowable();
    }

    public synchronized static String modifyUser(Integer id, User user) throws Throwable {
        Call<String> call_response = Globals.userAPI.modifyUser(id, user);
        CompletableFuture<String> futureResponse = new CompletableFuture<>();

        VibeCallback<String> callback = new VibeCallback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                futureResponse.complete(response.body());
            }
        };

        call_response.enqueue(callback);
        futureResponse.join();
        if (!callback.didErrorOccur())
            return futureResponse.get();
        throw callback.getErrorThrowable();
    }

    public synchronized static String deleteUser(Integer id) throws Throwable {
        Call<String> call_response = Globals.userAPI.deleteUser(id);
        CompletableFuture<String> futureResponse = new CompletableFuture<>();

        VibeCallback<String> callback = new VibeCallback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                futureResponse.complete(response.body());
            }
        };

        call_response.enqueue(callback);
        futureResponse.join();
        if (!callback.didErrorOccur())
            return futureResponse.get();
        throw callback.getErrorThrowable();
    }
}