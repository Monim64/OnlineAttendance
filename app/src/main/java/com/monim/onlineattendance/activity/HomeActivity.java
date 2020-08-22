package com.monim.onlineattendance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.monim.onlineattendance.R;
import com.monim.onlineattendance.adapters.StoreListAdapter;
import com.monim.onlineattendance.databinding.ActivityHomeBinding;
import com.monim.onlineattendance.interfaces.OnItemClickListener;
import com.monim.onlineattendance.models.StoreItem;
import com.monim.onlineattendance.utils.AppUtils;
import com.monim.onlineattendance.view_models.StoreListViewModel;

import java.util.List;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {

    private StoreListViewModel storeListViewModel;
    private StoreListAdapter storeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storeListViewModel = ViewModelProviders.of(this).get(StoreListViewModel.class);
        storeListAdapter = new StoreListAdapter(this, storeListViewModel);

        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setLifecycleOwner(this);
        binding.setStoreViewModel(storeListViewModel);

        RecyclerView storeListView = binding.storeListRecyclerView;
        storeListView.setHasFixedSize(true);
        storeListView.setAdapter(storeListAdapter);
        storeListAdapter.setFooterOnClickListener(onFooterItemClickListener);

        SwipeRefreshLayout refreshLayout = binding.swipeToRefreshLayout;
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (storeListViewModel.getMetaData().getValue() != null) {
                    int currentPage = storeListViewModel.getMetaData().getValue().getCurrentPage();
                    getStoreList(currentPage > 0 ? currentPage : 1);

                } else {
                    getStoreList(1);
                }
                refreshLayout.setRefreshing(false);
            }
        });

        getStoreList(1);
    }

    private void getStoreList(int pageNumber) {
        if (!AppUtils.isInternetEnabled(this)) {
            AppUtils.showToast(this, getString(R.string.no_internet_toast), false);
            return;
        }

        storeListViewModel.getAllStores(pageNumber).observe(this, new Observer<List<StoreItem>>() {
            @Override
            public void onChanged(List<StoreItem> storeItems) {
                storeListAdapter.setStoreList(storeItems);
                storeListAdapter.notifyDataSetChanged();
            }
        });
    }

    private OnItemClickListener onFooterItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClickListener(View view, int pageNumber) {
            if (pageNumber == 0)
                return;

            getStoreList(pageNumber);
        }
    };

}