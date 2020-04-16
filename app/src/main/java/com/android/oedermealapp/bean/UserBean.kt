package com.android.oedermealapp.bean

import java.io.Serializable

class UserBean(
    var account: String,
    var password: String,
    var imageUrl: String,
    var level: Int,
    var age: Int,
    var imageIndex: Int,
    var permission: Int,
    var id: Int
) : Serializable