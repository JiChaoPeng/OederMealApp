package com.android.oedermealapp.holder

import android.view.View
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.loadImage
import com.android.oedermealapp.bean.MealBean
import kotlinx.android.synthetic.main.layout_holder_meal_list.view.*

class MealListHolder(itemView: View) : BaseRecyclerViewHolder(itemView) {
    private var num = 0

    override fun config(model: Any?) {
        super.config(model)
        if (model is MealBean){
            itemView.foodName.text = model.name
            num = model.num
            itemView.foodPrice.text = "￥ " + model.price
            itemView.foodContent.text = model.content
            loadImage(itemView.foodImage, model.image_url)
            }
        }
}