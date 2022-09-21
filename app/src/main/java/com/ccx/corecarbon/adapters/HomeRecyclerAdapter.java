package com.ccx.corecarbon.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ccx.corecarbon.activities.CookStoveFormActivity;
import com.ccx.corecarbon.databinding.HomeItemLayoutBinding;
import com.ccx.corecarbon.models.HomeModel;

import java.util.ArrayList;
import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {

    //    private final List<HomeModel> listfull;
    private final Context context;
    private List<HomeModel> list;
    /*private final Filter listFilter = new Filter() {
        @Override
        public FilterResults performFiltering(CharSequence charSequence) {
            List<HomeModel> filteredlist = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredlist.addAll(HomeRecyclerAdapter.this.listfull);
            } else {
                String filterpattern = charSequence.toString().toLowerCase().trim();
                for (HomeModel item : HomeRecyclerAdapter.this.listfull) {
                    if (item.mobile.toLowerCase().contains(filterpattern)) {
                        filteredlist.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredlist;
            return results;
        }

        @Override
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            HomeRecyclerAdapter.this.list.clear();
            HomeRecyclerAdapter.this.list.addAll((List) filterResults.values);
            HomeRecyclerAdapter.this.notifyDataSetChanged();
        }
    };*/
    private boolean isCompletedData;
    private boolean isAssignedData;

    public HomeRecyclerAdapter(@NonNull Context context) {
        this.context = context;
//        this.listfull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public HomeRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(HomeItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerAdapter.MyViewHolder holder, int position) {
        HomeModel model = null;
        try {
            model = list.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (model != null) {
            holder.bind(model);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    //    public Filter getFilter() {
//        return this.listFilter;
//    }
    public void setList(List<HomeModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(@NonNull List<HomeModel> list) {
        final int count = getItemCount();
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyItemRangeInserted(count - 1, list.size());
    }

    public void setAssignedData(boolean assignedData) {
        isAssignedData = assignedData;
    }

    public void setCompletedData(boolean completedData) {
        isCompletedData = completedData;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final HomeItemLayoutBinding binding;

        public MyViewHolder(@NonNull HomeItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.constraintLayout.setOnClickListener(this);
        }

        public void bind(@NonNull HomeModel model) {
            binding.setModel(model);
            binding.setIsCompleted(isCompletedData);
            binding.setIsAssigned(isAssignedData);
            binding.nameTv.setText(model.name);
            binding.mobileTv.setText(model.mobile);
        }

        @Override
        public void onClick(View v) {
            final HomeModel model = binding.getModel();
            if (model == null) {
                return;
            }
            Intent intent = new Intent(context, CookStoveFormActivity.class);
            intent.putExtra("edit", true);
            intent.putExtra("name", model.name);
            intent.putExtra("_id", model._id);
            intent.putExtra("mobileNumber", model.mobile);
            intent.putExtra("userPhoto", model.userPhoto);
            intent.putExtra("mobileType", model.mobileType);
            intent.putExtra("aadharNumber", model.aadharNumber);
            intent.putExtra("aadharPhoto", model.idPhoto);
            intent.putExtra("housePhoto", model.housePhoto);
            intent.putExtra("bankAccountNumber", model.bankAccountNumber);
            intent.putExtra("passbookphoto", model.bankpassbook);
            intent.putExtra("newtove", model.newPhoto);
            intent.putExtra("tradiphoto", model.tradiPhoto);
            intent.putExtra("agreement", model.agreementPhoto);
            intent.putExtra("serialImage", model.serialNumberPhoto);
            intent.putExtra("bankBranch", model.bankBranch);
            intent.putExtra("bankIfsc", model.bankIfsc);
            intent.putExtra("bankName", model.bankName);
            intent.putExtra("geoLocation", model.geoLocation);
            intent.putExtra("district", model.district);
            intent.putExtra("houseNumber", model.houseNumber);
            intent.putExtra("kyc", model.kyc);
            intent.putExtra("mandal", model.mandal);
            intent.putExtra("manufacturer", model.manufacturer);
            intent.putExtra("personCount", model.personCount);
            intent.putExtra("pincode", model.pincode);
            intent.putExtra("serialNumber", model.serialNumber);
            intent.putExtra("state", model.state);
            intent.putExtra("typeofStove", model.typeofStove);
            intent.putExtra("village", model.village);
            intent.putExtra("dateOfDistribution", model.dateOfDistribution);
            intent.putExtra("dateofBirth", model.dateofBirth);
            context.startActivity(intent);
        }
    }
}
