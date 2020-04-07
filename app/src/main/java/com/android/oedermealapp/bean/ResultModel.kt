package com.android.oedermealapp.bean

class ResultModel {
    val isSucceed: Boolean
        get() = resultCode == 200

    var resultCode = 0
    var data: String? = null

}