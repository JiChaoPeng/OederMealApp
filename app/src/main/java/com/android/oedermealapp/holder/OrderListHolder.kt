package com.android.oedermealapp.holder

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.loadImage
import com.android.oedermealapp.bean.FoodBean
import com.android.oedermealapp.util.ShoppingUtil
import kotlinx.android.synthetic.main.layout_holder_order.view.*

class OrderListHolder(itemView: View) : BaseRecyclerViewHolder(itemView) {
    private var num = 0

    override fun config(model: Any?) {
        super.config(model)
        if (model is FoodBean){
            itemView.foodName.text = model.name
            num = model.num
            itemView.foodNum.text = num.toString() + ""
            itemView.foodPrice.text = "￥ " + model.price
            itemView.foodContent.text = model.content
            loadImage(itemView.foodImage, model.image_url)
            itemView.foodAdd.setOnClickListener {
                num++
                itemView.foodNum.text = num.toString() + ""
                addFood(model as FoodBean?, true)
            }
            itemView.foodDelete.setOnClickListener { v: View? ->
                if (num > 0) {
                    num--
                    itemView.foodNum.text = num.toString() + ""
                    addFood(model as FoodBean?, false)
                }
            }
        }

    }

    private fun addFood(model: FoodBean?, isAdd: Boolean) {
        ShoppingUtil.addFood(model, isAdd)
    }
}