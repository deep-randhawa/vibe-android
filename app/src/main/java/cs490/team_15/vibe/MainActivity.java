package cs490.team_15.vibe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
