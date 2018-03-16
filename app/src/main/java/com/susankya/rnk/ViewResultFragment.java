
package com.susankya.rnk;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


/**
 * Created by ajay on 8/9/2015.
 */
public class ViewResultFragment extends Fragment implements FragmentCodes {
    public static final String FILENAME="Listofsavedresult.txt";
    public static String changableFilename=null;

    private TextView nameTV,terminal;
    private Button bArray[];
    private int drawableArray[];

    private int roll,level,FIRSTTIMEFLAG=0;
    private String faculty,link,studentName;
    private String sRoll,sLevel,exam;
    private String studentInfo[],mStudentValues[];
    public static   final  String NAME="name";
    public static   final  String TERMINAL="terminal";
    public static   final  String FACULTY="faculty";
    public static   final  String CLASS="class";
    public static   final  String ROLL="roll";
    private LinearLayout rl;
    boolean isPassed=true;
    private String result;
  private   TextView resultTextView_subject,resultTextView_marks;
    private ImageView statusImage;
    private int previousPositionOfSpinner=5,currentPositionOfSpinner=0; //5 because if we put 0 then condition on line 100 will not work
    JSONObject jO;
    ExamValueAssigner examValueAssigner;
    private Marks reportCard=new Marks();
    private ArrayList<Marks> marksList=new ArrayList<Marks>();
    private GridView resultGrid;
    private SubjectMark markArrayAdapter;


