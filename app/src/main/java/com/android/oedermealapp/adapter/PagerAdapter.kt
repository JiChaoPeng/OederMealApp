package com.android.oedermealapp.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.PagerAdapter

class PagerAdapter(private val fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    private var fragments: List<Fragment>? = null

    private var title  = arrayListOf("点餐","购物","我的")

    fun setFragments(fragments: List<Fragment>?) {
        this.fragments = fragments
    }

    override fun getItem(position: Int): Fragment {
        return fragments!![position]
    }

    override fun getCount(): Int {
        return fragments!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var fragment: Fragment? = null
        try {
            removeFragment(container, position)
            fragment = super.instantiateItem(container, position) as Fragment
        } catch (e: Exception) {
        }
        return fragment!!
    }

    private fun removeFragment(container: ViewGroup, index: Int) {
        val tag = getFragmentTag(container.id, index)
        val fragment = fm.findFragmentByTag(tag) ?: return
        val ft: FragmentTransaction? = fm.beginTransaction()
        ft?.remove(fragment)
        ft?.commit()
        fm.executePendingTransactions()
    }

    private fun getFragmentTag(viewId: Int, index: Int): String {
        return try {
            val cls = FragmentPagerAdapter::class.java
            val parameterTypes = arrayOf(
                Int::class.javaPrimitiveType, Long::class.javaPrimitiveType
            )
            val method = cls.getDeclaredMethod(
                "makeFragmentName",
                *parameterTypes
            )
            method.isAccessible = true
            method.invoke(this, viewId, index) as String
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}