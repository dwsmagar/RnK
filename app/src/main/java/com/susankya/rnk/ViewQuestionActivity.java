package com.susankya.rnk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewQuestionActivity extends AppCompatActivity {
    JSONArray array;
    ListView listView;
    ArrayList<question> mQuestions;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);
        toolbar=(Toolbar) findViewById(R.id.toolbars);
setSupportActionBar(toolbar);
        listView=(ListView)findViewById(R.id.list);
   if(getIntent()!=null) {
       setTitle(getIntent().getStringExtra(AddDeleteQuestionsFragment.SECTION_NAME));
            mQuestions=new ArrayList<>();
            String data=getIntent().getStringExtra(AddDeleteQuestionsFragment.RESPONSEFROMNET);
            try {
                array = (JSONArray) new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jO = array.getJSONObject(i);
                    question n=new question();
                    String nothing=jO.getString("questionCode");
                    String questionnum=Integer.toString(jO.getInt("questionNumber"));
                    n.setQuestion(jO.getString("question"));
                    n.setQuestionNumber(jO.getInt("questionNumber"));
                    n.setOption1(jO.getString("option1"));
                    n.setOption2(jO.getString("option2"));
                    n.setOption3(jO.getString("option3"));
                    n.setOption4(jO.getString("option4"));
                    n.setCorrect_answer(jO.getInt("correctOption"));
                    mQuestions.add(n);
                }
                adapt adapter=new adapt(mQuestions);
                //Log.d("forquestion", "onCreate: "+mQuestions.size());
listView.setAdapter(adapter);
            } catch (Exception e) {
                //Log.d("forquestion", "onCreate: "+e.toString());

                e.printStackTrace();
            }
        }

    }
    private class adapt extends ArrayAdapter<question> {
        public adapt(ArrayList<question> c){
            super(getApplicationContext(),0,c);
        }
        @Override
        public View getView(int pos, View v, ViewGroup vg){
            if(v==null){
                v=getLayoutInflater().inflate(R.layout.question_list_layout,null);
            }
            question c=getItem(pos);
            TextView q_num=(TextView)v.findViewById(R.id.q_num);
            q_num.setText(""+(pos+1));
            TextView ques=(TextView)v.findViewById(R.id.question);
            ques.setText(") "+c.getQuestion().toString());
            String[] options=c.getOptions();
            TextView  op1=(TextView)v.findViewById(R.id.option1);
            op1.setText("a)"+options[0]);
            TextView  op2=(TextView)v.findViewById(R.id.option2);
            op2.setText("b)"+options[1]);
            TextView  op3=(TextView)v.findViewById(R.id.option3);
            op3.setText("c)"+options[2]);
            TextView  op4=(TextView)v.findViewById(R.id.option4);
            op4.setText("d)"+options[3]);
            return v;
        }

    }

}
