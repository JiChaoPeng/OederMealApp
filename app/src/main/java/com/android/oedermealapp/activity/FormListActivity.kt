package com.android.oedermealapp.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.R
import com.android.oedermealapp.adapter.FormListAdapter
import com.android.oedermealapp.bean.FormBean
import com.android.oedermealapp.bean.FormListBean
import com.android.oedermealapp.bean.ResultT
import com.android.oedermealapp.data.LocalStore
import com.android.oedermealapp.net.NetWork
import kotlinx.android.synthetic.main.activity_form_list.*
import kotlinx.android.synthetic.main.activity_form_list.recyclerView
import kotlinx.android.synthetic.main.activity_form_list.titleBar
import kotlinx.android.synthetic.main.activity_order_form.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormListActivity : BaseActivity() {
    private val adapter: FormListAdapter = FormListAdapter()
    override fun getContentView(): Int {
        return  R.layout.activity_form_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setTitle("我的订单")
        titleBar.setLeftOptionImageVisible(true)
        titleBar.leftOptionEvent = {
            finish()
        }
        recyclerView.adapter = adapter
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        recyclerView!!.layoutManager = layout
        adapter.viewHolderConfig.itemClickListener = {
            if (it is FormBean) {
                FormDetailActivity.newInstance(this, it)
            }
        }
        NetWork.netWork.networkServices.allForm(LocalStore.localUser.value?.account)
            .enqueue(object : Callback<ResultT<FormListBean?>?> {
                override fun onResponse(
                    call: Call<ResultT<FormListBean?>?>,
                    response: Response<ResultT<FormListBean?>?>
                ) {
                    Log.d("BaseButterKnife1", "onResponse " + response.body())
                    //services处理是否为正确用户以及密码
                    if (response.body() != null && response.body()!!.isSucceed && response.body()!!.bean != null) {
                        adapter.modelList.clear()
                        adapter.modelList.addAll(response.body()!!.bean!!.list)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@FormListActivity, "获取失败 请重试", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResultT<FormListBean?>?>,
                    t: Throwable
                ) {
                    Toast.makeText(this@FormListActivity, "网络错误", Toast.LENGTH_SHORT).show()
                    Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
                }
            })
    }

}