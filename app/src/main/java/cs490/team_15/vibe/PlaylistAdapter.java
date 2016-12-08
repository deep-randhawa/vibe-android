package cs490.team_15.vibe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import cs490.team_15.vibe.API.models.Playlist;

/**
 * Created by Austin Dewey on 12/7/2016.
 */

public class PlaylistAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    ArrayList<Playlist> playlists;

    public PlaylistAdapter(Context context, ArrayList<Playlist> playlists) {
        this.playlists = playlists;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return playlists.size();
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
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.playlist_row_layout, null);
        holder.tv = (TextView) rowView.findViewById(R.id.playlistTextView);
        holder.img = (ImageView) rowView.findViewById(R.id.playlistImageView);
        holder.tv.setText(playlists.get(i).getName());
        //holder.img.setImageResource(R.drawable.test_image);
        Bitmap bm = null;
        try {
            bm = new GetImageTask().execute(playlists.get(i).getUrl()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (bm != null) {
            holder.img.setImageBitmap(bm);
        }
        else {
            holder.img.setImageResource(R.drawable.test_image);
        }
        /*try {
            URL url = new URL(playlists.get(i).getUrl());
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.img.setImageBitmap(image);
        } catch (IOException e) {
            System.out.println(e);
        }*/
        return rowView;
    }

    public static class GetImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return image;
            } catch (IOException e) {
                System.out.println(e);
            }
            return null;
        }
    }
}
