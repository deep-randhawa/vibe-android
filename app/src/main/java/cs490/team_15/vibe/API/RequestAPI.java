package cs490.team_15.vibe.API;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import cs490.team_15.vibe.API.models.Request;
import cs490.team_15.vibe.API.models.User;
import cs490.team_15.vibe.MainActivity;
import cs490.team_15.vibe.RequestFragment;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deep on 11/24/16.
 */
public class RequestAPI {

    public static void getAllRequests(User user, final ArrayAdapter<Request> requestArrayAdapter) {
        if (user == null)
            return;
        if (requestArrayAdapter == null) {
            return;
        }
        Call<List<Request>> call_requests = Globals.requestAPI.getAllRequests(user.id);
        call_requests.enqueue(new VibeCallback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                requestArrayAdapter.clear();
                requestArrayAdapter.addAll(response.body() == null ? new ArrayList<Request>() : response.body());
                requestArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public static void createNewRequest(Request request, final Context currentActivityContext) throws Throwable {
        Call<Request> call_request = Globals.requestAPI.createNewRequest(request);
        call_request.enqueue(new VibeCallback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                Toast.makeText(currentActivityContext, "Request Sent!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void voteOnSong(Integer userID, String songID, final Context currentActivityContext) throws Throwable {
        Call<String> call_request = Globals.requestAPI.addVoteToSong(userID, songID);
        call_request.enqueue(new VibeCallback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(currentActivityContext, "Added vote!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void deleteRequests(User user, final Context currentActivityContext) throws Throwable {
        if (user == null)
            return;
        Call<String> call_str = Globals.requestAPI.deleteRequests(user.id);
        call_str.enqueue(new VibeCallback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(currentActivityContext, "Deleted all requests for specified user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // TODO: Delete a specific request
}
