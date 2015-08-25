package de.domradio.activity.holder;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.domradio.R;
import de.domradio.activity.WebActivity;
import de.domradio.domain.News;
import de.domradio.service.AnalyticsTracker;

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView date;
    public final TextView title;
    public final TextView description;
    private final CardView cardView;
    private final Activity activity;
    private News news;

    public NewsViewHolder(View itemView, Activity activity) {
        super(itemView);
        this.activity = activity;
        date = (TextView) itemView.findViewById(R.id.news_list_item_date);
        title = (TextView) itemView.findViewById(R.id.news_list_item_title);
        description = (TextView) itemView.findViewById(R.id.news_list_item_description);
        cardView = (CardView) itemView.findViewById(R.id.event_list_item_layout);
        cardView.setOnClickListener(this);
    }

    public void setNews(News news) {
        this.news = news;
        title.setText(news.getTitle());
        description.setText(news.getDescription());
        date.setText(news.getDate());
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra("title", news.getTitle());
        intent.putExtra("link", news.getLink().toString());
        activity.startActivity(intent);
        AnalyticsTracker.openNews(activity.getApplication(), news);
    }
}
