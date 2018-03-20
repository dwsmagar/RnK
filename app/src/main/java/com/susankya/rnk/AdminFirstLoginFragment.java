package com.susankya.rnk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdminFirstLoginFragment extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    @BindView(R.id.username)
    EditText username_edit;
    @BindView(R.id.password)
    EditText password_edit;
    @BindView(R.id.backArrow)
    LinearLayout backArrow;
    @BindView(R.id.login)
    TextView loginText;
    @BindView(R.id.titleBoy)
    TextView title;
    @BindView(R.id.fragment_login_login)
    Button login_btn;
    @BindView(R.id.signupLayout)
    View signup_layout;
    @BindView(R.id.image)
    ImageView imageView;

    private String URLboy;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;
    private boolean error = false;
    public String dbName;
    private String username, password;
    private String link = Utilities.encodeLinkSpace(FragmentCodes.HOST + "Home/Susankya Database/login.php");
    private UserInfo userInfo;
    private String kunActivityHo;
    private int userNum;
    private boolean shouldStayLoggedIn = false;

    public static AdminFirstLoginFragment newInstance(String param1, String param2) {
        AdminFirstLoginFragment fragment = new AdminFirstLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AdminFirstLoginFragment() {
        //
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kunActivityHo = getArguments().getString("from");
            // Toast.makeText(getActivity(),kunActivityHo,Toast.LENGTH_LONG).show();
        }

        sqLiteHelper = new SQLiteHelper(getActivity());
        db = sqLiteHelper.getWritableDatabase();
        db.execSQL(
                "create table if not exists " + USERS_TABLE + " ( " + C_SN + " integer primary key autoincrement," +
                        USER_NO + " int (10)," + FIRST_NAME + " varchar(100)," + LAST_NAME + " varchar(100)," +
                        PASSWORD + " varchar(100)," + USERNAME + " varchar(50)," +
                        VERIFIED + " int(1)," + INSTITUTION + " varchar," + LEVEL + " varchar, " +
                        DBNAME + " varchar," +
                        GENDER + " varchar(50)," + PROFILE_PIC_LOCATION + " varchar," + LOCATION + " varchar,"
                        + PHONE_NUMBER + " int(10), " + INTEREST + " varchar, " + HOBBY + " varchar);"
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_first_login, container, false);
        ButterKnife.bind(this, v);
        String account_type = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE).getString("type", "");
        loginText.setText(account_type + " login");
        title.setText("Enter your credentials to manage your activities");
        signup_layout.setVisibility(View.GONE);
        checkType(account_type);
//        Toast.makeText(getActivity(), ""+account_type, Toast.LENGTH_SHORT).show();
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSharedPreferences("account", Context.MODE_PRIVATE).edit().clear().commit();
                Intent intent = new Intent(getActivity(), AccountTypeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        username = username_edit.getText().toString().trim().toLowerCase();
                        password = password_edit.getText().toString().trim();
                        EditText[] etArray = new EditText[]{username_edit, password_edit};
                        String RegID = FirebaseInstanceId.getInstance().getToken();
                        if (new BlankEditText(new String[]{FragmentCodes.FIELD_EMPTY, FragmentCodes.FIELD_EMPTY}, etArray).areAllFieldsOK()) {
                            PhpConnect phpConnect = new PhpConnect(Utilities.encodeLinkSpace(link), "Logging in...", getActivity(), 1,
                                    new String[]{username, password, CMDXXX, RegID},
                                    new String[]{"userName", "password", "cmdxxx", "regId"});
                            phpConnect.setListener(
                                    new PhpConnect.ConnectOnClickListener() {
                                        @Override
                                        public void onConnectListener(String res) {
                                            Log.d("erroryaha", res);
                                            try {
                                                JSONObjectAuto jsonObject = new JSONObjectAuto(new JSONObject(res));
                                                String location = jsonObject.getString("location");
                                                URLboy = jsonObject.getString("cover_pic");
                                                String doe = jsonObject.getString("date_of_establishment");
                                                String desc = jsonObject.getString("description");
                                                String contact = jsonObject.getString("public_contact");
                                                String website = jsonObject.getString("website");
                                                String email = jsonObject.getString("email");
                                                String college_code = jsonObject.getString("college_code");
                                                getActivity().getSharedPreferences(EnterCodeFragment.SCHOOL_CODE_PREF, Context.MODE_PRIVATE).
                                                        edit().putString(EnterCodeFragment.SCHOOL_CODE, college_code).commit();
                                                dbName = jsonObject.getString("database_name");

                                                String sn = jsonObject.getString("sn");
                                                Log.d("frompost", sn);
                                                String name = jsonObject.getString("college_name");
                                                dbName = jsonObject.getString("database_name");

                                                SharedPreferences dbN = getActivity().getApplicationContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor ee = dbN.edit();
                                                ee.putString("dbName", dbName);
                                                ee.putString("cover_pic", URLboy);
                                                ee.putString("location", location);
                                                ee.putString("username", username);
                                                ee.putString("sn", sn);
                                                ee.putString("institution", name);
                                                ee.commit();
                                            } catch (Exception e) {
                                                Log.d("erroryaha", e.toString());
                                                userInfo = new UserInfo(0, "0");
                                                error = true;
                                            }

                                            if (error) {
                                                new AlertDialogBuilder("Incorrect email or password", "Incorrect email or password. Please try again.", "OK", "", getActivity());
                                                error = false;
                                            } else {
                                                SharedPreferences sharedPreferences1 = getActivity().getApplicationContext().getSharedPreferences("rememberlogin", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                                editor1.putBoolean("isloggedin", true);
                                                editor1.commit();
                                                SharedPreferences loginTypeSP = getActivity().getApplicationContext().getSharedPreferences("type", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor loginTypeET = loginTypeSP.edit();
                                                loginTypeET.putInt("type", ADMIN);
                                                loginTypeET.putString("cover_pic", FragmentCodes.HOST + "Home/Susankya%20Database" + URLboy);
                                                loginTypeET.commit();

                                                ImmortalApplication im = (ImmortalApplication) getActivity().getApplicationContext();
                                                if (!UtilitiesAdi.giveMeSN(getActivity(), dbName).equals("0"))
                                                    im.setTheSN(UtilitiesAdi.giveMeSN(getActivity(), dbName));

                                                SharedPreferences sps = getActivity().getApplicationContext().getSharedPreferences("visitedOnce", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor11 = sps.edit();
                                                editor11.putBoolean("visited", true);
                                                editor11.putBoolean("loggedIn", true);
                                                editor11.commit();
                                                UtilitiesAdi.setProfileLoaded(getActivity(), false);
                                                Intent i = new Intent(getActivity(), NavDrawerActivity.class);
                                                startActivity(i);
                                                getActivity().finish();

                                            }
                                        }
                                    }
                            );
                        }
                    }
                }
        );
        return v;
    }

    private void checkType(String account) {
        switch (account) {
            case "reception":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
                break;
            case "super-admin":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_manager));
                break;
            case "sub-admin":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_man));
                break;
            default:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_student));
        }
    }
}
