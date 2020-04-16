package com.android.oedermealapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.R
import com.android.oedermealapp.data.LocalStore
import com.android.oedermealapp.util.AlertCallBack
import com.android.oedermealapp.util.AlertUtil
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
        managerAccount.setOnClickListener { v: View? -> startActivity(Intent(this, AccountListActivity::class.java))}
        managerWaiter.setOnClickListener { v: View? ->
            WaiterActivity.isWaiter = true
            startActivity(Intent(this, WaiterActivity::class.java))
        }
        logOut.setOnClickListener { v: View? ->
            AlertUtil.showAlert(this, "退出", "确定退出当前账号？", object : AlertCallBack  {
                override fun neutralButton() {
                    LocalStore.localUser.value=null
                    startActivity(Intent(this@AdminActivity, SplashActivity::class.java))
                    finish()
                }

                override fun negativeButton() {}
            })
        }
    }

}