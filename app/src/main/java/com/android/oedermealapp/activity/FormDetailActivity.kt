package com.android.oedermealapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.frameworktool.base.BaseActivity
import com.android.frameworktool.view.TitleBarLayout
import com.android.oedermealapp.MainActivity.Companion.LevelCook
import com.android.oedermealapp.MainActivity.Companion.LevelNormal
import com.android.oedermealapp.MainActivity.Companion.LevelWaiter
import com.android.oedermealapp.MainActivity.Companion.Root
import com.android.oedermealapp.R
import com.android.oedermealapp.activity.FormDetailActivity
import com.android.oedermealapp.adapter.FormDetailAdapter
import com.android.oedermealapp.bean.FoodListBean
import com.android.oedermealapp.bean.FormBean
import com.android.oedermealapp.bean.ResultModel
import com.android.oedermealapp.bean.UserBean
import com.android.oedermealapp.data.LocalStore
import com.android.oedermealapp.net.NetWork
import com.android.oedermealapp.net.NetWork.Companion.netWork
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_form_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FormDetailActivity : BaseActivity() {
    private var formBean: FormBean? = null
    private val adapter: FormDetailAdapter = FormDetailAdapter()
    private var user: UserBean? = null
    override fun getContentView(): Int {
        return R.layout.activity_form_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColorWhite))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setTitle("订单详情")
        formBean =
            intent.getSerializableExtra(FormBeanKey) as FormBean
        user = LocalStore.localUser.value
        if (formBean == null || user == null) {
            finish()
        }
        recyclerView.adapter = adapter
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layout
        val foodListBean =
            Gson().fromJson(formBean!!.foodDetail, FoodListBean::class.java)
        adapter.modelList.clear()
        adapter.modelList.addAll(foodListBean.list)
        adapter.notifyDataSetChanged()
        if (foodListBean.list.size > 0) {
            var num = 0
            for (bean in foodListBean.list) {
                num += bean.num * bean.price
            }
            price!!.text = "￥ $num"
        }
        val currentDateTimeString =
            SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA)
                .format(Date(formBean!!.time))
        name!!.text =
            "下单账号 ：" + formBean!!.ownerName.toString() + "   下单时间：" + currentDateTimeString
        if (user!!.level == LevelNormal) {
            commit.visibility = View.GONE
        } else if (user!!.level == LevelWaiter || user!!.account == Root && WaiterActivity.isWaiter) {
            commit.visibility = View.VISIBLE
            commit.text = "确认已收钱 确认后订单完成"
            commit.setOnClickListener { v: View? ->
                netWork.networkServices.payForm(formBean!!.id)
                    .enqueue(object : Callback<ResultModel?> {
                        override fun onResponse(
                            call: Call<ResultModel?>,
                            response: Response<ResultModel?>
                        ) {
                            Log.d("BaseButterKnife1", "onResponse " + response.body())
                            if (response.body() != null && response.body()!!.isSucceed) {
                                Toast.makeText(this@FormDetailActivity, "操作成功", Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this@FormDetailActivity,
                                    "操作失败 请重试",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(
                            call: Call<ResultModel?>,
                            t: Throwable
                        ) {
                            Toast.makeText(this@FormDetailActivity, "网络错误  请重试", Toast.LENGTH_SHORT)
                                .show()
                            Log.d(
                                "BaseButterKnife1",
                                "onFailure" + t.cause + t.message
                            )
                        }
                    })
            }
            if (formBean!!.isPay == 1) {
                commit.visibility=View.GONE
            }
        } else if (user!!.level == LevelCook || user!!.account == Root && !WaiterActivity.isWaiter) {
            commit.visibility = View.VISIBLE
            if (formBean!!.isFinish == 1) {
                commit.visibility=View.GONE
            }
            commit.text = "确认餐品已全部上齐"
            commit.setOnClickListener { v: View? ->
                netWork.networkServices.finishForm(formBean!!.getId())
                    .enqueue(object : Callback<ResultModel?> {
                        override fun onResponse(
                            call: Call<ResultModel?>,
                            response: Response<ResultModel?>
                        ) {
                            Log.d("BaseButterKnife1", "onResponse " + response.body())
                            if (response.body() != null && response.body()!!.isSucceed) {
                                Toast.makeText(this@FormDetailActivity, "操作成功", Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this@FormDetailActivity,
                                    "操作失败 请重试",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(
                            call: Call<ResultModel?>,
                            t: Throwable
                        ) {
                            Toast.makeText(this@FormDetailActivity, "网络错误  请重试", Toast.LENGTH_SHORT)
                                .show()
                            Log.d(
                                "BaseButterKnife1",
                                "onFailure" + t.cause + t.message
                            )
                        }
                    })
            }
        }
    }

    companion object {
        private const val FormBeanKey = "FormBeanKey"
        @JvmStatic
        fun newInstance(context: Context, model: FormBean?) {
            val intent = Intent(context, FormDetailActivity::class.java)
            intent.putExtra(FormBeanKey, model)
            context.startActivity(intent)
        }
    }
}