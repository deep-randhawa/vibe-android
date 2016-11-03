package cs490.team_15.vibe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;

public class FindDJActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_dj);

        AutoCompleteTextView findDJTextView = (AutoCompleteTextView) findViewById(R.id.findDJTextView);
        String[] djArray = {"DJ 1", "DJ 2", "DJ 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, djArray);
        findDJTextView.setAdapter(adapter);
        findDJTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                System.out.println(adapterView.getItemAtPosition(i));
            }
        });

        Button connectBtn = (Button) findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindDJActivity.this, UserActivity.class);
                FindDJActivity.this.startActivity(intent);
            }
        });

        Button imADJBtn = (Button) findViewById(R.id.imADJBtn);
        imADJBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindDJActivity.this, DJTabbedActivity.class);
                FindDJActivity.this.startActivity(intent);
            }
        });
    }
}
