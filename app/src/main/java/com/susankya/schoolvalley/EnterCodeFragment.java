package com.susankya.schoolvalley;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EnterCodeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String SCHOOL_CODE_PREF = "school_code_preference";
    public static final String SCHOOL_CODE = "school_code";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.code_et)
    EditText codeET;
    @BindView(R.id.code_confirm_btn)
    Button confirm;
    private View view;
    private String code, link = FragmentCodes.MAIN_DATABASE + "enterCode.php";

    public EnterCodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterCodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterCodeFragment newInstance(String param1, String param2) {
        EnterCodeFragment fragment = new EnterCodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_enter_code, container, false);
        ButterKnife.bind(this, v);
        toolbar.setTitle("Enter Access Code");
        confirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        code = codeET.getText().toString().trim();
                        if (!code.isEmpty()) {
                            view = v;
                            checkCode();
                        } else
                            Snackbar.make(v, "Please enter a valid code", Snackbar.LENGTH_SHORT).show();
                    }
                }
        );
        return v;
    }

    String sn, schoolName, dbName;

    void checkCode() {
        String params[] = new String[]{FragmentCodes.CMDXXX, code};
        String fields[] = new String[]{"cmdxxx", "code"};

        new PhpConnect(link, "Please wait...", getActivity(), 1, false, params, fields)
                .setListener(
                        new PhpConnect.ConnectOnClickListener() {
                            @Override
                            public void onConnectListener(String res) {
                                Log.d("fatal", "Signup"+res);
                                try {
                                    JSONObject j = new JSONObject(res);
                                    String sn = j.getString("sn");
                                    String sName = j.getString("college_name");
                                    String dbName = j.getString("database_name");
                                    String category = j.getString("category");
                                    getActivity().getSharedPreferences(SCHOOL_CODE_PREF, Context.MODE_PRIVATE).edit().putString(SCHOOL_CODE, code).commit();
                                    ((ImmortalApplication) getActivity().getApplication()).set(sn, sName, dbName, category);
                                    if (!sn.isEmpty()) {
                                        Fragment f = new FirstSignUpFragment();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, f).addToBackStack(null).commit();
                                    } else
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Snackbar.make(view, "The code you entered is incorrect. Please try again.", Snackbar.LENGTH_LONG).show();
                                            }
                                        });
                                } catch (Exception e) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Snackbar.make(view, "The code you entered is incorrect. Please try again.", Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                                    //Log.d("entercodeactivity",e.toString());
                                }

                            }
                        }
                );
    }
}
