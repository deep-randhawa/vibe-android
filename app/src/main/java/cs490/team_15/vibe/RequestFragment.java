package cs490.team_15.vibe;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import cs490.team_15.vibe.API.models.User;

/**
 * Created by Austin Dewey on 11/29/2016.
 */

public class RequestFragment extends ListFragment implements AdapterView.OnItemClickListener{

    public RequestFragment() {
    }

    public static RequestFragment newInstance() {
        RequestFragment fragment = new RequestFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        User[] arr = {};
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(getContext(), android.R.layout.simple_list_item_1, arr);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
