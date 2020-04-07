package com.android.oedermealapp.net;

import com.android.oedermealapp.bean.FoodBean;
import com.android.oedermealapp.bean.FoodListBean;
import com.android.oedermealapp.bean.ResultBean;
import com.android.oedermealapp.bean.ResultModel;
import com.android.oedermealapp.bean.UserBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 2020/3/29
 */
public interface NetWorkServices {
    @FormUrlEncoded
    @POST("order/v1/signUp")
    Call<ResultBean<UserBean>> signUp(
            @Field("account") String account,
            @Field("password") String password,
            @Field("level") int level,
            @Field("age") int age,
            @Field("name") String name,
            @Field("imageIndex") int imageIndex,
            @Field("imageUrl") String imageUrl);

    @FormUrlEncoded
    @POST("order/v1/signIn")
    Call<ResultBean<UserBean>> signIn(
            @Field("account") String account,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("/order/v1/food/add")
    Call<ResultBean<FoodBean>> addFood(
            @Field("name") String name,
            @Field("content") String content,
            @Field("price") String price,
            @Field("address") String address,
            @Field("imageUrl") String imageUrl,
            @Field("roomId") int roomId);

    @FormUrlEncoded
    @POST("/order/v1/food/allbyid")
    Call<ResultBean<FoodListBean>> allFoodByRoomId(
            @Field("roomId") String roomId);

    @FormUrlEncoded
    @POST("/order/v1/food/all")
    Call<ResultBean<FoodListBean>> allFood(
            @Field("roomId") String roomId);

    @Multipart
    @POST("android/v1/food/Upload")
    Call<ResultModel> upload(
            @Part MultipartBody.Part file);

}
