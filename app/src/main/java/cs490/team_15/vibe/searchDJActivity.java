package cs490.team_15.vibe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class searchDJActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dj);

        View v = findViewById(R.id.searchBox);
        getSupportActionBar().setCustomView(v);
    }
}
