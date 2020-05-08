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
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.text_black))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setTitle("登陆")
        titleBar.setLeftOptionImageVisible(true)
        titleBar.leftOptionEvent = {
            finish()
        }
        initClick()
    }

    private fun initClick() {
        loginButton.setOnClickListener {
            buttonClick()
        }
        signUpButton.setOnClickListener {
            startActivity(
                Intent(this, SignUpActivity::class.java)
            )
        }
    }

    private fun buttonClick() {
        if (loginAccount.text == null || TextUtils.isEmpty(loginAccount.text)) {
            ToastUtils.showToast(this@SignInActivity, "账号不能为空！")
        } else if (loginPassword.text == null || TextUtils.isEmpty(loginPassword.text)) {
            ToastUtils.showToast(this@SignInActivity, "密码不能为空！")
        } else {
            if (loginAccount.text.toString() == MainActivity.Root && loginPassword.text.toString() == MainActivity.Root) {
                LocalStore.localUser.value =
                    UserBean(MainActivity.Root, MainActivity.Root, "", 3, 0, 0, 2, 10000)
                finish()
            } else {
                login()
            }
        }
    }

    private fun login() {
        netWork.networkServices.signIn(
            loginAccount.text.toString(),
            loginPassword.text.toString()
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
}