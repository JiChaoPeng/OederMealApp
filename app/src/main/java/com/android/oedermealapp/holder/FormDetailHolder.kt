package com.android.oedermealapp.holder

import android.view.View
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.loadImage
import com.android.oedermealapp.bean.MealBean
import kotlinx.android.synthetic.main.layout_holder_form_detail.view.*

class FormDetailHolder(itemView: View) : BaseRecyclerViewHolder(itemView) {
    override fun config(model: Any?) {
        super.config(model)
        if (model is MealBean){
            itemView.foodName.text = model.name
            loadImage(itemView.imageView, model.image_url)
            val num: Int =model.num
            itemView. foodNum.text = "x️$num"
            itemView.foodPrice.text = "￥ " + num * model.price
        }

    }
}