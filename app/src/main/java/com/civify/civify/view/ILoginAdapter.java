package com.civify.civify.view;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ILoginAdapter implements LoginAdapter {

    private static final String TAG = ILoginAdapter.class.getSimpleName();

    @Override
    public void login(String email, String password, LoginFinishedCallback loginFinishedCallback) {
        CivifyLoginService civifyLoginService =
                ServiceGenerator.createService(CivifyLoginService.class, email, password);
        Call<User> call = civifyLoginService.basicLogin();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Login succesful");
                    loginFinishedCallback.onLoginFinished(response.body());
                }
                else {
                    Log.e(TAG, "Login error");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}