    @Override
    public void onDestroyOptionsMenu() {
  //      Toast.makeText(getActivity(),"CALLED",Toast.LENGTH_LONG).show();
        this.setMenuVisibility(false);
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onDestroyView() {
        onDestroyOptionsMenu();
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        onDestroyView();
        super.onPause();
    }

    public void onCreate(Bundle SIS)
    {
        super.onCreate(SIS);
        try {
        setHasOptionsMenu(true);
        mStudentValues=getArguments().getStringArray("values");
        studentName=getArguments().getString("name");
        roll=getArguments().getInt("roll");
        level=getArguments().getInt("class");
        faculty="sc";
        exam=getArguments().getString("exam");
             result=getArguments().getString("result",null);
            jO=new JSONObject(result);
        } catch (Exception e) {
           //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }



    }
    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((NavDrawerActivity) getActivity()).onSectionAttached(3);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_save:

                try {
                       if( ListOfSavedResult(studentName,exam,faculty,sLevel,roll)){
                        changableFilename=studentName+exam+faculty+sLevel+Integer.toString(roll);
                        save(jO, changableFilename);
                    }
                }
                catch (Exception e){
                   //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class SubjectMark extends BaseAdapter{
        private Context mContext;

        public SubjectMark(Context c) {
            mContext = c;
        }

        public int getCount() {
            return 8;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v;
            ImageView tick;
            RelativeLayout relativeLayout;
            TextView tv0,tv1;

                v=inflater.inflate(R.layout.result_listview,null);
            tv0=(TextView)v.findViewById(R.id.subject);
            tick=(ImageView)v.findViewById(R.id.result_icon);
            tv1=(TextView)v.findViewById(R.id.marks);
            Marks c=marksList.get(position);

           if(position==8)
            {
                tv0.setText(c.getSubject());
                tv1.setText((c.getMarks()));

               /* if(c.isPassed("R"))
                {
                    ((RelativeLayout)v).setBackgroundColor(getResources().getColor(R.color.passed));
                    tick.setImageResource(R.drawable.tick);
                }
                else
                {
                    ((RelativeLayout)v).setBackgroundColor(getResources().getColor(R.color.failed));
                    tick.setImageResource(R.drawable.cross);
                }*/
              //  resultGrid.setColumnWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            else
            {
               /* if(c.isPassed())
                {
                    ((RelativeLayout)v).setBackgroundColor(getResources().getColor(R.color.passed));
                    tick.setImageResource(R.drawable.tick);
                }

                else
                {
                    ((RelativeLayout)v).setBackgroundColor(getResources().getColor(R.color.failed));
                    tick.setImageResource(R.drawable.cross);
                }*/



                tv0.setText(c.getSubject());
                tv1.setText((c.getMarks()));
                if(position==5 && level==11)
                {
                   //((RelativeLayout)v).setBackgroundColor(getResources().getColor(R.color.passed));
                    tv1.setText("--");
                    tick.setImageResource(android.R.color.transparent);
                }
            }

                // if it's not recycled, initialize some attributes



            return v;

        }

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);

    }
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle SIS)
    {
        View v=inflater.inflate(R.layout.view_result_fragment,container,false);
        resultGrid=(GridView)v.findViewById(R.id.gridview);
        resultTextView_subject=(TextView)v.findViewById(R.id.subject);
        resultTextView_marks=(TextView)v.findViewById(R.id.marks);
        statusImage=(ImageView)v.findViewById(R.id.result_icon);
        rl=(LinearLayout)v.findViewById(R.id.rl);
        terminal=(TextView)v.findViewById(R.id.terminalTV);
        sRoll=Integer.toString(roll);
        sLevel=Integer.toString(level);


        nameTV=(TextView) v.findViewById(R.id.name);

        examValueAssigner=new ExamValueAssigner(roll,level,exam,faculty);
        examValueAssigner.setmClass(level);
        examValueAssigner.setmExam(exam);
        examValueAssigner.setmFaculty(faculty);
        drawableArray=examValueAssigner.getmDrawableId();
        setResult();
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

    private String s(int val)
    {
        return Integer.toString(val);
    }
    private String s(double val)
    {
        return Double.toString(val);
    }
    private void setResult()
    {

        try
        {
            nameTV.setText(studentName+" (Roll no.: "+" "+roll+")");
            String firstLetter=exam.substring(0,1).toUpperCase();
            String whole=firstLetter+exam.substring(1);
            terminal.setText(whole+" Terminal Exam");
            marksList=new ArrayList<Marks>();
            String subjectTitle[]=examValueAssigner.getSubjectsList();
            for(int i=0;i<subjectTitle.length;i++)
            {
                Marks m=new Marks();

                m.setSubject(subjectTitle[i]);
                m.setMarks(mStudentValues[i]);
                //Log.d("from Marks m",mStudentValues[i]);
                marksList.add(m);

            }

            resultTextView_subject.setText("RESULT");
            //Log.d("ERROR YAA LUKEKO RAEXA","1");
            resultTextView_marks.setText(mStudentValues[8]);
            //Log.d("ERROR YAA LUKEKO RAEXA","2");
     /*       if(isPassed)
            {
              //  rl.setBackgroundColor(getResources().getColor(R.color.passed));
                statusImage.setImageResource(R.drawable.tick);
                //Log.d("ERROR YAA LUKEKO RAEXA","3");
            }
            else
            {
              //  rl.setBackgroundColor(getResources().getColor(R.color.failed));
                statusImage.setImageResource(R.drawable.cross);
            }*/
            //Log.d("ERROR YAA LUKEKO RAEXA","5");
            markArrayAdapter=new SubjectMark(getActivity());
            //Log.d("ERROR YAA LUKEKO RAEXA","6");
            resultGrid.setAdapter(markArrayAdapter);
            //Log.d("ERROR YAA LUKEKO RAEXA","7");
            manageResultItems();
        }
        catch (Exception e)
        {
            //Log.d("ERROR YAA LUKEKO RAEXA",e.toString());
        }


    }
    private void manageResultItems()
    {
        if(level==11 && faculty.equals("sc"))
        {

        }
        if(level==11 && faculty.equals("com"))
          ;

    }

    public boolean save(JSONObject job,String mFilename)
    {
        Writer writer = null;
        try {
            OutputStream out = getActivity().getApplicationContext()
                    .openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(job.toString());
      //     Toast.makeText(getActivity(),"Successfully saved result",Toast.LENGTH_SHORT).show();
            return true;
        }
        catch(Exception e ){
            //Log.d("error",e.toString());
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            return false;
        }
        finally {
            if (writer != null)
                try {
                    writer.close();
                }
                catch (Exception e){
                    //Log.d("error1",e.toString());
                    //Toast.makeText(getActivity(),"Error closing file",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public boolean ListOfSavedResult(String s1,String s2,String s3,String s4,int roll)
    {
        Writer writer = null;
        try {
            JSONArray JA=load(FILENAME);
            JSONObject job=new JSONObject();
            job.put(NAME,s1);
            job.put(TERMINAL,s2);
            job.put(FACULTY,s3);
            job.put(CLASS,s4);
            job.put(ROLL,roll);
            for(int i=0;i<JA.length();i++){
                if(JA.getJSONObject(i).toString().equals(job.toString()))
                {
                    Toast.makeText(getActivity(),"This result is already saved",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            JA.put(job);

            OutputStream out = getActivity().getApplicationContext()
                    .openFileOutput(FILENAME, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(JA.toString());
            Toast.makeText(getActivity(),"Result Saved. To see it navigate through Home",Toast.LENGTH_SHORT).show();
            return true;
        }
        catch(Exception e ){
            //Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            return false;
        }
        finally {
            if (writer != null)
                try {
                    writer.close();
                }
                catch (Exception e){
                //    Toast.makeText(getActivity(),"Error closing file",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public JSONArray load(String mFilename)// throws IOException, JSONException
    {
        String line=null;
        JSONArray array=new JSONArray();
        ArrayList<String> crimes = new ArrayList<String>();
        String[] str=new String[4];
        BufferedReader reader = null;
        try {
            // Open and read the file into a StringBuilder
            InputStream in = getActivity().getApplicationContext().openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

        }
        catch (Exception e) {
           // Toast.makeText(getActivity(), "could Not Find File", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                }
                catch (Exception e){
                   // Toast.makeText(getActivity(), "Error closing file", Toast.LENGTH_SHORT).show();
                }
        }
        return array;
    }
}
