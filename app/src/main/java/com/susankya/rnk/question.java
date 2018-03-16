package com.susankya.rnk;

import java.util.Random;

/**
 * Created by Administrator on 8/7/2015.
 */
public class question {
    private String question,option1,option2,option3,option4;

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    private String questionCode;
    private Random r=new Random();
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    private int questionNumber;
    private int correct_answer;

public question(int n, int ans){
    int s=r.nextInt();
    question="";
    option1="";
    option2="";
    option3="";
    option4="";

    questionNumber=n;
}
    public question()
    {

    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption1() {
        return option1;
    }

    public int getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(int correct_answer) {
        this.correct_answer = correct_answer;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public void setQuestion(String q){
        question=q;
    }
    public void setOptions(String o1,String o2,String o3,String o4){
        option1=o1;
        option2=o2;
        option3=o3;
        option4=o4;
    }

    public String toString(){
        return this.question;
    }
    public int getQuestionNumber(){
        return questionNumber;
    }
    public String getQuestion(){
        return question;
    }
    public String[] getOptions(){
        return  new String[] {option1,option2,option3,option4};
    }
    public String getCorrectOption()
    {
        switch(correct_answer)
        {
            case 1:
                return option1;
            case 2:
                return option2;
            case 3:
                return option3;
            case 4:
                return option4;
            default:
                return "ERROR";

        }
    }
}
