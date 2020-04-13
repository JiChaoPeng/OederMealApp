package com.android.oedermealapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.adapter.PagerAdapter
import com.android.oedermealapp.event.RefreshEvent
import com.android.oedermealapp.event.RefreshFragmentEvent
import com.android.oedermealapp.fragment.BaseFragment
import com.android.oedermealapp.fragment.MineInformationFragment
import com.android.oedermealapp.fragment.OrderMealFragment
import com.android.oedermealapp.fragment.ShoppingCartFragment
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {
    companion object {
        const val roomId: Int = 1
    }

    private var fragmentList: List<Fragment>? = null
    private var adapter: PagerAdapter? = null
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColorWhite))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setTitle(resources.getString(R.string.app_name))

        adapter = PagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(tabViewpager)
        fragmentList = listOf(
            OrderMealFragment(),
            ShoppingCartFragment(),
            MineInformationFragment()
        )
        adapter?.setFragments(fragmentList)
        tabViewpager.adapter = adapter

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetMessage(message: RefreshFragmentEvent?) {
        if (fragmentList != null && fragmentList!!.size > 2) {
            (fragmentList!![0] as? BaseFragment)?.initData()
            (fragmentList!![1] as? BaseFragment)?.initData()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }


}
