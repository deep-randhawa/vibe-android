package API;

import API.models.Request;
import API.models.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by deep on 11/17/16.
 */
public class Globals {
    private static final String BASE_URL = "http://localhost:9000/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public static final UserAPI userAPI = retrofit.create(UserAPI.class);
    public static final RequestAPI requestAPI = retrofit.create(RequestAPI.class);

    interface UserAPI {
        @GET("/user")
        Call<List<User>> getAllUsers();

        @GET("/user/{id}")
        Call<User> getUser(@Path("id") Integer id);

        @POST("/user")
        Call<User> createNewUser(@Body User user);

        @PUT("/user/{id}")
        Call<String> modifyUser(@Path("id") Integer id, @Body User user);

        @DELETE("/user/{id}")
        Call<String> deleteUser(@Path("id") Integer id);
    }

    interface RequestAPI {
        @GET("/request/{user_id}")
        Call<List<Request>> getAllRequests(@Path("user_id") Integer userID);

        @POST("/request")
        Call<Request> createNewRequest(@Body Request request);

        @DELETE("/request/{user_id}")
        Call<String> deleteRequests(@Path("user_id") Integer userID);
    }
}