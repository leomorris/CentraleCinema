package lmorris.centralecinema;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class LoadingDataActivity extends AppCompatActivity {

    private static final String TAG = "LoadingDataActivity";

    private ProgressDialog dialog;

    private static String url = "http://voyage3.corellis.eu/filmsSeances.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_data);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Veuillez patienter...");
        dialog.setCancelable(false);
        dialog.show();

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(LoadingDataActivity.this);

        // Request a JSONArray from the URL
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, response.toString());
                        startFilmsListActivity(response);
                        dialog.dismiss();
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "JsonArrayRequest failed");
                dialog.dismiss();
            }
        });

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }

    private void startFilmsListActivity(JSONArray response){
        Intent intent = new Intent(LoadingDataActivity.this, FilmsListActivity.class);
        intent.putExtra("JSON_ARRAY", response.toString());
        startActivity(intent);
    }
}
