package com.example.project_prm392.API.BankAPI;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BankService {
    @GET("v2/banks")
    Call<BankResponse> getBanks();
}
