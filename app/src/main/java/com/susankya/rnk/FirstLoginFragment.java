package com.susankya.rnk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FirstLoginFragment extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//    private CheckBox mLoggedIn;
//    private Button mLoginButton;
//    private TextView signUp;

    @BindView(R.id.username)
    EditText username_edit;
    @BindView(R.id.password)
    EditText password_edit;
    @BindView(R.id.create_account)
    TextView create;
    @BindView(R.id.fragment_login_login)
    Button login;
    @BindView(R.id.backArrow)
    View backArrow;
    @BindView(R.id.login)
    TextView loginText;
    @BindView(R.id.image)
    ImageView imageView;

    public String dbName;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;
    private String username, password;
    String link = Utilities.encodeLinkSpace(FragmentCodes.MAIN_DATABASE + "users/login.php");
    private UserInfo userInfo;
    private String kunActivityHo;
    private int userNum;
    private boolean shouldStayLoggedIn = false;

    public static FirstLoginFragment newInstance(String param1, String param2) {
        FirstLoginFragment fragment = new FirstLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FirstLoginFragment() {
        //
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kunActivityHo = getArguments().getString("from");
            // Toast.makeText(getActivity(),kunActivityHo,Toast.LENGTH_LONG).show();
        }
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
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
        SharedPreferences sp =getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        loginText.setText("Student Login");
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_student));
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear().commit();
                Intent intent = new Intent(getActivity(), AccountTypeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        create.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.putString("type","student");
                        editor.commit();
                        String sn = "65";
                        String sName = "R&K Associates";
                        String dbName = "SusankyaNirajshah076@gmail.com";
                        String category = "Consultancy";
                        ((ImmortalApplication) getActivity().getApplication()).set(sn, sName, dbName, category);
                        Fragment f = new FirstSignUpFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.first_container, f).addToBackStack(null).commit();
                    }
                }
        );

//        mLoggedIn.setOnCheckedChangeListener(
//                new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        shouldStayLoggedIn = isChecked;
//                    }
//                }
//        );

