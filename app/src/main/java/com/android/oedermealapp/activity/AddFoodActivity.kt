package com.android.oedermealapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.frameworktool.base.BaseActivity
import com.android.oedermealapp.MainActivity.Companion.roomId
import com.android.oedermealapp.R
import com.android.oedermealapp.bean.FoodBean
import com.android.oedermealapp.bean.ResultBean
import com.android.oedermealapp.bean.ResultModel
import com.android.oedermealapp.net.NetWork.Companion.netWork
import com.android.oedermealapp.util.ToastUtils
import kotlinx.android.synthetic.main.activity_add_food.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddFoodActivity : BaseActivity() {

    private var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initClick()
    }

    override fun getContentView(): Int {
        return R.layout.activity_add_food
    }

    private fun initView() {
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColorWhite))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setLeftOptionImageVisible(true)
        titleBar.setTitle("添加菜单")
        titleBar.leftOptionEvent = {
            finish()
        }
    }

    private fun initClick() {
        upLoad.setOnClickListener { v: View? ->
            imageUrl = null
            val intent1 = Intent(Intent.ACTION_PICK)
            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent1, 100)
        }
        addFood.setOnClickListener { v: View? ->
            if (foodName.text == null || TextUtils.isEmpty(foodName.text)) {
                ToastUtils.showToast(this@AddFoodActivity, "名称不能为空！")
            } else if (foodContent.text == null || TextUtils.isEmpty(foodContent.text)) {
                ToastUtils.showToast(this@AddFoodActivity, "内容不能为空！")
            } else if (foodPrice.text == null || TextUtils.isEmpty(foodPrice.text)) {
                ToastUtils.showToast(this@AddFoodActivity, "价格不能为空！")
            } else {
                addFood()
            }
        }
    }

    private fun addFood() {
        netWork.networkServices.addFood(
            foodName!!.text.toString(), foodContent!!.text.toString(),
            foodPrice!!.text.toString(), "", imageUrl, roomId
        ).enqueue(object : Callback<ResultBean<FoodBean?>?> {
            override fun onResponse(
                call: Call<ResultBean<FoodBean?>?>,
                response: Response<ResultBean<FoodBean?>?>
            ) {
                Log.d("BaseButterKnife1", "onResponse")
                if (response.body() != null && response.body()?.isSucceed == true) {
                    Toast.makeText(this@AddFoodActivity, "添加成功！", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    ToastUtils.showToast(this@AddFoodActivity, "添加失败 请重试！")
                }
            }

            override fun onFailure(
                call: Call<ResultBean<FoodBean?>?>,
                t: Throwable
            ) {
                ToastUtils.showToast(this@AddFoodActivity, "网络错误 请重试！")
                Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
            }
        })
    }

    private fun uploadImage(path: String) {
        val file = File(path)
        // 上传文件参数
        val body = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
        val files: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, body)
        netWork.networkServices?.upload(files)?.enqueue(object : Callback<ResultModel> {
            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                Log.d("AddRoomActivity", "faild ${t.message} ${t.cause}")
                ToastUtils.showToast(this@AddFoodActivity, "网络错误！")
            }

            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {
                Log.d("AddRoomActivity", response.toString())
                if (response.isSuccessful) {
                    imageUrl = response.body()?.data
                } else {
                    ToastUtils.showToast(this@AddFoodActivity, "上传图片失败")
                }
            }
        })
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
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
                    cursor!!.moveToNext()
                    val filePath = cursor.getString(0)
                    cursor.close()
                    uploadImage(filePath)
                }
            }
        }
    }
}