package com.susankya.rnk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FirstSignUpFragment extends android.support.v4.app.Fragment implements FragmentCodes {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private UserInfo userInfo;
    private String mParam1;
    private boolean checked = false;
    private String mParam2;

    public String[] collegeNames, databaseNames;
    ArrayList<String> collegeNameArray = new ArrayList<>(), databaseNameArray = new ArrayList<>();
    String link = FragmentCodes.MAIN_DATABASE + "users/SignUp.php";
    SQLiteHelper sqLiteHelper;
    UsernameValidator usernameValidator;
    SQLiteDatabase db;
    int userNum;

    public static int chosen = 0;
    String collegeSN;
    String mobileNumber;
    String selectedDB;
    String institution;
    public static TextViewPlus selectedInst;
    String category;
    String emailAdd, passStr, confirmPass, fName, lName, mobileStr, locationStr, gender;

    @BindView(R.id.emailAddress)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.re_password)
    EditText re_password;
    @BindView(R.id.firstName)
    EditText first_name;
    @BindView(R.id.lastName)
    EditText last_name;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.email_layout)
    TextInputLayout email_layout;
    @BindView(R.id.password_layout)
    TextInputLayout password_layout;
    @BindView(R.id.confirm_layout)
    TextInputLayout confirm_layout;
    @BindView(R.id.firstname_layout)
    TextInputLayout firstname_layout;
    @BindView(R.id.lastname_layout)
    TextInputLayout lastname_layout;
    @BindView(R.id.mobile_layout)
    TextInputLayout mobile_layout;
    @BindView(R.id.address_layout)
    TextInputLayout address_layout;
    @BindView(R.id.btnConfirm)
    Button create;
    @BindView(R.id.radioMale)
    RadioButton male;
    @BindView(R.id.radioFemale)
    RadioButton female;
    @BindView(R.id.radioGroup1)
    RadioGroup radioGroup;
    @BindView(R.id.backArrow)
    View backArrow;

    public static FirstSignUpFragment newInstance(String param1, String param2) {
        FirstSignUpFragment fragment = new FirstSignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FirstSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Toast.makeText(getActivity(),currentActivity+"SIGN",Toast.LENGTH_LONG).show();
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
        if (StudentInActivity.searchItemArrayList != null)
            StudentInActivity.searchItemArrayList.clear();
        loadCollegeList();
    }


    private void checkCategory() {
        String c = ImmortalApplication.category.toLowerCase();
        category = c;
    }

    private boolean arePasswordsMatching() {
        if (password.equals(re_password)) return true;
        else return false;
    }

    public void loadCollegeList() {
        String res = UtilitiesAdi.loadJSON(getActivity().getApplicationContext(), "collegelist");
        try {
            JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
            collegeNames = new String[jsonArray.length()];
            databaseNames = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String collegeName = jsonObject.getString("college_name");
                String databaseName = jsonObject.getString("database_name");
                SearchItem s = new SearchItem();
                s.name = collegeName;
                String sn = jsonObject.getString("sn");
                s.collegeSN = Integer.parseInt(sn);
                s.location = jsonObject.getString("location");
                StudentInActivity.searchItemArrayList.add(s);
                collegeNames[i] = collegeName;
                databaseNames[i] = databaseName;
                collegeNameArray.add(collegeName);
                databaseNameArray.add(databaseName);
            }
        } catch (Exception e) {

        }
    }


    public class UsernameValidator {

        private Pattern pattern;
        private Matcher matcher;

        private static final String USERNAME_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x07\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        public UsernameValidator() {
            pattern = Pattern.compile(USERNAME_PATTERN);
        }

        public boolean validate(final String username) {
            matcher = pattern.matcher(username);
            return matcher.matches();
        }
    }

    public boolean isPasswordValid() {
        if (password.length() < 6)
            return false;
        else return true;
    }

    public void getLevelList() {
        String link = "http://46.101.81.232/App/New App/ChhalFaal/Profile/level.php";
        link = Utilities.makeUrl(link);

        new PhpConnect(link, "Almost done", getActivity(), 1, new String[]{CMDXXX}, new String[]{"cmdxxx"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                try {
                    JSONArray JA = (JSONArray) new JSONTokener(res).nextValue();
                    String[] strings = new String[JA.length()];
                    for (int i = 0; i < JA.length(); i++) {
                        strings[i] = JA.getString(i);
                    }
                    Set set = new HashSet(Arrays.asList(strings));
                    getActivity().getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE).edit().putStringSet("levels", set).apply();
                    getActivity().getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE).edit().putBoolean("hasGotLevel", true).apply();
                    // loadCollegeList();
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first_signup, container, false);
        ButterKnife.bind(this, v);
        if (getActivity().getSharedPreferences("visitedOnce", Context.MODE_PRIVATE).getBoolean("visited", false)) {
            //getCollegeList();
        }
        usernameValidator = new UsernameValidator();
        checkCategory();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();
            }
        });
        create.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean emailErr, passErr, confirmErr, mobileErr, fnameErr, lnameErr, locationErr, genderErr;

                        emailAdd = email.getText().toString().trim();
                        passStr = password.getText().toString().trim();
                        confirmPass = re_password.getText().toString().trim();
                        mobileStr = contact.getText().toString().trim();
                        fName = first_name.getText().toString().trim();
                        lName = last_name.getText().toString().trim();
                        locationStr = address.getText().toString().trim();

                        institution = ((ImmortalApplication) getActivity().getApplication()).bLSName;
                        collegeSN = ((ImmortalApplication) getActivity().getApplication()).beforeLoginSN;

                        link = Utilities.encodeLinkSpace(link);

                        if (emailAdd.isEmpty() || emailAdd.length() == 0) {
                            email_layout.setError("This field is empty");
                          //  email_layout.setErrorTextAppearance(getResources().getColor(R.id.error_color));
                            emailErr = true;
                        } else if (!usernameValidator.validate(emailAdd)) {
                            email_layout.setError("Not a valid email address");
                            emailErr = true;
                        } else {
                            email_layout.setError(null);
                            emailErr = false;
                        }

                        if (passStr.isEmpty() || passStr.length() == 0) {
                            password_layout.setError("This field is empty");
                            passErr = true;
                        } else if (!isPasswordValid()) {
                            password_layout.setError("Password should be of minimum 6 characters long");
                            passErr = true;
                        } else {
                            password_layout.setError(null);
                            passErr = false;
                        }

                        if (confirmPass.isEmpty() || confirmPass.length() == 0) {
                            confirm_layout.setError("This field is empty");
                            confirmErr = true;
                        } else if (!confirmPass.equals(passStr)) {
                            confirm_layout.setError("Password do not match");
                            confirmErr = true;
                        } else {
                            confirm_layout.setError(null);
                            confirmErr = false;
                        }

                        if (fName.isEmpty() || fName.length() == 0) {
                            firstname_layout.setError("This field is empty");
                            fnameErr = true;
                        } else {
                            firstname_layout.setError(null);
                            fnameErr = false;
                        }

                        if (lName.isEmpty() || lName.length() == 0) {
                            lastname_layout.setError("This field is empty");
                            lnameErr = true;
                        } else {
                            lastname_layout.setError(null);
                            lnameErr = false;
                        }