//        forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.first_container, ForgotPasswordFragment.newInstance("user")).addToBackStack(null).commit();
//            }
//        });

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.putString("type","student");
                        editor.commit();
                        username = username_edit.getText().toString().trim().toLowerCase();
                        password = password_edit.getText().toString().trim();
                        String encodedUsername = "", encodedPassword = "";
                        try {
                            encodedUsername = URLEncoder.encode(username, "utf-8");
                            encodedPassword = URLEncoder.encode(password, "utf-8");
                        } catch (Exception e) {
                            //
                        }

                        EditText[] editArr = new EditText[]{username_edit, password_edit};
                        if (new BlankEditText(new String[]{FragmentCodes.FIELD_EMPTY, FragmentCodes.FIELD_EMPTY}, editArr).areAllFieldsOK()) {

                            String token = FirebaseInstanceId.getInstance().getToken();

                            PhpConnect phpConnect = new PhpConnect(link, "Logging in...", getActivity(), 1,
                                    new String[]{username, password, CMDXXX, token},
                                    new String[]{"userid", "password", "cmdxxx", "regId"});
                            phpConnect.setListener(
                                    new PhpConnect.ConnectOnClickListener() {
                                        @Override
                                        public void onConnectListener(String res) {
                                            Log.d("erroryaha", "from login " + res);
                                            try {
                                                JSONObject jO = new JSONObject(res);
                                                if (jO.has("college_code")) {
                                                    getActivity().getSharedPreferences(EnterCodeFragment.SCHOOL_CODE_PREF, Context.MODE_PRIVATE).edit().
                                                            putString(EnterCodeFragment.SCHOOL_CODE, jO.getString("college_code")).commit();
                                                }

                                                userNum = jO.getInt("user_no");

                                                String firstName = jO.getString("firstName");
                                                String lastName = jO.getString("lastName");
                                                userInfo = new UserInfo();
                                                userInfo.setUserNumber(userNum);
                                                userInfo.setFirstName(firstName);
                                                userInfo.setLastName(lastName);
                                                userInfo.setUserName(username);
                                                userInfo.setPassword(password);

                                                if (!jO.isNull("verified"))
                                                    userInfo.setVerified(jO.getInt("verified"));
                                                else userInfo.setVerified(0);
                                                if (!jO.isNull("college_name"))
                                                    userInfo.setInstitution(jO.getString("college_name"));
                                                else userInfo.setInstitution("null");
                                                if (!jO.isNull("level"))
                                                    userInfo.setLevel(jO.getString("level"));
                                                if (!jO.isNull("college_sn"))
                                                    userInfo.setCollegeSN(jO.getString("college_sn"));
                                                else userInfo.setCollegeSN("null");
                                                if (!jO.isNull("section"))
                                                    userInfo.setSection(jO.getString("section"));
                                                else userInfo.setSection("null");
                                                if (!jO.isNull("faculty"))
                                                    userInfo.setFaculty(jO.getString("faculty"));
                                                else userInfo.setFaculty("null");
                                                if (!jO.isNull("dbName"))
                                                    userInfo.setDbName(jO.getString("dbName"));
                                                else userInfo.setDbName("null");
                                                if (!jO.isNull("gender"))
                                                    userInfo.setGender(jO.getString("gender"));
                                                else userInfo.setGender("null");
                                                if (!jO.isNull("profile_pic_location"))
                                                    userInfo.setProfilepiclocation(jO.getString("profile_pic_location"));
                                                else userInfo.setProfilepiclocation("null");
                                                if (!jO.isNull("phone_number"))
                                                    userInfo.setPhoneNumber(jO.getString("phone_number"));
                                                else userInfo.setPhoneNumber("0");
                                                if (!jO.isNull("location"))
                                                    userInfo.setLocation(jO.getString("location"));
                                                else userInfo.setLocation("null");
                                                if (!jO.isNull("cover_pic"))
                                                    userInfo.coverPic = jO.getString("cover_pic");
                                                if (!jO.isNull("blocked"))
                                                    userInfo.setBlocked(jO.getInt("blocked"));
                                                else userInfo.setHobby("null");
                                                userInfo.setFullName(userInfo.getFirstName() + " " + userInfo.getLastName());
                                                sqLiteHelper.insertUser(userInfo);
                                                getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE).edit()
                                                        .putString(StudentProfileFragment.TAG_FOR_USER_DETAIL_JSON, res).commit();
                                            } catch (Exception e) {
                                                Log.d("erroryaha", "here" + e.toString());
                                                userInfo = new UserInfo(0, "0");
                                                //new AlertDialogBuilder("Incorrect username or password","Incorrect username or password. Please try again.","OK","",getActivity());
                                            }

                                            if (userInfo.getUserNumber() == 0) {
                                                new AlertDialogBuilder("Incorrect email or password", "Incorrect email or password. Please try again.", "OK", "", getActivity());
                                            } else {
//                                                Log.d("error", "error" + userInfo.getUserNumber() + "\n" + userInfo.getFullName());

                                                Utilities.saveUserInfo(username, userInfo.getFullName(), userInfo.getUserNumber(), userInfo.getLevel(), userInfo.getInstitution(), userInfo.getGender(), getActivity());
                                                SharedPreferences sharedPreferences1 = getActivity().getApplicationContext().getSharedPreferences("rememberlogin", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                                editor1.putBoolean("isloggedin", true);
                                                editor1.commit();

                                                SharedPreferences loginTypeSP = getActivity().getApplicationContext().getSharedPreferences("type", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor loginTypeET = loginTypeSP.edit();
                                                loginTypeET.putInt("type", STUDENT);
                                                loginTypeET.commit();

                                                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("firstName", userInfo.getFirstName());
                                                editor.putString("lastName", userInfo.getLastName());
                                                editor.putString("level", userInfo.getLevel());
                                                editor.putString("institution", userInfo.getInstitution());
                                                editor.putString("gender", userInfo.getGender());
                                                editor.putString("fullName", userInfo.getFullName());
                                                editor.putString("cover_pic", userInfo.coverPic);
                                                editor.putInt("number", userInfo.getUserNumber());
                                                editor.putString("username", userInfo.getUserName());
                                                editor.putString("password", password);
                                                editor.putInt("verified", userInfo.getVerified());
                                                editor.putString("section", userInfo.getSection());
                                                editor.putString("faculty", userInfo.getFaculty());
                                                editor.putInt("blocked", userInfo.getBlocked());
                                                editor.putString("hobby", userInfo.getHobby());
                                                editor.putString("intrest", userInfo.getInterest());
                                                editor.putString("location", userInfo.getLocation());
                                                editor.putString("profile_pic_location", userInfo.getProfilepiclocation());
                                                editor.putString("phone_number", userInfo.getPhoneNumber());
                                                editor.putString("dbName", userInfo.getDbName());
                                                editor.putString("sn", userInfo.getUserNumber() + "");
                                                editor.putString("college_sn", userInfo.getCollegeSN());
                                                editor.commit();

                                                ImmortalApplication im = (ImmortalApplication) getActivity().getApplicationContext();
//                                                Toast.makeText(im, "" + UtilitiesAdi.giveMeSN(getActivity(), userInfo.getDbName()), Toast.LENGTH_SHORT).show();
                                                if (!UtilitiesAdi.giveMeSN(getActivity(), userInfo.getDbName()).equals("0"))
                                                    im.setTheSN(UtilitiesAdi.giveMeSN(getActivity(), userInfo.getDbName()));

                                                SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("dbName", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor e = sp.edit();
                                                e.putString("dbName", userInfo.getDbName());
                                                e.putBoolean("collegeSelected", true);
                                                e.putBoolean("signedUp", true);
                                                if (userInfo.getDbName().equals("none"))
                                                    e.putBoolean("collegeValid", false);
                                                else
                                                    e.putBoolean("collegeValid", true);
                                                e.commit();

                                                SharedPreferences sps = getActivity().getApplicationContext().getSharedPreferences("visitedOnce", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor11 = sps.edit();
                                                editor11.putBoolean("visited", true);
                                                editor11.putBoolean("loggedIn", true);
                                                editor11.commit();

                                                Intent i = new Intent(getActivity(), NavDrawerActivity.class);
                                                UtilitiesAdi.setProfileLoaded(getActivity(), false);
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

    private boolean emailValid(TextInputLayout view, String text) {
        if (!Utilities.isValidString(text)) {
            view.setError("This field is empty.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            view.setError("Not a valid email address.");
            return false;
        } else {
            view.setError(null);
            return true;
        }
    }

    private boolean passwordValid(TextInputLayout view, String text) {
        if (!Utilities.isValidString(text)) {
            view.setError("This field is empty");
            return false;
        } else {
            view.setError(null);
            return true;
        }
    }
}
