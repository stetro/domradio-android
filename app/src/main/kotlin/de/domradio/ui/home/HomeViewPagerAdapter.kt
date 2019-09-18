package de.domradio.ui.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import de.domradio.R
import de.domradio.ui.articlelist.ArticleListFragment

class HomeViewPagerAdapter(val context: Context, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    private val fragmentPairs = listOf(
        Pair(
            ArticleListFragment(),
            R.string.article_list_title
        )
    )

    override fun getItem(position: Int): Fragment = fragmentPairs[position].first

    override fun getCount(): Int = fragmentPairs.size

    override fun getPageTitle(position: Int): CharSequence? =
        context.getString(fragmentPairs[position].second)

}