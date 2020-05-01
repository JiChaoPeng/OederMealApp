package com.android.oedermealapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.MainActivity
import com.android.oedermealapp.R
import com.android.oedermealapp.adapter.MealListAdapter
import com.android.oedermealapp.bean.FoodListBean
import com.android.oedermealapp.bean.MealBean
import com.android.oedermealapp.bean.ResultT
import com.android.oedermealapp.data.LocalStore
import com.android.oedermealapp.net.NetWork
import com.android.oedermealapp.util.ToastUtils
import kotlinx.android.synthetic.main.activity_meal_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealListActivity : BaseActivity() {
    private val adapter: MealListAdapter = MealListAdapter()
    override fun getContentView(): Int {
        return R.layout.activity_meal_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
    private fun initView() {
        recyclerView.adapter = adapter
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layout
        titleBar.setTitle("餐品管理")
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.text_black))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setRightOptionText(
            "添加餐品"
        )
        titleBar.rightOptionEvent = {
            AddMealActivity.newInstance(this, true, null)
        }
        adapter.viewHolderConfig.itemClickListener = {
            if (it is MealBean) {
                AddMealActivity.newInstance(this, false, it)
            }
        }
    }

    private fun getData() {
        NetWork.netWork.networkServices.allFood(MainActivity.roomId.toString())
            .enqueue(object : Callback<ResultT<FoodListBean?>?> {
                override fun onResponse(
                    call: Call<ResultT<FoodListBean?>?>,
                    response: Response<ResultT<FoodListBean?>?>
                ) {
                    Log.d("BaseButterKnife1", "onResponse")
                    if (response.body()?.bean != null && response.body()?.isSucceed == true) {
                        adapter.modelList.clear()
                        val bean = response.body()?.bean?.list
                        if (bean != null && LocalStore.shopping.value != null) {
                            for (foodBean in bean) {
                                for (food in LocalStore.shopping.value!!.list) {
                                    if (food.id == foodBean.id) {
                                        foodBean.num = food.num
                                    }
                                }
                            }
                            adapter.modelList.addAll(bean)
                            adapter.notifyDataSetChanged()
                        } else if (bean != null) {
                            adapter.modelList.addAll(bean)
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        ToastUtils.showToast(this@MealListActivity, "数据为空")
                    }
                }

                override fun onFailure(
                    call: Call<ResultT<FoodListBean?>?>,
                    t: Throwable
                ) {
                    ToastUtils.showToast(this@MealListActivity, "网络错误")
                    Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
                }
            })
    }
}
