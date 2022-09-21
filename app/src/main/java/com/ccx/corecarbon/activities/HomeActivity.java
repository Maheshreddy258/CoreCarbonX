package com.ccx.corecarbon.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccx.corecarbon.R;
import com.ccx.corecarbon.adapters.HomeRecyclerAdapter;
import com.ccx.corecarbon.databinding.ActivityHomeBinding;
import com.ccx.corecarbon.models.HomeDataModel;
import com.ccx.corecarbon.models.HomeModel;
import com.ccx.corecarbon.room_util.AppDatabase;
import com.ccx.corecarbon.util.Constants;
import com.ccx.corecarbon.util.CoreCarbonSharedPreferences;
import com.ccx.corecarbon.util.NetworkUtil;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";
    private final int MAX_ITEM_PER_PAGE = 20;
    private ActivityHomeBinding binding;
    //    private List<HomeModel> list = new ArrayList<>();
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private String searchQuery;
    private List<HomeDataModel> homelist = new ArrayList<>();

    private int completedPageNo = 0;
    private int unCompletedPageNo = 0;

    //    private boolean isCompletedPageEnd = false;
//    private boolean isUncompletedPageEnd = false;
    private int completedItemCount = 0;
    private int unCompletedItemCount = 0;

    private boolean isPageLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        Toolbar toolbar2 = findViewById(R.id.tool_bar);
        this.toolbar = toolbar2;
        TextView title = toolbar2.findViewById(R.id.title_tv);
        final String userName = CoreCarbonSharedPreferences.getUserName();
        if (userName == null || userName.trim().isEmpty()) {
            title.setText(R.string.cook_stove);
        } else {
            title.setText(userName);
        }
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        progressDialog = new ProgressDialog(this);
        binding.addBtn.setOnClickListener(view -> {
            if (!NetworkUtil.isOnline(this)) {
                AppDatabase appDatabase = AppDatabase.getInstance(this);
                long totalCount = appDatabase.formDao().getOfflineRecordCount();
                if (totalCount >= 30) {
                    Toast.makeText(this, "You have reached maximum offline records 30! Sync the offline records then add new one!!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Intent intent = new Intent(HomeActivity.this, CookStoveFormActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        });

        homeRecyclerAdapter = new HomeRecyclerAdapter(this);
        homeRecyclerAdapter.setCompletedData(binding.tabLayout.getSelectedTabPosition() == 0);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(homeRecyclerAdapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isPageLoading) {
                    return;
                }
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    return;
                }
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) {
                    return;
                }
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                ) {
                    if (binding.tabLayout.getSelectedTabPosition() == 0) {
//                        if (!isCompletedPageEnd) {
                        if (totalItemCount < completedItemCount) {
                            isPageLoading = true;
                            new Handler().postDelayed(() -> GetList(false), 300);
                        }
                    } else {
//                        if (!isUncompletedPageEnd) {
                        if (totalItemCount < unCompletedItemCount) {
                            isPageLoading = true;
                            new Handler().postDelayed(() -> GetList(false), 300);
                        }
                    }
                }
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                homeRecyclerAdapter.setCompletedData(tab.getPosition() == 0);
                GetList(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                viewModel.setTabPosition(tab.getPosition());
//                adapter.setData(viewModel.getAll());
            }
        });
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        GetList(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
        EditText editText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(Color.WHITE);

        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + "Enter mobile number..." + "</font>"));

        searchView.setIconified(false);
        MenuItemCompat.setShowAsAction(menuItem, 9);
        MenuItemCompat.setActionView(menuItem, (View) searchView);
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(view -> {
            searchView.setQuery("", false);
            searchView.onActionViewCollapsed();
            searchView.setIconified(true);
            searchQuery = "";
            GetList(true);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                GetList(true);
                return false;
            }

            public boolean onQueryTextChange(String newText) {
               /* if (HomeActivity.this.homeRecyclerAdapter == null) {
                    return false;
                }
                HomeActivity.this.homeRecyclerAdapter.getFilter().filter(newText);*/
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.logout == item.getItemId()) {
            gotoNextActivity(LoginActivity.class, true);
            return true;
        }
        if (R.id.offline_data == item.getItemId()) {
            gotoNextActivity(OfflineListActivity.class, false);
            return true;
        }
        if (R.id.assigned_list == item.getItemId()) {
            gotoNextActivity(AssignedListActivity.class, false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void GetList(boolean resetPageNo) {
        if (resetPageNo) {
            completedPageNo = 0;
            unCompletedPageNo = 0;
        }
        if (!NetworkUtil.isOnline(this)) {
            isPageLoading = false;
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        isPageLoading = true;
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //
        //
        //
        //
        final AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody);
                    JSONObject jsonObjectRoot = new JSONObject(str);
                    if (jsonObjectRoot.getBoolean("error")) {
                        binding.nodataTv.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (jsonObjectRoot.has("count")) {
                        if (binding.tabLayout.getSelectedTabPosition() == 0) {
                            completedItemCount = jsonObjectRoot.getInt("count");
                        } else {
                            unCompletedItemCount = jsonObjectRoot.getInt("count");
                        }
                    } else {
                        if (binding.tabLayout.getSelectedTabPosition() == 0) {
                            completedItemCount = 0;
                        } else {
                            unCompletedItemCount = 0;
                        }
                    }
//                    String message = jsonObject.getString("message");
                    List<HomeModel> list = new ArrayList<>();
                    JSONArray jsonArray = jsonObjectRoot.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(new HomeModel(jsonArray.getJSONObject(i)));
                    }
//                            Collections.reverse(list);
                    if (binding.tabLayout.getSelectedTabPosition() == 0) {
                        if (completedPageNo == 1) {
                            homeRecyclerAdapter.setList(list);
                        } else {
                            homeRecyclerAdapter.addList(list);
                        }
                    } else {
                        if (unCompletedPageNo == 1) {
                            homeRecyclerAdapter.setList(list);
                        } else {
                            homeRecyclerAdapter.addList(list);
                        }
                    }
                    if (list.size() == 0) {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.nodataTv.setVisibility(View.VISIBLE);
                    } else {
//                        if (list.size() < MAX_ITEM_PER_PAGE) {
//                            if (binding.tabLayout.getSelectedTabPosition() == 0) {
//                                isCompletedPageEnd = true;
//                            } else {
//                                isUncompletedPageEnd = true;
//                            }
//                        } else {
//                            if (binding.tabLayout.getSelectedTabPosition() == 0) {
//                                isCompletedPageEnd = false;
//                            } else {
//                                isUncompletedPageEnd = false;
//                            }
//                        }
                        binding.nodataTv.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (binding.tabLayout.getSelectedTabPosition() == 0) {
                        --completedPageNo;
                    } else {
                        --unCompletedPageNo;
                    }
                } finally {
                    isPageLoading = false;
                    progressDialog.dismiss();
                    setTabCount();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                setTabCount();
                isPageLoading = false;
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                if (binding.tabLayout.getSelectedTabPosition() == 0) {
                    --completedPageNo;
                } else {
                    --unCompletedPageNo;
                }
            }
        };
        //
        //
        //
        //
        final boolean isPost;
        RequestParams requestParams = new RequestParams();
        requestParams.put("createdBy._id_like", CoreCarbonSharedPreferences.getNewId());
        requestParams.put("sort", "yes");
        final String url;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            isPost = true;
            url = Constants.PRODUCTION_URL + "api/generic/search/CookStove";
            requestParams.put("_page", 1);
            requestParams.put("_limit", MAX_ITEM_PER_PAGE);
            requestParams.put("mobileNumber", searchQuery);
        } else {
            isPost = false;
            url = Constants.PRODUCTION_URL + "api/cookStove/all/CookStove";
            requestParams.put("_limit", MAX_ITEM_PER_PAGE);
            if (binding.tabLayout.getSelectedTabPosition() == 0) {
                requestParams.put("_page", ++completedPageNo);
            } else {
                requestParams.put("_page", ++unCompletedPageNo);
            }
        }
        if (binding.tabLayout.getSelectedTabPosition() == 0) {
            requestParams.put("photo_like", "ExistsTrue");
        } else {
            requestParams.put("photo_like", "ExistsFalse");
        }
        final AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-access-token", CoreCarbonSharedPreferences.getToken());
        if (isPost) {
            client.post(url, requestParams, asyncHttpResponseHandler);
        } else {
            client.get(url, requestParams, asyncHttpResponseHandler);
        }
        Log.i(TAG, "GetList:" + url);
    }

    private void setTabCount() {
        TabLayout.Tab tab = binding.tabLayout.getTabAt(binding.tabLayout.getSelectedTabPosition());
        if (tab != null) {
            if (binding.tabLayout.getSelectedTabPosition() == 0) {
                tab.setText("Completed(" + homeRecyclerAdapter.getItemCount() + "/" + completedItemCount + ")");
            } else {
                tab.setText("Incompleted(" + homeRecyclerAdapter.getItemCount() + "/" + unCompletedItemCount + ")");
            }
        }
    }
}