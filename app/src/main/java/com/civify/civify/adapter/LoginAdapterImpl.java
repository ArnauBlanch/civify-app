package com.civify.civify.adapter;

import android.content.SharedPreferences;
import android.util.Log;

import com.civify.civify.model.CivifyEmailCredentials;
import com.civify.civify.model.CivifyUsernameCredentials;
import com.civify.civify.model.User;
import com.civify.civify.service.CivifyLoginService;
import com.civify.civify.service.CivifyMeService;
import com.civify.civify.utils.ServiceGenerator;

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



@SuppressWarnings("LawOfDemeter")
public class LoginAdapterImpl implements LoginAdapter {

    public static final String AUTH_TOKEN = "authToken";
    private static final String TAG = LoginAdapterImpl.class.getSimpleName();
    private static final String AUTH_TOKEN_JSON = "auth_token";
    private static final String NEEDS_LOGIN_MESSAGE = "Needs login";
    private static final String USER_NOT_EXISTS_MESSAGE = "User not exists";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
    private LoginFinishedCallback mLoginFinishedCallback;
    private SharedPreferences mSharedPreferences;
    private String mFirstCredential;
    private String mPassword;
    private String mAuthToken;

    public LoginAdapterImpl(SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
    }

    @Override
    public void login(String firtsCredential, String password,
                      LoginFinishedCallback loginFinishedCallback) {
        this.mFirstCredential = firtsCredential;
        this.mPassword = password;
        this.mLoginFinishedCallback = loginFinishedCallback;
        callLoginService();
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

        CivifyLoginService civifyLoginService =
                ServiceGenerator.getInstance().createLoginService();
        Call<String> call;
        if (isEmail()) {
            call = civifyLoginService
                    .loginWithEmail(new CivifyEmailCredentials(mFirstCredential,
                            getPassHash(mPassword)));
        } else {
            call = civifyLoginService
                    .loginWithUsername(new CivifyUsernameCredentials(mFirstCredential,
                            getPassHash(mPassword)));
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Not_sent: Login succesful token: " + response.body());
                    fetchToken(response.body());
                    callMeService();
                } else {
                    Log.e(TAG, "Login error using login service" + response.code());
                    mLoginFinishedCallback.onLoginFailed(generateException(response.code()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Not_sent: Login error using login service" + t.getMessage());
            }
        });

    }

    private boolean isEmail() {
        return mFirstCredential.contains("@");
    }

    private void callMeService() {
        if (hasToken()) {
            CivifyMeService civifyMeService =
                    ServiceGenerator.getInstance().createService(CivifyMeService.class);
            Call<User> call =
                    civifyMeService.getUser(mAuthToken);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "User obtained using Me Service");
                        mLoginFinishedCallback.onLoginSucceeded(response.body());
                    } else {
                        Log.e(TAG, "Not_sent: Me error on response" + response.code()
                                + response.message());
                        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            mLoginFinishedCallback.onLoginFailed(
                                    new LoginError(LoginError.ErrorType.NOT_LOGGED_IN,
                                            NEEDS_LOGIN_MESSAGE));
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(TAG, "Not_sent: Me error" + t.getMessage());
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
