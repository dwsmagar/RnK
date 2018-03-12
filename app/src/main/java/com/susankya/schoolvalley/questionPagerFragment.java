package com.susankya.schoolvalley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class questionPagerFragment extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private ViewPager pager;
    public static boolean isHasSeenResult() {
        return hasSeenResult;
    }
    public static void setHasSeenResult(boolean hasSeenResult) {
        questionPagerFragment.hasSeenResult = hasSeenResult;
    }

    private static boolean hasSeenResult;
    private ArrayList<question> mQuestions;
    private static ViewGroup mContainerView;
    private ArrayList<Integer> answers,correctAnswers;
    private static TextView secondTimer,minuteTimer,hourTimer;
    private int mParam1;
    private Button pre,next;
   private static boolean isTimerFinished;
public static CountDownTimer countDownTimer;

     public static questionPagerFragment newInstance(int param1) {
        questionPagerFragment fragment = new questionPagerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public questionPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
        hasSeenResult=false;
setRetainInstance(true);
    answers=new ArrayList();
        correctAnswers=new ArrayList();
        if(ActivityForFragment.isDekhamta())
            startActivity();
    }
    private static TimexTimer timer= ActivityForFragment.getTimer();

    public int totalQuestions,millis;
    public void countdownTimer()
    {
        if(ActivityForFragment.timerStarted==0)
        {
            timer= Utilities.parseTimer(millis);
            ActivityForFragment.setTimer(timer);
            doItBro(millis);
            ActivityForFragment.timerStarted=1;
          }
    }
    public  void doItBro(int milliseconds)
    {
        countDownTimer= new CountDownTimer(milliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                int secs=timer.getSecond();
                int mins=timer.getMinute();
                int hour=timer.getHour();
                if(mins==0 && hour>1)
                {
                    timer.setHour(--hour);
                    timer.setMinute(59);
                    timer.setSecond(59);
                }
                else if(mins==0 && hour==1)
                {
                    timer.setHour(0);
                    timer.setMinute(59);
                    timer.setSecond(59);
                }
                else if(secs==0 && mins!=0)
                {
                    timer.setSecond(59);
                    --mins;
                    timer.setMinute(mins);
                }
                else
                {
                    --secs;
                    timer.setSecond(secs);
                }
            secondTimer.setText(timer.getSecond()+"");
            minuteTimer.setText(timer.getMinute()+"");
            hourTimer.setText(timer.getHour()+"");
            ActivityForFragment.setTimer(timer);
                }

            public void onFinish() {
                if(isVisible()){
                startActivity();}
                else
             ActivityForFragment.setDekhamta(true);
            ActivityForFragment.setIsTimerFinished(true);
                isTimerFinished=true;
                secondTimer.setText("0");
                minuteTimer.setText("0");
                hourTimer.setText("0");
                this.cancel();
        }
        }.start();
    }
    public  void startActivity(){
        Intent intent = new Intent(getActivity(), TestResultActivity.class);
        intent.putExtra(CODE,ActivityForFragment.getQuestionCode());
        intent.putIntegerArrayListExtra(ANSWERS, ActivityForFragment.getAnswers());
        intent.putIntegerArrayListExtra(CORRECTANSWERS, ActivityForFragment.getCorrectAnswers());
        startActivity(intent);
        getActivity().finish();
    }

    public void manageButton(){
        try {
            int index = pager.getCurrentItem() + 1;
            pre.setText("Previous");
            next.setText("Next");
            if(pager.getCurrentItem()==0) {
                pre.setClickable(false);
                pre.setText("");
            }
            else{
                pre.setClickable(true);
            pre.setText("Previous");
            }
            if(pager.getCurrentItem() == pager.getAdapter().getCount() - 1) {
                next.setClickable(false);
                next.setText("");
            }
            else{
                next.setClickable(true);
        next.setText("Next");
            }}
        catch (Exception w){
            //Toast.makeText(getActivity(),w.toString(),Toast.LENGTH_SHORT).show();
        }
    }
public static void stopTimer(Context context){
        final SharedPreferences sp=context.getSharedPreferences("Public",Context.MODE_PRIVATE);
    if(sp.getBoolean(isTest,false)){
        if(countDownTimer!=null)
        countDownTimer.cancel();
    }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_pager, container, false);
        pager=(ViewPager)v.findViewById(R.id.pager);
        pre=(Button)v.findViewById(R.id.previous);
        next=(Button)v.findViewById(R.id.next);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
                 manageButton();
                if(pager.getCurrentItem()==0) {
                    pre.setClickable(false);
                }

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pre.setClickable(true);
                pager.setCurrentItem(pager.getCurrentItem()+1);
                manageButton();
                if(pager.getCurrentItem()==0) {
                    pre.setClickable(false);
                }
            }
        });
        secondTimer=(TextView)v.findViewById(R.id.txtTimerSecond);
        minuteTimer=(TextView)v.findViewById(R.id.txtTimerMinute);
        hourTimer=(TextView)v.findViewById(R.id.txtTimerHour);
        LinearLayout timerView=(LinearLayout)v.findViewById(R.id.timerView);
        final SharedPreferences sp=getActivity().getSharedPreferences("Public", Context.MODE_PRIVATE);
             try {
            mQuestions = ActivityForFragment.getSharedQuestions();
            if (mQuestions == null)
                mQuestions = NavDrawerActivity.getSharedQuestions();
                 for(int i=0;i<mQuestions.size();i++){
                     question q=mQuestions.get(i);
                     correctAnswers.add(q.getCorrect_answer());
                 }
                 ActivityForFragment.setCorrectAnswers(correctAnswers);
        }
        catch (Exception e){
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        totalQuestions=mQuestions.size();
        millis=totalQuestions *60000;
       if(sp.getBoolean(isTest,false)){
               countdownTimer();
             }
        else {
            timerView.setVisibility(View.GONE);
        }
         try{

        FragmentManager fm=getFragmentManager();
        pager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                question q=mQuestions.get(position);
                return questionFragment.newInstance(q.getQuestionNumber());
            }

            @Override
            public int getCount() {
                return mQuestions.size();// mCrime.size();
            }
        });
        for (int i = 0; i < mQuestions.size(); i++) {
            if (mQuestions.get(i).getQuestionNumber()==mParam1){
                pager.setCurrentItem(i);
              //  Toast.makeText(getActivity(),""+mParam1,Toast.LENGTH_SHORT).show();
                break;
            }
        }}
        catch (Exception e){
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        manageButton();
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                manageButton();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return  v;
    }

}
