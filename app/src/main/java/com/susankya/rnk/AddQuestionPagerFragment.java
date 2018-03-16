package com.susankya.rnk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class AddQuestionPagerFragment extends android.support.v4.app.Fragment implements FragmentCodes {

public static final String TAG_CODE="question_code";
public static final String TAG_ISFROMSAVED="isFromSaved";
    public static final String TAG_NUMOFQUESTIONS="no_of_questions";
     private int numberOfPages = 6;
    private int mParam1;
    private SQLiteHelper sqLiteHelper;
    private static SQLiteDatabase db;
    private boolean hasUploadingStarted;
    private static int indexOfQuestion=0;
    private static ViewPager viewPager;
    private ProgressDialog dialog;
    private String quesStore[][];
    private boolean isFromSaved;
    private String ques,opt1,opt2,opt3,opt4,correct,questionCode;
    protected int I=0,isItLastQuestion;
    private static ArrayList<question> mQuestions;

    public boolean checkIfAnyFieldIsEmpty()
    {
        for (int i = 0; i < mParam1; i++) {
            if(mQuestions.get(i).getQuestion().trim().isEmpty() || mQuestions.get(i).getOption1().trim().isEmpty()
                || mQuestions.get(i).getOption2().trim().isEmpty() || mQuestions.get(i).getOption3().trim().isEmpty()
                || mQuestions.get(i).getOption4().trim().isEmpty()||
                    Integer.toString(mQuestions.get(i).getCorrect_answer()).trim().isEmpty())
                return true;
        }
        return false;
    }

    public boolean isCorrectAnswerAValidNumber()
    {
        boolean val=true;
        for (int i = 0; i < mParam1; i++) {


            int a=mQuestions.get(i).getCorrect_answer();
            if(a==1 || a==2 || a==3 || a==4)
                val=true;
            else return false;

        }
        return  val;
    }

    public static ArrayList<question> getmQuestions() {
        return mQuestions;
    }

    public static void setmQuestions(ArrayList<question> mQuestions) {
        AddQuestionPagerFragment.mQuestions = mQuestions;
    }

    private int numOfQuestions;
    public static AddQuestionPagerFragment newInstance(int numofQuestions,String code,boolean isFromSaved)
    {
        AddQuestionPagerFragment fragment=new AddQuestionPagerFragment();
        Bundle args=new Bundle();
        args.putString(TAG_CODE,code);
        args.putInt(TAG_NUMOFQUESTIONS, numofQuestions);
        args.putBoolean(TAG_ISFROMSAVED, isFromSaved);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_submit_question_set,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.submitSet:
            {
                if(!checkIfAnyFieldIsEmpty())
                {
                    if(isCorrectAnswerAValidNumber())
                    {
                        int flag=0;
                        for (int i = 0; i < mParam1; i++) {
                            quesStore[i][0]=mQuestions.get(i).getQuestion();
                            quesStore[i][1]=mQuestions.get(i).getOption1();
                            quesStore[i][2]=mQuestions.get(i).getOption2();
                            quesStore[i][3]=mQuestions.get(i).getOption3();
                            quesStore[i][4]=mQuestions.get(i).getOption4();
                            quesStore[i][5]= Integer.toString(mQuestions.get(i).getCorrect_answer());
                            // Toast.makeText(getActivity(),"question: "+i+"  and op: "+quesStore[i][5],Toast.LENGTH_LONG).show();

                        }
                        dialog=new ProgressDialog(getActivity());
                        try
                        {
                            manageData();
                            // login(indexOfQuestion);
                        }
                        catch (Exception e)
                        {

                        }
                    }
                    else  Toast.makeText(getActivity(), "The correct option field must be either 1, 2, 3 or 4.", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getActivity(), "Please fill all fields.", Toast.LENGTH_LONG).show();

            }
                return true;
            case R.id.saveSet:{

                try{
                    db.execSQL("create table "+TABLE_LIST_OF_CODE+" ("+C_SN+" integer primary key autoincrement, "+CODE+" varchar(50)); ");
                    db.execSQL("create table "+QUESTION_TABLE+" ("+C_SN+" integer primary key autoincrement, "+CODE+" varchar(50), "+QUESTION+" varchar, "+
                            OPTION1+" varchar, "+OPTION2+" varchar, "+OPTION3+" varchar, "
                            +OPTION4+" varchar, "+CORRECT_OPTION+" integer); ");
                }
                catch (Exception e){
                    // Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                }
                saveQuestion();
                Toast.makeText(getActivity(), "Questions saved!", Toast.LENGTH_SHORT).show();
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public AddQuestionPagerFragment(){

}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    if(getArguments()!=null){
        questionCode=getArguments().getString(TAG_CODE);
        numOfQuestions=getArguments().getInt(TAG_NUMOFQUESTIONS);
        isFromSaved=getArguments().getBoolean(TAG_ISFROMSAVED,false);
    }
        sqLiteHelper = new SQLiteHelper(getActivity());
        db = sqLiteHelper.getWritableDatabase();
        indexOfQuestion=0;
        setHasOptionsMenu(true);
        mParam1=numOfQuestions;
       getActivity().setTitle("Add question for code:"+questionCode);
        mQuestions =new ArrayList();

        if(!isFromSaved){
        for(int i=0;i<numOfQuestions;i++){
            question q=new question(i,1);
            mQuestions.add(q);
        }}
        else {
            try{
                String[] columns=new String[]{CODE,QUESTION,OPTION1,OPTION2,OPTION3,OPTION4,CORRECT_OPTION};
                Cursor c=db.query(QUESTION_TABLE,columns,CODE+"= ? ",new String[]{questionCode},null,null,null);
                //Toast.makeText(getActivity(),"reaches",Toast.LENGTH_SHORT).show();
                while(c.moveToNext()){
                    mParam1++;
                    numOfQuestions++;
                    question q=new question();
                    int i = c.getColumnIndex(QUESTION);
                    int j=c.getColumnIndex(OPTION1);
                    int k=c.getColumnIndex(OPTION2);
                    int l=c.getColumnIndex(OPTION3);
                    int m=c.getColumnIndex(OPTION4);
                    int n=c.getColumnIndex(CORRECT_OPTION);
                    q.setQuestion(c.getString(i));
                    q.setOptions(c.getString(j),c.getString(k),c.getString(l),c.getString(m));
                    q.setCorrect_answer(c.getInt(n));
                    mQuestions.add(q);
                }
                }catch (Exception e){
                //Log.d("TAG", "onCreate:"+e.toString());
                Toast.makeText(getActivity(), "Please try again!", Toast.LENGTH_SHORT).show();
            }
        }
        quesStore=new String[mParam1][6];
        hasUploadingStarted=false;

    }

   /* @Override
    public void onPause() {
      if(indexOfQuestion<mParam1) {
          if (!uploadQuestion.isCancelled())
              uploadQuestion.cancel(true);
          if(hasUploadingStarted)
          Toast.makeText(getActivity(),"UPLOADED: " + (indexOfQuestion+1) + " out of " + mParam1 + " for question code:" + questionCode,Toast.LENGTH_SHORT).show();
      }
        super.onPause();
    }*/
private void saveQuestion(){
   try {
       db.delete(TABLE_LIST_OF_CODE,CODE+"= ? ",new String[]{questionCode});
       try {
               db.delete(QUESTION_TABLE, CODE + "= ? ", new String[]{questionCode});
       }
       catch (Exception e){
           //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
       }
   }
   catch (Exception e){
      // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
   }
    ContentValues cv1=new ContentValues();
    cv1.put(CODE,questionCode);
    db.insert(TABLE_LIST_OF_CODE, null, cv1);
    for (int i = 0; i < mParam1; i++) {
        question q=mQuestions.get(i);
        {
           try {
               ContentValues cv = new ContentValues();
               cv.put(CODE, questionCode);
               cv.put(QUESTION, q.getQuestion());
               cv.put(OPTION1, q.getOption1());
               cv.put(OPTION2, q.getOption2());
               cv.put(OPTION3, q.getOption3());
               cv.put(OPTION4, q.getOption4());
               cv.put(CORRECT_OPTION, q.getCorrect_answer());
               db.insert(QUESTION_TABLE, null, cv);
           }
        catch (Exception e){
           // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        }

    }
}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.add_question_pager,container,false);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        final Button pre=(Button)v.findViewById(R.id.previous);
        final Button next=(Button)v.findViewById(R.id.next);
            pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                //  manageButton();
                next.setText ((viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
                if(viewPager.getCurrentItem()==0) {
                    pre.setClickable(false);
                }

            }
        });
              next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pre.setClickable(true);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                //manageButton();
                next.setText((viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
                if (viewPager.getCurrentItem() == 0) {
                    pre.setClickable(false);
                }
            }
        });
        FragmentManager fm=getChildFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return AddQuestionFragment.newInstance(position, questionCode);
            }

            @Override
            public int getCount() {
                return mParam1;
            }
        });

        return v;
    }

    private void manageData(){
        int positionOfQues=indexOfQuestion;
        isItLastQuestion=positionOfQues;
        ques=quesStore[positionOfQues][0];
        opt1=quesStore[positionOfQues][1];
        opt2=quesStore[positionOfQues][2];
        opt3=quesStore[positionOfQues][3];
        opt4=quesStore[positionOfQues][4];
        correct=quesStore[positionOfQues][5];
        uploadQuestion(new String[]{questionCode,ques,opt1,opt2,opt3,opt4,correct});
    }

    private void uploadQuestion(String[] str) {//code,question,op1,op2,op3,op4,correctoption
        String link = NEW_HOST + "adding_questions.php";
        new PhpConnect(link, "Adding...(" + (indexOfQuestion + 1) + "/" + mParam1 + ")", getActivity(), 1, new String[]{str[0], str[1], str[2], str[3], str[4], str[5], str[6],CMDXXX,UtilitiesAdi.giveMeSN(getActivity(),Utilities.getDatabaseName(getActivity()))}, new String[]{"code", "question", "opt1", "opt2", "opt3", "opt4", "correct","cmdxxx","college_sn"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
//Toast.makeText(getActivity(), res.toString(), Toast.LENGTH_SHORT).show();
                hasUploadingStarted = true;
                  indexOfQuestion++;
                if (res.trim().equals("1") && indexOfQuestion == mParam1) {
                    Toast.makeText(getActivity(), "Successfully added question", Toast.LENGTH_SHORT).show();

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    mQuestions.clear();
                    getActivity().finish();
                } else if (indexOfQuestion < mParam1 && res.trim().equals("1")) {
manageData();
                } else if (res.trim().equals("0")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setTitle("Failed to add");
                    builder1.setMessage("Couldn't add question. Make sure you entered correct values and try again.");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else {
                    Toast.makeText(getActivity(), "Unexpected error. Please try in a moment, you can save the question for future use.", Toast.LENGTH_SHORT).show();
                }
          }
        });
    }

}
