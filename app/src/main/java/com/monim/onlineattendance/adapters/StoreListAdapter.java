package com.monim.onlineattendance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.monim.onlineattendance.R;
import com.monim.onlineattendance.databinding.StoreLayoutBinding;
import com.monim.onlineattendance.databinding.StoreListFooterBinding;
import com.monim.onlineattendance.interfaces.OnItemClickListener;
import com.monim.onlineattendance.models.StoreItem;
import com.monim.onlineattendance.view_models.StoreListViewModel;

import java.util.List;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.StoreListViewHolder> {

    private final int NORMAL = 1;
    private final int FOOTER = 2;

    private Context context;
    //    private List<StoreItem> storeList;
    private StoreListViewModel storeListViewModel;
    private OnItemClickListener onItemClickListener;

    public StoreListAdapter(Context context, StoreListViewModel storeListViewModel) {
        this.context = context;
        this.storeListViewModel = storeListViewModel;
    }

    private static final DiffUtil.ItemCallback<StoreItem> diffCallback = new DiffUtil.ItemCallback<StoreItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull StoreItem oldStore, @NonNull StoreItem newStore) {
            return oldStore.getStoreId() == newStore.getStoreId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull StoreItem oldStore, @NonNull StoreItem newStore) {
            return oldStore.getStoreName().equals(newStore.getStoreName()) && oldStore.getStoreAddress().equals(newStore.getStoreAddress());
        }
    };

    private AsyncListDiffer<StoreItem> storeList = new AsyncListDiffer<>(this, diffCallback);

    public void setStoreList(List<StoreItem> storeList) {
        this.storeList.submitList(storeList);
    }

    public void setFooterOnClickListener(OnItemClickListener onClickListener) {
        onItemClickListener = onClickListener;
    }

    @NonNull
    @Override
    public StoreListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            StoreListFooterBinding footerBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.store_list_footer, parent, false);
            return new StoreListViewHolder(footerBinding);
        }
        StoreLayoutBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.store_layout, parent, false);
        return new StoreListViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreListViewHolder holder, int position) {
        if (getItemViewType(position) == FOOTER) {
            holder.bindFooterView();

        } else {
            StoreItem store = storeList.getCurrentList().get(position);
            holder.bindView(store);
        }
    }

    @Override
    public int getItemCount() {
        return (storeList == null || storeList.getCurrentList().size() == 0) ? 0 : (storeList.getCurrentList().size() + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == storeList.getCurrentList().size()) {
            return FOOTER;
        }

        return NORMAL;
    }

    class StoreListViewHolder extends RecyclerView.ViewHolder {

        private StoreLayoutBinding itemBinding;
        private StoreListFooterBinding footerBinding;

        public StoreListViewHolder(StoreLayoutBinding binding) {
            super(binding.getRoot());

            itemBinding = binding;
        }

        public void bindView(StoreItem item) {
            itemBinding.setStoreViewModel(storeListViewModel);
            itemBinding.setStoreItem(item);
            itemBinding.executePendingBindings();
        }

        public StoreListViewHolder(StoreListFooterBinding binding) {
            super(binding.getRoot());

            footerBinding = binding;
        }

        public void bindFooterView() {
            footerBinding.setStoreViewModel(storeListViewModel);
            footerBinding.executePendingBindings();

            footerBinding.startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (storeListViewModel.getMetaData().getValue() == null)
                        return;

                    onItemClickListener.onItemClickListener(v, storeListViewModel.getMetaData().getValue().startEnabled() ? 1 : 0);
                }
            });

            footerBinding.previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (storeListViewModel.getMetaData().getValue() == null)
                        return;

                    onItemClickListener.onItemClickListener(v,
                            storeListViewModel.getMetaData().getValue().previousEnabled()
                                    ? storeListViewModel.getMetaData().getValue().getCurrentPage() - 1
                                    : 0);
                }
            });

            footerBinding.nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (storeListViewModel.getMetaData().getValue() == null)
                        return;

                    onItemClickListener.onItemClickListener(v, storeListViewModel.getMetaData().getValue().nextEnabled()
                            ? storeListViewModel.getMetaData().getValue().getCurrentPage() + 1
                            : 0);
                }
            });

            footerBinding.endButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (storeListViewModel.getMetaData().getValue() == null)
                        return;

                    onItemClickListener.onItemClickListener(v, storeListViewModel.getMetaData().getValue().endEnabled()
                            ? storeListViewModel.getMetaData().getValue().getLastPage()
                            : 0);
                }
            });
        }
    }

}
