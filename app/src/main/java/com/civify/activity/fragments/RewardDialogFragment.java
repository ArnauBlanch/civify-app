package com.civify.activity.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.civify.R;
import com.civify.model.Reward;
import com.civify.utils.ConfirmDialog;

public class RewardDialogFragment extends DialogFragment {

    private static final String DIALOG_TAG = "coins_dialog";
    private static final String COINS = "coins";
    private static final String EXPERIENCE = "experience";
    private static final String LEVEL = "level";

    public static void show(FragmentActivity activity, Reward reward) {
        if (!reward.empty()) {
            RewardDialogFragment fragment = new RewardDialogFragment();

            Bundle args = new Bundle();
            args.putInt(COINS, reward.getCoins());
            int exp = reward.getExperience();
            args.putInt(EXPERIENCE, exp);
            fragment.setArguments(args);
            fragment.show(activity.getSupportFragmentManager(), DIALOG_TAG);
        }
    }

    public static void show(FragmentActivity activity, int level) {
        RewardDialogFragment fragment = new RewardDialogFragment();
        Bundle args = new Bundle();
        args.putInt(LEVEL, level);
        fragment.setArguments(args);
        fragment.show(activity.getSupportFragmentManager(), DIALOG_TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int coins = getArguments().getInt(COINS);
        int experience = getArguments().getInt(EXPERIENCE);
        int level = getArguments().getInt(LEVEL);

        ConfirmDialog dialog = new ConfirmDialog(getContext());
        dialog.setTitle(R.string.congratulations);
        dialog.setView(setValues(coins, experience, level));

        return dialog;
    }

    private View setValues(int coins, int experience, int level) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_reward, null);

        TextView numCoins = (TextView) view.findViewById(R.id.dialog_num_coins);
        if (coins != 0) {
            numCoins.setText(String.valueOf(coins));
            if (coins < 0) numCoins.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        } else {
            view.findViewById(R.id.dialog_coins_icon).setVisibility(View.GONE);
            numCoins.setVisibility(View.GONE);
        }

        TextView xp = (TextView) view.findViewById(R.id.dialog_xp_num);
        if (experience != 0) {
            xp.setText(String.valueOf(experience));
            if (experience < 0) xp.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            if (coins == 0) {
                xp.setPadding(0, 0, 0, 0);
            }
        } else {
            view.findViewById(R.id.dialog_xp).setVisibility(View.GONE);
            xp.setVisibility(View.GONE);
        }

        TextView levelUp = (TextView) view.findViewById(R.id.level_up);
        if (level > 0) {
            levelUp.setText(String.valueOf(level));
        } else {
            levelUp.setVisibility(View.GONE);
            view.findViewById(R.id.level_tag).setVisibility(View.GONE);
            view.findViewById(R.id.star_icon).setVisibility(View.GONE);
        }
        
        return view;
    }
}
