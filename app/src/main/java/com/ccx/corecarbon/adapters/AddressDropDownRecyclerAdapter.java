package com.ccx.corecarbon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ccx.corecarbon.R;
import com.ccx.corecarbon.dialogs.AddressDropDownDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * CreatedBy Vijayakumar_KA On 18-Jun-2022 12:54 PM.
 */
public class AddressDropDownRecyclerAdapter extends RecyclerView.Adapter<AddressDropDownRecyclerAdapter.StatesRecyclerAdapterViewHolder> implements Filterable {

    private final AddressDropDownDialog.AddressDropDownDialogListener listener;
    private List<String> actualData;
    private List<String> filteredData;
    final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charSequenceString = constraint.toString();
            if (charSequenceString.isEmpty()) {
                filteredData = actualData;
            } else {
                List<String> filteredList = new ArrayList<>();
                for (String name : actualData) {
                    if (name.toLowerCase().contains(charSequenceString.toLowerCase())) {
                        filteredList.add(name);
                    }
                    filteredData = filteredList;
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                filteredData = (List<String>) results.values;
            } catch (Exception e) {
                e.printStackTrace();
            }
            notifyDataSetChanged();
        }
    };

    public AddressDropDownRecyclerAdapter(AddressDropDownDialog.AddressDropDownDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public StatesRecyclerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_address_drop_down_item, parent, false);
        return new StatesRecyclerAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StatesRecyclerAdapterViewHolder holder, int position) {
        String item = filteredData.get(position);
        holder.textView.setText(item == null ? "---" : item);
        holder.itemView.setOnClickListener(v -> listener.onAddressSelected(item));
    }

    @Override
    public int getItemCount() {
        return filteredData == null ? 0 : filteredData.size();
    }

    public void setData(List<String> data) {
        this.actualData = data;
        this.filteredData = data;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    static class StatesRecyclerAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        StatesRecyclerAdapterViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}