package com.android.oedermealapp.holder

import android.view.View
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.oedermealapp.bean.FoodListBean
import com.android.oedermealapp.bean.FormBean
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_holder_form_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class FormListHolder(itemView: View) : BaseRecyclerViewHolder(itemView) {
    override fun config(model: Any?) {
        super.config(model)
        if (model is FormBean){
            val foodListBean =
                Gson().fromJson((model as FormBean?)?.foodDetail, FoodListBean::class.java)
            if (foodListBean != null && foodListBean.list.size> 0) {
                var num = 0
                for (bean in foodListBean.list) {
                    num += bean.num
                }
                itemView.name.text = foodListBean.list[0].name + "等" + num + "件餐品"
                itemView.price.text = "￥ " + model.price
            }
            val currentDateTimeString =
                SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA)
                    .format(Date(model.time))
            itemView.time.text = currentDateTimeString
        }
    }
}