//                        if (locationStr.isEmpty() || locationStr.length() == 0) {
//                            address_layout.setError("This field is empty");
//                            locationErr = true;
//                        } else {
//                            address_layout.setError(null);
//                            locationErr = false;
//                        }
//
//                        int ID = radioGroup.getCheckedRadioButtonId();
//
//                        if (ID == R.id.radioMale) {
//                            genderErr = false;
//                            gender = "Male";
//                        }
//                        else if (ID == R.id.radioFemale){
//                            genderErr = false;
//                            gender = "Female";}
//                        else{
//                            genderErr = true;
//                            Toast.makeText(getActivity(), "Choose the gender", Toast.LENGTH_SHORT).show();}
//
//                        if (mobileStr.isEmpty() || mobileStr.length() == 0) {
//                            mobile_layout.setError("This field is empty");
//                            mobileErr = true;
//                        } else if (mobileStr.length() < 10) {
//                            mobile_layout.setError("Mobile number should not be less than 10");
//                            mobileErr = true;
//                        } else {
//                            mobile_layout.setError(null);
//                            mobileErr = false;
//                        }

                        if (!emailErr && !passErr && !confirmErr && !lnameErr && !fnameErr ) {
                            if (checked) {
                                institution = "Not Selected";
                                selectedDB = "none";
                            }
                            callPhp();
                        }
                    }
                }
        );
        return v;
    }


    String place1, place2;
    String placeholders[], parameters[];

    void callPhp() {
        String token = FirebaseInstanceId.getInstance().getToken();
        String consultancyplace[] = new String[]{"firstName", "lastName", "userid", "password", "cmdxxx", "college_sn","regId"};
        String consultancypara[] = new String[]{fName, lName, emailAdd, passStr, CMDXXX, "64",token};

        placeholders = consultancyplace;
        parameters = consultancypara;

        new PhpConnect(link, "Please wait...", getActivity(), 1, parameters, placeholders).setListener(
                new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        Log.d("fatal", res);

                        if (res.trim().equals("0")) {
                            new AlertDialogBuilder("Email exists", "Please enter a different email address and try again.", "OK", "", getActivity());
                        } else if (res.trim().equals("0p")) {
                            new AlertDialogBuilder("Invalid mobile number", "Please enter a valid mobile number and try again.", "OK", "", getActivity());
                            mobileStr = "";
                        } else if (res.trim().equals("1")) {
                            SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("dbName", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("collegeSelected", true);
                            editor.putBoolean("signedUp", true);

                            SharedPreferences sps = getActivity().getApplicationContext().getSharedPreferences("visitedOnce", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sps.edit();
                            editor1.putBoolean("visited", true);
                            editor1.putBoolean("loggedIn", true);
                            editor1.commit();

//                            if (selectedInst.getText().equals("None")) {
//                                editor.putBoolean("collegeValid", false);
//                            } else
//                                editor.putBoolean("collegeValid", true);
                            editor.commit();

                            String token = FirebaseInstanceId.getInstance().getToken();
                            String loginLink = Utilities.encodeLinkSpace(FragmentCodes.MAIN_DATABASE + "users/login.php");
                            PhpConnect phpConnect = new PhpConnect(loginLink, "Logging in...", getActivity(), 1,
                                    new String[]{emailAdd, passStr, "2568wwexve", token},
                                    new String[]{"userid", "password", "cmdxxx", "regId"});
                            phpConnect.setListener(
                                    new PhpConnect.ConnectOnClickListener() {
                                        @Override
                                        public void onConnectListener(String res) {
                                            try {
                                                Log.d("fatal", "Login " + res);

                                                JSONObject jO = new JSONObject(res);
                                                userNum = jO.getInt("user_no");
                                                String firstName = jO.getString("firstName");
                                                String lastName = jO.getString("lastName");
                                                String collegeSN = jO.getString("college_sn");
                                                String section = jO.getString("section");
                                                String roll = jO.getString("roll");

                                                userInfo = new UserInfo();
                                                userInfo.setUserNumber(userNum);
                                                userInfo.setFirstName(firstName);
                                                userInfo.setLastName(lastName);
                                                userInfo.collegeSN = collegeSN;
                                                userInfo.roll = roll;
                                                userInfo.section = section;
                                                userInfo.setUserName(emailAdd);
                                                userInfo.setPassword(passStr);

                                                if (!jO.isNull("verified"))
                                                    userInfo.setVerified(jO.getInt("verified"));
                                                else
                                                    userInfo.setVerified(0);

                                                if (!jO.isNull("college_name"))
                                                    userInfo.setInstitution(jO.getString("college_name"));
                                                else
                                                    userInfo.setInstitution("null");

                                                if (!jO.isNull("college_sn"))
                                                    userInfo.setCollegeSN(jO.getString("college_sn"));
                                                else
                                                    userInfo.setCollegeSN("null");

                                                if (!jO.isNull("level"))
                                                    userInfo.setLevel(jO.getString("level"));
                                                else
                                                    userInfo.setLevel("null");

                                                if (!jO.isNull("dbName"))
                                                    userInfo.setDbName(jO.getString("dbName"));
                                                else
                                                    userInfo.setDbName("null");

                                                if (!jO.isNull("gender"))
                                                    userInfo.setGender(jO.getString("gender"));
                                                else
                                                    userInfo.setGender("null");

                                                if (!jO.isNull("profile_pic_location"))
                                                    userInfo.setProfilepiclocation(jO.getString("profile_pic_location"));
                                                else
                                                    userInfo.setProfilepiclocation("null");

                                                if (!jO.isNull("phone_number"))
                                                    userInfo.setPhoneNumber(jO.getString("phone_number"));
                                                else
                                                    userInfo.setPhoneNumber("0");

                                                if (!jO.isNull("location"))
                                                    userInfo.setLocation(jO.getString("location"));
                                                else
                                                    userInfo.setLocation("null");

                                                if (!jO.isNull("cover_pic"))
                                                    userInfo.coverPic = jO.getString("cover_pic");

                                                if (!jO.isNull("college_sn"))
                                                    userInfo.setCollegeSN(jO.getString("college_sn"));

                                                userInfo.setBlocked(jO.getInt("blocked"));
                                                userInfo.setFullName(userInfo.getFirstName() + " " + userInfo.getLastName());
                                                sqLiteHelper.insertUser(userInfo);
                                                getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE).edit()
                                                        .putString(StudentProfileFragment.TAG_FOR_USER_DETAIL_JSON, res).commit();
                                            } catch (Exception e) {
                                                //Log.d("erroryaha", e.toString());
                                                userInfo = new UserInfo(0, "0");
                                            }

                                            if (userInfo.getUserNumber() == 0) {
                                                new AlertDialogBuilder("Incorrect username or password", "Incorrect username or password. Please try again.", "OK", "", getActivity());
                                            } else {
                                                Utilities.saveUserInfo(emailAdd, userInfo.getFullName(), userInfo.getUserNumber(), userInfo.getLevel(), userInfo.getInstitution(), userInfo.getGender(), getActivity());
                                                SharedPreferences sharedPreferences1 = getActivity().getApplicationContext().getSharedPreferences("rememberlogin", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                                editor1.putBoolean("isloggedin", true);
                                                editor1.commit();
                                                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("firstName", userInfo.getFirstName());
                                                editor.putString("lastName", userInfo.getLastName());
                                                editor.putString("level", userInfo.getLevel());
                                                editor.putString("institution", userInfo.getInstitution());
                                                editor.putInt("blocked", userInfo.getBlocked());
                                                editor.putString("gender", userInfo.getGender());
                                                editor.putString("fullName", userInfo.getFullName());
                                                editor.putInt("number", userInfo.getUserNumber());
                                                editor.putString("mobile", userInfo.getPhoneNumber());
                                                editor.putString("section", userInfo.section);
                                                editor.putString("roll", userInfo.roll);
                                                editor.putString("location", userInfo.getLocation());
                                                editor.putString("cover_pic", userInfo.coverPic);
                                                editor.putString("sn", userInfo.getUserNumber() + "");
                                                editor.putString("username", userInfo.getUserName());
                                                editor.putString("password", passStr);
                                                editor.putString("college_sn", userInfo.getCollegeSN());
                                                editor.commit();
                                                Utilities.setDatabaseName(getActivity(), userInfo.getDbName());
                                                UtilitiesAdi.setProfileLoaded(getActivity(), false);
                                                Toast.makeText(getActivity(), "Account created successfully.", Toast.LENGTH_LONG).show();

                                                getActivity().runOnUiThread(
                                                        new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                getActivity().getApplicationContext().getSharedPreferences("type", Context.MODE_PRIVATE).edit().putInt("type", STUDENT).commit();
                                                                Intent i = new Intent(getActivity().getApplicationContext(), NavDrawerActivity.class);
                                                                startActivity(i);
                                                                getActivity().finish();
                                                                //getActivity().startActivity(new Intent(getActivity(), NavDrawerActivity.class));
                                                            }
                                                        }
                                                );
                                            }
                                        }
                                    }
                            );
                        }
                    }
                }
        );
    }
}
