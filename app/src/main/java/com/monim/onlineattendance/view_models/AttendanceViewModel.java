package com.monim.onlineattendance.view_models;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.monim.onlineattendance.R;
import com.monim.onlineattendance.interfaces.ApiHandler;
import com.monim.onlineattendance.models.PostAttendance;
import com.monim.onlineattendance.models.PostAttendanceResponse;
import com.monim.onlineattendance.network.ApiClient;
import com.monim.onlineattendance.utils.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceViewModel extends ViewModel {

    public MutableLiveData<String> userName = new MutableLiveData<>();
    public MutableLiveData<String> userId = new MutableLiveData<>();

    private MutableLiveData<PostAttendance> attendanceData = new MutableLiveData<>();
    private MutableLiveData<PostAttendanceResponse> responseData = new MutableLiveData<>();

    private MutableLiveData<Boolean> isRequesting = new MutableLiveData<>();

    public MutableLiveData<PostAttendance> getAttendanceData() {
        return attendanceData;
    }

    public void onSubmitClick(View view) {
        PostAttendance postAttendance = new PostAttendance();
        postAttendance.setUserName(userName.getValue());
        postAttendance.setUserId(userId.getValue());
        postAttendance.setUniqueRequestId(UUID.randomUUID().toString());

        attendanceData.setValue(postAttendance);
    }

    public MutableLiveData<PostAttendanceResponse> getResponseData(PostAttendance postAttendance, Context context) {
        sendAttendanceData(postAttendance, context);
        return responseData;
    }

    public MutableLiveData<Boolean> getIsRequesting() {
        return isRequesting;
    }

    public void setIsRequesting(Boolean isRequesting) {
        this.isRequesting.setValue(isRequesting);
    }

    private void sendAttendanceData(PostAttendance postAttendance, Context context) {
        setIsRequesting(true);
        Call<PostAttendanceResponse> submitCall = ApiClient.getRetrofitInstance().create(ApiHandler.class).submitAttendanceData(postAttendance);
        submitCall.enqueue(new Callback<PostAttendanceResponse>() {
            @Override
            public void onResponse(@NotNull Call<PostAttendanceResponse> call, @NotNull Response<PostAttendanceResponse> response) {
                if (response.body() != null) {
                    responseData.setValue(response.body());
                } else {
                    AppUtils.showToast(context, response.message(), true);
                }
                setIsRequesting(false);
            }

            @Override
            public void onFailure(@NotNull Call<PostAttendanceResponse> call, @NotNull Throwable t) {
                AppUtils.showToast(context, context.getString(R.string.common_error_message), true);
                setIsRequesting(false);
            }
        });
    }
}
