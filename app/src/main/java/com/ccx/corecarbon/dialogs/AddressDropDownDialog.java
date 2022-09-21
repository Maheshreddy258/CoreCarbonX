package com.ccx.corecarbon.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import com.ccx.corecarbon.adapters.AddressDropDownRecyclerAdapter;
import com.ccx.corecarbon.R;
import com.ccx.corecarbon.util.CoreCarbonSharedPreferences;
import com.ccx.corecarbon.databinding.DialogAddressDropDownBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * CreatedBy Vijayakumar_KA On 18-Jun-2022 01:43 PM.
 */
public class AddressDropDownDialog extends Dialog implements SearchView.OnQueryTextListener {
    private final DialogAddressDropDownBinding binding;
    private final AddressDropDownRecyclerAdapter adapter;
    private final AddressDropDownDialogListener listener;
    private final AddressType addressType;
    private final String filterValue;
    //
    private final Handler handler;
    private final Runnable runnable;
    private boolean searchSubmitHandled = false;
    public AddressDropDownDialog(@NonNull Context context, @NonNull AddressType addressType, @NonNull String filterValue, @NonNull AddressDropDownDialogListener listener) {
        super(context, R.style.Theme_Dialog_90Percentage);
        this.addressType = addressType;
        this.filterValue = filterValue;
        this.listener = listener;
        binding = DialogAddressDropDownBinding.inflate(LayoutInflater.from(context));
        adapter = new AddressDropDownRecyclerAdapter(stateName -> {
            dismiss();
            this.listener.onAddressSelected(stateName);
        });
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);
        binding.searchView.setOnQueryTextListener(this);
        setContentView(binding.getRoot());
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        handler = new Handler();
        runnable = () -> {
            if (searchSubmitHandled) {
                searchSubmitHandled = false;
                return;
            }
            try {
                final String query = binding.searchView.getQuery() != null ? binding.searchView.getQuery().toString() : "";
                adapter.getFilter().filter(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (addressType == AddressType.STATE) {
            binding.searchView.setQueryHint("Search State");
            getStates();
        } else if (addressType == AddressType.DISTRICT) {
            binding.searchView.setQueryHint("Search District");
            getDistricts();
        } else if (addressType == AddressType.MANDAL) {
            binding.searchView.setQueryHint("Search Mandal/Block");
            getMandals();
        } else {
            binding.searchView.setQueryHint("Search Village");
            getVillages();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        searchSubmitHandled = false;
        adapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 300);
        return true;
    }

    private void getStates() {
        new Thread(() -> {
            List<String> states = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(CoreCarbonSharedPreferences.getStates());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    states.add(jsonObject1.getString("stateName"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (binding != null && adapter != null) {
                binding.recyclerView.post(() -> adapter.setData(states));
            }
        }).start();
    }

    private void getDistricts() {
        new Thread(() -> {
            List<String> districts = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(CoreCarbonSharedPreferences.getDistricts());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject stateIdObject = jsonObject.getJSONObject("stateId");
                    String stateName = stateIdObject.getString("stateName");
                    if (stateName.contentEquals(filterValue)) {
                        String districtName = jsonObject.getString("districtName");
                        districts.add(districtName);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (binding != null && adapter != null) {
                binding.recyclerView.post(() -> adapter.setData(districts));
            }
        }).start();
    }

    private void getMandals() {
        new Thread(() -> {
            List<String> mandals = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(CoreCarbonSharedPreferences.getMandals());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject stateIdObject = jsonObject.getJSONObject("districtId");
                    String districtName = stateIdObject.getString("districtName");
                    if (districtName.contentEquals(filterValue)) {
                        String mandalName = jsonObject.getString("mandalName");
                        mandals.add(mandalName);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (binding != null && adapter != null) {
                binding.recyclerView.post(() -> adapter.setData(mandals));
            }
        }).start();

    }

    private void getVillages() {
        new Thread(() -> {
            List<String> villages = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(CoreCarbonSharedPreferences.getVillages());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject mandalObject = jsonObject.getJSONObject("mandalId");
                    String mandalName = mandalObject.getString("mandalName");
                    if (mandalName.contentEquals(filterValue)) {
                        villages.add(jsonObject.getString("villageName"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (binding != null && adapter != null) {
                binding.recyclerView.post(() -> adapter.setData(villages));
            }
        }).start();
    }

    public enum AddressType {
        STATE,
        DISTRICT,
        MANDAL,
        VILLAGE
    }

    public interface AddressDropDownDialogListener {
        void onAddressSelected(String name);
    }
}
