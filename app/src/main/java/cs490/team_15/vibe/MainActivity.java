package cs490.team_15.vibe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    String[] items = new String[] {"Elizabeth Nichols", "Sarah Baker", "Johnny Arnold", "Steven Bryant"};
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView selectedDJ = (TextView)findViewById(R.id.selectedDJText);

        Button connectButton = (Button)findViewById(R.id.connectButton);
        connectButton.setTransformationMethod(null);

        Button djLoginButton = (Button) findViewById(R.id.djLoginButton);
        djLoginButton.setTransformationMethod(null);

        djLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, DJLoginActivity.class);
            startActivity(intent);
            }
        });


        listView=(ListView)findViewById(R.id.listView);
        editText=(EditText)findViewById(R.id.txtSearch);

        initList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clicked = parent.getItemAtPosition(position).toString();
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                selectedDJ.setText(clicked);

            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    // reset listview
                    initList();
               }
                else {
                    //perform search
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void searchItem(String text) {
        for (String item:items) {
            if (!item.contains(text)) {
                listItems.remove(item);
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void initList() {
        listItems = new ArrayList<>(Arrays.asList(items));
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, listItems);
        listView.setAdapter(adapter);
    }
}
