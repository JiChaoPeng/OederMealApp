package com.android.oedermealapp.bean

class FoodListBean(var list: ArrayList<MealBean>)
class ShoppingListBean(var list: ArrayList<ShoppingBean>)
class ShoppingBean(
    var owner: String,
    var list: ArrayList<FoodListBean>
)