package com.civify.activity.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.civify.R;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.UserSimpleCallback;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

public class CoinsDialogFragment extends DialogFragment {

    private static final String DIALOG_TAG = "coins_dialog";
    private static final String COINS = "coins";
    private static final String COINS_ADDED = "coins_added";

    public static CoinsDialogFragment addCoins(FragmentActivity activity, int coins) {
        CoinsDialogFragment fragment = new CoinsDialogFragment();

        final Bundle args = new Bundle();
        args.putInt(COINS, coins);

        UserAdapter userAdapter = AdapterFactory.getInstance().getUserAdapter(activity);
        userAdapter.addCoins(UserAdapter.getCurrentUser().getUserAuthToken(),
                coins, new UserSimpleCallback() {
                    @Override
                    public void onSuccess(User user) {
                        UserAdapter.setCurrentUser(user);
                        args.putBoolean(COINS_ADDED, true);
                    }

                    @Override
                    public void onFailure() {
                        args.putBoolean(COINS_ADDED, false);
                    }
                });

        fragment.setArguments(args);
        fragment.show(activity.getSupportFragmentManager(), DIALOG_TAG);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int coins = getArguments().getInt(COINS);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_coins, null);

        TextView numCoins = (TextView) view.findViewById(R.id.dialog_num_coins);
        numCoins.setText(String.valueOf(coins));

        builder.setView(view)
                .setTitle(getString(R.string.congratulations))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!getArguments().getBoolean(COINS_ADDED)) {
                            Snackbar.make(view, getString(R.string.couldnt_add_coins),
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

        return builder.create();
    }
}
