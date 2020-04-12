package com.android.oedermealapp.holder

import android.view.View
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.loadImage
import com.android.oedermealapp.bean.MealBean
import com.android.oedermealapp.event.RefreshFragmentEvent
import com.android.oedermealapp.util.ShoppingUtil
import kotlinx.android.synthetic.main.layout_holder_order.view.*
import org.greenrobot.eventbus.EventBus

class OrderListHolder(itemView: View) : BaseRecyclerViewHolder(itemView) {
    private var num = 0

    override fun config(model: Any?) {
        super.config(model)
        if (model is MealBean){
            itemView.foodName.text = model.name
            num = model.num
            itemView.foodNum.text = num.toString() + ""
            itemView.foodPrice.text = "￥ " + model.price
            itemView.foodContent.text = model.content
            loadImage(itemView.foodImage, model.image_url)
            itemView.foodAdd.setOnClickListener {
                num++
                itemView.foodNum.text = num.toString() + ""
                addFood(model as MealBean?, true)
            }
            itemView.foodDelete.setOnClickListener { v: View? ->
                if (num > 0) {
                    num--
                    itemView.foodNum.text = num.toString() + ""
                    addFood(model as MealBean?, false)
                }
            }
        }

    }

    private fun addFood(model: MealBean?, isAdd: Boolean) {
        ShoppingUtil.addFood(model, isAdd)
        EventBus.getDefault().post(RefreshFragmentEvent())
    }
}