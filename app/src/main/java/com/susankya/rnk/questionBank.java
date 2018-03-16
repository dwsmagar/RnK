package com.susankya.rnk;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Administrator on 8/7/2015.
 */
public class questionBank {
private  static ArrayList<question> questions;
    private  Context mAppContext;
    private static questionBank sQuestionBank;
    public questionBank(Context context){
        mAppContext=context;
        questions=new ArrayList<question>();
        for(int i=0;i<10;i++){
            question q=new question(questions.size()+1,2);
            questions.add(q);
        }
    }
    public static questionBank get(Context c) {
        if ( sQuestionBank== null) {
            sQuestionBank = new questionBank(c.getApplicationContext());
        }
        return sQuestionBank;
    }
    public static  ArrayList<question> getQuestions(){
        return questions;
    }
    void addNotice(question q){
        try {
          questions.add(q);
        }
        catch(Exception e){
            //Toast.makeText(mAppContext, "problem adding object", Toast.LENGTH_SHORT).show();
        }
    }
    public question getQuestion(int pos) {
        for (question q : questions) {
            if (q.getQuestionNumber()==pos) {
                return q;
            }
        }
        return null;
    }

    public void addQuestion(question q) {
        questions.add(q);
    }
}
