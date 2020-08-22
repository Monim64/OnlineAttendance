package com.monim.onlineattendance.view_models;

import android.content.Intent;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.monim.onlineattendance.R;
import com.monim.onlineattendance.activity.AttendanceActivity;
import com.monim.onlineattendance.interfaces.ApiHandler;
import com.monim.onlineattendance.models.Links;
import com.monim.onlineattendance.models.MetaData;
import com.monim.onlineattendance.models.StoreData;
import com.monim.onlineattendance.models.StoreItem;
import com.monim.onlineattendance.network.ApiClient;
import com.monim.onlineattendance.utils.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreListViewModel extends ViewModel {

    private MutableLiveData<List<StoreItem>> storeList = new MutableLiveData<>();
    private MutableLiveData<Links> links = new MutableLiveData<>();
    private MutableLiveData<MetaData> metaData = new MutableLiveData<>();

    private MutableLiveData<Boolean> isHideEmptyView = new MutableLiveData<>();

    public LiveData<List<StoreItem>> getAllStores(int pageNumber) {
        storeList = new MutableLiveData<>();
        loadStoreList(pageNumber);
        return storeList;
    }

    public void setStoreList(List<StoreItem> storeList) {
        setIsHideEmptyView(storeList != null && storeList.size() > 0);
        this.storeList.setValue(storeList);
    }

    public MutableLiveData<Links> getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links.setValue(links);
    }

    public MutableLiveData<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData.setValue(metaData);
    }

    public MutableLiveData<Boolean> getIsHideEmptyView() {
        return isHideEmptyView;
    }

    public void setIsHideEmptyView(Boolean isHideEmptyView) {
        this.isHideEmptyView.setValue(isHideEmptyView);
    }

    private void loadStoreList(int pageNumber) {
        Call<StoreData> storeDataCall = ApiClient.getRetrofitInstance().create(ApiHandler.class).getStoreList(pageNumber);
        storeDataCall.enqueue(new Callback<StoreData>() {
            @Override
            public void onResponse(@NotNull Call<StoreData> call, @NotNull Response<StoreData> response) {
                if (response.isSuccessful()) {
                    setStoreList((response.body() == null || response.body().getStoreList() == null) ? new ArrayList<>() : response.body().getStoreList());
                    setLinks((response.body() == null || response.body().getFooterLinks() == null) ? null : response.body().getFooterLinks());
                    setMetaData((response.body() == null || response.body().getMetaData() == null) ? null : response.body().getMetaData());

                } else {
                    setStoreList(new ArrayList<>());
                    setLinks(null);
                    setMetaData(null);
                }
            }

            @Override
            public void onFailure(@NotNull Call<StoreData> call, @NotNull Throwable t) {
                setStoreList(new ArrayList<>());
                setLinks(null);
                setMetaData(null);
            }
        });
    }

    public void onStoreClick(View view, StoreItem store) {
        if (!AppUtils.isInternetEnabled(view.getContext())) {
            AppUtils.showToast(view.getContext(), view.getContext().getString(R.string.no_internet_toast), false);
            return;
        }

        Intent intent = new Intent(view.getContext(), AttendanceActivity.class);
        view.getContext().startActivity(intent);
    }
}
