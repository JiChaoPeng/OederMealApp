package com.android.oedermealapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.oedermealapp.R
import com.android.oedermealapp.activity.AddMealActivity
import com.android.oedermealapp.activity.SignInActivity
import com.android.oedermealapp.bean.UserBean
import com.android.oedermealapp.data.LocalStore
import com.android.oedermealapp.util.AlertCallBack
import com.android.oedermealapp.util.AlertUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_mine.*

class MineInformationFragment : Fragment() {

    private var bean: UserBean? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logOut.setOnClickListener {
            AlertUtil.showAlert(activity, "退出", "确定退出当前账号？", object : AlertCallBack {
                override fun neutralButton() {
                    LocalStore.localUser.value = null
                    name!!.text = ""
                    initData()
                }

                override fun negativeButton() {}
            })
        }
        login!!.setOnClickListener {
            startActivity(
                Intent(activity, SignInActivity::class.java)
            )
        }
        addFood!!.setOnClickListener {
            startActivity(
                Intent(activity, AddMealActivity::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {
        bean = LocalStore.localUser.value
        if (bean != null) { //本地已经存在用户缓存
            mineLayout!!.visibility = View.VISIBLE
            loginLayout!!.visibility = View.GONE
            initView()
        } else {
            mineLayout!!.visibility = View.GONE
            loginLayout!!.visibility = View.VISIBLE
        }
    }

    private fun initView() {
        val mRequestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        if (bean?.imageUrl == null || bean?.imageUrl.equals("")) {
            Glide.with(context!!).load(R.mipmap.ic_launcher).apply(mRequestOptions).into(avatar)
        } else {
            Glide.with(context!!).load(bean?.imageUrl).apply(mRequestOptions).into(avatar)
        }
        name.text = bean?.account
    }
}