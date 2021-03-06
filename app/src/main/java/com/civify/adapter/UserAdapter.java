package com.civify.adapter;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.civify.activity.fragments.RewardDialogFragment;
import com.civify.model.MessageResponse;
import com.civify.model.Reward;
import com.civify.model.User;
import com.civify.service.UserService;
import com.civify.utils.ListenerQueue;
import com.civify.utils.ServiceGenerator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter {

    public static final int INVALID = 0;
    public static final int USED = 1;
    public static final int VALID_UNUSED = 2;
    public static final int EMAIL_SENT_CODE = 3;
    public static final int EMAIL_NOT_SENT_CODE = 4;
    public static final String TAG = UserAdapter.class.getSimpleName();
    public static final String USER_CREATED = "User created";
    public static final String USER_NOT_CREATED = "User not created";
    public static final String USER_EXISTS = "User exists";
    public static final String USER_DOESNT_EXIST = "User not exists";
    public static final String EMAIL_SENT = "Email sent";
    public static final Pattern VALID_PASSWORD = Pattern.compile(
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9@&#$%]{8,40}$");
    public static final Pattern VALID_EMAIL = Pattern.compile(
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"
                    + "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    public static final Pattern VALID_USERNAME = Pattern.compile(
            "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
    private static final String EMAIL_STRING = "email";

    private static final ListenerQueue ON_CURRENT_USER_UPDATE = new ListenerQueue();
    private static User sCurrentUser;
    private String mAuthToken;

    private UserService mUserService;

    protected UserAdapter(UserService userService) {
        mUserService = userService;
    }

    public UserAdapter(SharedPreferences sharedPreferences) {
        this(ServiceGenerator.getInstance().createService(UserService.class), sharedPreferences);
    }

    public UserAdapter(UserService service, SharedPreferences sharedPreferences) {
        this(service);
        this.mAuthToken = sharedPreferences.getString(LoginAdapterImpl.AUTH_TOKEN, "");
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
                    callback.onFailure(response.body().getMessage());
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
        return VALID_PASSWORD.matcher(password).matches();
    }

    public void sentResetPasswrdEmail(@NonNull String email, final ValidationCallback callback) {
        JsonObject passwordResetEmail = new JsonObject();
        JsonObject emailJson = new JsonObject();
        emailJson.addProperty(EMAIL_STRING, email);
        passwordResetEmail.add("password_reset", emailJson);

        Log.d(TAG, "Sending JSON for password reset: " + passwordResetEmail);
        Call<MessageResponse> call = mUserService.sendResetPasswrdMail(passwordResetEmail);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK
                        && response.body().getMessage().equals(EMAIL_SENT)) {
                    callback.onValidationResponse(EMAIL_SENT_CODE);
                } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND
                        && getMessageFromError(response.errorBody()).equals(USER_DOESNT_EXIST)) {
                    callback.onValidationResponse(EMAIL_NOT_SENT_CODE);
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

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
        emailJson.addProperty(EMAIL_STRING, email);

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
        return VALID_EMAIL.matcher(email).matches();
    }

    private boolean checkValidUsername(@NonNull String username) {
        return VALID_USERNAME.matcher(username).matches();
    }

    private String getPassHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(String.valueOf(password).getBytes("UTF-8"));
            return String.format("%064x", new BigInteger(1, md.digest()));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            Log.wtf(TAG, e);
        }
        return password;
    }

    private String getMessageFromError(ResponseBody errorBody) {
        try {
            return new JsonParser().parse(errorBody.string()).getAsJsonObject().get("message")
                    .getAsString();
        } catch (IOException e) {
            Log.e(TAG, "Parsing error", e);
            return "";
        }
    }

    public static void setCurrentUser(User user) {
        sCurrentUser = user;
        if (user != null) ON_CURRENT_USER_UPDATE.run();
    }

    public static User getCurrentUser() {
        if (sCurrentUser == null) Log.w(TAG, "Current user is not set");
        return sCurrentUser;
    }

    public String getUserAuthToken() {
        return mAuthToken;
    }

    public void getUser(String userAuthToken, final UserSimpleCallback callback) {
        Call<User> call = mUserService.getUser(mAuthToken, userAuthToken);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    User user = response.body();
                    if (user.getUserAuthToken().equals(getCurrentUser().getUserAuthToken())) {
                        setCurrentUser(user);
                    }
                    callback.onSuccess(user);
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    public void addOnCurrentUserUpdateListener(@NonNull final Runnable onUpdate) {
        ON_CURRENT_USER_UPDATE.enqueue(onUpdate);
    }

    public void showRewardDialog(@NonNull final FragmentActivity context,
            @NonNull Reward reward, @Nullable final UserSimpleCallback updateCallback) {
        RewardDialogFragment.show(context, reward);
        User user = getCurrentUser();
        final int oldLevel = user.getLevel();
        getUser(user.getUserAuthToken(),
                new UserSimpleCallback() {
                    @Override
                    public void onSuccess(User user) {
                        if (user.getLevel() > oldLevel) {
                            RewardDialogFragment.show(context, user.getLevel());
                        }
                        if (updateCallback != null) updateCallback.onSuccess(user);
                    }

                    @Override
                    public void onFailure() {
                        if (updateCallback != null) updateCallback.onFailure();
                    }
                });
    }
}
