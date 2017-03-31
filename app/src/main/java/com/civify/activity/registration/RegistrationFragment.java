package com.civify.activity.registration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.civify.civify.R;

@SuppressWarnings("CyclicClassDependency")
public class RegistrationFragment extends Fragment {

    private View mView;
    private int mPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mPage = getArguments().getInt("page");
        mView = getActivity().getLayoutInflater()
                .inflate(getLayoutResource(mPage), container, false);
        mView.setTag(mPage);
        setTextWatchers();
        return mView;
    }

    private static int getLayoutResource(int pageId) {
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

    private void setTextWatchers() {
        switch (mPage) {
            case 0:
                break;
            case 1:
                ((EditText) mView.findViewById(R.id.username_input)).addTextChangedListener(
                        new UsernameTextWatcher(mView, R.id.username_validation_icon,
                                R.id.username_validation_text));
                break;
            case 2:
                ((EditText) mView.findViewById(R.id.email_input)).addTextChangedListener(
                        new EmailTextWatcher(mView, R.id.email_validation_icon,
                                R.id.email_validation_text));
                break;
            default:
                ((EditText) mView.findViewById(R.id.password_input)).addTextChangedListener(
                        new PasswordTextWatcher(mView, R.id.password_validation_icon,
                                R.id.password_validation_text));
                ((EditText) mView.findViewById(R.id.password2_input)).addTextChangedListener(
                        new PasswordMatchTextWatcher(mView, R.id.password2_validation_icon,
                                R.id.password2_validation_text, R.id.password_input));
        }
    }
}
