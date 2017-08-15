package lmorris.centralecinema;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static lmorris.centralecinema.R.id.affiche;

public class FilmAdapter extends BaseAdapter{
    private List<JSONObject> data;
    private LayoutInflater layoutInflater;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private class ViewHolder{
        NetworkImageView affiche;
        TextView titre;
        TextView genre;
        TextView categorie;
    }

    public FilmAdapter(Context context, List<JSONObject> data){
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(20);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.content_films_list, null);
            holder.affiche = (NetworkImageView) convertView.findViewById(affiche);
            holder.titre = (TextView) convertView.findViewById(R.id.titre);
            holder.genre = (TextView) convertView.findViewById(R.id.genre);
            holder.categorie = (TextView) convertView.findViewById(R.id.categorie);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject film = data.get(position);

        try {
            holder.titre.setText(film.getString("titre"));
            holder.genre.setText("Genre : " + film.getString("genre"));
            holder.categorie.setText("Catégorie : " + film.getString("categorie"));
            String url = film.getString("affiche").replace("{{width}}","200").replace("{{height}}","200");
            holder.affiche.setImageUrl(url, imageLoader);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
