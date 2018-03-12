package com.susankya.schoolvalley;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class questionFragment extends Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private int mParam1;
    private int correctAnswer;
    private  int totalCorrectAnswer;
    private static ViewGroup mContainerView;
    private static ImageView showAnswer,showResult;
    ArrayList<question> mQuestions=new ArrayList<question>();
    private View v;
    private RadioGroup radio_options;
    private static ArrayList<Integer> answers,correctAnswers;
    private static ListView listView;
   private TextView secondTimer,minuteTimer,hourTimer;
    private int TRIED=0;
    private question q;
    private static RadioGroup rg;
    private static boolean isTimeFinished=false;
    private static CountDownTimer countDownTimer;
    int millis=0;
    private static RadioButton op1,op2,op3,op4;
    TimexTimer timer= ActivityForFragment.getTimer();
  public static questionFragment newInstance(int  ques_num) {
        questionFragment fragment = new questionFragment();
        Bundle args = new Bundle();
            args.putSerializable(ARG_PARAM1, ques_num);
            fragment.setArguments(args);

return fragment;
    }


    public questionFragment() {
        totalCorrectAnswer=0;
        // Required empty public constructor
    }

private void resultAnalyser(){
    try {
        correctAnswers= ActivityForFragment.getCorrectAnswers();
        ActivityForFragment.setAnswers(answers);
    int attemptQuestion=0;
    totalCorrectAnswer=0;
    for(int i=0;i<answers.size();i++){
        if(answers.get(i)!=0)
            attemptQuestion++;
        if(answers.get(i)==correctAnswers.get(i))
            totalCorrectAnswer++;
    }
    if(!isTimeFinished)
    if(attemptQuestion!=answers.size()){
        Toast.makeText(getActivity(),"Please attempt all questions.",Toast.LENGTH_SHORT).show();
        return;
    }
questionPagerFragment.setHasSeenResult(true);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle("Test");
        builder1.setMessage("Have you completed?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                     startActivity();
                    }
                });   builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();


}catch (Exception e){
        //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
    }}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
       answers=new ArrayList();
       correctAnswers=new ArrayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_question, container, false);
        TextView tv=(TextView)v.findViewById(R.id.testtext);
        mQuestions= ActivityForFragment.getSharedQuestions();
        final SharedPreferences sp = getActivity().getSharedPreferences("Public", Context.MODE_PRIVATE);
        millis=mQuestions.size()*1000;
       showAnswer=(ImageView)v.findViewById(R.id.showAnswer);
        showResult=(ImageView)v.findViewById(R.id.showResult);
        secondTimer=(TextView)v.findViewById(R.id.txtTimerSecond);
        View result=v.findViewById(R.id.resultLL);
        View sheet=v.findViewById(R.id.sheetLL);
        result.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultAnalyser();
                    }
                }
        );
        sheet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        ShowAnswersDialog dialog = new ShowAnswersDialog();
                        Bundle bundle=new Bundle();
                        bundle.putBoolean("isResult",false);
                        dialog.setArguments(bundle);
                        dialog.show(fragmentManager, "SHOWING");
                    }
                }
        );
        minuteTimer=(TextView)v.findViewById(R.id.txtTimerMinute);
        hourTimer=(TextView)v.findViewById(R.id.txtTimerHour);
        answers= ActivityForFragment.getAnswers();
        q=getQuestionFromArray(mParam1);

        if(!sp.getBoolean(isTest,true)){
        showResult.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);
        }

