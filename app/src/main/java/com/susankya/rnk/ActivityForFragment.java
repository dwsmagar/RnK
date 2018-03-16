package com.susankya.rnk;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;

import java.util.ArrayList;

public class ActivityForFragment extends AppCompatActivity implements FragmentCodes{
public static String EXTRA_TAG="Index";
    public static String EXTRA_TAG_DATA1="com.timex.greenland.Data1";
    public static String EXTRA_TAG_DATA2="com.timex.greenland.Data2";
    public static String EXTRA_TAG_DATA3_1="com.timex.greenland.Data3_1";
    public static String EXTRA_TAG_DATA3_2="com.timex.greenland.Data3_2";
    public static String EXTRA_TAG_DATA3_3="com.timex.greenland.Data3_3";
    public static String EXTRA_TAG_DATA4="com.timex.greenland.Data4";
    public static String EXTRA_TAG_DATA5="com.timex.greenland.Data5";
    public static String EXTRA_TAG_DATA6="com.timex.greenland.Data6";
private static CountDownTimer countDownTimer;
    public static ArrayList<Integer> getAnswers() {
        return answers;
    }
    private static ArrayList<question> SharedQuestions;
    private static String questionCode;

    public static String getQuestionCode() {
        return questionCode;
    }

    public static void setQuestionCode(String questionCode) {
        ActivityForFragment.questionCode = questionCode;
    }

    public static ArrayList<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public static void setCorrectAnswers(ArrayList<Integer> correctAnswers) {
        ActivityForFragment.correctAnswers = correctAnswers;
    }

    public static boolean isDekhamta() {
        return dekhamta;
    }
public static Toolbar toolbar;
    public static void setDekhamta(boolean dekhamta) {
        ActivityForFragment.dekhamta = dekhamta;
    }

    private static boolean dekhamta=false;
    private static ArrayList<Integer> correctAnswers=new ArrayList<Integer>();
    public static void setAnswers(ArrayList<Integer> answers) {
        ActivityForFragment.answers = answers;
    }
   private static boolean isTimerFinished=false;

    public static boolean isIsTimerFinished() {
        return isTimerFinished;
    }

    public static void setIsTimerFinished(boolean isTimerFinished) {
        ActivityForFragment.isTimerFinished = isTimerFinished;
    }

    public static int testTimerMilliSeconds=100* 60 * 1000;

    public static int getTimerStarted() {
        return timerStarted;
    }

    public static void setTimerStarted(int timerStarted) {
        ActivityForFragment.timerStarted = timerStarted;
    }

    public static int timerStarted=0;
    public static boolean isCheckingStarted=false;
    public static TimexTimer getTimer() {
        return timer;
    }

    public static void setTimer(TimexTimer timer) {
        ActivityForFragment.timer = timer;
    }

    public static TimexTimer timer= Utilities.parseTimer(6000000);
    private static  ArrayList<Integer> answers;
    private int index=0;
    private Fragment fragment;
    private String mParam1;
    private static ArrayList<question> mQuestions;
    private JSONArray array;

    public static ArrayList<notice> getArrayList() {
        return ArrayList;
    }
    public static void setArrayList(ArrayList<notice> arrayList) {
        ArrayList = arrayList;
    }
    private static ArrayList<notice> ArrayList;
private Intent intent;
    public static ArrayList<question> getSharedQuestions() {
        return SharedQuestions;
    }

    public static void setSharedQuestions(ArrayList<question> sharedQuestions) {
        SharedQuestions = sharedQuestions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCheckingStarted=false;
        answers=new ArrayList();
        dekhamta=false;
        setContentView(R.layout.activity_fragment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getIntent()!=null){
            intent=getIntent();
            index=getIntent().getIntExtra(EXTRA_TAG,0);
             }
    try{
        switch(index){
            case 1:{
                String listDetail=intent.getStringExtra(EXTRA_TAG_DATA1);
               // fragment = showResultOffline.newInstance(null, null, listDetail.toString());
            }
            break;
            case 2:{

                  mParam1=intent.getStringExtra(EXTRA_TAG_DATA2);
                questionCode=intent.getStringExtra(CODE);
                setTitle("Test for " +questionCode);
                toolbar.setTitle(mParam1.toUpperCase());
       fragment= showQuestionOffline.newInstance(mParam1);
            }
            break;
            case 3:
            {
          //  fragment = addQuestionsFragmentBackup.newInstance(intent.getIntExtra(EXTRA_TAG_DATA3_1, 1), intent.getIntExtra(EXTRA_TAG_DATA3_2, 1));
           fragment= AddQuestionPagerFragment.newInstance(intent.getIntExtra(EXTRA_TAG_DATA3_1, 1), intent.getStringExtra(EXTRA_TAG_DATA3_2),intent.getBooleanExtra(EXTRA_TAG_DATA3_3,false));
            }
            break;
            case 4:
            {
              String question_code=intent.getStringExtra(EXTRA_TAG_DATA4);
              fragment = deleteQuestionFragment.newInstance(question_code);
            }
            break;
            case 5:
            {
                fragment = noticePagerFragment.newInstance(intent.getStringExtra(EXTRA_TAG_DATA5));
            }
            break;
            case 6:
            {
                fragment=new SettingsFragment();
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragment!=null)
     fragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }
    catch (Exception e){
            //Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        if(index==2){
            questionPagerFragment.stopTimer(getApplicationContext());
            isTimerFinished=true;
            timerStarted=0;
            questionFragment.stopTimer(getApplicationContext());
            isCheckingStarted=false;
        }

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        FragmentManager fm=getSupportFragmentManager();
        if(fm.getBackStackEntryCount()==1)
            this.finish();
        else {
            fm.popBackStack();
       /*     if (fm.getBackStackEntryCount() > 2) {
                fm.popBackStack();
            } else
                super.onBackPressed();*/
        }
    }
}
