package com.susankya.rnk;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class showResultListOffline extends android.support.v4.app.Fragment {
    public static String changableFilename=null;
private TextView emptyView;
    private TextView nameTV;
    private Button bArray[];
    private int drawableArray[];
    private Spinner spinner;
    private int roll,level;
    private String faculty,link,studentName;
    private String sRoll,sLevel,exam;
    private String studentInfo[],mStudentValues[];
    private int previousPositionOfSpinner=5,currentPositionOfSpinner=0; //5 because if we put 0 then condition on line 100 will not work
    JSONObject job;
    private boolean isPassed=true;
    ExamValueAssigner examValueAssigner;
    private Marks reportCard=new Marks();
    private ArrayList<Marks> marksList;
    private GridView resultGrid;
    private SubjectMark markArrayAdapter;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//private ListView mListView;
private String jsonString;
    private ViewGroup mContainerView;
    private String mParam1;
    private String mParam2;
    private LinearLayout rl;
    private JSONObject listDetail=new JSONObject();
    public static   final  String NAME="name";
    public static   final  String TERMINAL="terminal";
    public static   final  String FACULTY="faculty";
    public static   final  String CLASS="class";
    public static   final  String ROLL="roll";
    private int ListLayout=0;
    private   TextView resultTextView_subject,resultTextView_marks;
    private ImageView statusImage;
    private static View activityView;
private JSONArray array;
    private ArrayList<resultListDetail> mList;
    public static showResultListOffline newInstance(String param1, String param2) {
        showResultListOffline fragment = new showResultListOffline();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
        public showResultListOffline() {
        mParam1 = "a";
        mParam2 = "b";
    }

        @Override
        public void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
            try {
                if (getArguments() != null) {
                    mParam1 = getArguments().getString(ARG_PARAM1);
                    mParam2 = getArguments().getString(ARG_PARAM2);
                }
               mList = new ArrayList<resultListDetail>();
                marksList=new ArrayList<Marks>();
                setHasOptionsMenu(true);
            }
        catch (Exception e){
      // Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        }

    private void ArrayMaker() {
        try {
            if ((jsonString = loadString(ViewResultFragment.FILENAME)) != null) {
                array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            }

            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject job = array.getJSONObject(i);//list of result
                    toMlist(job);
                    NavDrawerActivity.setViewListValue(i, null);
                } catch (Exception e) {
                 //   Toast.makeText(getActivity(), "ERROR " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch(Exception e){
           // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.result_add_button,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                ((NavDrawerActivity) getActivity()).selectItem(4);
                  break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.delete_file_context, menu);
    }
    private void setAdapter(ArrayList<resultListDetail> tp){
        try {
                       for (int pos = 0; pos <tp.size(); pos++) {
                           final int index=pos;
                final ViewGroup v = (ViewGroup) LayoutInflater.from(getActivity()).inflate(
               R.layout.offline_result_list_item, mContainerView, false);
               ImageView delete=(ImageView)v.findViewById(R.id.delete_button);
             //   ImageView showResult=(ImageView)v.findViewById(R.id.image);
                resultListDetail d = tp.get(pos);
                resultTextView_subject = (TextView) v.findViewById(R.id.subject);
                resultTextView_marks = (TextView) v.findViewById(R.id.marks);
                statusImage = (ImageView) v.findViewById(R.id.result_icon);
                rl = (LinearLayout) v.findViewById(R.id.rl);
                TextView t1 = (TextView) v.findViewById(R.id.name_s);
                TextView t2 = (TextView) v.findViewById(R.id.classes);
                 TextView t4 = (TextView) v.findViewById(R.id.terminal_s);
                LinearLayout ll = (LinearLayout) v.findViewById(R.id.shouldClick);
                t1.setText(d.getName());
                t2.setText(d.getCLASS());
                t4.setText(d.getTerminal());
                resultGrid = (GridView) v.findViewById(R.id.gridview);
                final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom);
                final LinearLayout result = (LinearLayout) v.findViewById(R.id.displayResult);
                // result.setVisibility(View.GONE);
              View.OnClickListener listener=new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          if (result.isShown())
                              result.setVisibility(View.GONE);
                          else {
                              if (resultGrid != null) {
                                  result.setVisibility(View.VISIBLE);
                                  result.setAnimation(animation);
                              }
                          }
                      }
                  };
                ll.setOnClickListener(listener);
                   //showResult.setOnClickListener(listener);

                try
                {
                    job = array.getJSONObject(pos);
                    exam = job.getString(showResultListOffline.TERMINAL);
                    level = Integer.parseInt(job.getString(showResultListOffline.CLASS));
                    faculty = job.getString(showResultListOffline.FACULTY);
                    roll = job.getInt(showResultListOffline.ROLL);
                  final   String filename=job.getString(showResultListOffline.NAME) + exam + faculty + Integer.toString(level) + Integer.toString(roll);

                   delete.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View viewssw) {
                           AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                           builder1.setTitle("Delete");
                           builder1.setMessage("Are you sure you want to delete this file?");
                           builder1.setCancelable(true);
                           builder1.setPositiveButton("Yes",
                                   new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int id) {
                                           if (ListOfSavedResult(job,index)) {
                                               getActivity().getApplicationContext().deleteFile(filename);
                                           }
                                           mContainerView.removeView(v);
                                         }
                                   });   builder1.setNegativeButton("No",
                                   new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int id) {
                                           return;
                                       }
                                   });
                           AlertDialog alert11 = builder1.create();
                           alert11.show();
                           if(mContainerView.getChildCount()==0){
                               emptyView.setVisibility(View.VISIBLE);
                           }
                           else
                               emptyView.setVisibility(View.INVISIBLE);
                          }
                   });
                    getJSONValues(load(filename));
                    // wait();
                    marksList = new ArrayList<Marks>();
                    String subjectTitle[] = null;
                    if (faculty.equals("sc")) {
                        subjectTitle = new String[]
                                {
                                        "Physics", "Chemistry", "Maths",
                                        "English", "Bio/Comp", "Nepali", "Total",
                                        "Average"
                                };
                    }
                    if (faculty.equals("com")) {
                        subjectTitle = new String[]
                                {
                                        "Accounts", "Economics", "Business/Comp",
                                        "English", "BMat/Makt", "Nepali", "Total",
                                        "Average"
                                };
                    }
                    for (int i = 0; i < subjectTitle.length; i++) {
                        Marks m = new Marks();
                        m.setSubject(subjectTitle[i]);
                        m.setMarks(mStudentValues[i]);
                        marksList.add(m);
                    }
                    markArrayAdapter = new SubjectMark(getActivity().getApplicationContext(), marksList);
                    resultGrid.setAdapter(markArrayAdapter);
                    manageResultItems();
                    resultTextView_subject.setText("RESULT");
                    resultTextView_marks.setText(mStudentValues[8]);
                  /*  if (isPassed) {
                        rl.setBackgroundColor(getResources().getColor(R.color.passed));
                        statusImage.setImageResource(R.drawable.tick);
                    } else {
                        rl.setBackgroundColor(getResources().getColor(R.color.failed));
                        statusImage.setImageResource(R.drawable.cross);
                    }*/

                    // notifyAll();
                     } catch (Exception e) {
                    //Toast.makeText(getActivity(), "here "+e.toString(), Toast.LENGTH_SHORT).show();
                }
                mContainerView.addView(v, 0);
            }
        }catch (Exception e){
           // Toast.makeText(getActivity(),"error in addview"+e.toString(),Toast.LENGTH_SHORT).show();
        }
        if(mContainerView.getChildCount()==0){
            emptyView.setVisibility(View.VISIBLE);
        }
        else
            emptyView.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show_result_list_offline, container, false);
        this.setMenuVisibility(true);
          TextView tv=(TextView)v.findViewById(R.id.emptyView);
        emptyView=(TextView)v.findViewById(R.id.emptyView);
        mContainerView = (ViewGroup) v.findViewById(R.id.lists);
        mList.clear();
        ArrayMaker();
        try {
             setAdapter(mList);
        } catch (Exception e) {
          //  Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    public void toMlist(JSONObject job) {
        resultListDetail detail = new resultListDetail();
        try {
            detail.setCLASS(job.getString(CLASS));
            detail.setFaculty(job.getString(FACULTY));
            detail.setTerminal(job.getString(TERMINAL));
            detail.setName(job.getString(NAME));
            detail.setRoll(job.getInt(ROLL));
            mList.add(detail);
             } catch (Exception e) {
           // Toast.makeText(getActivity(), "ERROR" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public String loadString(String mFilename)// throws IOException, JSONException
    {
        String line = null;
        BufferedReader reader = null;
        try {
            InputStream in = getActivity().getApplicationContext().openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            line = jsonString.toString();


        } catch (Exception e) {
      //    Toast.makeText(getActivity(), "could Not Find File", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                }
                catch (Exception e) {
                   // Toast.makeText(getActivity(), "Error closing file", Toast.LENGTH_SHORT).show();
                }
        }
        return line;
    }

    public boolean ListOfSavedResult(JSONObject job,int pos) {
        Writer writer = null;
        try {
            JSONArray JA = (JSONArray) new JSONTokener(loadString(ViewResultFragment.FILENAME)).nextValue();
            JSONArray temp = new JSONArray();
            for (int i = 0; i < JA.length(); i++) {
             //  if (!JA.getJSONObject(i).toString().equals(job.toString()))
             if(pos!=i){
                   temp.put(JA.getJSONObject(i));
                       }
            }
            OutputStream out = getActivity().getApplicationContext()
                    .openFileOutput(ViewResultFragment.FILENAME, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(temp.toString());
            Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
            if(temp.length()==JA.length())
                return false;
            else
                return true;
        } catch (Exception e) {
          //  Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            return false;
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (Exception e) {
                  //  Toast.makeText(getActivity(), "Error closing file", Toast.LENGTH_SHORT).show();
                }
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

    private void getJSONValues(JSONObject resultSet) throws JSONException
    {
//Toast.makeText(getActivity(),resultSet.toString(),Toast.LENGTH_SHORT).show();
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
            studentName=NAME;
            double AVERAGE=resultSet.getDouble("Percentage");
            //Log.d("name",NAME);
            int TOTAL=resultSet.getInt("Total");
            String POSITION=resultSet.getString("position");
            mStudentValues=new String[]{s(PHYSICS),s(CHEMISTRY),s(MATHS)
                    ,s(ENGLISH),s(BIOCOMP),s(NEPALI),s(TOTAL),s(AVERAGE),POSITION};
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
            studentName=NAME;
            //Log.d("name",NAME);
            //Log.d("NEPALI",s(NEPALI));
            double AVERAGE=resultSet.getDouble("Percentage");
            int TOTAL=resultSet.getInt("Total");
            String POSITION=resultSet.getString("position");
            mStudentValues=new String[]{s(PHYSICS),s(CHEMISTRY),s(MATHS)
                    ,s(ENGLISH),s(BIOCOMP),s(NEPALI),s(TOTAL),s(AVERAGE),POSITION};
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
            studentName=NAME;
            double AVERAGE=resultSet.getDouble("Percentage");
            int TOTAL=resultSet.getInt("Total");
            String POSITION=resultSet.getString("position");
            mStudentValues=new String[]{s(ACCOUNTS),s(ECONOMICS),s(BUSINESSCOMP)
                    ,s(ENGLISH),s(BUSINESSMATHCOMP),s(NEPALI),s(TOTAL),s(AVERAGE),POSITION};

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
            studentName=NAME;
            double AVERAGE=resultSet.getDouble("Percentage");
            int TOTAL=resultSet.getInt("Total");
            String POSITION=resultSet.getString("position");
            mStudentValues=new String[]{s(ACCOUNTS),s(ECONOMICS),s(BUSINESSCOMP)
                    ,s(ENGLISH),s(BUSINESSMATHCOMP),s(NEPALI),s(TOTAL),s(AVERAGE),POSITION};
        }
    }
    private class SubjectMark extends BaseAdapter {
        private Context mContext;
        private ArrayList<Marks> marksList;
        public SubjectMark(Context c,ArrayList<Marks> marksList) {
            mContext = c;
            this.marksList=marksList;
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

            /*    if(c.isPassed("R"))
                {
                    ((RelativeLayout)v).setBackgroundColor(getResources().getColor(R.color.passed));
                    tick.setImageResource(R.drawable.tick);
                }
                else
                {
                    ((RelativeLayout)v).setBackgroundColor(getResources().getColor(R.color.failed));
                    tick.setImageResource(R.drawable.cross);
                }
                //resultGrid.setColumnWidth(RelativeLayout.LayoutParams.MATCH_PARENT);*/
            }
            else
            {
             /*   if(c.isPassed())
                {
                    ((RelativeLayout)v).setBackgroundColor(getResources().getColor(R.color.passed));
                    tick.setImageResource(R.drawable.tick);
                }

                else
                {
                    ((RelativeLayout)v).setBackgroundColor(getResources().getColor(R.color.failed));
                    tick.setImageResource(R.drawable.cross);
                }


*/
                tv0.setText(c.getSubject());
                tv1.setText((c.getMarks()));
                if(position==5 && level==11)
                {
                  //  ((RelativeLayout)v).setBackgroundColor(getResources().getColor(R.color.passed));
                    tv1.setText("--");
                    tick.setImageResource(android.R.color.transparent);
                }
            }

            // if it's not recycled, initialize some attributes
            return v;

        }

    }
    public JSONObject load(String mFilename)// throws IOException, JSONException
    {
        String line=null;
        JSONObject obj=new JSONObject();
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
            line =jsonString.toString();
            //Parse theJSON using JSONTokener
            obj = (JSONObject) new JSONTokener(jsonString.toString()).nextValue();

        }
        catch (Exception e) {

           // Toast.makeText(getActivity(), "could Not Find File", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                }
                catch (Exception e){
                    //Toast.makeText(getActivity(), "Error closing file", Toast.LENGTH_SHORT).show();
                }
        }
        return obj;
    }
    private String s(int val)
    {
        return Integer.toString(val);
    }
    private String s(double val)
    {
        return Double.toString(val);
    }
    public class resultListDetail {
        private String terminal;
        private String CLASS;

        public int getRoll() {
            return roll;
        }

        public void setRoll(int roll) {
            this.roll = roll;
        }

        private int roll;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;

        public String getFaculty() {
            return faculty;
        }

        public void setFaculty(String faculty) {
            this.faculty = faculty;
        }

        private String faculty;

        public String getTerminal() {
            return terminal;
        }

        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }

        public String getCLASS() {
            return CLASS;
        }

        public void setCLASS(String CLASS) {
            this.CLASS = CLASS;
        }
    }
}