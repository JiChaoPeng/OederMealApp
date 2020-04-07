package com.android.oedermealapp.util

import com.android.oedermealapp.bean.FoodBean
import com.android.oedermealapp.bean.FoodListBean
import com.android.oedermealapp.data.LocalStore
import java.util.*

object ShoppingUtil {
    fun addFood(foodBean: FoodBean?, isAdd: Boolean) {
        if (foodBean == null) return
        val shoppingValue = LocalStore.shopping.value
        if (shoppingValue?.list == null || shoppingValue.list.size <= 0
        ) {
            val foodBeans = ArrayList<FoodBean>()
            foodBean.num=1
            foodBeans.add(foodBean)
            LocalStore.shopping.value = FoodListBean(foodBeans)
            return
        }
        if (shoppingValue.list.isNotEmpty()) {
            for (i in shoppingValue.list.indices) {
                if (shoppingValue.list[i].id == foodBean.id) { //                    model已存在
                    if (isAdd) {
                        shoppingValue.list[i].num = shoppingValue.list[i].num + 1
                    } else {
                        if (shoppingValue.list[i].num > 1) {
                            shoppingValue.list[i].num = shoppingValue.list[i].num - 1
                        } else {
                            shoppingValue.list.removeAt(i)
                        }
                    }
                    LocalStore.shopping.value = shoppingValue
                    return
                }
            }
        } else { //model不存在
            val foodBeans = ArrayList<FoodBean>()
            foodBeans.add(foodBean)
            LocalStore.shopping.value = FoodListBean(foodBeans)
            return
        }
        if (isAdd) {
            foodBean.num = foodBean.num + 1
            shoppingValue.list.add(0, foodBean)
            LocalStore.shopping.value = shoppingValue
        }
    }

    fun setChecked(foodBean: FoodBean, isChecked: Boolean) {
        val shoppingValue = LocalStore.shopping.value
        if (shoppingValue?.list != null && shoppingValue.list.size > 0) {
            for (i in shoppingValue.list.indices) {
                if (shoppingValue.list[i].id == foodBean.id) {
                    shoppingValue.list[i].isChecked = isChecked
                    LocalStore.shopping.value = shoppingValue
                    return
                }
            }
        }
    }
}