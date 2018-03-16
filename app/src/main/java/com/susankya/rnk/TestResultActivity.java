package com.susankya.rnk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class TestResultActivity extends AppCompatActivity implements FragmentCodes {
private static ArrayList<Integer> answers;
    private ViewGroup mContainerView;
    private int correctAnswer;
    private  int totalCorrectAnswer;
    private  String QuestionCode;
    private static ArrayList<Integer> correctAnswers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setTitle("Result");
        setContentView(R.layout.activity_test_result);
        final Button button=(Button)findViewById(R.id.start);

        try {
    answers=new ArrayList<>();
        correctAnswers=new ArrayList<>();
        if(getIntent()!=null){
            QuestionCode=getIntent().getStringExtra(CODE);
            answers=getIntent().getIntegerArrayListExtra(ANSWERS);
            correctAnswers=getIntent().getIntegerArrayListExtra(CORRECTANSWERS);
        }
         totalCorrectAnswer=0;
        int attemptQuestion=0;
        for(int i=0;i<answers.size();i++){
            if(answers.get(i)!=0)
                attemptQuestion++;
            if(answers.get(i)==correctAnswers.get(i))
                totalCorrectAnswer++;
        }
        mContainerView = (ViewGroup)findViewById(R.id.container);
        LinearLayout resultLayout=(LinearLayout)findViewById(R.id.resultLayout);
        TextView totalQuestion=(TextView)findViewById(R.id.totalQuestion);
        TextView correctAnswers=(TextView)findViewById(R.id.correctAnswers);
        TextView questionCode=(TextView)findViewById(R.id.questionCode);
            questionCode.setText("Question code: "+QuestionCode);
        totalQuestion.setText("Total Questions: "+answers.size());
        correctAnswers.setText("Correct Answers: "+totalCorrectAnswer);
      for(int i=answers.size();i>0;i--){
            addItem(i,answers.get(i-1));
        }
        }
        catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void addItem(int num,int correctOption) {
        ImageView option1,option2,option3,option4,correction;
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.answer, mContainerView, false);
        ((TextView) newView.findViewById(R.id.num)).setText(""+num);
        correction=(ImageView)newView.findViewById(R.id.correction);
        option1=(ImageView)newView.findViewById(R.id.option1);
        option2=(ImageView)newView.findViewById(R.id.option2);
        option3=(ImageView)newView.findViewById(R.id.option3);
        option4=(ImageView)newView.findViewById(R.id.option4);

        int resIDCorrectAns=R.drawable.tick_sure;
        int resIDSelectedOption=R.drawable.tick_unsure;
            try{
                if(answers.get(num-1)==correctAnswers.get(num-1))
                    correction.setImageResource(R.drawable.tick_sure);
                else {
                    correction.setImageResource(R.drawable.wronganswer);
                    switch (correctAnswers.get(num-1)){

                        case 1:
                            option1.setImageResource(resIDCorrectAns);
                            break;
                        case 2:
                            option2.setImageResource(resIDCorrectAns);
                            break;
                        case 3:
                            option3.setImageResource(resIDCorrectAns);
                            break;
                        case 4:
                            option4.setImageResource(resIDCorrectAns);
                            break;
                    }
                }}catch (Exception e){
                //Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();

            }

       if(correctOption==correctAnswers.get(num-1))
           resIDSelectedOption=R.drawable.tick_sure;
        switch (correctOption) {
            case 1:
                option1.setImageResource(resIDSelectedOption);
                break;
            case 2:
                option2.setImageResource(resIDSelectedOption);
                break;
            case 3:
                option3.setImageResource(resIDSelectedOption);
                break;
            case 4:
                option4.setImageResource(resIDSelectedOption);
                break;
        }
        mContainerView.addView(newView, 0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_test_result, menu);
        return true;
    }
    private void captureScreen() {
        View v = getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        String name="SCREEN" + System.currentTimeMillis() + ".png";
       // String path="/storage/emulated/0/DCIM/Screenshots/";
         try {
            FileOutputStream fos = openFileOutput(name,MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
           //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        start(name,getApplicationContext());
        //   open(path+name);
    }
    private void start(String path,Context c){
        SharedPreferences s=c.getApplicationContext().getSharedPreferences("rememberlogin", Context.MODE_PRIVATE);
        if(!s.getBoolean("isloggedin",false)){
            Intent i = new Intent(TestResultActivity.this, StartActivity.class);
            startActivity(i);
        }

    }


private void open(String path){
    Intent i=new Intent();
    i.setAction(Intent.ACTION_VIEW);
    Uri uri= Uri.fromFile(new File(path));
    i.setDataAndType(uri,"image/*");
    startActivity(i);
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
