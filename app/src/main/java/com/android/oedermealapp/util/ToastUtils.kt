package com.android.oedermealapp.util

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.android.oedermealapp.R

object ToastUtils  {

    fun showToast(context: Context?, resId: Int?, duration: Int = Toast.LENGTH_SHORT) {
        if (!checkTokenIsValid(context) || resId == null) return
        val toast = Toast(context)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null)
        val textView = view.findViewById<TextView>(R.id.contentTextView)
        textView.setText(resId)
        toast.view = view
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = duration
        toast.show()
    }

    fun showToast(context: Context?, text: String?, duration: Int = Toast.LENGTH_SHORT) {
        if (!checkTokenIsValid(context) || text == null) return

        val toast = Toast(context)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null)
        val textView = view.findViewById<TextView>(R.id.contentTextView)
        textView.text = text
        toast.view = view
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = duration
        toast.show()
    }

    fun checkTokenIsValid(context: Context?): Boolean {
        return context is Activity && !context.isFinishing
    }
}