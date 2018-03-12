package com.susankya.schoolvalley;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;


public class showQuestionOffline extends android.support.v4.app.Fragment {
      private static final String ARG_PARAM1 = "param1";
     private String mParam1;
private ArrayList<question> mQuestions;
private JSONArray array;
    private ListView listView;
    private ArrayList<Integer> answers;
    public static showQuestionOffline newInstance(String param1) {
        showQuestionOffline fragment = new showQuestionOffline();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
          fragment.setArguments(args);
        return fragment;
    }

    public showQuestionOffline() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
      //  getActivity().setTitle("");
        mQuestions=new ArrayList();
        mQuestions=new SQLiteHelper(getActivity()).loadQuestions(mParam1);
        /*try {
            array = (JSONArray) new JSONTokener(mParam1).nextValue();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jO = array.getJSONObject(i);
                question n=new question();
                String nothing=jO.getString("question_code");
                String questionnum=Integer.toString(jO.getInt("question_number"));
                n.setQuestion(jO.getString("question"));
                n.setQuestionNumber(jO.getInt("question_number"));
                n.setOption1(jO.getString("option1"));
                n.setOption2(jO.getString("option2"));
                n.setOption3(jO.getString("option3"));
                n.setOption4(jO.getString("option4"));
                n.setCorrect_answer(jO.getInt("correct_option"));
                mQuestions.add(n);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show_question_offline, container, false);
     listView=(ListView)v.findViewById(R.id.list);
   adapt adapter =new adapt(mQuestions);
      listView.setAdapter(adapter);
        ArrayList<question> newList=new ArrayList();
        answers=new ArrayList();
        for(int i=0;i<mQuestions.size();i++){
            question q;
            q=mQuestions.get(i);
            answers.add(i,0);
            q.setQuestionNumber(i+1);
            newList.add(q);

        }
        if(ActivityForFragment.getAnswers().size()==0)
        ActivityForFragment.setAnswers(answers);
        ActivityForFragment.setSharedQuestions(newList);
         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                question c = (question) parent.getAdapter().getItem(position);
                android.support.v4.app.Fragment fragment=null;
                if(fragment==null)
                  fragment= questionPagerFragment.newInstance(c.getQuestionNumber());
                if(fragment!=null)
                {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment).addToBackStack(null)
                            .commit();
                }
            }
        });
    return v;
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
