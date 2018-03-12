package com.susankya.schoolvalley;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserProfileDialog extends DialogFragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String email, fullname, rollNo, section, mobile;

    String link = FragmentCodes.MAIN_DATABASE + "Results.php";

    @BindView(R.id.name)
    TextViewPlus nametv;
    @BindView(R.id.roll)
    TextViewPlus rollTV;
    @BindView(R.id.section)
    TextViewPlus sectionTV;
    @BindView(R.id.email)
    TextViewPlus emailTV;
    @BindView(R.id.mobile)
    TextViewPlus mobileTV;

    public UserProfileDialog() {
        // Required empty public constructor
    }

    public static UserProfileDialog newInstance(String fname, String roll, String sec, String email, String mob) {
        UserProfileDialog fragment = new UserProfileDialog();
        fragment.email = email;
        fragment.fullname = fname;
        fragment.rollNo = roll;
        fragment.section = sec;
        fragment.mobile = mob;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.fragment_user_profile_dialog, container, false);
        ButterKnife.bind(this, v);
        if (fullname == null) nametv.setText("Null");
        else nametv.setText(fullname);
        if (email == null) emailTV.setText("Null");
        else emailTV.setText(email);
        if (section == null) sectionTV.setText("Null");
        else sectionTV.setText(section);
        if (rollNo == null  || rollNo.equals("")) rollTV.setText("Null");
        else rollTV.setText(rollNo);
        if (mobile == null) mobileTV.setText("Null");
        else mobileTV.setText(mobile);
        return v;
    }
}
