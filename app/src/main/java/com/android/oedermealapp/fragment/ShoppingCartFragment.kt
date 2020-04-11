package com.android.oedermealapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.oedermealapp.R
import com.android.oedermealapp.activity.AddOrderFormActivity
import com.android.oedermealapp.adapter.ShoppingListAdapter
import com.android.oedermealapp.data.LocalStore
import com.android.oedermealapp.event.RefreshEvent
import kotlinx.android.synthetic.main.fragment_shopping.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ShoppingCartFragment : Fragment() {

    private var price = 0
    private val adapter: ShoppingListAdapter = ShoppingListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shopping, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClick()
        initPrice()
    }


    private fun initPrice() {
        price = 0
        LocalStore.shopping.value?.let {
            for (model in LocalStore.shopping.value!!.list) {
                if (model.isChecked && model.num > 0) {
                    price += model.num * model.price
                }
            }
            priceText.text = "￥ $price"
        }
    }
    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetMessage(message: RefreshEvent?) {
        initPrice()
    }

    override fun onPause() {
        super.onPause()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }


    private fun initClick() {
        commit?.setOnClickListener { v: View? ->
            if (price > 0) {
                startActivity(Intent(activity, AddOrderFormActivity::class.java))
            } else {
                Toast.makeText(activity, "请先选择餐品再结算", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {
        recyclerView.adapter = adapter
        val layout = LinearLayoutManager(context)
        layout.orientation = LinearLayoutManager.VERTICAL
        recyclerView!!.layoutManager = layout
        adapter.modelList.clear()
        adapter.addModels(LocalStore.shopping.value?.list)
        adapter.notifyDataSetChanged()
    }
}