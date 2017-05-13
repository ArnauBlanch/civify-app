package com.civify.activity.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.civify.R;
import com.civify.model.Reward;

public class RewardDialogFragment extends DialogFragment {

    private static final String DIALOG_TAG = "coins_dialog";
    private static final String COINS = "coins";
    private static final String EXPERIENCE = "experience";

    public static RewardDialogFragment showDialog(FragmentActivity activity, Reward reward) {
        RewardDialogFragment fragment = new RewardDialogFragment();

        Bundle args = new Bundle();
        args.putInt(COINS, reward.getCoins());
        args.putInt(EXPERIENCE, reward.getExperience());
        fragment.setArguments(args);
        fragment.show(activity.getSupportFragmentManager(), DIALOG_TAG);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int coins = getArguments().getInt(COINS);
        int experience = getArguments().getInt(EXPERIENCE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = setValues(coins, experience);

        builder.setView(view)
                .setTitle(getString(R.string.congratulations))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    private View setValues(int coins, int experience) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_reward, null);

        TextView numCoins = (TextView) view.findViewById(R.id.dialog_num_coins);
        if (coins > 0) {
            numCoins.setText(String.valueOf(coins));
        } else {
            view.findViewById(R.id.dialog_coins_icon).setVisibility(View.GONE);
            numCoins.setVisibility(View.GONE);
        }

        TextView xp = (TextView) view.findViewById(R.id.dialog_xp_num);
        if (experience > 0) {
            xp.setText(String.valueOf(experience));
            if (coins == 0) {
                xp.setPadding(0, 0, 0, 0);
            }
        } else {
            view.findViewById(R.id.dialog_xp).setVisibility(View.GONE);
            xp.setVisibility(View.GONE);
        }
        
        return view;
    }
}
