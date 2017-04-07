package com.civify.adapter;

import android.content.SharedPreferences;
import android.util.Log;

import com.civify.model.CivifyEmailCredentials;
import com.civify.model.CivifyUsernameCredentials;
import com.civify.model.User;
import com.civify.service.CivifyLoginService;
import com.civify.service.CivifyMeService;
import com.civify.utils.ServiceGenerator;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAdapterImpl implements LoginAdapter {

    public static final String NEEDS_LOGIN_MESSAGE = "Needs login";
    public static final String USER_NOT_EXISTS_MESSAGE = "User not exists";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
    private static final String AUTH_TOKEN = "authToken";
    private static final String AUTH_TOKEN_JSON = "auth_token";
    private static final String TAG = LoginAdapterImpl.class.getSimpleName();
    private LoginFinishedCallback mLoginFinishedCallback;
    private SharedPreferences mSharedPreferences;
    private CivifyLoginService mCivifyLoginService;
    private CivifyMeService mCivifyMeService;
    private String mFirstCredential;
    private String mPassword;
    private String mAuthToken;

    public LoginAdapterImpl(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    LoginAdapterImpl(CivifyLoginService civifyLoginService, CivifyMeService civifyMeService,
                      SharedPreferences sharedPreferences) {
        mCivifyLoginService = civifyLoginService;
        mCivifyMeService = civifyMeService;
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public void login(String firstCredential, String password,
            LoginFinishedCallback loginFinishedCallback) {
        this.mFirstCredential = firstCredential;
        this.mPassword = password;
        this.mLoginFinishedCallback = loginFinishedCallback;
        callLoginService();
    }

    public void logout() {
        mSharedPreferences.edit()
                .remove(AUTH_TOKEN)
                .apply();
    }

    public void isLogged(LoginFinishedCallback loginFinishedCallback) {
        this.mLoginFinishedCallback = loginFinishedCallback;
        callMeService();
    }

    private boolean hasToken() {
        mAuthToken = mSharedPreferences.getString(AUTH_TOKEN, "");
        return !mAuthToken.isEmpty();
    }

    private LoginError generateException(int statusCode) {
        switch (statusCode) {
            case HttpURLConnection.HTTP_NOT_FOUND:
                return new LoginError(LoginError.ErrorType.USER_NOT_EXISTS,
                        USER_NOT_EXISTS_MESSAGE);
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                return new LoginError(
                        LoginError.ErrorType.INVALID_CREDENTIALS,
                        INVALID_CREDENTIALS_MESSAGE);
            default:
                return null;
        }
    }

    private void callLoginService() {

        if (mCivifyLoginService == null) {
            mCivifyLoginService =
                    ServiceGenerator.getInstance().createService(CivifyLoginService.class);
        }
        Call<String> call;
        if (isEmail()) {
            call = mCivifyLoginService
                    .loginWithEmail(new CivifyEmailCredentials(mFirstCredential,
                            getPassHash(mPassword)));
        } else {
            call = mCivifyLoginService
                    .loginWithUsername(new CivifyUsernameCredentials(mFirstCredential,
                            getPassHash(mPassword)));
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    fetchToken(response.body());
                    callMeService();
                } else {
                    mLoginFinishedCallback.onLoginFailed(generateException(response.code()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private boolean isEmail() {
        return mFirstCredential.contains("@");
    }

    private void callMeService() {
        if (hasToken()) {
            if (mCivifyMeService == null) {
                mCivifyMeService =
                        ServiceGenerator.getInstance().createService(CivifyMeService.class);
            }
            Call<User> call =
                    mCivifyMeService.getUser(mAuthToken);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        mLoginFinishedCallback.onLoginSucceeded(response.body());
                    } else {
                        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            mLoginFinishedCallback.onLoginFailed(
                                    new LoginError(LoginError.ErrorType.NOT_LOGGED_IN,
                                            NEEDS_LOGIN_MESSAGE));
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            mLoginFinishedCallback.onLoginFailed(
                    new LoginError(LoginError.ErrorType.NOT_LOGGED_IN, NEEDS_LOGIN_MESSAGE));
        }
    }

    private void storeToken() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(AUTH_TOKEN, mAuthToken);
        editor.apply();
    }

    private void fetchToken(String body) {
        JSONObject tokenAsJson = null;
        try {
            tokenAsJson = new JSONObject(body);
            mAuthToken = tokenAsJson.get(AUTH_TOKEN_JSON).toString();
            storeToken();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getPassHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(String.valueOf(password).getBytes("UTF-8"));
            return String.format("%064x", new BigInteger(1, md.digest()));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
