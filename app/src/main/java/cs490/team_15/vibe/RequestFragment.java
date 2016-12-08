package cs490.team_15.vibe;

import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import cs490.team_15.vibe.API.RequestAPI;
import cs490.team_15.vibe.API.UserAPI;
import cs490.team_15.vibe.API.models.Request;

/**
 * Created by Austin Dewey on 11/29/2016.
 */

public class RequestFragment extends ListFragment implements AdapterView.OnItemClickListener {

    static ArrayAdapter<Request> mRequestArrayAdapter;
    static RequestFragment mCurrentInstance;
    static HashSet <String> songSet;
    Timer timer;

    public RequestFragment() {
    }

    public static RequestFragment getInstance() {
        if (mCurrentInstance == null)
            mCurrentInstance = new RequestFragment();
        return mCurrentInstance;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songSet = new HashSet<String>();
        RequestAPI.getAllRequests(MainActivity.getCurrentUser(), this.mRequestArrayAdapter);

        setTimerForAdvertise();
    }

    void setTimerForAdvertise() {
        timer = new Timer();
        TimerTask updateProfile = new CustomTimerTask();
        timer.scheduleAtFixedRate(updateProfile, 1000, 1000);
    }

    public class CustomTimerTask extends TimerTask
    {

        private Handler mHandler = new Handler();

        @Override
        public void run()
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            RequestAPI.getAllRequests(MainActivity.getCurrentUser(), mRequestArrayAdapter);
                        }
                    });
                }
            }).start();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mRequestArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        setListAdapter(this.mRequestArrayAdapter);
        getListView().setOnItemClickListener(this);
    }

    public void onLoggedIn(int id) {
        try {
            RequestAPI.getAllRequests(MainActivity.getCurrentUser(), this.mRequestArrayAdapter);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (MainActivity.isLoggedIn()) {    // Is a DJ

        }
        else {                              // Is a partier
            adapterView.setSelection(i);
            Request r = (Request)adapterView.getItemAtPosition(i);
            Request newR = new Request(r.userID, r.songID, r.numVotes, r.songName, r.artistName, r.albumName);
            try {
                if (songSet.contains(r.songID)) {
                    return;
                }
                RequestAPI.voteOnSong(r.userID, r.songID, getContext());

                songSet.add(r.songID);

                RequestAPI.getAllRequests(MainActivity.getCurrentUser(), this.mRequestArrayAdapter);
                // RequestAPI.createNewRequest(newR, getContext());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
