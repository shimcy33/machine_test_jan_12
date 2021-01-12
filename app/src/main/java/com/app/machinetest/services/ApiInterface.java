package com.app.machinetest.services;


import com.app.machinetest.models.EmployeeListModel;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("/v2/5d565297300000680030a986")
    Call<List<EmployeeListModel>> getEmployees();
}
