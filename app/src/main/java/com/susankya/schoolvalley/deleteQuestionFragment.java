package com.susankya.schoolvalley;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class deleteQuestionFragment extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
private ListView mQuestionList;
    private String mParam1;
    private String questionsCode;
    private ArrayList<question> mQuestions;
private boolean[] index;
    private int indexForArray;
    private ProgressDialog dialog ;
    private Button check,checkall;
    private adapt adapter;
      private int numberOfQuestions=0,totalNumberOfSelectedQuestions=0,currentSelectedQuestion;
    private int[] selectedQuestionsPositionArray;
    public static deleteQuestionFragment newInstance(String param1) {
        deleteQuestionFragment fragment = new deleteQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    public deleteQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            questionsCode=mParam1;

        }
        indexForArray=0;
getActivity().setTitle("Delete Questions: " + questionsCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v=inflater.inflate(R.layout.activity_main, container, false);
        TextView emptyView=(TextView)v.findViewById(R.id.emptyView);
            mQuestions=new ArrayList<>();
    mQuestionList=(ListView)v.findViewById(R.id.list);
mQuestionList.setEmptyView(emptyView);
       // connectToPHP(0, "GET");
String link=NEW_HOST+"gettingquestionstable.php";
        new PhpConnect(link,"Loading...",getActivity(),1,new String[]{CMDXXX,Utilities.getDatabaseName(getActivity()),questionsCode},new String[]{"cmdxxx","dbName","code"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                try
                {
                    //Log.d("AA", res);

                    JSONArray array = (JSONArray) new JSONTokener(res)
                            .nextValue();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jO = array.getJSONObject(i);
                        question n=new question();
                        String nothing=jO.getString("question_code");
                        String questionnum= Integer.toString(jO.getInt("question_number"));
                        n.setQuestion(jO.getString("question"));
                        n.setQuestionNumber(jO.getInt("question_number"));
                        n.setOption1(jO.getString("option1"));
                        n.setOption2(jO.getString("option2"));
                        n.setOption3(jO.getString("option3"));
                        n.setOption4(jO.getString("option4"));
                        n.setCorrect_answer(jO.getInt("correct_option"));
                        mQuestions.add(n);
                    }
                    //Log.d("sss", mQuestions.get(0).getQuestion());
                    adapter=new adapt(mQuestions);
                    mQuestionList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if(mQuestions.size()!=0) {
                        index = new boolean[mQuestions.size()];
                        for(int i=0;i<mQuestions.size();i++)
                            index[i]=false;
                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(getActivity(), "No questions to load.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        });
        checkall=(Button)v.findViewById(R.id.check_all);
        checkall.setOnClickListener(
               new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(checkall.getText().toString().equals("Check All")){
                       for(int s=0;s<mQuestions.size();s++)
                       {
                           index[s]=true;

                       }
                      adapter.notifyDataSetChanged();
                       checkall.setText("Uncheck All");}
                       else {
                           for(int s=0;s<mQuestions.size();s++)
                           {
                               index[s]=false;
                           }
                           adapter.notifyDataSetChanged();
                           checkall.setText("Check All");
                       }
                   }
               }

        );
        check=(Button)v.findViewById(R.id.add_notice);
        check.setText("Delete selected");
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<mQuestions.size();i++)
                {
                    if(index[i])
                    {
                        totalNumberOfSelectedQuestions++;
                    }
                }
                selectedQuestionsPositionArray=new int[totalNumberOfSelectedQuestions];
                for(int i=0,s=0;i<mQuestions.size();i++)
                {
                    if(index[i])
                    {
                        selectedQuestionsPositionArray[s]=i;
                        s++;
                    }
                }
                //Log.d("numberOfQuestions", Integer.toString(totalNumberOfSelectedQuestions));
                deleteQuestion(selectedQuestionsPositionArray[indexForArray]+1);
                    }
        });
        return v;
    }
private void deleteQuestion(int i){
    String link=NEW_HOST+"deleting_questions_table.php";
    new PhpConnect(link,"Deleting...( "+i+" )",getActivity(),1,new String[]{CMDXXX,databaseName,questionsCode,i+""},new String[]{"cmdxxx","dbName","code","num"}).setListener(new PhpConnect.ConnectOnClickListener() {
        @Override
        public void onConnectListener(String res) {
            numberOfQuestions++;
            try {
                  if (res.trim().equals("1") && totalNumberOfSelectedQuestions != numberOfQuestions) {
                    indexForArray++;
                    deleteQuestion(selectedQuestionsPositionArray[indexForArray] + 1);

                }

                if (res.trim().equals("1") && totalNumberOfSelectedQuestions == numberOfQuestions) {
                    Toast.makeText(getActivity(), "Successfully deleted " + totalNumberOfSelectedQuestions + ((totalNumberOfSelectedQuestions == 1) ? " question." : " questions."), Toast.LENGTH_SHORT).show();
                    numberOfQuestions = 0;
                    totalNumberOfSelectedQuestions = 0;
                    getActivity().finish();
                }
            } catch (Exception e) {
                //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }



        }
    });
}

    private class adapt extends ArrayAdapter<question> {
        private  int x=0;
        public adapt(ArrayList<question> c){
            super(getActivity(),0,c);
        }
        @Override
        public View getView(int pos,View v,ViewGroup vg){
            final ViewHolder holder;
            if(v==null){
                holder=new ViewHolder();
                v=getActivity().getLayoutInflater().inflate(R.layout.fragment_delete_question_list,null);
                holder.ques=(TextView)v.findViewById(R.id.question);
                holder.op1=(TextView)v.findViewById(R.id.option1);
                holder.op2=(TextView)v.findViewById(R.id.option2);
                holder.op3=(TextView)v.findViewById(R.id.option3);
                holder.op4=(TextView)v.findViewById(R.id.option4);
                holder.checkBox=(CheckBox)v.findViewById(R.id.checkbox);
                v.setTag(holder);
            }
            else {

                holder = (ViewHolder) v.getTag();
            }
            holder.ref=pos;
            final question c=getItem(pos);
            final    String[] options=c.getOptions();
            holder.ques.setText(c.getQuestion());
            holder.op1.setText("a)"+options[0]);
            holder.op2.setText("b)"+options[1]);
            holder.op3.setText("c)"+options[2]);
            holder.op4.setText("d)"+options[3]);
            holder.checkBox.setChecked(index[holder.ref]);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        index[holder.ref]=true;
                    else
                        index[holder.ref]=false;
                }
            });
            return v;
        }

        private class ViewHolder {
            TextView ques,op1,op2,op3,op4;
            CheckBox checkBox;
            int ref;
        }
    }

}
