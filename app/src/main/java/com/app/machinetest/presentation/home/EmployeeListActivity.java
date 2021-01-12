package com.app.machinetest.presentation.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.machinetest.R;
import com.app.machinetest.Utilities.ConstantMethods;
import com.app.machinetest.Utilities.DBHelper;
import com.app.machinetest.models.EmployeeListModel;
import com.app.machinetest.services.ApiInterface;
import com.app.machinetest.services.Client;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeListActivity extends AppCompatActivity implements EmployeeItemClickListener {

    ImageView ivBack;
    EditText etSearch;

    Context context;
    boolean doubleBackToExit = false;
    EmployeeListAdapter employeeListAdapter;
    RecyclerView rvEmployees;
    LinearLayoutManager layoutManager;
    List<EmployeeListModel> modelList = new ArrayList<>();
    private DBHelper mydb;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        initValues();
    }

    private void initValues() {
        context = EmployeeListActivity.this;
        ivBack = findViewById(R.id.ivBack);
        etSearch = findViewById(R.id.etSearch);
        rvEmployees = findViewById(R.id.rvEmployees);
        progressBar = findViewById(R.id.progressBar);
        mydb = new DBHelper(this);

        if (mydb.getAllEmployees().size() > 0) {
            for (int i = 0; i < mydb.getAllEmployees().size(); i++) {
                Gson gson = new Gson();
                EmployeeListModel employeeListModel = gson.fromJson(mydb.getAllEmployees().get(i),
                        EmployeeListModel.class);

                modelList.add(employeeListModel);
                Log.e("my db retrieve", "" + i);

            }
            setAdapter(modelList);
        } else {
            callApiToGetEmployees();
        }


        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    void filter(String text) {
        List<EmployeeListModel> temp = new ArrayList();
        for (EmployeeListModel model : modelList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (model.getName().contains(text) || model.getEmail().contains(text)) {
                temp.add(model);
            }
        }
        //update recyclerview
        employeeListAdapter.updateList(temp);
    }

    private void callApiToGetEmployees() {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface serviceAPI = Client.getClient();
        Call<List<EmployeeListModel>> loadSizeCall = serviceAPI.getEmployees();
        loadSizeCall.enqueue(new Callback<List<EmployeeListModel>>() {
            @Override
            public void onResponse(Call<List<EmployeeListModel>> call, Response<List<EmployeeListModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response != null && response.body().size() > 0) {
                    modelList = response.body();
                    setAdapter(modelList);
                    saveDataOffline();
                }

            }

            @Override
            public void onFailure(Call<List<EmployeeListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ConstantMethods.showErrorInfo(context, getString(R.string.something_wrong));
            }
        });
    }

    private void saveDataOffline() {


        for (int i = 0; i < modelList.size(); i++) {
            Gson gson = new Gson();
            String json = gson.toJson(modelList.get(i));
            if (mydb.insertEmployee(json)) {
                Log.e("DB", " entered " + i);

            } else {
                Log.e("DB", "not entered " + i);

            }
        }

    }


    private void setAdapter(List<EmployeeListModel> response) {
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvEmployees.setLayoutManager(layoutManager);
        employeeListAdapter = new EmployeeListAdapter(context, response, this);
        rvEmployees.setAdapter(employeeListAdapter);

    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity();
        } else {
            ConstantMethods.showToast(context, getString(R.string.press_again_exit_msg));
        }
        //close drawer here
        this.doubleBackToExit = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, 2000);
    }

    @Override
    public void itemClick(int pos, EmployeeListModel model) {
        Intent intent = new Intent(context, EmployeeDetailActivity.class);
        intent.putExtra("EmployeeItem", model);
        startActivity(intent);


    }
}