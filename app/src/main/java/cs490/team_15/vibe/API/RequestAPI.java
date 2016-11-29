package cs490.team_15.vibe.API;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import cs490.team_15.vibe.API.models.Request;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deep on 11/24/16.
 */
public class RequestAPI {

    public synchronized static List<Request> getAllRequests(Integer userID) throws Throwable {
        Call<List<Request>> call_requests = Globals.requestAPI.getAllRequests(userID);
        CompletableFuture<List<Request>> futureRequests = new CompletableFuture<>();

        VibeCallback<List<Request>> callback = new VibeCallback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                futureRequests.complete(response.body());
            }
        };

        call_requests.enqueue(callback);
        futureRequests.join();
        if (!callback.didErrorOccur())
            return futureRequests.get();
        throw callback.getErrorThrowable();
    }

    public synchronized static Request createNewRequest(Request request) throws Throwable {
        Call<Request> call_request = Globals.requestAPI.createNewRequest(request);
        CompletableFuture<Request> futureRequest = new CompletableFuture<>();

        VibeCallback<Request> callback = new VibeCallback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                futureRequest.complete(response.body());
            }
        };

        call_request.enqueue(callback);
        futureRequest.join();
        if (!callback.didErrorOccur())
            return futureRequest.get();
        throw callback.getErrorThrowable();
    }

    public synchronized static String deleteRequests(Integer userID) throws Throwable {
        Call<String> call_str = Globals.requestAPI.deleteRequests(userID);
        CompletableFuture<String> futureStr = new CompletableFuture<>();

        VibeCallback<String> callback = new VibeCallback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                futureStr.complete(response.body());
            }
        };

        call_str.enqueue(callback);
        futureStr.join();
        if (!callback.didErrorOccur())
            return futureStr.get();
        throw callback.getErrorThrowable();
    }

}
