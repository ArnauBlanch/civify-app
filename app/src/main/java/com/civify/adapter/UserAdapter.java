package com.civify.adapter;

import android.support.annotation.NonNull;

import com.civify.model.MessageResponse;
import com.civify.model.User;
import com.civify.service.UserService;
import com.civify.utils.ServiceGenerator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter {
    public static final int INVALID = 0;
    public static final int USED = 1;
    public static final int VALID_UNUSED = 2;
    public static final String USER_CREATED = "User created";
    public static final String USER_NOT_CREATED = "User not created";
    public static final String USER_EXISTS = "User exists";
    public static final String USER_DOESNT_EXIST = "User not exists";

    private UserService mUserService;


    public UserAdapter() {
        this(ServiceGenerator.getInstance().createService(UserService.class));
    }

    UserAdapter(UserService userService) {
        mUserService = userService;
    }

    public void setService(UserService userService) {
        mUserService = userService;
    }

    public void registerUser(User user, final SimpleCallback callback) {
        user.setPassword(getPassHash(user.getPassword()));
        user.setPasswordConfirmation(getPassHash(user.getPasswordConfirmation()));

        Call<MessageResponse> call = mUserService.registerUser(user);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED
                        && response.body().getMessage().equals(USER_CREATED)) {
                    callback.onSuccess();
                } else if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST
                        && getMessageFromError(response.errorBody()).equals(USER_NOT_CREATED)) {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void checkValidUnusedEmail(String email, final ValidationCallback callback) {
        if (!checkValidEmail(email)) {
            callback.onValidationResponse(INVALID);
            return;
        }
        checkUnusedEmail(email, callback);
    }

    public void checkValidUnusedUsername(String username, final ValidationCallback callback) {
        if (!checkValidUsername(username)) {
            callback.onValidationResponse(INVALID);
            return;
        }
        checkUnusedUsername(username, callback);
    }

    public boolean checkValidPassword(@NonNull String password) {
        // Between 8 and 40 characters long, at least one digit, one lowercase character and
        // one uppercase character (valid special characters: @ & # $ %)
        return password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9@&#$%]{8,40}$");
    }

    private void checkUnusedUsername(String username, final ValidationCallback callback) {
        JsonObject usernameJson = new JsonObject();
        usernameJson.addProperty("username", username);

        Call<MessageResponse> call = mUserService.checkUnusedUsername(usernameJson);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK
                        && response.body().getMessage().equals(USER_EXISTS)) {
                    callback.onValidationResponse(USED);
                } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND
                        && getMessageFromError(response.errorBody()).equals(USER_DOESNT_EXIST)) {
                    callback.onValidationResponse(VALID_UNUSED);
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void checkUnusedEmail(String email, final ValidationCallback callback) {
        JsonObject emailJson = new JsonObject();
        emailJson.addProperty("email", email);

        Call<MessageResponse> call = mUserService.checkUnusedEmail(emailJson);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK
                        && response.body().getMessage().equals(USER_EXISTS)) {
                    callback.onValidationResponse(USED);
                } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND
                        && getMessageFromError(response.errorBody()).equals(USER_DOESNT_EXIST)) {
                    callback.onValidationResponse(VALID_UNUSED);
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private boolean checkValidEmail(@NonNull String email) {
        return email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }

    private boolean checkValidUsername(@NonNull String username) {
        return username.matches("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
    }

    private String getPassHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(String.valueOf(password).getBytes("UTF-8"));
            return String.format("%064x", new BigInteger(1, md.digest()));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return password;
    }

    private String getMessageFromError(ResponseBody errorBody) {
        try {
            return (new JsonParser().parse(errorBody.string()).getAsJsonObject()).get("message")
                    .getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
