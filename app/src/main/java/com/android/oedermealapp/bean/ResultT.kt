package com.android.oedermealapp.bean

class ResultT<T> {
    val isSucceed: Boolean
        get() = resultCode == 200
    var resultCode = 0
    var bean: T? = null
        private set
    var data: String? = null

    fun setBean(bean: T) {
        this.bean = bean
    }

}