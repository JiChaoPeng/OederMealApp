package com.android.oedermealapp.adapter

import android.view.ViewGroup
import com.android.frameworktool.recycler.BaseRecyclerAdapter
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.appInflate
import com.android.oedermealapp.R
import com.android.oedermealapp.bean.MealBean
import com.android.oedermealapp.holder.OrderFormHolder

class OrderFormAdapter : BaseRecyclerAdapter() {
    init {
        modelList.apply {
            add(MealBean::class.java)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder {
        val creator =
            OrderFormHolder(parent.appInflate(R.layout.layout_holder_order_form, false))
        creator.bindViewClickEvent(viewHolderConfig)
        return creator
    }
}