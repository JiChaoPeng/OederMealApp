package com.android.oedermealapp.holder

import android.view.View
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.loadImage
import com.android.oedermealapp.bean.MealBean
import kotlinx.android.synthetic.main.layout_holder_order_form.view.*

class OrderFormHolder(itemView: View) : BaseRecyclerViewHolder(itemView) {
    override fun config(model: Any?) {
        super.config(model)
        if (model is MealBean){
            itemView.foodName.text = model.name
            val num: Int = model.num
            itemView.foodNum.text = "x️$num"
            loadImage(itemView.image,model.image_url)
            itemView.foodPrice.text = "￥ " + num * model.price
        }
    }
}