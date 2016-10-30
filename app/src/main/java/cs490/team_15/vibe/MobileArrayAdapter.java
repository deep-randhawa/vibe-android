package cs490.team_15.vibe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static java.util.Collections.copy;

/**
 * Created by Austin Dewey on 10/29/2016.
 */

public class MobileArrayAdapter extends ArrayAdapter<Playlist> {
    private final Context context;
    private final Playlist[] values;
    private final int type;

    public MobileArrayAdapter(Context context, Playlist[] values, int type) {
        super(context, R.layout.activity_djlogin, values);
        this.context = context;
        this.values = values;
        this.type = type;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        private String url;

        public DownloadImageTask(ImageView imageView, String url) {
            this.imageView = imageView;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            //String urlOfImage = urls[0];
            Bitmap image = null;
            try {
                InputStream is = new URL(this.url).openStream();
                image = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.playlist_row_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        textView.setText(values[position].getName());

        if (this.type == DJLoginActivity.PLAYLIST) {
            String img = values[position].getImg();
            new DownloadImageTask(imageView, img).execute();
        }
        else if (this.type == DJLoginActivity.SONG) {

        }

        return rowView;
    }
}
