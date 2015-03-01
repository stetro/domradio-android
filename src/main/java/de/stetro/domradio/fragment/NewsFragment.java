package de.stetro.domradio.fragment;


import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import java.util.ArrayList;
import java.util.List;

import de.stetro.domradio.R;
import de.stetro.domradio.adapter.NewsListAdapter;
import de.stetro.domradio.domain.News;

public class NewsFragment extends ListFragment implements AdapterView.OnItemClickListener, Callback, SwipeRefreshLayout.OnRefreshListener {

    private static final String FEED_URL = "http://www.domradio.de/rss-feeds/domradio-rss.xml";
    private ArrayList<News> news = new ArrayList<>();
    private NewsListAdapter newsListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void loadNewsFeed() {
        PkRSS.with(getActivity()).load(FEED_URL).callback(this).async();
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_fragment_swiper);
        swipeRefreshLayout.setOnRefreshListener(this);
        loadNewsFeed();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsListAdapter = new NewsListAdapter(getActivity(), R.layout.news_list_item, news);
        getListView().setAdapter(newsListAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.news_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_activity_menu_refresh) {
            loadNewsFeed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void OnPreLoad() {
        Log.d("NewsFragment", "Preloaded RSS ...");
    }

    @Override
    public void OnLoaded(final List<Article> articles) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                news.clear();
                for (Article a : articles) {
                    News n = new News(a.getTitle(), a.getDescription(), a.getDate(), a.getSource());
                    news.add(n);
                }
                swipeRefreshLayout.setRefreshing(false);
                newsListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void OnLoadFailed() {
        Toast.makeText(getActivity(), R.string.error_feed, Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        loadNewsFeed();
    }
}
