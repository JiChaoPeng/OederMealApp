package com.android.oedermealapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.MainActivity
import com.android.oedermealapp.MainActivity.Companion.Root
import com.android.oedermealapp.R
import com.android.oedermealapp.activity.WaiterActivity
import com.android.oedermealapp.adapter.FormListManagerAdapter
import com.android.oedermealapp.bean.FormBean
import com.android.oedermealapp.bean.FormListBean
import com.android.oedermealapp.bean.ResultT
import com.android.oedermealapp.bean.UserBean
import com.android.oedermealapp.data.LocalStore
import com.android.oedermealapp.data.LocalStore.localUser
import com.android.oedermealapp.net.NetWork.Companion.netWork
import com.android.oedermealapp.util.AlertCallBack
import com.android.oedermealapp.util.AlertUtil
import com.scwang.smartrefresh.header.DropBoxHeader
import com.scwang.smartrefresh.header.FunGameBattleCityHeader
import kotlinx.android.synthetic.main.activity_waiter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WaiterActivity : BaseActivity() {
    private val adapter = FormListManagerAdapter()
    private var user: UserBean? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.text_black))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        user = localUser.value
        if (user == null) {
            finish()
        }
        if (user!!.level == MainActivity.LevelWaiter || (user!!.account == Root && isWaiter)) {
            titleBar.setTitle("服务员界面")
            titleBar.setRightOptionText(
                "餐品管理"
            )
            titleBar.rightOptionEvent = {
                startActivity(Intent(this, MealListActivity::class.java))
            }
        } else if (user!!.level == MainActivity.LevelCook || (user!!.account == Root && isWaiter)) {
            titleBar.setTitle("厨师界面")
            isWaiter = false
        }
        if (user!!.level == MainActivity.LevelWaiter || user!!.level == MainActivity.LevelCook) {
            titleBar.setLeftOptionImageVisible(true)
            titleBar.setLeftOptionImageResource(R.mipmap.tuichu)
            titleBar.leftOptionEvent = {
                AlertUtil.showAlert(this, "退出", "确定退出当前账号？", object : AlertCallBack {
                    override fun neutralButton() {
                        localUser.value = null
                        startActivity(Intent(this@WaiterActivity, SplashActivity::class.java))
                        finish()
                    }

                    override fun negativeButton() {}
                })
            }
        }
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layout;
        recyclerView.adapter = adapter
        smartRefresh.setRefreshHeader(FunGameBattleCityHeader(this));
        smartRefresh.setOnRefreshListener {
            initData()
        }
        adapter.viewHolderConfig.itemClickListener = {
            if (it is FormBean) {
                FormDetailActivity.newInstance(this, it)
            }
        }
        initData()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {
        netWork.networkServices.allForm()
            .enqueue(object : Callback<ResultT<FormListBean?>?> {
                override fun onResponse(
                    call: Call<ResultT<FormListBean?>?>,
                    response: Response<ResultT<FormListBean?>?>
                ) {
                    Log.d("BaseButterKnife1", "onResponse " + response.body())
                    if (response.body() != null && response.body()!!.isSucceed && response.body()!!.bean != null) {
                        adapter.modelList.clear()
                        for (bean in response.body()!!.bean!!.list) {
                            if (isWaiter) {
                                if (bean.isFinish == 1) {
                                    adapter.modelList.add(bean)
                                }
                            } else {
                                if (bean.isFinish == 0) {
                                    adapter.modelList.add(bean)
                                }
                            }
                        }
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@WaiterActivity, "获取失败 请重试", Toast.LENGTH_SHORT).show()
                    }
                    smartRefresh.finishLoadMore()
                    smartRefresh.finishRefresh()
                }

                override fun onFailure(
                    call: Call<ResultT<FormListBean?>?>,
                    t: Throwable
                ) {
                    Toast.makeText(this@WaiterActivity, "网络错误", Toast.LENGTH_SHORT).show()
                    Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
                    smartRefresh.finishLoadMore()
                    smartRefresh.finishRefresh()
                }
            })
    }

    override fun getContentView(): Int {
        return R.layout.activity_waiter
    }

    companion object {
        var isWaiter = true
    }
}