package de.domradio.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import de.domradio.R
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.home_fragment), ViewPager.OnPageChangeListener {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onPageScrollStateChanged(state: Int) = Unit

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) =
        Unit

    override fun onPageSelected(position: Int) {
        homeViewModel.setViewPagerPosition(position)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        context?.let { context ->
            home_fragment_view_pager.adapter = HomeViewPagerAdapter(context, childFragmentManager)
        }
        home_fragment_tab_layout.setupWithViewPager(home_fragment_view_pager)
    }

    override fun onResume() {
        home_fragment_view_pager.addOnPageChangeListener(this)
        super.onResume()
    }

    override fun onPause() {
        home_fragment_view_pager.removeOnPageChangeListener(this)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
