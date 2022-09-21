package com.ccx.corecarbon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ccx.corecarbon.databinding.RecyclerItemOfflineListBinding;
import com.ccx.corecarbon.room_util.Form;

import java.util.List;

public class OfflineListAdapter extends RecyclerView.Adapter<OfflineListAdapter.OfflineListAdapterViewHolder> {

    private final Context context;
    private final OfflineListAdapterListener listener;
    private List<Form> list;

    public OfflineListAdapter(Context context, List<Form> list, OfflineListAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OfflineListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemOfflineListBinding binding = RecyclerItemOfflineListBinding.inflate(LayoutInflater.from(context), parent, false);
        binding.setCallback(listener);
        return new OfflineListAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineListAdapterViewHolder holder, int position) {
        Form form = null;
        try {
            form = list.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (form != null) {
            holder.binding.setForm(form);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<Form> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface OfflineListAdapterListener {
        void onClickItem(@Nullable Form form);

        void onSyncItem(@Nullable Form form);
    }

    public static class OfflineListAdapterViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerItemOfflineListBinding binding;

        public OfflineListAdapterViewHolder(@NonNull RecyclerItemOfflineListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
