package com.android.oedermealapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.oedermealapp.MainActivity.Companion.roomId
import com.android.oedermealapp.R
import com.android.oedermealapp.adapter.OrderListAdapter
import com.android.oedermealapp.bean.FoodListBean
import com.android.oedermealapp.bean.ResultBean
import com.android.oedermealapp.data.LocalStore
import com.android.oedermealapp.net.NetWork.Companion.netWork
import com.android.oedermealapp.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderFragment : Fragment() {

    private val adapter: OrderListAdapter = OrderListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initData() {
        netWork.networkServices.allFood(roomId.toString())
            .enqueue(object : Callback<ResultBean<FoodListBean?>?> {
                override fun onResponse(
                    call: Call<ResultBean<FoodListBean?>?>,
                    response: Response<ResultBean<FoodListBean?>?>
                ) {
                    Log.d("BaseButterKnife1", "onResponse")
                    if (response.body() != null && response.body()?.isSucceed == true) {
                        adapter.modelList.clear()
                        val bean = response.body()?.bean?.list
                        if (bean != null && LocalStore.shopping.value != null) {
                            for (foodBean in bean) {
                                for (food in LocalStore.shopping.value!!.list) {
                                    if (food.id == foodBean.id) {
                                        foodBean.num = food.num
                                    }
                                }
                            }
                            adapter.modelList.addAll(bean)
                            adapter.notifyDataSetChanged()
                        } else if (bean != null) {
                            adapter.modelList.addAll(bean)
                            adapter.notifyDataSetChanged()
                        }

                    } else {
                        ToastUtils.showToast(activity, "获取数据失败")
                    }
                }

                override fun onFailure(
                    call: Call<ResultBean<FoodListBean?>?>,
                    t: Throwable
                ) {
                    ToastUtils.showToast(activity, "网络错误")
                    Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
                }
            })
    }


    private fun initView() {
        recyclerView.adapter = adapter
        val layout = LinearLayoutManager(context)
        layout.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layout
    }
}