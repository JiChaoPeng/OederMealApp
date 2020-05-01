package com.android.oedermealapp.adapter

import android.view.ViewGroup
import com.android.frameworktool.recycler.BaseRecyclerAdapter
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.appInflate
import com.android.oedermealapp.R
import com.android.oedermealapp.bean.MealBean
import com.android.oedermealapp.holder.MealListHolder
import com.android.oedermealapp.holder.OrderListHolder

class MealListAdapter : BaseRecyclerAdapter() {
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
            MealListHolder(parent.appInflate(R.layout.layout_holder_meal_list, false))
        creator.bindViewClickEvent(viewHolderConfig)
        return creator
    }
}