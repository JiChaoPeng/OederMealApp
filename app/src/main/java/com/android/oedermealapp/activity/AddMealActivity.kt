package com.android.oedermealapp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.frameworktool.base.BaseActivity
import com.android.frameworktool.util.loadImage
import com.android.frameworktool.util.onSingleClick
import com.android.oedermealapp.MainActivity.Companion.roomId
import com.android.oedermealapp.R
import com.android.oedermealapp.bean.MealBean
import com.android.oedermealapp.bean.ResultT
import com.android.oedermealapp.bean.ResultModel
import com.android.oedermealapp.net.NetWork
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

class AddMealActivity : BaseActivity() {

    private var imageUrl: String? = null
    private var isAdd = true
    private var meal: MealBean? = null

    companion object {
        private const val Is_Add = "Is_Add"
        private const val Is_Meal_Bean = "Is_Meal_Bean"
        fun newInstance(context: Context?, isAdd: Boolean = true, meal: MealBean? = null) {
            val intent = Intent(context, AddMealActivity::class.java).apply {
                putExtra(Is_Add, isAdd)
                putExtra(Is_Meal_Bean, meal)
            }
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isAdd = intent.getBooleanExtra(Is_Add, true)
        meal = intent.getSerializableExtra(Is_Meal_Bean) as? MealBean
        titleBar.setTitleTextColor(ContextCompat.getColor(this, R.color.text_black))
        titleBar.setBackGroundColor(ContextCompat.getColor(this, R.color.titleTheme))
        titleBar.setLeftOptionImageVisible(true)
        if (isAdd) {
            titleBar.setTitle("添加菜单")
            addFood.text = "添加进菜单"
        } else {
            titleBar.setTitle("修改餐品")
            delMeal.visibility = View.VISIBLE
            delMeal.onSingleClick {
                deleteMeal()
            }
            addFood.text = "确认修改"
            if (meal == null) finish()
            foodName.setText(meal!!.name)
            foodContent.setText(meal!!.content)
            foodPrice.setText("${meal!!.price}")
            imageUrl = meal!!.image_url
            loadImage(imageView,imageUrl!!)
        }
        titleBar.leftOptionEvent = {
            finish()
        }
        upLoad.setOnClickListener { v: View? ->
            imageUrl = null
            val intent1 = Intent(Intent.ACTION_PICK)
            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent1, 100)
        }
        addFood.setOnClickListener { v: View? ->
            if (foodName.text == null || TextUtils.isEmpty(foodName.text)) {
                ToastUtils.showToast(this@AddMealActivity, "名称不能为空！")
            } else if (foodContent.text == null || TextUtils.isEmpty(foodContent.text)) {
                ToastUtils.showToast(this@AddMealActivity, "内容不能为空！")
            } else if (foodPrice.text == null || TextUtils.isEmpty(foodPrice.text)) {
                ToastUtils.showToast(this@AddMealActivity, "价格不能为空！")
            } else {
                if (isAdd) {
                    addFood()
                } else {
                    refreshFood()
                }
            }
        }
    }

    private fun deleteMeal() {
        netWork.networkServices.delFood(meal!!.id).enqueue(object :Callback<ResultModel>{
            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                ToastUtils.showToast(this@AddMealActivity, "网络错误 请重试！")
            }

            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {
                if (response.body() != null && response.body()?.isSucceed == true) {
                    Toast.makeText(this@AddMealActivity, "删除成功！", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    ToastUtils.showToast(this@AddMealActivity, "删除失败 请重试！")
                }
            }

        })
    }

    override fun getContentView(): Int {
        return R.layout.activity_add_food
    }

    private fun addFood() {
        netWork.networkServices.addFood(
            foodName!!.text.toString(), foodContent!!.text.toString(),
            foodPrice!!.text.toString(), "", imageUrl, roomId
        ).enqueue(object : Callback<ResultT<MealBean?>?> {
            override fun onResponse(
                call: Call<ResultT<MealBean?>?>,
                response: Response<ResultT<MealBean?>?>
            ) {
                Log.d("BaseButterKnife1", "onResponse")
                if (response.body() != null && response.body()?.isSucceed == true) {
                    Toast.makeText(this@AddMealActivity, "添加成功！", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    ToastUtils.showToast(this@AddMealActivity, "添加失败 请重试！")
                }
            }

            override fun onFailure(
                call: Call<ResultT<MealBean?>?>,
                t: Throwable
            ) {
                ToastUtils.showToast(this@AddMealActivity, "网络错误 请重试！")
                Log.d("BaseButterKnife1", "onFailure" + t.cause + t.message)
            }
        })
    }

    private fun refreshFood() {
        netWork.networkServices.refreshFood(
            meal!!.id,
            foodName!!.text.toString(), foodContent!!.text.toString(),
            foodPrice!!.text.toString(), "", imageUrl, roomId
        ).enqueue(object : Callback<ResultT<MealBean?>?> {
            override fun onResponse(
                call: Call<ResultT<MealBean?>?>,
                response: Response<ResultT<MealBean?>?>
            ) {
                Log.d("BaseButterKnife1", "onResponse ${response}")
                if (response.body() != null && response.body()?.isSucceed == true) {
                    Toast.makeText(this@AddMealActivity, "添加成功！", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    ToastUtils.showToast(this@AddMealActivity, "添加失败 请重试！")
                }
            }

            override fun onFailure(
                call: Call<ResultT<MealBean?>?>,
                t: Throwable
            ) {
                ToastUtils.showToast(this@AddMealActivity, "网络错误 请重试！")
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
                ToastUtils.showToast(this@AddMealActivity, "网络错误！")
            }

            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {
                Log.d("AddRoomActivity", response.toString())
                if (response.body()!=null&&response.body()?.isSucceed==true&&response.body()?.data!=null){
                    imageUrl = response.body()?.data
                    loadImage(imageView,imageUrl!!)
                    ToastUtils.showToast(this@AddMealActivity, "上传图片成功！")
                }else{
                    ToastUtils.showToast(this@AddMealActivity, "上传图片失败 请检查权限以及图片源后重试！")
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