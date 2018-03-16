package com.susankya.rnk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;


public class ResultFragment extends android.support.v4.app.Fragment {

   private EditText roll;
    private Spinner spinnerClass, spinnerExam;
    private Button button,objectiveResult;
    private int spinnerValue,rollNumber;
    private String selectedClass,selectedFaculty,link,exam="first",studentName;
    private String[]mStudentValues;
    public static final String ROLL_ARG="ROLL";
    public static final String CLASS_ARG="CLASS";
    public static final String FACULTY_ARG="FACULTY";
    @Override
    public void onCreate(Bundle SIS)
    {
        super.onCreate(SIS);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((NavDrawerActivity) getActivity()).onSectionAttached(3);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_result, container, false);

        button=(Button)v.findViewById(R.id.submit);
        Button other=(Button)v.findViewById(R.id.other_results_button);
        other.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getFragmentManager();
                        // Toast.makeText(getActivity(),Integer.toString(postNumForComment),Toast.LENGTH_LONG).show();
                        OtherResultsDialog dialog=new OtherResultsDialog();
                        dialog.show(fm,"SHOWING");
                    }
                }
        );
        LinearLayout ll=(LinearLayout)v.findViewById(R.id.ll);
        Animation animation= ((NavDrawerActivity)getActivity()).getAnimation();
        ll.setAnimation(animation);
        spinnerClass =(Spinner) v.findViewById(R.id.spinnerclass);

        roll=(EditText) v.findViewById(R.id.roll);
        button=(Button) v.findViewById(R.id.submit);
        spinnerExam =(Spinner) v.findViewById(R.id.spinnerterm);

        String examArray[]=getActivity().getResources().getStringArray(R.array.exam_list);
        String classArray[]=getActivity().getResources().getStringArray(R.array.class_spinner_array);
        CustomSpinnerAdapter classSpinnerAdapter=new CustomSpinnerAdapter(getActivity().getApplicationContext(), R.id.spinnerclass,
                classArray  );
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(getActivity().getApplicationContext(), R.id.spinnerexam,
          examArray  );
        spinnerExam.setAdapter(customSpinnerAdapter);
        spinnerClass.setAdapter(classSpinnerAdapter);



        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String checker=roll.getText().toString().trim();

                        if(checker.equals(""))
                        {
                            roll.setError("Enter roll number");
                            //Toast.makeText(getActivity().getApplicationContext(),"ENTER ROLL NUMBER",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            selectedClass= spinnerClass.getSelectedItem().toString();
                            selectedFaculty="sc";
                            String[] termText=spinnerExam.getSelectedItem().toString().toLowerCase().split(" ");
                            exam=termText[0];
                            link = FragmentCodes.DOMAIN+"resultGiver.php?roll="+checker+"&terminal="+exam+"&class="+selectedClass+"&faculty="+selectedFaculty;
                            loadResult();
                        }
                    }
                }
        );
        return v;
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

    private void loadResult()
    {
        new PhpConnect(link,"Loading...",getActivity(),1).setListener(
                new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {

                        try
                        {
                            getJSONValues(new JSONObject(res));
                        }
                        catch (Exception e)
                        {

                        }

                        if(!res.equals("0"))
                        {
                            Bundle b=new Bundle();
                            b.putString("result",res);
                            b.putString("name",studentName);
                            b.putInt("roll", Integer.parseInt(roll.getText().toString().trim()));
                            b.putInt("class", Integer.parseInt(selectedClass));
                            b.putString("exam",exam);
                            b.putStringArray("values",mStudentValues);
                            Fragment fragment=new ViewResultFragment();
                            fragment.setArguments(b);
                            getFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                        }
                        else
                            Toast.makeText(getActivity()
                                    , "Invalid roll number or the result is not published", Toast.LENGTH_LONG).show();


                    }
                }
        );
    }


    private void getJSONValues(JSONObject resultSet)
    {
        try
        {
            if(selectedClass.equals("11") && selectedFaculty.equals("sc"))
            {
                //Log.d("here", "reached class 11 sc");
                double PHYSICS=resultSet.getDouble("Physics");
                double CHEMISTRY=resultSet.getDouble("Chemistry");
                double MATHS=resultSet.getDouble("Math");
                double ENGLISH=resultSet.getDouble("English");
                double BIOCOMP=resultSet.getDouble("Bio_Comp");
                double NEPALI=0;
                String NAME=resultSet.getString("Name");
                studentName=NAME;
                double AVERAGE=resultSet.getDouble("Percentage");
                //Log.d("name", NAME);
                double TOTAL=resultSet.getDouble("Total");
                String POSITION=resultSet.getString("position");
                mStudentValues=new String[]{s(PHYSICS),s(CHEMISTRY),s(MATHS)
                        ,s(ENGLISH),s(BIOCOMP),s(NEPALI),s(TOTAL),s(AVERAGE),POSITION};
            }
            if(selectedClass.equals("12") && selectedFaculty.equals("sc"))
            {
                double PHYSICS=resultSet.getDouble("Physics");
                double CHEMISTRY=resultSet.getDouble("Chemistry");
                double MATHS=resultSet.getDouble("Math");
                double ENGLISH=resultSet.getDouble("English");
                double BIOCOMP=resultSet.getDouble("Bio_Comp");
                double NEPALI=resultSet.getDouble("Nepali");
                String NAME=resultSet.getString("Name");
                studentName=NAME;
                //Log.d("name", NAME);
                //Log.d("NEPALI", s(NEPALI));
                double AVERAGE=resultSet.getDouble("Percentage");
                double TOTAL=resultSet.getDouble("Total");
                String POSITION=resultSet.getString("position");
                mStudentValues=new String[]{s(PHYSICS),s(CHEMISTRY),s(MATHS)
                        ,s(ENGLISH),s(BIOCOMP),s(NEPALI),s(TOTAL),s(AVERAGE),POSITION};
                for(int i=0;i<10;i++)
                {
                    //Log.d("MARK", mStudentValues[i]);
                }
            }
            logit();

        }
        catch (Exception e)
        {
            //Log.d("here is error bro", e.toString());
        }


    }
    private String s(int i)
    {
        return Integer.toString(i);
    }
    private String s(double i)
    {
        return Double.toString(i);
    }

    private void logit()
    {

    }
}

