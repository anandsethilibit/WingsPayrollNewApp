package com.libit.wingspayroll.Network;

import com.libit.wingspayroll.Model.DocumentImageUploadModel;
import com.libit.wingspayroll.Model.SendAttendenceModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("api/Login")
    Call<ResponseBody> loginuser(@Query("ContactNo") String ContactNo);

    @GET("api/Admin")
    Call<ResponseBody> loginadmin(@Query("username") String username,@Query("password") String password);

    @POST ("api/Data")
    Call<ResponseBody>MarkAttendece(@Body SendAttendenceModel model);

    @POST("Data/Sitedata")
    Call<ResponseBody> getDocument(@Query("sid") String sid);

    @POST("Registration/BannerImage")
    Call<ResponseBody> DocumentUpload(@Body DocumentImageUploadModel model);

    @GET("api/GetDailyAtt")
    Call<ResponseBody> attendance(@Query("date") String date);

    @GET("api/MobileAttendance")
    Call<ResponseBody> Mobileattendance(@Query("date") String date);

    @GET ("api/GetMonthlyAtt")
    Call<ResponseBody>MonthlyAttendance(@Query("Month")String Month,@Query("Year")String Year,@Query("empid")String empid,
                                        @Query("mon")Integer mon);
    @GET("api/EmployeeData")
    Call<ResponseBody> getEmployee();

    @POST("api/EmployeeStatus")
    Call<ResponseBody> MAttendanceStatus(@Query("id") Integer id,@Query("status") String status);

    @GET("api/BirthdayReport")
    Call<ResponseBody> getEmployeeBirthday();

}
