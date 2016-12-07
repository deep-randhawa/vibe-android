package cs490.team_15.vibe;

import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import cs490.team_15.vibe.API.UserAPI;
import cs490.team_15.vibe.API.models.User;

/**
 * Created by Austin Dewey on 11/29/2016.
 */

public class DjFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ArrayAdapter<User> mUserArrayAdapter;
    static DjFragment mCurrentInstance;

    User DJ;
    int djID;
    String djName;

    public DjFragment() {
    }

    public static DjFragment getInstance() {
        if (mCurrentInstance == null)
            mCurrentInstance = new DjFragment();
        return mCurrentInstance;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dj, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mUserArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        setListAdapter(this.mUserArrayAdapter);
        getListView().setOnItemClickListener(this);

        try {
            UserAPI.getAllUsers(this.mUserArrayAdapter);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    View updatedview = null;

    @Override
    public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.setSelection(i);
        DJ = (User)adapterView.getItemAtPosition(i);
        djID = DJ.id;
        djName = DJ.name;

        if (updatedview != null) {
            updatedview.setBackgroundColor(Color.TRANSPARENT);
        }
        updatedview = view;
        view.setBackgroundColor(Color.GRAY);
        Toast.makeText(getContext(), "Connected to DJ " + djName, Toast.LENGTH_SHORT).show();

        getActivity().setTitle("Connected to DJ " + djName);

        //System.out.println("DJ id: " + DJ.id);
    }
}
