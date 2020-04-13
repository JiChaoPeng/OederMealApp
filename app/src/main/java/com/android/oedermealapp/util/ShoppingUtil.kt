package com.android.oedermealapp.util

import com.android.oedermealapp.bean.FoodListBean
import com.android.oedermealapp.bean.MealBean
import com.android.oedermealapp.data.LocalStore
import java.util.*

object ShoppingUtil {
    fun removeShopping(foodBeans: List<MealBean>) {
        val shoppingValue = LocalStore.shopping.value
        var i = 0
        val index: MutableList<Int> = ArrayList()
        if (shoppingValue?.list != null && shoppingValue.list.size > 0) {
            for (model in foodBeans) {
                for ((i, shoppingModel) in shoppingValue.list.withIndex()) {
                    if (model.id == shoppingModel.id) {
                        index.add(i)
                    }
                }
            }
            for (x in index) {
                shoppingValue.list.removeAt(x)
            }
            LocalStore.shopping.value = shoppingValue
        }
    }

    fun addFood(mealBean: MealBean?, isAdd: Boolean) {
        if (mealBean == null) return
        val shoppingValue = LocalStore.shopping.value
        if (shoppingValue?.list == null || shoppingValue.list.size <= 0
        ) {
            val foodBeans = ArrayList<MealBean>()
            mealBean.num = 1
            foodBeans.add(mealBean)
            LocalStore.shopping.value = FoodListBean(foodBeans)
            return
        }
        if (shoppingValue.list.isNotEmpty()) {
            for (i in shoppingValue.list.indices) {
                if (shoppingValue.list[i].id == mealBean.id) { //                    model已存在
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
            val foodBeans = ArrayList<MealBean>()
            foodBeans.add(mealBean)
            LocalStore.shopping.value = FoodListBean(foodBeans)
            return
        }
        if (isAdd) {
            mealBean.num = mealBean.num + 1
            shoppingValue.list.add(0, mealBean)
            LocalStore.shopping.value = shoppingValue
        }
    }

    fun setChecked(mealBean: MealBean, isChecked: Boolean) {
        val shoppingValue = LocalStore.shopping.value
        if (shoppingValue?.list != null && shoppingValue.list.size > 0) {
            for (i in shoppingValue.list.indices) {
                if (shoppingValue.list[i].id == mealBean.id) {
                    shoppingValue.list[i].isChecked = isChecked
                    LocalStore.shopping.value = shoppingValue
                    return
                }
            }
        }
    }
}