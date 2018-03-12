package com.susankya.schoolvalley;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class AddQuestionFragment extends android.support.v4.app.Fragment implements FragmentCodes {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int mParam1;
    private String mParam2, questionCode;
    private question q;
    private String blockCharacterSet = "056789";
    protected int I=0,isItLastQuestion;

  private static EditText questionet,option1et,option2et,option3et,option4et;
    private static Spinner correct_answer;
    public static ArrayList<question> mQuestions;
    private String[] options=new String[]{"1","2","3","4"};
   private String ques,opt1,opt2,opt3,opt4,correct;
    private String quesStore[][];
    public static AddQuestionFragment newInstance(int param1, String param2) {
        AddQuestionFragment fragment = new AddQuestionFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getArguments() != null) {
                mParam1 = getArguments().getInt(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
            mQuestions = AddQuestionPagerFragment.getmQuestions();
            q=mQuestions.get(mParam1);
           } catch (Exception e){
            //Toast.makeText(getActivity(),"Error in onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
        }
        questionCode=mParam2;
       }

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v;
        v=inflater.inflate(R.layout.question_list,null);
        correct_answer=(Spinner)v.findViewById(R.id.options);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,options);
        correct_answer.setAdapter(adapter);
        TextView questionNumber=(TextView)v.findViewById(R.id.questionNumber);
        questionNumber.setText("Question #"+(mParam1+1));
        questionet=(EditText)v.findViewById(R.id.question);
        option1et=(EditText)v.findViewById(R.id.option1);
        option2et=(EditText) v.findViewById(R.id.option2);
        option3et=(EditText)v.findViewById(R.id.option3);
        option4et=(EditText)v.findViewById(R.id.option4);
        questionet.setText(q.getQuestion());
        option1et.setText(q.getOption1());
        option2et.setText(q.getOption2());
        option3et.setText(q.getOption3());
        option4et.setText(q.getOption4());
        if(q.getCorrect_answer()!=0){
          for(int i=0;i<options.length;i++){
              if(options[i].equals(q.getCorrect_answer()+"")){
                  correct_answer.setSelection(i);
              }
          }
        }

        questionet.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                mQuestions.get(mParam1).setQuestion(s.toString());
                AddQuestionPagerFragment.setmQuestions(mQuestions);
            }
        });
       option1et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                mQuestions.get(mParam1).setOption1(s.toString());
                AddQuestionPagerFragment.setmQuestions(mQuestions);
            }
        });
        option2et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                mQuestions.get(mParam1).setOption2(s.toString());

                AddQuestionPagerFragment.setmQuestions(mQuestions);}
        });
        option3et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mQuestions.get(mParam1).setOption3(s.toString());
                AddQuestionPagerFragment.setmQuestions(mQuestions);
            }
        });
       option4et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mQuestions.get(mParam1).setOption4(s.toString());
                AddQuestionPagerFragment.setmQuestions(mQuestions);
            }
        });
        correct_answer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mQuestions.get(mParam1).setCorrect_answer(Integer.parseInt(correct_answer.getSelectedItem().toString()));
                AddQuestionPagerFragment.setmQuestions(mQuestions);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static question getQuestion(){
    question q=new question();
    q.setQuestion(questionet.getText().toString().trim());
    q.setOptions(option1et.getText().toString(),option2et.getText().toString(),option3et.getText().toString(),option4et.getText().toString());
    q.setCorrect_answer(1);
    return q;
}


}
