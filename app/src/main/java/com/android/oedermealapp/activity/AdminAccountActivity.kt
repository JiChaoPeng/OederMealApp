package com.android.oedermealapp.activity

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.android.frameworktool.base.BaseActivity
import com.android.frameworktool.view.TitleBarLayout
import com.android.oedermealapp.MainActivity.Companion.LevelCook
import com.android.oedermealapp.MainActivity.Companion.LevelWaiter
import com.android.oedermealapp.R
import com.android.oedermealapp.activity.AdminAccountActivity
import com.android.oedermealapp.bean.ResultModel
import com.android.oedermealapp.bean.ResultT
import com.android.oedermealapp.bean.UserBean
import com.android.oedermealapp.net.NetWork
import com.android.oedermealapp.util.AlertCallBack
import com.android.oedermealapp.util.AlertUtil
import com.android.oedermealapp.util.ToastUtils
import kotlinx.android.synthetic.main.activity_admin_account.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

open class AdminAccountActivity : BaseActivity() {

    private var image: String? = null
    private var user: UserBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColorWhite))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setTitle("餐饮人员管理")
        titleBar.setLeftOptionImageVisible(true)
        titleBar.leftOptionEvent = { view: View? ->
            finish()
        }
        user =
            intent.getSerializableExtra(USER_KEY) as UserBean?
        if (user != null) {
            deleteButton!!.visibility = View.VISIBLE
            loginAccount.setText(user?.account)
            loginPassword.setText(user?.password)
            if (user!!.level == LevelCook) {
                isCook.isChecked = true
                isWaiter.isChecked = false
            } else if (user!!.level == LevelWaiter) {
                isWaiter.isChecked = true
                isCook.isChecked = false
            }
            image = user!!.imageUrl
        }
        isCook.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            isWaiter.isChecked = !isChecked
        }
        isWaiter.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            isCook.isChecked = !isChecked
        }
        deleteButton!!.setOnClickListener { v: View? ->
            AlertUtil.showAlert(this, "删除", "确定删除当前账号？删除后无法恢复！", object : AlertCallBack {
                override fun neutralButton() {
                    delete()
                }
                override fun negativeButton() {}
            })
        }
        loginButton!!.setOnClickListener { v: View? -> buttonClick() }
        update!!.setOnClickListener { v: View? ->
            image = null
            val intent1 = Intent(Intent.ACTION_PICK)
            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent1, 100)
        }
    }

    private fun buttonClick() {
        if (loginAccount!!.text == null || TextUtils.isEmpty(loginAccount!!.text)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show()
        } else if (loginPassword!!.text == null || TextUtils.isEmpty(loginPassword!!.text)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show()
        } else if (image == null) {
            Toast.makeText(this, "照片不能为空", Toast.LENGTH_SHORT).show()
        } else {
            if (user == null) {
                signUp()
            } else {
                updateUser()
            }
        }
    }

    private fun signUp() {
        var level = 0
        if (isWaiter!!.isChecked) {
            level = LevelWaiter
        } else if (isCook!!.isChecked) {
            level = LevelCook
        }
        NetWork.netWork.networkServices.signUp(
            loginAccount!!.text.toString(), loginPassword!!.text.toString(),
            level, 0, loginAccount!!.text.toString(), 0, image
        ).enqueue(object : Callback<ResultT<UserBean?>?> {
            override fun onResponse(
                call: Call<ResultT<UserBean?>?>,
                response: Response<ResultT<UserBean?>?>
            ) {
                Log.d("BaseButterKnife1", "onResponse")
                if (response.body() != null && response.body()!!.isSucceed && response.body()!!.bean != null) {
                    Toast.makeText(this@AdminAccountActivity, "注册成功！", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AdminAccountActivity, "注册失败 请重试！", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(
                call: Call<ResultT<UserBean?>?>,
                t: Throwable
            ) {
                Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
                Toast.makeText(this@AdminAccountActivity, "网络错误  注册失败 请重试！", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun delete() {
        NetWork.netWork.networkServices.delete(user!!.id)
            .enqueue(object : Callback<ResultT<UserBean?>?> {
                override fun onResponse(
                    call: Call<ResultT<UserBean?>?>,
                    response: Response<ResultT<UserBean?>?>
                ) {
                    Log.d("BaseButterKnife1", "onResponse")
                    if (response.body() != null && response.body()!!.isSucceed) {
                        Toast.makeText(this@AdminAccountActivity, "删除成功！", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else if (response.body() != null) {
                        Toast.makeText(
                            this@AdminAccountActivity,
                            "删除失败 " + response.body()!!.data,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this@AdminAccountActivity, "删除失败 ", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<ResultT<UserBean?>?>,
                    t: Throwable
                ) {
                    Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
                    Toast.makeText(this@AdminAccountActivity, "网络错误  删除失败 请重试！", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun updateUser() {
        var level = 0
        if (isWaiter!!.isChecked) {
            level = LevelWaiter
        } else if (isCook!!.isChecked) {
            level = LevelCook
        }
        NetWork.netWork.networkServices?.update(
            loginAccount!!.text.toString(), loginPassword!!.text.toString(),
            level, 0, loginAccount!!.text.toString(), 0, image, user!!.id
        )?.enqueue(object : Callback<ResultT<UserBean?>?> {
            override fun onResponse(
                call: Call<ResultT<UserBean?>?>,
                response: Response<ResultT<UserBean?>?>
            ) {
                Log.d("BaseButterKnife1", "onResponse")
                if (response.body() != null && response.body()!!.isSucceed  && response.body()!!.bean != null) {
                    Toast.makeText(this@AdminAccountActivity, "更新成功！", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AdminAccountActivity, "更新失败 请重试！", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(
                call: Call<ResultT<UserBean?>?>,
                t: Throwable
            ) {
                Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
                Toast.makeText(this@AdminAccountActivity, "网络错误  更新失败 请重试！", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun getContentView(): Int {
        return R.layout.activity_admin_account
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data != null) {
                val uri = data.data
                if (uri != null) { //图库
                    val cursor = contentResolver.query(
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
        NetWork.netWork.networkServices?.upload(files)?.enqueue(object : Callback<ResultModel> {
            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                Log.d("AddRoomActivity", "faild ${t.message} ${t.cause}")
                ToastUtils.showToast(this@AdminAccountActivity, "网络错误！请重试")
            }

            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {
                Log.d("AddRoomActivity", response.toString())
                if (response.body()!=null&&response.body()?.isSucceed==true&&response.body()?.data!=null){
                    image = response.body()?.data
                    ToastUtils.showToast(this@AdminAccountActivity, "上传图片成功！")
                }else{
                    ToastUtils.showToast(this@AdminAccountActivity, "上传图片失败 请检查权限以及图片源后重试！")
                }
            }

        })
    }

    companion object {
        private const val USER_KEY = "User_Key"
        fun newInstance(context: Context, user: UserBean?) {
            val intent = Intent(context, AdminAccountActivity::class.java)
            intent.putExtra(USER_KEY, user)
            context.startActivity(intent)
        }
    }
}