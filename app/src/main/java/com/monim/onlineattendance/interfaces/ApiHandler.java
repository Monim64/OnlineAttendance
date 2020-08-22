package com.monim.onlineattendance.interfaces;

import com.monim.onlineattendance.models.PostAttendance;
import com.monim.onlineattendance.models.PostAttendanceResponse;
import com.monim.onlineattendance.models.StoreData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiHandler {

    @GET("/api/stores")
    Call<StoreData> getStoreList(@Query("page") int pageNumber);

    @POST("/api/attendance")
    Call<PostAttendanceResponse> submitAttendanceData(@Body PostAttendance attendanceData);

}
