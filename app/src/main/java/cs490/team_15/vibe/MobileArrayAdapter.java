package cs490.team_15.vibe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Austin Dewey on 10/29/2016.
 */

public class MobileArrayAdapter extends ArrayAdapter {
    private final Context context;
    private final String[] values;

    public MobileArrayAdapter(Context context, String[] values) {
        super(context, R.layout.activity_djlogin, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_djlogin, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        textView.setText(values[position]);

        String s = values[position];

        return null;
    }
}
