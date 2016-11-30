package cs490.team_15.vibe.API;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import cs490.team_15.vibe.API.models.Request;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deep on 11/24/16.
 */
public class RequestAPI {

    public static void getAllRequests(Integer userID, final ArrayAdapter<Request> requestArrayAdapter) {
        Call<List<Request>> call_requests = Globals.requestAPI.getAllRequests(userID);
        call_requests.enqueue(new VibeCallback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                requestArrayAdapter.clear();
                requestArrayAdapter.addAll(response.body());
            }
        });
    }

    public static void createNewRequest(Request request, final Activity currentActivity) throws Throwable {
        Call<Request> call_request = Globals.requestAPI.createNewRequest(request);
        call_request.enqueue(new VibeCallback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                Toast.makeText(currentActivity.getApplicationContext(), "Created new Request", Toast.LENGTH_SHORT);
            }
        });
    }

    public static void deleteRequests(Integer userID, final Activity currentActivity) throws Throwable {
        Call<String> call_str = Globals.requestAPI.deleteRequests(userID);
        call_str.enqueue(new VibeCallback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(currentActivity.getApplicationContext(), "Deleted all requests for specified user", Toast.LENGTH_SHORT);
            }
        });
    }
}
