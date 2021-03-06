package com.civify.adapter.award;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.civify.adapter.LoginAdapterImpl;
import com.civify.adapter.UserAdapter;
import com.civify.model.RewardContainer;
import com.civify.model.award.Award;
import com.civify.model.award.ExchangedAward;
import com.civify.service.award.AwardService;
import com.civify.service.award.AwardSimpleCallback;
import com.civify.service.award.ListAwardsSimpleCallback;
import com.civify.service.award.ListExchangedAwardSimpleCallback;
import com.civify.service.award.RewardCallback;
import com.civify.utils.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AwardAdapterImpl implements AwardAdapter {

    private AwardService mAwardService;
    private AwardSimpleCallback mAwardSimpleCallback;
    private SharedPreferences mSharedPreferences;

    public AwardAdapterImpl(SharedPreferences sharedPreferences) {
        this(ServiceGenerator.getInstance().createService(AwardService.class), sharedPreferences);
    }

    AwardAdapterImpl(AwardService awardService, SharedPreferences sharedPreferences) {
        this.mAwardService = awardService;
        this.mSharedPreferences = sharedPreferences;
    }

    @Override
    public void getOfferedAwards(@NonNull final ListAwardsSimpleCallback callback) {
        Call<List<Award>> serviceCall = mAwardService.getAwards(getToken());
        serviceCall.enqueue(new Callback<List<Award>>() {
            @Override
            public void onResponse(Call<List<Award>> call, Response<List<Award>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<Award>> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void getOfferedAward(@NonNull String awardAuthToken, @NonNull final AwardSimpleCallback
            callback) {
        Call<Award> serviceCall = mAwardService.getAward(getToken(), awardAuthToken);
        serviceCall.enqueue(new Callback<Award>() {
            @Override
            public void onResponse(Call<Award> call, Response<Award> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Award> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void getExchangedAwards(@NonNull String authToken, @NonNull final
            ListExchangedAwardSimpleCallback callback) {
        Call<List<ExchangedAward>> call = mAwardService.getExchangedAwards(getToken(), UserAdapter
                .getCurrentUser().getUserAuthToken());
        call.enqueue(new Callback<List<ExchangedAward>>() {
            @Override
            public void onResponse(Call<List<ExchangedAward>> call, Response<List<ExchangedAward>>
                    response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<ExchangedAward>> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void exchangeAward(@NonNull String awardToken, @NonNull final RewardCallback callback) {
        Call<RewardContainer> call = mAwardService.exchangeAward(getToken(), awardToken);
        call.enqueue(new Callback<RewardContainer>() {
            @Override
            public void onResponse(Call<RewardContainer> call, Response<RewardContainer> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().getReward());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<RewardContainer> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    private String getToken() {
        return mSharedPreferences.getString(LoginAdapterImpl.AUTH_TOKEN, "");
    }

}
