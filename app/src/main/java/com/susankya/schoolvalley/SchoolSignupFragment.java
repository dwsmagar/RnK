package com.susankya.schoolvalley;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SchoolSignupFragment extends android.support.v4.app.Fragment implements FragmentCodes {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private UserInfo userInfo;
    private boolean checked=false;
    int userNum;
    SQLiteHelper sqLiteHelper;
    String mobileNumber;
    @BindView(R.id.mobileNum)EditText mobileNumET;
    SQLiteDatabase db;
    @BindView(R.id.email_address)EditText emailET;
    @BindView(R.id.school_name)EditText schoolNameET;
    @BindView(R.id.location)EditText locationET;
  @BindView(R.id.toolbar)  Toolbar toolbar;
    String collegeSN;
    String link= FragmentCodes.HOST+"Home/Susankya Database/Signup.php";
    private EditText mUsername,mPassword,mConfirmPassword,mFirstName,mLastName;
    private Button mSignUp;
    String username,password,confirmPassword;
    public static SchoolSignupFragment newInstance(String param1, String param2) {
        SchoolSignupFragment fragment = new SchoolSignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SchoolSignupFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteHelper=new SQLiteHelper(getActivity());
        db=sqLiteHelper.getWritableDatabase();
        db.execSQL(
                "create table if not exists " + USERS_TABLE + " ( " + C_SN + " integer primary key autoincrement," +
                        USER_NO + " int (10)," + FIRST_NAME + " varchar(100)," + LAST_NAME + " varchar(100)," +
                        PASSWORD + " varchar(100)," + USERNAME + " varchar(50)," +
                        VERIFIED + " int(1)," + INSTITUTION + " varchar," + LEVEL + " varchar, " +
                        DBNAME + " varchar," +
                        GENDER + " varchar(50)," + PROFILE_PIC_LOCATION + " varchar," + LOCATION + " varchar,"
                        + PHONE_NUMBER + " int(10), " + INTEREST + " varchar, " + HOBBY + " varchar);"

        );
        if(StudentInActivity.searchItemArrayList!=null)
        StudentInActivity.searchItemArrayList.clear();
    }


    private boolean arePasswordsMatching()
    {

            if(password.equals(confirmPassword)) return true;
            else return false;

    }


    public class UsernameValidator{

        private Pattern pattern;
        private Matcher matcher;

        private static final String USERNAME_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        public UsernameValidator(){
            pattern = Pattern.compile(USERNAME_PATTERN);
        }


        public boolean validate(final String username){

            matcher = pattern.matcher(username);
            return matcher.matches();

        }
    }
    public boolean isUsernameValid()
    {
        if(username.length()<6)
            return false;
        else return true;
    }
    public boolean isPasswordValid()
    {
        if(password.length()<6)
            return false;
        else return true;
    }


    UsernameValidator usernameValidator;
    String email;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_school_signup, container, false);
        ButterKnife.bind(this, view);
        usernameValidator=new UsernameValidator();
        toolbar.setTitle("Register your school");
        mUsername=(EditText)view.findViewById(R.id.fragment_signup_username);
        mFirstName=(EditText)view.findViewById(R.id.fragment_signup_firstname);
        mLastName=(EditText)view.findViewById(R.id.fragment_signup_lastname);
        mPassword=(EditText)view.findViewById(R.id.fragment_signup_password);
        mConfirmPassword=(EditText)view.findViewById(R.id.fragment_signup_confirm_password);
        mSignUp=(Button)view.findViewById(R.id.fragment_signup_signup);

        mSignUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        username=mUsername.getText().toString().trim().toLowerCase();
                        password=mPassword.getText().toString();
                        mobileNumber=mobileNumET.getText().toString();
                        confirmPassword=mConfirmPassword.getText().toString();
                        email=emailET.getText().toString();
                        link= Utilities.encodeLinkSpace(link);
                        String[] strings=new String[]{FragmentCodes.FIELD_EMPTY, FragmentCodes.FIELD_EMPTY, FragmentCodes.FIELD_EMPTY, FragmentCodes.FIELD_EMPTY, FragmentCodes.FIELD_EMPTY,FragmentCodes.FIELD_EMPTY,FragmentCodes.FIELD_EMPTY};
                        EditText[] editTexts=new EditText[]{mUsername,mFirstName,mLastName,mPassword,mobileNumET,emailET,locationET};

                        if(isFieldEmpty(mUsername) || isFieldEmpty(mPassword) || isFieldEmpty(mobileNumET)
                                || isFieldEmpty(mConfirmPassword) || isFieldEmpty(emailET)
                               || isFieldEmpty(schoolNameET) || isFieldEmpty(locationET))
                        {
                            Snackbar.make(view,"All fields must be filled",Snackbar.LENGTH_LONG).show();
                        }
                        else
                        {
                            if(arePasswordsMatching())
                            {
                                if(isUsernameValid()) {

                                    if (!usernameValidator.validate(email))
                                        Snackbar.make(view,"Email address is not valid",Snackbar.LENGTH_LONG).show();

                                }

                                if(!isPasswordValid())
                                   Snackbar.make(view,"Your password should be of minimum 6 characters long",Snackbar.LENGTH_LONG).show();

                                if(isUsernameValid() && isPasswordValid() && usernameValidator.validate(email))
                                {
                                    callPhp();
                                }
                            }
                            else Snackbar.make(view,"Passwords do not match",Snackbar.LENGTH_LONG).show();
                        }

                    }
                }
        );

        return view;
    }


    boolean isFieldEmpty(EditText e)
    {
        return (e.getText().toString().trim().isEmpty());
    }

    void callPhp()
    {

        new PhpConnect(link,"Please wait...",getActivity(),1,
                new String[]{username,password,password,schoolNameET.getText().toString(),mobileNumber,email,locationET.getText().toString().trim(),CMDXXX},
                new String[]{"userName","password","rePassword","CollegeName","primaryN","secondaryN","location","cmdxxx"}).setListener(
                new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        if(res.trim().equals("0"))
                        {
                            new AlertDialogBuilder("Email exists","Please enter a different email address and try again.","OK","",getActivity());
                        }
                        else if(res.trim().equals("0p"))
                        {
                            mobileNumET.setText("");
                            mobileNumber="";
                            new AlertDialogBuilder("Invalid mobile number","Please enter a valid mobile number and try again.","OK","",getActivity());
                        }
                        else if(res.trim().equals("1"))
                        {
                            getActivity().runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {

                                            UtilitiesAdi.showPopUp(getContext(), "Thank you",
                                                    "We will review the information you provided and confirm your acco" +
                                                            "unt as soon as possible. Thank you!",
                                                    "OK",
                                                    "",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    },
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                        }

                                        }
                            );
                        }
                    }
                }
        );

    }
}
