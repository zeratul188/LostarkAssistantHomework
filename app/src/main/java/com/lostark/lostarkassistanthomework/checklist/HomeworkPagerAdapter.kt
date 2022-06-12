package com.lostark.lostarkassistanthomework.checklist

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class HomeworkPagerAdapter(private val fragments: ArrayList<Fragment>, private val fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }
}