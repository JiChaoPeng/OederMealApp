package com.android.oedermealapp.activity

import android.annotation.SuppressLint
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.frameworktool.base.BaseActivity
import com.android.frameworktool.util.onSingleClick
import com.android.oedermealapp.R
import com.android.oedermealapp.adapter.OrderFormAdapter
import com.android.oedermealapp.bean.FoodListBean
import com.android.oedermealapp.bean.FormBean
import com.android.oedermealapp.bean.MealBean
import com.android.oedermealapp.bean.ResultT
import com.android.oedermealapp.data.LocalStore
import com.android.oedermealapp.net.NetWork
import com.android.oedermealapp.util.ShoppingUtil.removeShopping
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_order_form.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AddOrderFormActivity : BaseActivity() {
    private var allPrice = 0
    private val adapter: OrderFormAdapter = OrderFormAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun getContentView(): Int {
        return R.layout.activity_order_form
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val value = LocalStore.localUser.value
        name.text = "下单用户：" + value?.account
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setTitle("结算")
        titleBar.setLeftOptionImageVisible(true)
        titleBar.leftOptionEvent = {
            finish()
        }
        commit.setOnClickListener { v: View? -> }
        recyclerView!!.adapter = adapter
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        recyclerView!!.layoutManager = layout
        val mealBeans: ArrayList<MealBean> = ArrayList()
        LocalStore.shopping.value?.let {
            for (model in it.list) {
                if (model.isChecked) {
                    mealBeans.add(model)
                    allPrice += model.price * model.num
                }
            }
            adapter.modelList.clear()
            adapter.modelList.addAll(mealBeans)
            adapter.notifyDataSetChanged()
            price.text = "总价 ： ￥ $allPrice"
        }
        commit.onSingleClick {

            NetWork.netWork.networkServices.addForm(
                allPrice, value?.account, 1, commentEdit.text.toString(), 0,
                Gson().toJson(FoodListBean(mealBeans)), 0, 0, System.currentTimeMillis()
            ).enqueue(object : Callback<ResultT<FormBean?>?> {
                override fun onResponse(
                    call: Call<ResultT<FormBean?>?>,
                    response: Response<ResultT<FormBean?>?>
                ) {
                    Log.d("BaseButterKnife1", "onResponse " + response.body())
                    if (response.body() != null && response.body()!!.isSucceed && response.body()!!.bean != null) {
                        removeShopping(mealBeans)
                        Toast.makeText(this@AddOrderFormActivity, "提交成功", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddOrderFormActivity, "提交失败 请重试", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<ResultT<FormBean?>?>,
                    t: Throwable
                ) {
                    Toast.makeText(this@AddOrderFormActivity, "网络错误", Toast.LENGTH_SHORT).show()
                    Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
                }
            })
        }
    }
}