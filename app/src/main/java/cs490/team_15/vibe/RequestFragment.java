package cs490.team_15.vibe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import cs490.team_15.vibe.API.RequestAPI;
import cs490.team_15.vibe.API.models.Request;

/**
 * Created by Austin Dewey on 11/29/2016.
 */

public class RequestFragment extends ListFragment implements AdapterView.OnItemClickListener {

    static ArrayAdapter<Request> mRequestArrayAdapter;
    static RequestFragment mCurrentInstance;

    private View hiddenPanel;

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
        RequestAPI.getAllRequests(MainActivity.getCurrentUser(), this.mRequestArrayAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        hiddenPanel = view.findViewById(R.id.hidden_panel);
        Button temp = (Button) view.findViewById(R.id.cancelButton);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUpDown();
            }
        });
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
        slideUpDown();
        if (MainActivity.isLoggedIn()) {    // Is a DJ - Add song to playlist
            slideUpDown();
        }
        else {                              // Is a partier
            adapterView.setSelection(i);
            Request r = (Request)adapterView.getItemAtPosition(i);
            Request newR = new Request(r.userID, r.songID, r.numVotes, r.songName, r.artistName, r.albumName);
            try {
                RequestAPI.voteOnSong(r.userID, r.songID, getContext());
                // RequestAPI.createNewRequest(newR, getContext());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public void slideUpDown() {
        if (!isPanelShown()) {
            Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
        }
        else {
            Animation bottomDown = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }
}
