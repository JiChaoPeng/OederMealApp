package com.android.oedermealapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.R
import com.android.oedermealapp.adapter.UserListAdapter
import com.android.oedermealapp.bean.ResultT
import com.android.oedermealapp.bean.UserBean
import com.android.oedermealapp.bean.UserList
import com.android.oedermealapp.net.NetWork
import kotlinx.android.synthetic.main.activity_account_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class AccountListActivity : BaseActivity() {
    private val adapter: UserListAdapter = UserListAdapter()
    override fun getContentView(): Int {
        return R.layout.activity_account_list
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColorWhite))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setTitle("餐饮人员列表")
        titleBar.setLeftOptionImageVisible(true)
        titleBar.leftOptionEvent = { view: View? ->
            finish()
        }
        titleBar!!.rightOptionEvent = { view: View? ->
            startActivity(Intent(this, AdminAccountActivity::class.java))
        }
        titleBar!!.setRightOptionText(
            "新增管理人员",
            ContextCompat.getColor(this, R.color.textColorWhite),
            View.VISIBLE
        )
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        recyclerView!!.layoutManager = layout
        recyclerView!!.adapter = adapter
        adapter.viewHolderConfig.itemClickListener={ o: Any? ->
            if (o is UserBean) {
                AdminAccountActivity.newInstance(this@AccountListActivity, o)
            }
        }
        initData()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {
        NetWork.netWork.networkServices.allUser()
            .enqueue(object : Callback<ResultT<UserList?>?> {
                override fun onResponse(
                    call: Call<ResultT<UserList?>?>,
                    response: Response<ResultT<UserList?>?>
                ) {
                    Log.d("BaseButterKnife1", "onResponse " + response.body())
                    if (response.body() != null && response.body()!!.isSucceed && response.body()!!.bean != null) {
                        adapter.modelList.clear()
                        for (user in response.body()!!.bean!!.list) {
                            if (user.level != 0) {
                                adapter.modelList.add(user)
                            }
                        }
                        adapter.notifyDataSetChanged()
                        if (adapter.modelListCount() > 0) {
                            noData!!.visibility = View.GONE
                        } else {
                            noData!!.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(this@AccountListActivity, "获取失败 请重试", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<ResultT<UserList?>?>,
                    t: Throwable
                ) {
                    Toast.makeText(this@AccountListActivity, "网络错误", Toast.LENGTH_SHORT).show()
                    Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
                }
            })
    }
}