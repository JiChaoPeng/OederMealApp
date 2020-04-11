package com.android.oedermealapp.holder

import android.view.View
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.loadImage
import com.android.oedermealapp.bean.MealBean
import com.android.oedermealapp.event.RefreshEvent
import com.android.oedermealapp.util.ShoppingUtil
import kotlinx.android.synthetic.main.layout_holder_shopping.view.*
import org.greenrobot.eventbus.EventBus

class ShoppingListHolder(itemView: View) :
    BaseRecyclerViewHolder(itemView) {
    private var num = 0
    override fun config(model: Any?) {
        super.config(model)
        if (model is MealBean) {
            itemView.foodName.text = model.name
            num = model.num
            itemView.foodNum.text = num.toString() + ""
            itemView.foodPrice.text = "￥ " + model.price
            itemView.checkBox.isChecked = model.isChecked
            itemView.foodContent.text = model.content
            loadImage(itemView.foodImage, model.image_url)
            itemView.foodAdd.setOnClickListener { v: View? ->
                num++
                itemView.foodNum.text = num.toString() + ""
                addFood(model as MealBean?, true)
                EventBus.getDefault().post(RefreshEvent())
            }
            itemView.foodDelete.setOnClickListener {
                if (num > 0) {
                    num--
                    itemView.foodNum.text = num.toString() + ""
                    addFood(model as MealBean?, false)
                    EventBus.getDefault().post(RefreshEvent())
                }
            }
            itemView.shoppingLayout.setOnClickListener { v: View? ->
                val checked: Boolean = !model.isChecked
                model.isChecked = checked
                itemView.checkBox.isChecked = checked
                ShoppingUtil.setChecked(model, checked)
                EventBus.getDefault().post(RefreshEvent())
            }
        }

    }

    private fun addFood(model: MealBean?, isAdd: Boolean) {
        ShoppingUtil.addFood(model, isAdd)
    }
}