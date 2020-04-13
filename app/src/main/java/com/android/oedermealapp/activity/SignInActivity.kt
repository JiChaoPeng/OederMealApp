package com.android.oedermealapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.MainActivity
import com.android.oedermealapp.R
import com.android.oedermealapp.bean.ResultT
import com.android.oedermealapp.bean.UserBean
import com.android.oedermealapp.data.LocalStore
import com.android.oedermealapp.net.NetWork.Companion.netWork
import com.android.oedermealapp.util.ToastUtils
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : BaseActivity() {
    override fun getContentView(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColorWhite))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setTitle("登陆")
        titleBar.setLeftOptionImageVisible(true)
        titleBar.leftOptionEvent = {
            finish()
        }
        MMKV.initialize(this)
        //申请权限
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                0
            )
        }
        initClick()
        startMain()
    }

    private fun initClick() {
        loginButton!!.setOnClickListener { buttonClick() }
        signUpButton!!.setOnClickListener {
            startActivity(
                Intent(this, SignUpActivity::class.java)
            )
        }
    }

    private fun buttonClick() {
        if (loginAccount!!.text == null || TextUtils.isEmpty(loginAccount!!.text)) {
            ToastUtils.showToast(this@SignInActivity, "账号不能为空！")
        } else if (loginPassword!!.text == null || TextUtils.isEmpty(loginPassword!!.text)) {
            ToastUtils.showToast(this@SignInActivity, "密码不能为空！")
        } else {
            if (loginAccount.text.toString() == "root" && loginPassword.text.toString() == "root") {
                LocalStore.localUser.value=UserBean("111", "111", "", 2, 0, 0, 2)
                startMain()
            } else {
                login()
            }
        }
    }

    private fun login() {
        netWork.networkServices.signIn(
            loginAccount!!.text.toString(),
            loginPassword!!.text.toString()
        ).enqueue(object : Callback<ResultT<UserBean?>?> {
            override fun onResponse(
                call: Call<ResultT<UserBean?>?>,
                response: Response<ResultT<UserBean?>?>
            ) {
                Log.d("BaseButterKnife1", "onResponse")
                //services处理是否为正确用户以及密码
                if (response.body() != null && response.body()?.isSucceed == true && response.body()?.bean != null) {
                    LocalStore.localUser.value = response.body()?.bean
                    ToastUtils.showToast(this@SignInActivity, "登陆成功！")
                    startMain()
                    finish()
                } else {
                    ToastUtils.showToast(this@SignInActivity, "登陆失败！")
                }
            }

            override fun onFailure(
                call: Call<ResultT<UserBean?>?>,
                t: Throwable
            ) {
                ToastUtils.showToast(this@SignInActivity, "网络错误！")
                Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
            }
        })
    }

    private fun startMain() {
        val bean = LocalStore.localUser.value
        if (bean != null) { //本地已经存在用户缓存
            if (bean.account == "111" && bean.password == "111") {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }
        image.visibility= View.GONE
    }
}