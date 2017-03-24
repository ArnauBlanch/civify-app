package com.civify.civify.activity.registration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.civify.civify.R;
import com.civify.civify.adapter.UserAdapter;
import com.civify.civify.utils.AdapterFactory;

@SuppressWarnings("Convert2Lambda")
public class RegistrationFragment extends Fragment {
    private View mView;
    private int mPage;
    private RegistrationActivity mActivity;
    private UserAdapter mUserAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mPage = getArguments().getInt("page");
        mUserAdapter = AdapterFactory.getInstance().getUserController();
        mActivity = (RegistrationActivity) getActivity();
        mView = mActivity.getLayoutInflater().inflate(getLayoutResource(mPage), container, false);
        mView.setTag(mPage);
        setupPageListeners();
        return mView;
    }

    private int getLayoutResource(int pageId) {
        switch (pageId) {
            case 0:
                return R.layout.registration_page_name;
            case 1:
                return R.layout.registration_page_username;
            case 2:
                return R.layout.registration_page_email;
            default:
                return R.layout.registration_page_password;
        }
    }

    private void setupPageListeners() {
        switch (mPage) {
            case 0:
                setupNameAndSurnameListener();
                break;
            case 1:
                setupUsernameListener();
                break;
            case 2:
                setupEmailListeners();
                break;
            default:
                setupPasswordListeners();
        }
    }

    private void setupNameAndSurnameListener() {
        final EditText nameEditText = (EditText) mView.findViewById(R.id.name_input);
        final EditText surnameEditText = (EditText) mView.findViewById(R.id.surname_input);
        mView.findViewById(R.id.button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.getText().length() > 0 && surnameEditText.getText().length() > 0) {
                    mActivity.nextPage();
                }
            }
        });
    }

    private void setupUsernameListener() {
        EditText usernameEditText = (EditText) mView.findViewById(R.id.username_input);
        usernameEditText.addTextChangedListener(
                new UsernameTextWatcher(
                        getContext(), mView,
                        R.id.username_validation_icon, R.id.username_validation_text
                )
        );
        mView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameField = (EditText) mView.findViewById(R.id.username_input);
                if (mUserAdapter.checkValidUnusedUsername(usernameField.getText().toString())
                        == UserAdapter.VALID_UNUSED) {
                    mActivity.nextPage();
                }
            }
        });
    }

    private void setupEmailListeners() {
        EditText emailEditText = (EditText) mView.findViewById(R.id.email_input);
        emailEditText.addTextChangedListener(
                new EmailTextWatcher(
                        getContext(), mView,
                        R.id.email_validation_icon, R.id.email_validation_text
                )
        );
        mView.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailField = (EditText) mView.findViewById(R.id.email_input);
                if (mUserAdapter.checkValidUnusedEmail(emailField.getText().toString())
                        == UserAdapter.VALID_UNUSED) {
                    mActivity.nextPage();
                }
            }
        });
    }

    private void setupPasswordListeners() {
        final EditText password = (EditText) mView.findViewById(R.id.password_input);
        final EditText matchingPassword = (EditText) mView.findViewById(R.id.password2_input);

        password.addTextChangedListener(
                new PasswordTextWatcher(
                        getContext(), mView,
                        R.id.password_validation_icon, R.id.password_validation_text
                ));
        matchingPassword.addTextChangedListener(
                new PasswordMatchTextWatcher(
                        getContext(), mView,
                        R.id.username_validation_icon, R.id.username_validation_text,
                        R.id.password_input
                ));

        mView.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserAdapter.checkValidPassword(password.getText().toString())
                        && (password.getText().toString()
                        .equals(matchingPassword.getText().toString()))) {
                    mActivity.register();
                }
            }
        });
    }
}
