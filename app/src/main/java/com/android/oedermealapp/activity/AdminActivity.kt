package com.android.oedermealapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.R
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : BaseActivity() {
    override fun getContentView(): Int {
        return R.layout.activity_admin
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.text_black))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setTitle("管理人员页面")
        managerCook.setOnClickListener { v: View? ->
            WaiterActivity.isWaiter = false
            startActivity(Intent(this, WaiterActivity::class.java))
        }
        managerAccount.setOnClickListener { v: View? -> }
        managerWaiter.setOnClickListener { v: View? ->
            WaiterActivity.isWaiter = true
            startActivity(Intent(this, WaiterActivity::class.java))
        }
    }

}