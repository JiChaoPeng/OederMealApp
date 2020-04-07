package com.android.oedermealapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.R
import com.android.oedermealapp.adapter.OrderFormAdapter
import com.android.oedermealapp.bean.FoodBean
import com.android.oedermealapp.data.LocalStore
import kotlinx.android.synthetic.main.activity_order_form.*
import java.util.*

class OrderFormActivity : BaseActivity() {
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
        name.text = "下单用户：" + LocalStore.localUser.value?.account
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
        val foodBeans: ArrayList<FoodBean> = ArrayList()
        LocalStore.shopping.value?.let {
            for (model in it.list) {
                if (model.isChecked) {
                    foodBeans.add(model)
                    allPrice += model.price * model.num
                }
            }
            adapter.modelList.addAll(foodBeans)
            adapter.notifyDataSetChanged()
            price.text = "总价 ： ￥ $allPrice"
        }

    }
}