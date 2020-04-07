package com.android.oedermealapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.R
import com.android.oedermealapp.bean.ResultBean
import com.android.oedermealapp.bean.UserBean
import com.android.oedermealapp.data.LocalStore
import com.android.oedermealapp.net.NetWork.Companion.netWork
import com.android.oedermealapp.util.ToastUtils
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
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
        initClick()
    }

    private fun initClick() {
        loginButton!!.setOnClickListener { buttonClick() }
        signUpButton!!.setOnClickListener {
            startActivity(
                Intent(this, CreateAccountActivity::class.java)
            )
        }
    }

    private fun buttonClick() {
        if (loginAccount!!.text == null || TextUtils.isEmpty(loginAccount!!.text)) {
            ToastUtils.showToast(this@LoginActivity, "账号不能为空！")
        } else if (loginPassword!!.text == null || TextUtils.isEmpty(loginPassword!!.text)) {
            ToastUtils.showToast(this@LoginActivity, "密码不能为空！")
        } else {
            login()
        }
    }

    private fun login() {
        netWork.networkServices.signIn(
            loginAccount!!.text.toString(),
            loginPassword!!.text.toString()
        ).enqueue(object : Callback<ResultBean<UserBean?>?> {
            override fun onResponse(
                call: Call<ResultBean<UserBean?>?>,
                response: Response<ResultBean<UserBean?>?>
            ) {
                Log.d("BaseButterKnife1", "onResponse")
                //services处理是否为正确用户以及密码
                if (response.body() != null && response.body()?.isSucceed == true) {
                    LocalStore.localUser?.value = response.body()?.bean
                    ToastUtils.showToast(this@LoginActivity, "登陆成功！")
                    finish()
                } else {
                    ToastUtils.showToast(this@LoginActivity, "登陆失败！")
                }
            }

            override fun onFailure(
                call: Call<ResultBean<UserBean?>?>,
                t: Throwable
            ) {
                ToastUtils.showToast(this@LoginActivity, "网络错误！")
                Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
            }
        })
    }
}