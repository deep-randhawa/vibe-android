package cs490.team_15.vibe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cs490.team_15.vibe.API.models.Request;

/**
 * Created by Austin Dewey on 12/8/2016.
 */

public class SearchAdapter extends BaseAdapter {

    ArrayList<Request> requests;
    Context context;
    private static LayoutInflater inflater = null;

    public SearchAdapter(Context context, ArrayList<Request> requests) {
        this.context = context;
        this.requests = requests;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder {
        TextView songName;
        TextView artistName;
        TextView albumName;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.search_row_layout, null);
        holder.songName = (TextView) rowView.findViewById(R.id.songNameTextView);
        holder.artistName = (TextView) rowView.findViewById(R.id.artistNameTextView);
        holder.albumName = (TextView) rowView.findViewById(R.id.albumNameTextView);
        holder.songName.setText(requests.get(i).songName);
        holder.artistName.setText(requests.get(i).artistName);
        holder.albumName.setText(requests.get(i).albumName);
        return rowView;
    }
}
