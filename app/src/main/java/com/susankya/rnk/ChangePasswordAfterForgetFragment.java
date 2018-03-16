package com.susankya.rnk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordAfterForgetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    @BindView(R.id.newPassword)EditText newPassword;
    @BindView(R.id.confirmNewPassword)EditText confirmNewpassword;
    @BindView(R.id.enterButton)Button enterButton;
    private boolean isCode=false;
    private String newPasswordText,confirmNewPasswordText;
    private String usernameText,codeText;
    // TODO: Rename and change types of parameters
    private String username;
    private String type;
    public ChangePasswordAfterForgetFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordAfterForgetFragment newInstance(String param1,String param2,String codeText) {
        ChangePasswordAfterForgetFragment fragment = new ChangePasswordAfterForgetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3,codeText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM1);
            type = getArguments().getString(ARG_PARAM2);
            codeText=getArguments().getString(ARG_PARAM3);
        }
    }
boolean check(){
    newPasswordText=newPassword.getText().toString();
    confirmNewPasswordText=confirmNewpassword.getText().toString();
    if(!(newPasswordText.isEmpty()||confirmNewPasswordText.isEmpty())){
        if(newPasswordText.equals(confirmNewPasswordText)){
            return true;
        }else {
            Toast.makeText(getActivity(),"Password do not match.",Toast.LENGTH_SHORT).show();
            return false;
        }
    }else {
        Toast.makeText(getActivity(),"Fill both the fields",Toast.LENGTH_SHORT).show();
    return false;
    }
}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_change_password_after_forget, container, false);
        ButterKnife.bind(this,v);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check()){
                String link=FragmentCodes.NEW_HOST+"Edit/forgetPassword.php";
                new PhpConnect(link, "Changing password...", getActivity(), 1,
                        new String[]{FragmentCodes.CMDXXX, "change_password", username, type,newPasswordText,codeText,codeText},
                        new String[]{"cmdxxx", "action", "id", "type","new_password","recovery_code"}).setListener(new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        if(res.trim().equals("1")){
                            Toast.makeText(getActivity(),"Password changed",Toast.LENGTH_SHORT).show();
                            Fragment fragment;
                            if(type.equals("user")){
                                fragment=new FirstLoginFragment();
                            }else
                            fragment=new AdminFirstLoginFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.first_container,fragment).commit();
                        }
                        else {
                            Toast.makeText(getActivity(),"Could not set new password",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                }
            }});
        return v;
    }


}
