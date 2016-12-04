package cs490.team_15.vibe;

import android.support.annotation.Nullable;
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

    public static RequestFragment getInstance() {
        if (mCurrentInstance == null)
            mCurrentInstance = new RequestFragment();
        return mCurrentInstance;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.getCurrentUser() != null)
            RequestAPI.getAllRequests(1, this.mRequestArrayAdapter);
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


//    @Override
//    public void onCreate() {
//        super.onResume();
//        if (MainActivity.getCurrentUser() != null)
//            RequestAPI.getAllRequests(1, this.mRequestArrayAdapter);
//    }

    public void onLoggedIn(int id) {
        try {
            RequestAPI.getAllRequests(id, this.mRequestArrayAdapter);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
