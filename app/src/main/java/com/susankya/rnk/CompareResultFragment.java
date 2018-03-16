package com.susankya.rnk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ajay on 8/10/2015.
 */
public class CompareResultFragment extends Fragment implements FragmentCodes {
    private EditText yourRoll,friendsRoll;
    private Spinner spinnerClass,spinnerExam,spinnerFaculty;
    private Button button;
    private int spinnerValue,rollNumber;
    private String selectedClass,selectedFaculty,selectedExam,exam;
    public static final String YROLL="YOURROLL";
    public static final String FROLL="FRIENDSROLL";
    public static final String CLASS="CLASS";
    public static final String FACULTY="FACULTY";
    public static final String EXAM="EXAM";
    private String yourValues[],friendsValues[],roll1,roll2;
    private int ERROR=0;
    private NetConnector netConnector;
    public void onCreate(Bundle SIS)
    {
        super.onCreate(SIS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_result_compare, container, false);

        LinearLayout ll=(LinearLayout)v.findViewById(R.id.ll);
        Animation animation= ((NavDrawerActivity)getActivity()).getAnimation();
        ll.setAnimation(animation);
        spinnerClass =(Spinner) v.findViewById(R.id.spinnerclass);
        spinnerExam=(Spinner) v.findViewById(R.id.spinnerexam);
        ArrayAdapter<CharSequence> adapterExam=ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.exam_list
        , R.layout.spinner_not_selected_layout);
        adapterExam.setDropDownViewResource(R.layout.spinner_layout);
        spinnerExam.setAdapter(adapterExam);
        yourRoll=(EditText) v.findViewById(R.id.your_roll);
        friendsRoll=(EditText) v.findViewById(R.id.your_friends_roll);
        button=(Button) v.findViewById(R.id.submit);
        spinnerFaculty=(Spinner) v.findViewById(R.id.spinnerfaculty);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.class_spinner_array, R.layout.spinner_not_selected_layout);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_layout);
// Apply the adapter to the spinnerClass
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.faculty_list,
                R.layout.spinner_not_selected_layout);
        adapter1.setDropDownViewResource(R.layout.spinner_layout);


// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        spinnerClass.setAdapter(adapter);
        spinnerFaculty.setAdapter(adapter1);


        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ERROR=0;
                        roll1=yourRoll.getText().toString().trim();
                         roll2=friendsRoll.getText().toString().trim();
                        assignExamValue(spinnerExam.getSelectedItemPosition());
                        if(roll1.isEmpty() || roll2.isEmpty())
                        {
                            if(roll1.isEmpty())
                                yourRoll.setError("Enter your roll number");
                            if(roll2.isEmpty())
                                friendsRoll.setError("Enter your friend's roll number");
                        }
                        else if(roll1.equals(roll2))
                        {
                            showError("Enter two different roll numbers.");
                        }
                        else
                        {
                            selectedClass= spinnerClass.getSelectedItem().toString();
                            selectedFaculty=spinnerFaculty.getSelectedItem().toString();
                            yourValues=new String[10];
                            friendsValues=new String[10];
                            setFacultyCode();
                            //Log.d("SEE", selectedClass);
                            //Log.d("SEE",selectedFaculty);



                            try
                            {
                                login(roll1,roll2,exam,selectedClass,selectedFaculty);
                            }
                            catch(Exception e)
                            {

                            }


                        }
                    }
                }
        );
    return v;
    }
    public void assignExamValue(int s)
    {
        switch(s)
        {
            case 0:
                exam="first";
                break;
            case 1:
                exam="second";
                break;
            case 2:
                exam="third";
                break;
            case 3:
                exam="final";
                break;
        }
    }
    public void login(String ROLL1,String ROLL2,String EXAM,String LEVEL,String FACULTY) throws Exception
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            netConnector=new NetConnector(getActivity(),getFragmentManager());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {

                new NetConnector(getActivity(),getFragmentManager()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,ROLL1,EXAM,LEVEL,FACULTY,"1").get();

                new NetConnector(getActivity(),getFragmentManager()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,ROLL2,EXAM,LEVEL,FACULTY,"2");

            }

            else
            {

                new NetConnector(getActivity(),getFragmentManager()).execute(ROLL1, EXAM, LEVEL, FACULTY, "1").get();

                new NetConnector(getActivity(),getFragmentManager()).execute(ROLL2,EXAM,LEVEL,FACULTY,"2");

            }
            //Log.d("so fast","so fast");



        } else {
            Toast.makeText(getActivity().getApplicationContext(), "NO CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }
    private void setFacultyCode()
    {
        if(selectedFaculty.equals("Science"))
            selectedFaculty="sc";
        else if(selectedFaculty.equals("Commerce"))
            selectedFaculty="com";
        else
            selectedFaculty="hm";
    }
    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((NavDrawerActivity) getActivity()).onSectionAttached(3);
    }
    private  void showError(String error)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle("Error");
        builder1.setMessage(error);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    private class NetConnector extends AsyncTask<String, Void, String> {
        Activity act;
        String link,mStudentValues[],returnStudentValues[],roll1Values[],roll2Values[];
        int level,counter=0;
        String faculty;
        FragmentManager fm;
        private ProgressDialog dialog;

        Context context;
        public NetConnector(Context mContext,FragmentManager FM)
        {

            context=mContext;
            fm=FM;
            dialog = new ProgressDialog(context);

        }
        @Override
        protected String doInBackground(String... arg0) {

            try{


                //Log.d("vieres", "Start");
                String roll=arg0[0];
                String exam=arg0[1];
                String sLevel=arg0[2];
                String mFaculty=arg0[3];
                counter=Integer.parseInt(arg0[4]);
                level=Integer.parseInt(sLevel);
                faculty=mFaculty;
                link = HOST+"resultGiver.php?roll="+roll+"&terminal="+exam+"&class="+level+"&faculty="+mFaculty;
                //link = "http://binarycalc.host22.com/fetch.php?username=sanjog";
                //Log.d("ss",link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(link);
                HttpResponse response = client.execute(request);
                String s= EntityUtils.toString(response.getEntity());
                ////Log.d("DDD","doInBackground");
                return s;
            }

            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.show();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            try
            {

                JSONObject jO=new JSONObject(result);
                getJSONValues(jO);

                if(counter==1)
                {
                    yourValues=getmStudentValues();
                }
                //Log.d("SSS",s(counter));
                if(counter==2 && ERROR==0)
                {
                    friendsValues=getmStudentValues();

                    Fragment fragment= ViewCompareResultFragment.newInstance(yourValues, friendsValues, roll1, roll2);
                    fm.beginTransaction().replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                }

            }
            catch(Exception e)
            {
                ERROR=1;

            }
            if(ERROR==1 && counter==2)
            {

                showError("Couldn't load result. Make sure you entered correct values and try again.");
                ERROR=0;
            }

        }

        private void getJSONValues(JSONObject resultSet) throws JSONException
        {

            if(level==11 && faculty.equals("sc"))
            {
                //Log.d("here","reached class 11 sc");
                int PHYSICS=resultSet.getInt("Physics");
                int CHEMISTRY=resultSet.getInt("Chemistry");
                int MATHS=resultSet.getInt("Math");
                int ENGLISH=resultSet.getInt("English");
                int BIOCOMP=resultSet.getInt("Bio_Comp");
                int NEPALI=0;
                String NAME=resultSet.getString("Name");
                double AVERAGE=resultSet.getDouble("Percentage");
                //Log.d("name",NAME);
                int TOTAL=resultSet.getInt("Total");
                String POSITION=resultSet.getString("Position");
                mStudentValues=new String[]{s(PHYSICS),s(CHEMISTRY),s(MATHS)
                        ,s(ENGLISH),s(BIOCOMP),NAME,s(AVERAGE),s(TOTAL),POSITION,s(NEPALI)};
            }
            if(level==12 && faculty.equals("sc"))
            {
                int PHYSICS=resultSet.getInt("Physics");
                int CHEMISTRY=resultSet.getInt("Chemistry");
                int MATHS=resultSet.getInt("Math");
                int ENGLISH=resultSet.getInt("English");
                int BIOCOMP=resultSet.getInt("Bio_Comp");
                int NEPALI=resultSet.getInt("Nepali");
                String NAME=resultSet.getString("Name");
                //Log.d("name",NAME);
                //Log.d("NEPALI",s(NEPALI));
                double AVERAGE=resultSet.getDouble("Percentage");
                int TOTAL=resultSet.getInt("Total");
                String POSITION=resultSet.getString("Position");
                mStudentValues=new String[]{s(PHYSICS),s(CHEMISTRY),s(MATHS)
                        ,s(ENGLISH),s(BIOCOMP),NAME,s(AVERAGE),s(TOTAL),POSITION,s(NEPALI)};
                for(int i=0;i<10;i++)
                {
                    //Log.d("MARK",mStudentValues[i]);
                }
            }
            else if(level==11 && faculty.equals("com"))
            {
                int ACCOUNTS=resultSet.getInt("Accounts");
                int ECONOMICS=resultSet.getInt("Economics");
                int BUSINESSCOMP=resultSet.getInt("Business_Comp");
                int ENGLISH=resultSet.getInt("English");
                int NEPALI=resultSet.getInt("Nepali");
                int BUSINESSMATHCOMP=0;
                String NAME=resultSet.getString("Name");
                double AVERAGE=resultSet.getDouble("Percentage");
                int TOTAL=resultSet.getInt("Total");
                String POSITION=resultSet.getString("Position");
                mStudentValues=new String[]{s(ACCOUNTS),s(ECONOMICS),s(BUSINESSCOMP)
                        ,s(ENGLISH),s(BUSINESSMATHCOMP),NAME,s(AVERAGE),s(TOTAL),POSITION,s(NEPALI)};

            }
            else if(level==12 && faculty.equals("com"))
            {
                int ACCOUNTS=resultSet.getInt("Accounts");
                int ECONOMICS=resultSet.getInt("Economics");
                int BUSINESSCOMP=resultSet.getInt("Business_Comp");
                int BUSINESSMATHCOMP=resultSet.getInt("Business_math_Marketing");
                int ENGLISH=resultSet.getInt("English");
                int NEPALI=resultSet.getInt("Nepali");
                String NAME=resultSet.getString("Name");
                double AVERAGE=resultSet.getDouble("Percentage");
                int TOTAL=resultSet.getInt("Total");
                String POSITION=resultSet.getString("Position");
                mStudentValues=new String[]{s(ACCOUNTS),s(ECONOMICS),s(BUSINESSCOMP)
                        ,s(ENGLISH),s(BUSINESSMATHCOMP),NAME,s(AVERAGE),s(TOTAL),POSITION,s(NEPALI)};
            }
        }
        private String s(int val)
        {
            return Integer.toString(val);
        }
        private String s(double val)
        {
            return Double.toString(val);
        }

        public String[] getmStudentValues()
        {
            return mStudentValues;
        }

    }

}
