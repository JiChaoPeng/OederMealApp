package com.android.oedermealapp.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.android.oedermealapp.R

object AlertUtil {
    fun showAlert(
        context: Context?,
        title: String?,
        message: String?,
        callback: AlertCallBack
    ) {
        val builder =
            AlertDialog.Builder(context!!)
        builder.setTitle(title) //设置内容
            .setIcon(R.mipmap.order) //设置是否可以点击对话框以外的地方消失
            .setMessage(message)
            .setCancelable(false)
            .setNeutralButton("确定") { dialogInterface, i ->
                callback.neutralButton()
                dialogInterface.dismiss()
            }
            .setNegativeButton("取消") { dialogInterface, i ->
                callback.negativeButton()
                dialogInterface.dismiss()
            }
        builder.create().show()
    }
}