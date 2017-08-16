package lmorris.centralecinema;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class DetailsAdapter extends BaseAdapter {
    private ArrayList data;
    private LayoutInflater layoutInflater;

    public DetailsAdapter(Context context, Map<String,String> detailsValues) {
        layoutInflater = LayoutInflater.from(context);
        data = new ArrayList();
        data.addAll(detailsValues.entrySet());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Map.Entry<String,String > getItem(int position) {
        return (Map.Entry) data.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.list_item,null);
        TextView label = (TextView) view.findViewById(R.id.label);
        TextView value = (TextView) view.findViewById(R.id.value);
        Map.Entry<String,String> item = getItem(position);
        label.setText(item.getKey());
        value.setText(item.getValue());

        return view;
    }
}
