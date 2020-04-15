package com.android.oedermealapp.adapter

import android.view.ViewGroup
import com.android.frameworktool.recycler.BaseRecyclerAdapter
import com.android.frameworktool.recycler.BaseRecyclerViewHolder
import com.android.frameworktool.util.appInflate
import com.android.oedermealapp.R
import com.android.oedermealapp.bean.FormBean
import com.android.oedermealapp.holder.FormListManagerHolder

class FormListManagerAdapter : BaseRecyclerAdapter() {
init {
    modelList.apply {
        add(FormBean::class.java)
    }
}
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder {
        val creator =
            FormListManagerHolder(parent.appInflate(R.layout.layout_holder_form_list, false))
        creator.bindViewClickEvent(viewHolderConfig)
        return creator
    }
}