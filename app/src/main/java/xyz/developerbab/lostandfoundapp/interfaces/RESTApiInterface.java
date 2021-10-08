package xyz.developerbab.lostandfoundapp.interfaces;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RESTApiInterface {

    // create user
    @POST("user/register/")
    Call<ResponseBody> createUser(@Body Map<String, String> obj);



    // login user
    @POST("user/login/")
    Call<ResponseBody> loginUser(@Body Map<String, String> obj);



    // login user
    @POST("mobile/store/lost")
    Call<ResponseBody> addocument(@Body Map<String, String> obj);


    @POST("mobile/store/found")
    Call<ResponseBody> foundocs(@Body Map<String, String> obj);



    @GET("mobile/getCategory")
    Call<String> getcategory();


    @GET("mobile/getAllLost/")
    Call<String> getall();

    @GET("mobile/getAllFound")
    Call<String> getallfound();



    @POST("mobile/getUserFound")
    Call<ResponseBody> myfound(@Body Map<String, String> obj);

    @POST("mobile/getUserLost")
    Call<ResponseBody> mylost(@Body Map<String, String> obj);


    @POST("mobile/deleteFound")
    Call<ResponseBody> deletefound(@Body Map<String, String> obj);


    @POST("mobile/deleteLost")
    Call<ResponseBody> deletelost(@Body Map<String, String> obj);


    @POST("mobile/approve/lost")
    Call<ResponseBody> approvelost(@Body Map<String, String> obj);


}
