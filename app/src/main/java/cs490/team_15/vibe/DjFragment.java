package cs490.team_15.vibe;

import android.graphics.Color;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cs490.team_15.vibe.API.RequestAPI;
import cs490.team_15.vibe.API.UserAPI;
import cs490.team_15.vibe.API.models.User;

/**
 * Created by Austin Dewey on 11/29/2016.
 */

public class DjFragment extends Fragment {

    ArrayAdapter<User> mUserArrayAdapter;
    static DjFragment mCurrentInstance;
    private ListView djLV;

    User DJ;
    int djID;
    String djName;
    Timer timer;

    public DjFragment() {
    }

    public static DjFragment getInstance() {
        if (mCurrentInstance == null)
            mCurrentInstance = new DjFragment();
        return mCurrentInstance;

    }

    View updatedview = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dj, container, false);
        djLV = (ListView) view.findViewById(R.id.djListView);
        ArrayList<User> u = new ArrayList<User>();
        mUserArrayAdapter = new DJAdapter(getContext(), R.layout.dj_row_layout, u);
        djLV.setAdapter(mUserArrayAdapter);
        djLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DJ = mUserArrayAdapter.getItem(i);
                MainActivity.setCurrentUser(DJ);
                djID = DJ.id;
                djName = DJ.name;

                if (updatedview != null) {
                    updatedview.setBackgroundColor(Color.TRANSPARENT);
                }
                updatedview = view;
                view.setBackgroundColor(Color.GRAY);
                Toast.makeText(getContext(), "Connected to " + djName, Toast.LENGTH_SHORT).show();
                getActivity().setTitle("Connected to " + djName);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            UserAPI.getAllUsers(this.mUserArrayAdapter);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        setTimerForAdvertise();

    }

    void setTimerForAdvertise() {
        timer = new Timer();
        TimerTask updateProfile = new CustomTimerTask();
        timer.scheduleAtFixedRate(updateProfile, 5000, 5000);
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
                            try {
                                UserAPI.getAllUsers(mUserArrayAdapter);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    });
                }
            }).start();

        }

    }
}
