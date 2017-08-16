package lmorris.centralecinema;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class FilmsDetailsActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private JSONObject data;
    private Map<String,String> detailsValues = new LinkedHashMap<>();
    private Map<String,String> detailsLabels = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_films_details);

        String  stringExtra = getIntent().getStringExtra("JSON_OBJECT");
        try {
            data = new JSONObject(stringExtra);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initializeDetailsLabels();
        loadDetailsValues();

        ListView listView = (ListView) findViewById(R.id.detailsListView);
        listView.setAdapter(new DetailsAdapter(FilmsDetailsActivity.this, detailsValues));

        requestQueue = Volley.newRequestQueue(FilmsDetailsActivity.this);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(20);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        if(data.has("medias")){
            try {
                JSONArray photos = data.getJSONArray("medias");
                for (int i = 0; i < photos.length(); i++){
                    JSONObject photo = photos.getJSONObject(i);
                    insertPhoto(photo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeDetailsLabels(){
        detailsLabels.put("titre", "Titre");
        detailsLabels.put("titre_ori", "Titre original");
        detailsLabels.put("web", "Site internet");
        detailsLabels.put("duree", "Durée (min)");
        detailsLabels.put("distributeur", "Distributeur");
        detailsLabels.put("participants", "Acteurs");
        detailsLabels.put("realisateur", "Réalisateur");
        detailsLabels.put("synopsis", "Synopsis");
        detailsLabels.put("annee", "Année");
        detailsLabels.put("date_sortie", "Date de sortie");
        detailsLabels.put("genre", "Genre");
        detailsLabels.put("categorie", "Catégorie");
        detailsLabels.put("pays", "Pays");
    }

    private void loadDetailsValues(){
        for (String detail : detailsLabels.keySet()) {
            try {
                String detailValue = data.getString(detail).replaceAll("\\r\\n|\\r|\\n", " ");
                if (!"".equals(detailValue)) {
                    detailsValues.put(detailsLabels.get(detail), detailValue);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertPhoto(JSONObject photo) throws JSONException {
        final String url = photo.getString("path").replace("{{width}}","300").replace("{{height}}","300");
        NetworkImageView image = new NetworkImageView(FilmsDetailsActivity.this);
        LinearLayout gallery = (LinearLayout) findViewById(R.id.gallery);
        image.setImageUrl(url, imageLoader);
        image.setPadding(2,0,2,0);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPhotoActivity(url);
            }
        });
        gallery.addView(image);
    }

    private void startPhotoActivity(String url){
        Intent intent = new Intent(FilmsDetailsActivity.this, PhotoActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }
}
