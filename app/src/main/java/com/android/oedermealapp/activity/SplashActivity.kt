package com.android.oedermealapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.MainActivity
import com.android.oedermealapp.MainActivity.Companion.LevelCook
import com.android.oedermealapp.MainActivity.Companion.LevelNormal
import com.android.oedermealapp.MainActivity.Companion.LevelWaiter
import com.android.oedermealapp.MainActivity.Companion.Root
import com.android.oedermealapp.R
import com.android.oedermealapp.data.LocalStore
import com.tencent.mmkv.MMKV

class SplashActivity : BaseActivity() {
    override fun getContentView(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MMKV.initialize(this)
        //请求存储权限
        val permission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1
            )
        }
    }
    //双击shift 输入类名 ctrl shift+f 搜索方法里任何东西
    override fun onResume() {
        super.onResume()
        //        0:普通用户 1：服务人员 2：厨师 3 root 密码 root 管理员
        val user = LocalStore.localUser.value
        when {
            user == null -> {
                startActivity(Intent(this, SignInActivity::class.java))
            }
            user.account == Root -> {
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            }
            user.level == LevelNormal -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            user.level == LevelWaiter -> {
                startActivity(Intent(this, WaiterActivity::class.java))
                WaiterActivity.isWaiter=true
                finish()
            }
            user.level == LevelCook -> {
                startActivity(Intent(this, WaiterActivity::class.java))
                WaiterActivity.isWaiter=false
                finish()
            }

        }
    }
}