package com.susankya.schoolvalley;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordDialog extends DialogFragment implements FragmentCodes {

    @BindView(R.id.old_pw_et)
    EditText oldPW;
    @BindView(R.id.new_pw_et)
    EditText newPW;
    @BindView(R.id.re_new_pw_et)
    EditText reNewPW;
    String oldPWText, newPWText, reNewPWText;
    String userlink = Utilities.encodeLinkSpace(FragmentCodes.HOST + "Android Call/Edit/ChangeUserPassword.php");
    String adminlink = Utilities.encodeLinkSpace(FragmentCodes.HOST + "Android Call/Edit/ChangeCollegePassword.php");
    String link;
    @BindView(R.id.ok_button)
    Button ok;
    @BindView(R.id.cancel_button)
    Button cancel;

    public void onResume() {
        getDialog().getWindow().addFlags(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getDialog().getWindow();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        super.onResume();
    }

    void changePasswordPHP() {
        if (Utilities.isAdmin(getContext()))
            link = adminlink;
        else
            link = userlink;

        new PhpConnect(link, "Please wait...", getActivity(), 1,
                new String[]{Utilities.getUsername(getActivity()), oldPWText, newPWText, CMDXXX, UtilitiesAdi.giveMeSN(getContext(), Utilities.getDatabaseName(getContext()))},
                new String[]{"user_id", "old_password", "new_password", "cmdxxx", "college_sn"}).setListener(
                new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        if (res.trim().equals("1")) {
                            Toast.makeText(getActivity(), "Your password has been changed.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "There was an error. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                        getDialog().dismiss();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_change_password, null, false);
        ButterKnife.bind(this, v);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        validate(false);
        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePasswordPHP();
                    }
                }
        );
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDialog().dismiss();
                    }
                }
        );
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                oldPWText = oldPW.getText().toString();
                newPWText = newPW.getText().toString();
                reNewPWText = reNewPW.getText().toString();
                if (!s.toString().isEmpty()) {
                    if (reNewPWText.equals(newPWText) && !reNewPWText.isEmpty()) {
                        if (!oldPWText.isEmpty())
                            validate(true);
                        else validate(false);
                    } else
                        validate(false);
                } else validate(false);

            }
        };
        newPW.addTextChangedListener(textWatcher);
        reNewPW.addTextChangedListener(textWatcher);

        oldPW.addTextChangedListener(textWatcher);

        return v;
    }

    void validate(boolean c) {
        if (!c) {
            ok.setClickable(false);
            ok.setFocusable(false);
            ok.setBackgroundDrawable(getActivity().getResources().getDrawable(R.color.grey_light));

        } else {
            ok.setClickable(true);
            ok.setFocusable(true);
            ok.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.overlay_selector));
        }
    }
}
