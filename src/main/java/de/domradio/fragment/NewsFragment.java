package de.domradio.fragment;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import java.util.ArrayList;
import java.util.List;

import de.domradio.R;
import de.domradio.adapter.NewsListAdapter;
import de.domradio.dialog.RssChooserDialog;
import de.domradio.domain.News;
import de.domradio.service.EventBusCallback;
import de.domradio.service.event.SetNewsFeedEvent;
import de.greenrobot.event.EventBus;

public class NewsFragment extends ListFragment implements AdapterView.OnItemClickListener, Callback, SwipeRefreshLayout.OnRefreshListener {

    private RssChooserDialog.FeedTopic currentFeed = RssChooserDialog.FeedTopic.ALL;
    private ArrayList<News> news = new ArrayList<>();
    private NewsListAdapter newsListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView title;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    private void loadNewsFeed() {
        setTitle();
        PkRSS.with(getActivity()).load(currentFeed.getUrl()).callback(this).async();
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_fragment_swiper);
        swipeRefreshLayout.setOnRefreshListener(this);
        title = (TextView) view.findViewById(R.id.news_list_title);
        setTitle();
        loadNewsFeed();
        setHasOptionsMenu(true);
        return view;
    }

    private void setTitle() {
        if (currentFeed.equals(RssChooserDialog.FeedTopic.ALL)) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(currentFeed.getTitle());
            title.setVisibility(View.VISIBLE);
        }
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
        if (item.getItemId() == R.id.main_activity_menu_choose) {
            new RssChooserDialog(getActivity()).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News n = news.get(position);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(n.getLink());
        startActivity(i);
    }

    @EventBusCallback
    public void onEvent(SetNewsFeedEvent e) {
        currentFeed = e.getFeedTopic();
        loadNewsFeed();

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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.error_feed, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadNewsFeed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
