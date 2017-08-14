package lmorris.centralecinema;

import android.app.ProgressDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_data);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Veuillez patienter...");
        dialog.setCancelable(false);
        dialog.show();

        String url = "http://voyage3.corellis.eu/filmsSeances.json";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, response.toString());
                        dialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "JsonArrayRequest failed");
                dialog.hide();
            }
        });

        queue.add(jsonArrayRequest);
    }
}
