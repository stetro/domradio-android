package de.domradio.ui.articlelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.domradio.R
import de.domradio.usecase.data.Article


class ArticleListAdapter : RecyclerView.Adapter<ArticleListViewHolder>() {

    var articles: MutableList<Article> = mutableListOf()
    lateinit var clickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        view.setOnClickListener(clickListener)
        return ArticleListViewHolder(view)
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ArticleListViewHolder, position: Int) {
        if (articles.size > position) {
            holder.bindData(articles[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.article_list_item
    }
}

class ArticleListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val title = view.findViewById<TextView>(R.id.article_list_item_title)
    private val date = view.findViewById<TextView>(R.id.article_list_item_date)
    private val text = view.findViewById<TextView>(R.id.article_list_item_text)

    fun bindData(rssItem: Article) {
        title.text = rssItem.title
        date.text = rssItem.date
        text.text = rssItem.description
    }
}
