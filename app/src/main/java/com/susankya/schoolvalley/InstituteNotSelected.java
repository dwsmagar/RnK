package com.susankya.schoolvalley;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class InstituteNotSelected extends android.support.v4.app.DialogFragment implements FragmentCodes {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private Spinner instituteList;
    private Button okay,cancel;
    public String[] collegeNames,databaseNames,aboutsPHP;
    private String mParam1;
    private String mParam2;
    String dbNAME;
    UserInfo userInfo;
    ArrayList<String> collegeNameArray=new ArrayList<>(),databaseNameArray=new ArrayList<>();
    public static InstituteNotSelected newInstance(String param1, String param2) {
        InstituteNotSelected fragment = new InstituteNotSelected();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InstituteNotSelected() {
        // Required empty public constructor
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
        View v=inflater.inflate(R.layout.fragment_institute_not_selected, container, false);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
   instituteList=(Spinner)v.findViewById(R.id.instituteSpinner);
        okay=(Button)v.findViewById(R.id.ok_button);
        cancel=(Button)v.findViewById(R.id.notListed_button);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getActivity(),"We are sorry to hear that! You can ask your institution to register " +
                                "at 46.101.81.232",Toast.LENGTH_LONG).show();
                        getDialog().dismiss();
                    }
                }
        );
loadCollegeList();
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbNAME = databaseNames[instituteList.getSelectedItemPosition()];
                String link = FragmentCodes.CHHALFAL + "Profile/UpdateWholeProfile.php";

                parseProfileJSON(UtilitiesAdi.loadProfileJSON(getActivity()));
        try {
            updateProfile();
        }
            catch(Exception e){
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            }
            }
        });
        return  v;
    }
    void parseProfileJSON(String res)
    {
        try {
            JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
            JSONObjectAuto jO = new JSONObjectAuto(jsonArray.getJSONObject(0));
                            /*"user_no": "2",
                                    "firstName": "Abc",
                                    "lastName": "Xyz",
                                    "password": "XXX",
                                    "userid": "test",
                                    "verified": "0",
                                    "institution": null,
                                    "level": null,
                                    "dbName": null,
                                    "gender": null,
                                    "profile_picture_location": null,
                                    "phone_number": null,
                                    "location": null,
                                    "interest": null,
                                    "hobby": null,
                                    "thumbmailLocation": "",
                                    "SeePostsFrom": "All",
                                    "totalPosts": "13",
                                    "totalAnswers": "10",
                                    "bestAnswers": "1"*/
            String userNumber = jO.getString("user_no");
            String firstName = jO.getString("firstName");
            String lastName = jO.getString("lastName");
            String userid = jO.getString("userid");
            String verified = jO.getString("verified");
            String totalPosts = jO.getString("totalPosts");
            String totalAnswers = jO.getString("totalAnswers");
            String bestAnswers = jO.getString("bestAnswers");
            String institution = jO.getString("institution");
            String level = jO.getString("level");
            String dbName = jO.getString("dbName");
            String genderPHP = jO.getString("gender");
            String profilepic = jO.getString("profile_picture_location");
            String phoneNum = jO.getString("phone_number");
            String location = jO.getString("location");
            String interest = jO.getString("interest");
            String hobby = jO.getString("hobby");
            String thumbnail = jO.getString("thumbmailLocation");
            String SeePostsFrom = jO.getString("SeePostsFrom");
            String gender = "";
            if (genderPHP.equals("m")) gender = "Male";
            else gender = "Female";

            userInfo = new UserInfo();
            userInfo.setFirstName(firstName);
            userInfo.setLastName(lastName);
            userInfo.setUserName(userid);
            userInfo.setSeePostsFrom(SeePostsFrom);
            userInfo.setVerified(Integer.parseInt(verified));
            userInfo.setGender(gender);
            userInfo.setInstitution(institution);


            userInfo.setFullName(firstName + " "+lastName);
            userInfo.setPhoneNumber(phoneNum);
            userInfo.setInterest(interest);
            userInfo.setHobby(hobby);

            userInfo.setUserNumber(Integer.parseInt(userNumber));

          aboutsPHP = new String[]{userid, firstName, lastName, level, instituteList.getSelectedItem().toString(), genderPHP, phoneNum, location, interest, hobby};


        } catch (Exception e) {
            //Log.d("errorxx", e + "");
            // UtilitiesAdi.toast(e.toString(),getActivity());
        }

    }
    void updateProfile()
    {

        String link= FragmentCodes.CHHALFAL+"Profile/UpdateWholeProfile.php";
        String aboutTitles[] = new String[]{"Username", "First Name", "Last Name", "Level", "Institution", "Gender", "Mobile Number",
                "Location", "Interests", "Hobbies","userNumber"};

        String[] placeholders=new String[]{"userName","firstName","lastName","level","institution","gender" ,"phone_number"
                ,"location" ,"interest","hobby","dbName"
                ,"proPicExists","userNumber"};


        String[] params=new String[placeholders.length];
        //  Toast.makeText(getActivity(), userAboutArrayList.size()+"",Toast.LENGTH_SHORT).show();
        for(int i=0;i<aboutsPHP.length;i++)
        {


            params[i]=aboutsPHP[i];
        }

        Utilities.setDatabaseName(getActivity().getApplicationContext(), dbNAME);

        params[10]= Utilities.getDatabaseName(getActivity());
        params[11]="1";
        params[12]= Utilities.getUserNumber(getActivity())+"";


        new PhpConnect(link,"Updating your profile...",getActivity(),1,params,placeholders).setListener(
                new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        try {
                            //Log.d("errorAtUpdate", res);
                            UtilitiesAdi.saveProfileJSON(getActivity(), res);
                            Utilities.setDatabaseName(getActivity().getApplicationContext(), dbNAME);
                            Utilities.updateUISharedPreferences(getActivity());

                            ((NavDrawerActivity)getActivity()).updateHeader();
                            getDialog().dismiss();
                            // Toast.makeText(getActivity(),"AFTER: "+Utilities.getDatabaseName(getActivity()),Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }
                    }
                }
        );

    }
    public void loadCollegeList()
    {
        String res= UtilitiesAdi.loadJSON(getActivity().getApplicationContext(), "collegelist");
        try
        {
            JSONArray jsonArray=(JSONArray) new JSONTokener(res).nextValue();
            collegeNames=new String[jsonArray.length()];
            databaseNames=new String[jsonArray.length()];
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String collegeName=jsonObject.getString("college_name");
                String databaseName=jsonObject.getString("database_name");
                collegeNames[i]=collegeName;
                databaseNames[i]=databaseName;
                collegeNameArray.add(collegeName);
                databaseNameArray.add(databaseName);

            }
            CustomSpinnerAdapter collegeAdapter=new CustomSpinnerAdapter(getActivity(), R.id.college_spinner, collegeNames);
            instituteList.setAdapter(collegeAdapter);
                   }

        catch (Exception e)
        {

        }

    }

}
