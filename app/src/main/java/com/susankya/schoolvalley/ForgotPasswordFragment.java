package com.susankya.schoolvalley;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class ForgotPasswordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String SAVED_USERNAME = "username";
    private static final String SAVED_USERNAMER_PREFERANCE = "username_preference";
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.enterButton)
    Button enterButton;
    @BindView(R.id.message)
    TextViewPlus message;
    @BindView(R.id.alreadyGotCode)
    TextViewPlus gotGotCode;
    private boolean isCode = false;
    private String usernameText, usernameTestSaved, codeText;
    // TODO: Rename and change types of parameters
    private String type;
    private String mParam2;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    public static ForgotPasswordFragment newInstance(String param1) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frogot_password, container, false);
        ButterKnife.bind(this, v);
        if (isCode) {
            gotGotCode.setVisibility(View.GONE);
        } else
            gotGotCode.setVisibility(View.VISIBLE);
        gotGotCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotGotCode.setVisibility(View.GONE);
                usernameTestSaved = getActivity().getSharedPreferences(SAVED_USERNAMER_PREFERANCE, Context.MODE_PRIVATE).getString(SAVED_USERNAME, "");
                if (!usernameTestSaved.equals("")) {
                    message.setText("A code has been sent to " + usernameTestSaved + ". Enter the code below");
                    username.setHint("Enter code");
                    enterButton.setText("Enter");
                    isCode = true;
                    username.setText("");
                    username.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else {
                    Toast.makeText(getActivity(), "We could not find any record. Please try again", Toast.LENGTH_SHORT).show();
                    gotGotCode.setVisibility(View.GONE);
                    //   message.setText("A code has been sent to "+usernameTestSaved+". Enter the code below");

                }
            }
        });
        username.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameText = username.getText().toString();
                if (!usernameText.isEmpty()) {
                    if (isCode) {
                        codeText = username.getText().toString();
                        String link = FragmentCodes.NEW_HOST + "Edit/forgetPassword.php";
                        new PhpConnect(link, "Verifying code...", getActivity(), 1, new String[]{FragmentCodes.CMDXXX, "check_code", usernameTestSaved, codeText, type}, new String[]{"cmdxxx", "action", "id", "recovery_code", "type"}).setListener(new PhpConnect.ConnectOnClickListener() {
                            @Override
                            public void onConnectListener(String res) {
                                Log.d("fatal", "onConnectListener: "+res);
                                if (res.trim().equals("1")) {
                                    //  getActivity().getSupportFragmentManager().popBackStack();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.first_container, ChangePasswordAfterForgetFragment.newInstance(usernameTestSaved, type, codeText)).commit();
                                } else {
                                    Toast.makeText(getActivity(), "Code did not match", Toast.LENGTH_SHORT).show();
                                }
                                //Log.d(TAG, "onConnectListener: "+res);
                            }
                        });
                    } else {
                        usernameText = username.getText().toString();
                        usernameTestSaved = usernameText;
                        getActivity().getSharedPreferences(SAVED_USERNAMER_PREFERANCE, Context.MODE_PRIVATE).edit().putString(SAVED_USERNAME, usernameTestSaved).commit();
                        String link = FragmentCodes.NEW_HOST + "Edit/forgetPassword.php";
                        new PhpConnect(link, "Verifying email...", getActivity(), 1, new String[]{FragmentCodes.CMDXXX, "send_code", usernameText, type}, new String[]{"cmdxxx", "action", "id", "type"}).setListener(new PhpConnect.ConnectOnClickListener() {
                            @Override
                            public void onConnectListener(String res) {
                                Log.d("fatal", "fatal "+res);
                                if (res.trim().equals("1")) {
                                    message.setText("A code has been sent to " + usernameTestSaved + ". Enter the code below");
                                    username.setHint("Enter code");
                                    enterButton.setText("Enter");
                                    isCode = true;
                                    username.setText("");
                                    username.setInputType(InputType.TYPE_CLASS_NUMBER);
                                } else {
                                    Toast.makeText(getActivity(), "Invalid email", Toast.LENGTH_SHORT).show();
                                }
                                //Log.d(TAG, "onConnectListener: "+res);
                            }
                        });
                    }
                } else {
                    if (!isCode)
                        Toast.makeText(getActivity(), "Enter email", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "Enter code", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return v;
    }


}
