package cs490.team_15.vibe;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import cs490.team_15.vibe.API.RequestAPI;
import cs490.team_15.vibe.API.models.Request;
import cs490.team_15.vibe.API.models.User;

/**
 * Created by Austin Dewey on 11/29/2016.
 */

public class RequestFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ArrayAdapter<Request> mRequestArrayAdapter;
    static RequestFragment mCurrentInstance;

    public RequestFragment() {
    }

    public static RequestFragment getInstance() {
        if (mCurrentInstance == null)
            mCurrentInstance = new RequestFragment();
        return mCurrentInstance;

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


    public void onLoggedIn() {
        try {
            // TODO: 11/29/16 change 1 to current userID
            RequestAPI.getAllRequests(1, this.mRequestArrayAdapter);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
