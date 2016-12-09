package cs490.team_15.vibe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cs490.team_15.vibe.API.models.Request;

/**
 * Created by Austin Dewey on 12/8/2016.
 */

public class RequestAdapter extends ArrayAdapter<Request> {

    List<Request> requests;
    private static LayoutInflater inflater = null;

    public RequestAdapter(Context context, int resource, List<Request> requests) {
        super(context, resource, requests);
        this.requests = requests;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        TextView votes;
        TextView songName;
        TextView artistName;
        TextView albumName;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView = view;
        if (rowView == null)
            rowView = inflater.inflate(R.layout.request_row_layout, null);
        holder.votes = (TextView) rowView.findViewById(R.id.requestVotesTextView);
        holder.songName = (TextView) rowView.findViewById(R.id.requestSongNameTextView);
        holder.artistName = (TextView) rowView.findViewById(R.id.requestArtistNameTextView);
        holder.albumName = (TextView) rowView.findViewById(R.id.requestAlbumNameTextView);

        holder.votes.setText(Integer.toString(requests.get(i).numVotes));
        holder.songName.setText(requests.get(i).songName);
        holder.artistName.setText(requests.get(i).artistName);
        holder.albumName.setText(requests.get(i).albumName);
        return rowView;
    }
}
