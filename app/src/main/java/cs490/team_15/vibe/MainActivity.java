package cs490.team_15.vibe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Replace this all with Jake's implementation */
        setContentView(R.layout.activity_djlogin);
        Intent intent = new Intent(MainActivity.this, DJLoginActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
