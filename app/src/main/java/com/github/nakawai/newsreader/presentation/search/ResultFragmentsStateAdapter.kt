package com.github.nakawai.newsreader.presentation.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.nakawai.newsreader.presentation.search.list.PlaceholderFragment

class ResultFragmentsStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        return PlaceholderFragment.newInstance(position)
    }
}
