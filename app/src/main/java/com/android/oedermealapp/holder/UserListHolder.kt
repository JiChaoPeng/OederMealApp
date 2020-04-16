package com.android.oedermealapp.holder

import android.view.View
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.loadImage
import com.android.oedermealapp.MainActivity.Companion.LevelCook
import com.android.oedermealapp.MainActivity.Companion.LevelWaiter
import com.android.oedermealapp.bean.UserBean
import kotlinx.android.synthetic.main.layout_holder_form_detail.view.*

class UserListHolder(itemView: View) : BaseRecyclerViewHolder(itemView) {
    override fun config(model: Any?) {
        super.config(model)
        if (model is UserBean) {
            itemView.foodName.text = model.account
            loadImage(itemView.imageView, model.imageUrl)
            if (model.level === LevelWaiter) {
                itemView.foodNum.text = "前台管理人员"
            } else if (model.level === LevelCook) {
                itemView.foodNum.text = "厨房管理人员"
            }
        }
    }
}