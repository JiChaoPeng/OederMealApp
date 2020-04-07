package com.android.oedermealapp.adapter

import android.view.ViewGroup
import com.android.frameworktool.recycler.BaseRecyclerAdapter
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.appInflate
import com.android.oedermealapp.R
import com.android.oedermealapp.bean.FoodBean
import com.android.oedermealapp.holder.ShoppingListHolder

class ShoppingListAdapter : BaseRecyclerAdapter() {
    init {
        modelList.apply {
            add(FoodBean::class.java)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder {
        val creator =
            ShoppingListHolder(parent.appInflate(R.layout.layout_holder_shopping, false))
        creator.bindViewClickEvent(viewHolderConfig)
        return creator
    }
}