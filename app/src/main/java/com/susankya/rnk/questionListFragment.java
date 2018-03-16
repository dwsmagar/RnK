package com.susankya.rnk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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

public class questionListFragment extends android.support.v4.app.Fragment implements FragmentCodes {

    private ArrayList<String> list=new ArrayList<String>();
    private String[] string={"Notice","Notice 0","Notice 1","Notice 2","Notice 3","Notice 4","Notice 5",};
    public static final  String QUESTION_NUM="Q.no.";
    private ListView mQuestionList;
    public static String FILENAME="LiSTOfQuestionCode.txt";
   private static String changableFilename=null;
    public static String QUESTION_CODE="question_code";
    private ArrayList<question> mquestions;
    private String questionsCode;
    private adapt adapter;
    private Button save;
    private  ArrayList<Integer> answers;
    private JSONArray array;
    @Override
    public void onCreate(Bundle SIS)
    {
        super.onCreate(SIS);
        questionsCode=getArguments().getString("code");
        //Log.d("fromquestionlistfragment", questionsCode);
        array=new JSONArray();
        answers=new ArrayList<>();
        //getFragmentManager().popBackStack();
    }


    private void connectToPHP()
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new GetQuestions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,questionsCode);
            else
                new GetQuestions().execute(questionsCode);

        } else {
            Toast.makeText(getActivity().getApplicationContext().getApplicationContext(), "NO CONNECTION", Toast.LENGTH_SHORT).show();


        }

    }
    private void saveQuestions(){
        try {
            if( ListOfSavedResult(questionsCode)){
                changableFilename=questionsCode;
                save(array,changableFilename);
            }
            if(NavDrawerActivity.isIsFromHome())
                getFragmentManager().popBackStack();
            else
            {
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, FragmentForHome.newInstance(2, null)).addToBackStack(null).commit();
            }
        }
        catch (Exception e){
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.layout_question_list_page, container, false);
       final questionBank mQuestionBank= questionBank.get(getActivity().getApplicationContext());
        final SharedPreferences sp=getActivity().getSharedPreferences("Public", Context.MODE_PRIVATE);
        sp.edit().putBoolean(isTest,false).commit();
    mquestions=new ArrayList<question>();
        mQuestionList =(ListView) v.findViewById(R.id.list) ;
        save=(Button) v.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 saveQuestions();
            }
        });
        //connectToPHP();
        mquestions=loadAllQuestions(questionsCode);
        adapter=new adapt(mquestions);
        mQuestionList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //saveQuestions();
        if(!mquestions.isEmpty())
        {
            for(int i=0;i<mquestions.size();i++)
            {
                //Log.d("printing from arraylist",mquestions.get(i).getQuestion());
            }
        }
        else
        //Log.d("printing empty","empty");


        NavDrawerActivity.setSharedQuestions(mquestions);
       ActivityForFragment.setSharedQuestions(mquestions);
        mQuestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /*          for(int i=0;i<mquestions.size();i++){
                    answers.add(0);
                }
                ActivityForFragment.setAnswers(answers);*/
                question c = (question) parent.getAdapter().getItem(position);
                android.support.v4.app.Fragment fragment = null;
                if (fragment == null)
                    fragment = questionPagerFragment.newInstance(c.getQuestionNumber());
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment).addToBackStack(null)
                            .commit();
                }
            }
        });


        return v;
    }
    @Override
    public void onResume() {
     if(NavDrawerActivity.isIsFromDialog())
         ((NavDrawerActivity) getActivity()).onSectionAttached(3);
       super.onResume();
    }

    public boolean save(JSONArray ja,String mFilename)
    {
        Writer writer = null;
        try {
            OutputStream out = getActivity().getApplicationContext()
                    .openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(ja.toString());
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
                  //  Toast.makeText(getActivity(),"Error closing file",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public ArrayList<question> loadAllQuestions(String questionsCode)
    {
        SQLiteHelper sqLiteHelper=new SQLiteHelper(getActivity());
        SQLiteDatabase db=sqLiteHelper.getWritableDatabase();


        return sqLiteHelper.loadQuestions(questionsCode);
    }
    public boolean ListOfSavedResult(String questionsCode)
    {
        boolean saved=false;
        Writer writer = null;
        try {

            JSONArray JA=load(FILENAME);
            JSONObject job=new JSONObject();
                  job.put(QUESTION_CODE, questionsCode);
           for(int i=0;i<JA.length();i++){
                if(JA.getJSONObject(i).toString().equals(job.toString()))
                {
                    //Toast.makeText(getActivity(),"These Questions are already saved",Toast.LENGTH_SHORT).show();
                    saved=true;
                          }
                  }

            if(!saved)
            {
                JA.put(job);

                OutputStream out = getActivity().getApplicationContext()
                        .openFileOutput(FILENAME, Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
                writer.write(JA.toString());
                Toast.makeText(getActivity(),"Questions saved!",Toast.LENGTH_SHORT).show();
                return true;
            }
            else

            return false;
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
                    //Toast.makeText(getActivity(),"Error closing file",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public JSONArray load(String mFilename)// throws IOException, JSONException
            {
                JSONArray array=new JSONArray();
            String line=null;
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
          //  Toast.makeText(getActivity(), "could Not Find File", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                }
                catch (Exception e){
                    //Toast.makeText(getActivity(), "Error closing file", Toast.LENGTH_SHORT).show();
                }
        }
        return array;
    }

private class adapt extends ArrayAdapter<question> {
        public adapt(ArrayList<question> c){
            super(getActivity(),0,c);
        }
        @Override
        public View getView(int pos,View v,ViewGroup vg){
            if(v==null){
                v=getActivity().getLayoutInflater().inflate(R.layout.question_list_layout,null);
            }
            try {
                question c = getItem(pos);
                TextView q_num = (TextView) v.findViewById(R.id.q_num);
                q_num.setText(""+c.getQuestionNumber());
                TextView ques = (TextView) v.findViewById(R.id.question);
                ques.setText(") " + c.getQuestion().toString());
                String[] options = c.getOptions();
                TextView op1 = (TextView) v.findViewById(R.id.option1);
                op1.setText("a)" + options[0]);
                TextView op2 = (TextView) v.findViewById(R.id.option2);
                op2.setText("b)" + options[1]);
                TextView op3 = (TextView) v.findViewById(R.id.option3);
                op3.setText("c)" + options[2]);
                TextView op4 = (TextView) v.findViewById(R.id.option4);
                op4.setText("d)" + options[3]);
            }
            catch (Exception e){
              //  Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            }
            return v;
        }

    }

    private class GetQuestions extends AsyncTask<String, Void, String> {
            private ProgressDialog dialog = new ProgressDialog(getActivity());
            @Override
            protected String doInBackground(String... arg0) {


                try {
                    String escape=arg0[0];

                    String link = HOST+"getting_questions_table.php?code="+escape;
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(link);
                    HttpResponse response = client.execute(request);
                    String s = EntityUtils.toString(response.getEntity());
                    return s;
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }

            @Override
            protected void onPreExecute() {

                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected void onPostExecute(String result) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                try
                {

                    //Log.d("AA", result);
                     array = (JSONArray) new JSONTokener(result)
                            .nextValue();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jO = array.getJSONObject(i);
                        question n=new question();
                        String nothing=jO.getString("question_code");
                        String questionnum=Integer.toString(jO.getInt("question_number"));
                        n.setQuestion(jO.getString("question"));
                        n.setQuestionNumber(i+1);
                        n.setOption1(jO.getString("option1"));
                        n.setOption2(jO.getString("option2"));
                        n.setOption3(jO.getString("option3"));
                        n.setOption4(jO.getString("option4"));
                        n.setCorrect_answer(jO.getInt("correct_option"));
                        mquestions.add(n);
                    }
                    //Log.d("sss",mquestions.get(0).getQuestion());
                    adapter=new adapt(mquestions);
                    mQuestionList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
saveQuestions();
                }
                catch(Exception e)
                {
                    Toast.makeText(getActivity(),"Incorrect questions code",Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }

            }
    }
}
