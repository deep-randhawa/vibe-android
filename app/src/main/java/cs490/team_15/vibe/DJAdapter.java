package cs490.team_15.vibe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cs490.team_15.vibe.API.models.User;

/**
 * Created by Austin Dewey on 12/9/2016.
 */

public class DJAdapter extends ArrayAdapter<User> {

    List<User> users;
    private static LayoutInflater inflater = null;

    public DJAdapter(Context context, int resource, List<User> users) {
        super(context, resource, users);
        this.users = users;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        TextView name;
        TextView spotifyID;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView = view;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.dj_row_layout, null);
        }
        holder.name = (TextView) rowView.findViewById(R.id.djNameTextView);
        holder.spotifyID = (TextView) rowView.findViewById(R.id.spotifyIDTextView);

        holder.name.setText(users.get(i).name);
        holder.spotifyID.setText(users.get(i).spotifyID);
        return rowView;
    }
}
