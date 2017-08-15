package lmorris.centralecinema;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilmsListActivity extends AppCompatActivity {

    private static final String TAG = "FilmsListActivity";

    private List<JSONObject> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_films_list);

        ListView listView = (ListView) findViewById(R.id.listView);

        String  stringExtra = getIntent().getStringExtra("JSON_ARRAY");
        loadDataFromString(stringExtra);
        Log.i(TAG, data.toString());

        listView.setAdapter(new FilmAdapter(this, data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                JSONObject film = (JSONObject) parent.getItemAtPosition(position);
                startFilmsDetailsActivity(film);
            }
        });
    }

    private void loadDataFromString(String stringExtra){
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(stringExtra);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                data.add(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void startFilmsDetailsActivity(JSONObject object){
        Intent intent = new Intent(FilmsListActivity.this, FilmsDetailsActivity.class);
        intent.putExtra("JSON_OBJECT", object.toString());
        startActivity(intent);
    }
}
