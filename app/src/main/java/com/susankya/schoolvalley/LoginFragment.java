package com.susankya.schoolvalley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;


public class LoginFragment extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CheckBox mLoggedIn;
    private Button mLoginButton;
    public static EditText mUsername,mPassword;
    private String mParam1;
    SQLiteHelper sqLiteHelper;
    SQLiteDatabase db;
    private String mParam2;
    private String username,password;
    String link= Utilities.encodeLinkSpace(FragmentCodes.HOST + "ChhalFaal/LoginSignup/login.php");
    private UserInfo userInfo;
    private int userNum;
    private boolean shouldStayLoggedIn=false;
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        //
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sqLiteHelper=new SQLiteHelper(getActivity());
        db=sqLiteHelper.getWritableDatabase();
        db.execSQL(
                "create table if not exists "+USERS_TABLE+" ( "+C_SN+" integer primary key autoincrement,"+
                        USER_NO+" int (10),"+FIRST_NAME+" varchar(100),"+LAST_NAME+" varchar(100),"+
                        PASSWORD+" varchar(100),"+USERNAME+" varchar(50),"+
                        VERIFIED+" int(1),"+INSTITUTION+" varchar,"+LEVEL+" varchar, "+
                        DBNAME+" varchar,"+
                        GENDER+" varchar(50),"+PROFILE_PIC_LOCATION+" varchar,"+LOCATION+" varchar,"
                        + PHONE_NUMBER+" int(10), "+INTEREST+" varchar, "+HOBBY+" varchar);"

        );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_login, container, false);
        mUsername=(EditText)v.findViewById(R.id.fragment_login_username);
        mPassword=(EditText)v.findViewById(R.id.fragment_login_password);
        mLoggedIn=(CheckBox)v.findViewById(R.id.fragment_login_checkbox);
        mLoggedIn.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

       shouldStayLoggedIn=isChecked;
    }
}
);
        mLoginButton=(Button)v.findViewById(R.id.fragment_login_login);
        mLoginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        username=mUsername.getText().toString().trim().toLowerCase();
                        password=mPassword.getText().toString();
                        String encodedUsername="",encodedPassword="";
                        try
                        {
                            encodedUsername= URLEncoder.encode(username, "utf-8");
                            encodedPassword=URLEncoder.encode(password,"utf-8");
                        }
                        catch(Exception e)
                        {
                            //
                        }
                        EditText[] etArray=new EditText[]{mUsername,mPassword};
                        if(new BlankEditText(new String[]{FragmentCodes.FIELD_EMPTY, FragmentCodes.FIELD_EMPTY},etArray).areAllFieldsOK())
                        {


                            PhpConnect phpConnect=new PhpConnect(link,"Logging in...",getActivity(),1,
                                    new String[]{username,password,"2568wwexve"},
                                    new String[]{"userid","password","cmdxxx"});
                            phpConnect.setListener(
                                    new PhpConnect.ConnectOnClickListener() {
                                        @Override
                                        public void onConnectListener(String res) {

                                            try
                                            {
                                                JSONObject jO=new JSONObject(res);
                                                //res.replace("null","0");
                                                 userNum=jO.getInt("user_no");
                                                String firstName=jO.getString("firstName");
                                                String lastName=jO.getString("lastName");
                                                userInfo=new UserInfo();
                                                userInfo.setUserNumber(userNum);
                                                userInfo.setFirstName(firstName);
                                                userInfo.setLastName(lastName);
                                                userInfo.setUserName(username);
                                                userInfo.setPassword(password);
                                               if(!jO.isNull("verified"))
                                                userInfo.setVerified(jO.getInt("verified"));
                                                else userInfo.setVerified(0);
                                                if(!jO.isNull("institution"))
                                                     userInfo.setInstitution(jO.getString("institution"));
                                                else userInfo.setInstitution("null");
                                                if(!jO.isNull("level"))
                                                userInfo.setLevel(jO.getString("level"));
                                                else userInfo.setLevel("null");
                                                if(!jO.isNull("dbName"))
                                                userInfo.setDbName(jO.getString("dbName"));
                                                else userInfo.setDbName("null");
                                                if(!jO.isNull("gender"))
                                                userInfo.setGender(jO.getString("gender"));
                                                else userInfo.setGender("null");
                                                if(!jO.isNull("profile_pic_location"))
                                                userInfo.setProfilepiclocation(jO.getString("profile_pic_location"));
                                                else userInfo.setProfilepiclocation("null");
                                                if(!jO.isNull("phone_number"))
                                                userInfo.setPhoneNumber(Integer.toString(jO.getInt("phone_number")));
                                                else userInfo.setPhoneNumber("0");
                                                if(!jO.isNull("location"))
                                                    userInfo.setLocation(jO.getString("location"));
                                                else userInfo.setLocation("null");
                                                if(!jO.isNull("intrest"))
                                                userInfo.setInterest(jO.getString("intrest"));
                                                else userInfo.setInterest("null");
                                                if(!jO.isNull("hobby"))
                                                userInfo.setHobby(jO.getString("hobby"));
                                                else userInfo.setHobby("null");
                                                userInfo.setFullName(userInfo.getFirstName()+" "+userInfo.getLastName());
                                               sqLiteHelper.insertUser(userInfo);

                                            }
                                            catch (Exception e)
                                            {
                                                //Log.d("erroryaha",e.toString());
                                                userInfo=new UserInfo(0,"0");
                                            }

                                            if(userInfo.getUserNumber()==0)
                                            {

                                                new AlertDialogBuilder("Incorrect username or password","Incorrect username or password. Please try again.","OK","",getActivity());
                                            }
                                            else
                                            {
                                                Utilities.saveUserInfo(username, userInfo.getFullName(), userInfo.getUserNumber(), userInfo.getLevel(), userInfo.getInstitution(), userInfo.getGender(), getActivity());
                                                SharedPreferences sharedPreferences1=getActivity().getApplicationContext().getSharedPreferences("rememberlogin",Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor1=sharedPreferences1.edit();
                                                editor1.putBoolean("isloggedin",true);
                                                editor1.commit();
                                                Toast.makeText(getActivity(),"Welcome, "+userInfo.getFullName(),Toast.LENGTH_SHORT).show();
                                                Intent i=new Intent(getActivity(),NavDrawerActivity.class);
                                                i.putExtra("usernumberho",userNum);
                                                SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                                editor.putString("firstName",userInfo.getFirstName());
                                                editor.putString("lastName",userInfo.getLastName());
                                                editor.putString("level",userInfo.getLevel());
                                                editor.putString("institution",userInfo.getInstitution());
                                                editor.putString("gender",userInfo.getGender());
                                                editor.putString("fullName",userInfo.getFullName());
                                                editor.putInt("number", userInfo.getUserNumber());
                                                editor.putString("username", userInfo.getUserName());
                                                editor.putString("password", password);
                                                editor.putInt("verified", userInfo.getVerified());
                                                editor.putString("hobby", userInfo.getHobby());
                                                editor.putString("intrest", userInfo.getInterest());
                                                editor.putString("location", userInfo.getLocation());
                                                editor.putString("profile_pic_location", userInfo.getProfilepiclocation());
                                                editor.putString("phone_number",userInfo.getPhoneNumber());
                                                editor.putString("dbName",userInfo.getDbName());
                                                editor.commit();
                                                SharedPreferences sp=getActivity().getApplicationContext().getSharedPreferences("dbName", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor e=sp.edit();
                                                e.putString("dbName", userInfo.getDbName());
                                                e.putBoolean("collegeSelected", true);
                                                e.putBoolean("signedUp", true);
                                                if(userInfo.getDbName().equals("none"))
                                                    e.putBoolean("collegeValid",false);
                                                else
                                                    e.putBoolean("collegeValid",true);
                                                e.commit();
                                                SharedPreferences sps=getActivity().getApplicationContext().getSharedPreferences("visitedOnce", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor11=sps.edit();
                                                editor11.putBoolean("visited",true);
                                                editor11.putBoolean("loggedIn",true);
                                                editor11.commit();
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

}
