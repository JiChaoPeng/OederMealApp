package com.android.oedermealapp.activity

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.R
import com.android.oedermealapp.bean.ResultT
import com.android.oedermealapp.bean.ResultModel
import com.android.oedermealapp.bean.UserBean
import com.android.oedermealapp.net.NetWork.Companion.netWork
import com.android.oedermealapp.util.ToastUtils
import kotlinx.android.synthetic.main.activity_create_account.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

open class SignUpActivity : BaseActivity() {
    private var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.text_black))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setLeftOptionImageVisible(true)
        titleBar.setTitle("注册")
        titleBar.leftOptionEvent = {
            finish()
        }
        initClick()
    }

    override fun getContentView(): Int {
        return R.layout.activity_create_account
    }

    private fun initClick() {
        uploadButton!!.setOnClickListener { v: View? ->
            imageUrl = null
            val intent1 = Intent(Intent.ACTION_PICK)
            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent1, 100)
        }
        signUpButton!!.setOnClickListener { v: View? -> buttonClick() }
    }

    private fun buttonClick() {
        if (loginAccount!!.text == null || TextUtils.isEmpty(loginAccount!!.text)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show()
        } else if (loginPassword!!.text == null || TextUtils.isEmpty(loginPassword!!.text)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show()
        } else if (imageUrl == null) {
            Toast.makeText(this, "请上传头像！", Toast.LENGTH_SHORT).show()
        } else {
            signUp()
        }
    }

    private fun signUp() {
        netWork.networkServices.signUp(
            loginAccount!!.text.toString(), loginPassword!!.text.toString(),
            0, 0, loginAccount!!.text.toString(), 0, imageUrl
        ).enqueue(object : Callback<ResultT<UserBean?>?> {
            override fun onResponse(
                call: Call<ResultT<UserBean?>?>,
                response: Response<ResultT<UserBean?>?>
            ) {
                Log.d("BaseButterKnife1", "onResponse")
                if (response.body() != null && response.body()?.isSucceed == true) {
                    ToastUtils.showToast(this@SignUpActivity, "注册成功！")
                    finish()
                }else{
                    ToastUtils.showToast(this@SignUpActivity, "注册失败 请重试！")
                }
            }

            override fun onFailure(
                call: Call<ResultT<UserBean?>?>,
                t: Throwable
            ) {
                ToastUtils.showToast(this@SignUpActivity, "网络错误 请重试！")
                Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data != null) {
                val uri = data.data
                if (uri != null) { //图库
                    val cursor: Cursor? = contentResolver.query(
                        uri,
                        arrayOf(MediaStore.Images.Media.DATA),
                        null,
                        null,
                        null
                    )
                    cursor?.moveToNext()
                    val filePath = cursor?.getString(0)
                    cursor?.close()
                    uploadImage(filePath)
                }
            }
        }
    }

    private fun uploadImage(path: String?) {
        if (path == null) return
        val file = File(path)
        // 上传文件参数
        val body = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
        val files: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, body)
        netWork.networkServices?.upload(files)?.enqueue(object : Callback<ResultModel> {
            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                Log.d("AddRoomActivity", "faild ${t.message} ${t.cause}")
                ToastUtils.showToast(this@SignUpActivity, "网络错误！请重试")
            }

            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {
                Log.d("AddRoomActivity", response.toString())
                if (response.body()!=null&&response.isSuccessful&&response.body()?.data!=null){
                    imageUrl = response.body()?.data
                    ToastUtils.showToast(this@SignUpActivity, "上传图片成功！")
                }else{
                    ToastUtils.showToast(this@SignUpActivity, "上传图片失败 请检查权限以及图片源后重试！")
                }
            }

        })
    }
}