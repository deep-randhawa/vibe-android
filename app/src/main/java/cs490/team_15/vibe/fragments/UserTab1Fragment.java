package cs490.team_15.vibe.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import cs490.team_15.vibe.R;

public class UserTab1Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public UserTab1Fragment() {
    }

    public static UserTab1Fragment newInstance() {
        UserTab1Fragment fragment = new UserTab1Fragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_user_tab1, container, false);

        Button song1Btn = (Button) rootView.findViewById(R.id.song1Btn);
        song1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SetSongsTask().execute("Song 1");
            }
        });

        Button song2Btn = (Button) rootView.findViewById(R.id.song2Btn);
        song2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SetSongsTask().execute("Song 2");
            }
        });

        Button song3Btn = (Button) rootView.findViewById(R.id.song3Btn);
        song3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SetSongsTask().execute("Song 3");
            }
        });

        return rootView;
    }

    private class SetSongsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String s = "";
            HttpURLConnection httpsURLConnection = null;
            try {
                String song = URLEncoder.encode(strings[0], "UTF-8");
                URL url = new URL("http://10.186.53.252/vibe/serviceSetSongs.php?name=" + song);
                URLConnection connection = url.openConnection();
                httpsURLConnection = (HttpURLConnection) connection;
                int responseCode = httpsURLConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d("MyApp", "Song successfully added");
                }
                else {
                    Log.w("MyApp", "Song could not be added");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                }
            }
            return s;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
