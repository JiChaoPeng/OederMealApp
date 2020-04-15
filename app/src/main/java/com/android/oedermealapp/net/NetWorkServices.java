package com.android.oedermealapp.net;

import com.android.oedermealapp.bean.FormBean;
import com.android.oedermealapp.bean.FormListBean;
import com.android.oedermealapp.bean.MealBean;
import com.android.oedermealapp.bean.FoodListBean;
import com.android.oedermealapp.bean.ResultT;
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
    @POST("meal/signUp")
    Call<ResultT<UserBean>> signUp(
            @Field("account") String account,
            @Field("password") String password,
            @Field("level") int level,
            @Field("age") int age,
            @Field("name") String name,
            @Field("imageIndex") int imageIndex,
            @Field("imageUrl") String imageUrl);

    @FormUrlEncoded
    @POST("meal/signIn")
    Call<ResultT<UserBean>> signIn(
            @Field("account") String account,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("/meal/add")
    Call<ResultT<MealBean>> addFood(
            @Field("name") String name,
            @Field("content") String content,
            @Field("price") String price,
            @Field("address") String address,
            @Field("imageUrl") String imageUrl,
            @Field("roomId") int roomId);

    @FormUrlEncoded
    @POST("/meal/allbyid")
    Call<ResultT<FoodListBean>> allFoodByRoomId(
            @Field("roomId") String roomId);

    @FormUrlEncoded
    @POST("/meal/all")
    Call<ResultT<FoodListBean>> allFood(
            @Field("roomId") String roomId);

    @Multipart
    @POST("/meal/Upload")
    Call<ResultModel> upload(
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("/form/all/ownername")
    Call<ResultT<FormListBean>> allForm(
            @Field("ownerName") String ownerName);


    @POST("/form/all")
    Call<ResultT<FormListBean>> allForm();

    @FormUrlEncoded
    @POST("/form/finish")
    Call<ResultModel> finishForm(@Field("id") int id);

    @FormUrlEncoded
    @POST("/form/pay")
    Call<ResultModel> payForm(@Field("id") int id);

    @FormUrlEncoded
    @POST("/form/add")
    Call<ResultT<FormBean>> addForm(
            @Field("price") int price,
            @Field("owner_name") String owner_name,
            @Field("seat_id") int seat_id,
            @Field("comment") String comment,
            @Field("commentStar") int commentStar,
            @Field("foodDetail") String foodDetail,
            @Field("isFinish") int isFinish,
            @Field("isPay") int isPay,
            @Field("time") long time);
}