radio_options=(RadioGroup)v.findViewById(R.id.options);
        TextView q_num=(TextView)v.findViewById(R.id.q_num);
        q_num.setText(""+(q.getQuestionNumber()));
       op1=(RadioButton)v.findViewById(R.id.option1);
        op2=(RadioButton)v.findViewById(R.id.option2);
       op3=(RadioButton)v.findViewById(R.id.option3);
        op4=(RadioButton)v.findViewById(R.id.option4);
        TextView question=(TextView)v.findViewById(R.id.question);
        question.setText(") "+q.getQuestion());

        String[] options=q.getOptions();
        correctAnswer=(q.getCorrect_answer())-1;
        op1.setText(options[0]);
        op2.setText(options[1]);
        op3.setText(options[2]);
        op4.setText(options[3]);
        try {
          //  Toast.makeText(getActivity(),""+answers.get(mParam1-1),Toast.LENGTH_SHORT).show();
            switch (ActivityForFragment.getAnswers().get(mParam1-1)) {
                case 1:
                    op1.setChecked(true);
                    break;
                case 2:
                    op2.setChecked(true);
                    break;
                case 3:
                    op3.setChecked(true);
                    break;
                case 4:
                    op4.setChecked(true);
                    break;
            }
        }
        catch (Exception e){
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        radio_options.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
    RadioButton r = (RadioButton) v.findViewById(checkedId);
    int checkedIndex = radio_options.indexOfChild(r);
    answers.remove(q.getQuestionNumber() - 1);
    answers.add(q.getQuestionNumber() - 1, checkedIndex + 1);
    TRIED = 1;
                      /*  if(checkedIndex==correctAnswer)
                        {
                           Toast.makeText(getActivity(),"Correct answer!",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                           Toast.makeText(getActivity(),"Wrong answer!",Toast.LENGTH_LONG).show();
                        }*/
                    //Toast.makeText(getActivity(),""+answers.get(mParam1-1),Toast.LENGTH_SHORT).show();
                    ActivityForFragment.setAnswers(answers);
}catch (Exception e)
{
    //Toast.makeText(getActivity(),"1"+e.toString(),Toast.LENGTH_SHORT).show();
}

                    }
                }
        );

       return v;
}

    public question getQuestionFromArray(int pos) {
        for (question c : mQuestions) {
            if (c.getQuestionNumber()==pos)
                return c;
        }
        return null;
    }
    public  void startActivity(){
        Intent intent = new Intent(getActivity(), TestResultActivity.class);
        intent.putExtra(CODE,ActivityForFragment.getQuestionCode());
        intent.putIntegerArrayListExtra(ANSWERS, ActivityForFragment.getAnswers());
        intent.putIntegerArrayListExtra(CORRECTANSWERS, ActivityForFragment.getCorrectAnswers());
        startActivity(intent);
        getActivity().finish();
    }
    public static class ShowAnswersDialog extends android.support.v4.app.DialogFragment {
        private boolean isResult;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return super.onCreateDialog(savedInstanceState);

        }

        private int totalCorrectAnswer;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
               isResult = getArguments().getBoolean("isResult",false);
                totalCorrectAnswer=getArguments().getInt("totalCorrectAnswer",0);
            }
             }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SIS) {
            View v = inflater.inflate(R.layout.fragment_take_test, null, false);
             mContainerView = (ViewGroup)v.findViewById(R.id.container);
            SharedPreferences sp=getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE,Context.MODE_PRIVATE);
             LinearLayout resultLayout=(LinearLayout)v.findViewById(R.id.resultLayout);
            TextView totalQuestion=(TextView)v.findViewById(R.id.totalQuestion);
            TextView correctAnswers=(TextView)v.findViewById(R.id.correctAnswers);
            totalQuestion.setText("Total Questions: "+answers.size());
            correctAnswers.setText("Correct Answers: "+totalCorrectAnswer);
             if(!isResult) {
                resultLayout.setVisibility(View.GONE);
            }
            getDialog().getWindow().setTitle("Answer Sheet");
            if(sp.getBoolean(isTest,false))
            answers=  ActivityForFragment.getAnswers();
            else
            answers=ActivityForFragment.getCorrectAnswers();
            for(int i=answers.size();i>0;i--){
                 addItem(i,answers.get(i-1));
             }
                  return v;
        }

        private void addItem(int num,int correctOption) {
             ImageView option1,option2,option3,option4,correction;
            final ViewGroup newView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(
                    R.layout.answer, mContainerView, false);
                    ((TextView) newView.findViewById(R.id.num)).setText(""+num);
            correction=(ImageView)newView.findViewById(R.id.correction);
            option1=(ImageView)newView.findViewById(R.id.option1);
            option2=(ImageView)newView.findViewById(R.id.option2);
            option3=(ImageView)newView.findViewById(R.id.option3);
            option4=(ImageView)newView.findViewById(R.id.option4);
            if(!isResult) {
                correction.setVisibility(View.INVISIBLE);
            }
            else{
                try{
                if(answers.get(num-1)==correctAnswers.get(num-1))
                    correction.setImageResource(R.drawable.tick_sure);
                else {
                    correction.setImageResource(R.drawable.wronganswer);
                switch (correctAnswers.get(num-1)){
                    case 1:
                        option1.setImageResource(R.drawable.tick_sure);
                        break;
                    case 2:
                        option2.setImageResource(R.drawable.tick_sure);
                        break;
                    case 3:
                        option3.setImageResource(R.drawable.tick_sure);
                        break;
                    case 4:
                        option4.setImageResource(R.drawable.tick_sure);
                        break;
                }
                }}catch (Exception e){
                    //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();

                }
            }
            switch (correctOption) {
                case 1:
                    option1.setImageResource(R.drawable.tick_unsure);
                    break;
                case 2:
                   option2.setImageResource(R.drawable.tick_unsure);
                    break;
                case 3:
                   option3.setImageResource(R.drawable.tick_unsure);
                    break;
                case 4:
                    option4.setImageResource(R.drawable.tick_unsure);
                    break;
            }
            mContainerView.addView(newView, 0);
        }

    }
    public static void stopTimer(Context context){
        final SharedPreferences sp=context.getSharedPreferences("Public",Context.MODE_PRIVATE);
        if(sp.getBoolean(isTest,false)){
            if(countDownTimer!=null)
                countDownTimer.cancel();
        }
}
}
