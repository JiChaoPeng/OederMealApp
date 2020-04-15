package com.android.oedermealapp.holder

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.loadImage
import com.android.oedermealapp.R
import com.android.oedermealapp.bean.FoodListBean
import com.android.oedermealapp.bean.FormBean
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_holder_form_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class FormListManagerHolder(itemView: View) :
    BaseRecyclerViewHolder(itemView) {
    @SuppressLint("SetTextI18n")
    override fun config(model: Any?) {
        super.config(model)
        if (model is FormBean) {
            val foodListBean =
                Gson().fromJson(model.foodDetail, FoodListBean::class.java)
            if (foodListBean?.list != null && foodListBean.list.size > 0) {
                var num = 0
                for (bean in foodListBean.list) {
                    num += bean.num
                }
                itemView.name.text = foodListBean.list[0].name + "等" + num + "件餐品"
                itemView.price.text = "下单金额：￥ " + model.price
                itemView.foodName.text = "下单账号： " + model.ownerName
                loadImage(itemView.image, foodListBean.list[0].image_url)
                if (model.isFinish == 1 && model.isPay == 1) {
                    itemView.notFinish.visibility=View.GONE
                    itemView.card.background = itemView.context.getDrawable(R.color.white)
                } else {
                    itemView.card.background =
                        itemView.context.getDrawable(R.drawable.shape_is_finish)
                    itemView.notFinish.visibility=View.VISIBLE
                }
            }
            val currentDateTimeString =
                SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA)
                    .format(Date(model.time))
            itemView.time.text = currentDateTimeString
        }

    }
}