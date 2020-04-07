package com.android.oedermealapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.frameworktool.util.onSingleClick
import com.android.oedermealapp.fragment.MineFragment
import com.android.oedermealapp.fragment.OrderFragment
import com.android.oedermealapp.fragment.ShoppingFragment
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val roomId: Int = 1
    }

    private var fragmentList: List<Fragment>? = null
    private var fragmentIndex: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MMKV.initialize(this)
        fragmentList = listOf(
            OrderFragment(),
            ShoppingFragment(),
            MineFragment()
        )
        orderTab.onSingleClick {
            switchPage(0)
        }
        shoppingTab.onSingleClick {
            switchPage(1)
        }
        mineTab.onSingleClick {
            switchPage(2)
        }
        switchPage(0)
    }


    private fun switchPage(index: Int) {
        if (fragmentList == null || fragmentIndex == index) return
        supportFragmentManager.beginTransaction().replace(R.id.container, fragmentList!![index])
            .commit()
        fragmentIndex = index
    }
}
