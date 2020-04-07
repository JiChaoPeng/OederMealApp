package com.android.oedermealapp.data

import com.android.frameworktool.storage.BooleanConvert
import com.android.frameworktool.storage.Key
import com.android.frameworktool.storage.ModelConvert
import com.android.oedermealapp.bean.FoodListBean
import com.android.oedermealapp.bean.UserBean
import wealoha.android.framework.storage.Entity
import wealoha.android.framework.storage.NotClearEntity

object LocalStore {
    private const val FirstLaunchKey = "IS_LAUNCH_KEY"
    private const val ShoppingModel = "ShoppingKey"
    private const val User = "User"
    var firstLaunch = NotClearEntity(
        Key(
            FirstLaunchKey,
            BooleanConvert(true)
        )
    )
    var localUser =
        Entity(
            Key(
                FirstLaunchKey, ModelConvert(
                    UserBean::class.java
                )
            )
        )
    var shopping =
        Entity(
            Key(
                ShoppingModel, ModelConvert(
                    FoodListBean::class.java
                )
            )
        )
}