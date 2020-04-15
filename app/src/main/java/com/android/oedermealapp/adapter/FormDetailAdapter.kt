package com.android.oedermealapp.adapter

import android.view.ViewGroup
import com.android.frameworktool.recycler.BaseRecyclerAdapter
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.appInflate
import com.android.oedermealapp.R
import com.android.oedermealapp.bean.MealBean
import com.android.oedermealapp.holder.FormDetailHolder

class FormDetailAdapter : BaseRecyclerAdapter() {
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
            FormDetailHolder(parent.appInflate(R.layout.layout_holder_form_detail, false))
        creator.bindViewClickEvent(viewHolderConfig)
        return creator
    }
}