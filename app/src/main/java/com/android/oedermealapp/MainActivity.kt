package com.android.oedermealapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.oedermealapp.adapter.PagerAdapter
import com.android.oedermealapp.fragment.MineInformationFragment
import com.android.oedermealapp.fragment.OrderMealFragment
import com.android.oedermealapp.fragment.ShoppingCartFragment
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val roomId: Int = 1
    }
    private var fragmentList: List<Fragment>? = null
    private var adapter: PagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MMKV.initialize(this)
        adapter = PagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(tabViewpager)
        fragmentList = listOf(
            OrderMealFragment(),
            ShoppingCartFragment(),
            MineInformationFragment()
        )
        adapter?.setFragments(fragmentList)
        tabViewpager.adapter=adapter
    }

}
