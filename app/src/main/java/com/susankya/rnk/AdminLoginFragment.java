package com.susankya.rnk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.json.JSONObject;


public class AdminLoginFragment extends android.support.v4.app.Fragment implements FragmentCodes {
     private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText mUsername,mPassword;
    private Button mLogin;
    private CheckBox mCheckBox;
    private String mParam1;
    private String mParam2,inputUsername,pass;
    private int NUMBER;
    private boolean toSave;
private boolean isLoggedIn;

    // TODO: Rename and change types and number of parameters
    public static AdminLoginFragment newInstance(String param1, String param2) {
        AdminLoginFragment fragment = new AdminLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static AdminLoginFragment newInstance(int n) {
        AdminLoginFragment fragment = new AdminLoginFragment();
        Bundle args = new Bundle();
        args.putInt("num", n);
        fragment.setArguments(args);
        return fragment;
    }

    public AdminLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            NUMBER=getArguments().getInt("num");
        }
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean isLoggedInFromSharedPreferences=sharedPref.getBoolean("loggedin",false);
        isLoggedIn=isLoggedInFromSharedPreferences;
        if((((NavDrawerActivity)getActivity()).isLoggedIn()) || isLoggedInFromSharedPreferences)
        {
            ((NavDrawerActivity)getActivity()).setLoggedIn(true);
           }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_admin_login, container, false);
        mCheckBox=(CheckBox)v.findViewById(R.id.checkbox);
        mCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        toSave=isChecked;
                    }
                }
        );
        mUsername=(EditText)v.findViewById(R.id.username);
        mPassword=(EditText)v.findViewById(R.id.password);
        mLogin=(Button) v.findViewById(R.id.login);
        mLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputUsername=mUsername.getText().toString().trim().toLowerCase();
                        pass=mPassword.getText().toString();
                        //  login(inputUsername,pass);
                        String link=NEW_HOST+"AdminLogin.php";
                        new PhpConnect(link,"Logging in. Please wait...",getActivity(),1,new String[]{CMDXXX,Utilities.getDatabaseName(getActivity()),inputUsername,pass},new String[]{"cmdxxx","dbName","id","password"} ).setListener(new PhpConnect.ConnectOnClickListener() {
                            @Override
                            public void onConnectListener(String res) {
                                try {
                                  //  Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
                                    JSONObject job=new JSONObject(res);
                                    String college_name=job.getString("name");
                                    String receivedID=job.getString("id");
                                    if(receivedID.equals(inputUsername))
                                    {
                                        SharedPreferences sharedPref = getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE,Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        if(toSave)
                                        {
                                            editor.putString("username",inputUsername);
                                            editor.putString("password", pass);
                                            editor.putBoolean("loggedin", true);
                                        }
                                        editor.putString("collegeName",college_name);

                                        editor.commit();
                                        editor.apply();
                                        ((NavDrawerActivity)getActivity()).setLoggedIn(true);
                                        FragmentManager fm=getFragmentManager();
                                        fm.beginTransaction().replace(R.id.content_frame, FragmentForAdminLogin.newInstance(college_name, receivedID)).addToBackStack(null).commit();
                                    }
                                    else

                                        new AlertDialogBuilder("Incorrect email or password","Incorrect email or password. Please try again.","OK","",getActivity());
                                } catch (Exception e) {
                                    mPassword.setText("");
                                    new AlertDialogBuilder("Incorrect email or password","Incorrect email or password. Please try again.","OK","",getActivity());

                                }
                            }
                        });
                    }
                }
        );
        return v;
    }


    /*@Override
    public void onResume() {
        super.onResume();
             ((NavDrawerActivity) getActivity()).onSectionAttached(6);
    }*/

    }


