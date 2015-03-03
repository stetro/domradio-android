package de.domradio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.domradio.R;
import de.domradio.domain.News;

public class NewsListAdapter extends ArrayAdapter<News> {

    private final ArrayList<News> news;
    private final LayoutInflater inflater;

    public NewsListAdapter(Context context, int resource, ArrayList<News> news) {
        super(context, resource, news);
        this.news = news;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.news_list_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) vi.findViewById(R.id.news_list_item_title);
            holder.date = (TextView) vi.findViewById(R.id.news_list_item_date);
            holder.description = (TextView) vi.findViewById(R.id.news_list_item_description);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        News item = getItem(position);
        holder.title.setText(item.getTitle());
        holder.date.setText(vi.getContext().getString(R.string.from) + " " + item.getDate());
        holder.description.setText(item.getDescription());
        return vi;
    }

    private static class ViewHolder {
        public TextView date;
        public TextView title;
        public TextView description;
    }
}
