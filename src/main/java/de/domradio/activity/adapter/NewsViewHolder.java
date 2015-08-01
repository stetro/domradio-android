package de.domradio.activity.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.domradio.R;
import de.domradio.domain.News;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    public TextView date;
    public TextView title;
    public TextView description;

    public NewsViewHolder(View itemView) {
        super(itemView);
        date = (TextView) itemView.findViewById(R.id.news_list_item_date);
        title = (TextView) itemView.findViewById(R.id.news_list_item_title);
        description = (TextView) itemView.findViewById(R.id.news_list_item_description);
    }

    public void setNews(News news) {
        title.setText(news.getTitle());
        description.setText(news.getDescription());
        date.setText(news.getDate());
    }
}
