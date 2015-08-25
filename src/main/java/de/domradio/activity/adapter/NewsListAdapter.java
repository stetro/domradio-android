package de.domradio.activity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.domradio.R;
import de.domradio.activity.holder.NewsViewHolder;
import de.domradio.domain.News;

public class NewsListAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private List<News> news = new ArrayList<>();
    private Activity activity;

    public NewsListAdapter(ArrayList<News> news, Activity activity) {
        this.news = news;
        this.activity = activity;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new NewsViewHolder(v, activity);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.setNews(news.get(position));

    }

    @Override
    public int getItemCount() {
        return news.size();
    }


}
