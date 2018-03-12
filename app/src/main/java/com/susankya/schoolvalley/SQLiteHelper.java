package com.susankya.schoolvalley;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 9/3/2015.
 */

public class SQLiteHelper extends SQLiteOpenHelper implements FragmentCodes {

    String ANSWER="answer";
    String DATE_TIME="date_time";
    public static final String SAVED_ANSWERS_TABLE="saved_answers";
    String POST_NUMBER="post_Number";
     private static final String DATABASE_CREATE = "create table " +
            "TABLE" + " (" + "COlUMN_SN" +
            " integer primary key autoincrement, " +"COLUMN_Q_NUM "+"integer, "+"COLUMN_QUESTIONS "+"varchar(500) "+"COLUMN_DATE "+
            "date "+"COLUMN_CHAPTER "+"integer "+"COLUMN_STATUS "+"integer "+
            "NAME" + " varchar(225));";

    SQLiteHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
  }
    // Called when no database exists in disk and the helper class needs
    // to create a new one.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+QUESTIONS_SETS_LIST_TABLE+" ("+C_SN+" integer primary key autoincrement, "+CODE+" varchar(50),SIZE int(6)); ");
        db.execSQL("create table  if not exists " + QUESTION_TABLE + " (" + C_SN + " integer primary key autoincrement, " + CODE + " varchar(50), " + QUESTION + " varchar, " +
                OPTION1 + " varchar, " + OPTION2 + " varchar, " + OPTION3 + " varchar, "
                + OPTION4 + " varchar, " + CORRECT_OPTION + " integer); ");
        db.execSQL("create table if not exists "+TABLENAME+" ("+DAY+" varchar(50), "+PERIOD_NO+" varchar(50), "+SUBJECT+" varchar(50), "+SECTION_NAME+" varchar(50), "+
                TEACHER_NAME+" varchar(50), "+START+" varchar(50), "+END+" varchar(50), "+COLLEGE_SN+" varchar(50)); ");
//`user_no`, `firstName`, `lastName`, `password`, `userid`, `verifed`, `institution`,
// `level`, `dbName`, `gender`, `profile_picture_location`, `phone_number`, `location`, `intrest`, `hobby`
        db.execSQL(
                "create table if not exists " + USERS_TABLE + " ( " + C_SN + " integer primary key autoincrement," +
                        USER_NO + " int (10)," + FIRST_NAME + " varchar(100)," + LAST_NAME + " varchar(100)," +
                        PASSWORD + " varchar(100)," + USERNAME + " varchar(50)," +
                        VERIFIED + " int(1)," + INSTITUTION + " varchar," + LEVEL + " varchar, " +
                        DBNAME + " varchar," +
                        GENDER + " varchar(50)," + PROFILE_PIC_LOCATION + " varchar," + LOCATION + " varchar,"
                        + PHONE_NUMBER + " int(10), " + INTEREST + " varchar, " + HOBBY + " varchar);"

        );

        db.execSQL(
                "create table if not exists " + SAVED_ANSWERS_TABLE + " ( " + C_SN + " integer primary key autoincrement," +
                        POST_NUMBER + " int (10)," + DATE_TIME + " varchar(100)," + ANSWER + " varchar);"
        );

                   }
    // Called when there is a database version mismatch meaning that
    // the version of the database on disk needs to be upgraded to
    // the current version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " +
                oldVersion + " to " +
                newVersion + ", which will destroy all old data");
         db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        // Create a new one.
        onCreate(db);
    }

    public void insertIntoAnswers(int postNum,String dateTime,String answer)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(POST_NUMBER,postNum);
        cv.put(DATE_TIME, dateTime);
        cv.put(ANSWER, answer);
        db.insert(SAVED_ANSWERS_TABLE, null, cv);
    }

    public void clearSavedAnswers()
    {
        SQLiteDatabase db=getWritableDatabase();

        db.execSQL("delete from "+SAVED_ANSWERS_TABLE);
    }

    public ArrayList<SavedAnswer> loadAnswers()
    {
        ArrayList<SavedAnswer> savedAnswers=new ArrayList<>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.rawQuery(
                "select * from "+SAVED_ANSWERS_TABLE+" ORDER BY Sn DESC",null
        );

        while(c.moveToNext())
        {

           int postNumber=c.getInt(1);
            String dateTime=c.getString(2);
            String answer=c.getString(3);
            SavedAnswer s=new SavedAnswer(postNumber,dateTime,answer);
            savedAnswers.add(s);
        }
        c.close();
        return savedAnswers;
    }

    public void insertUser(UserInfo userInfo)
    {
        //`user_no`, `firstName`, `lastName`, `password`, `userid`, `verifed`, `institution`,
// `level`, `dbName`, `gender`, `profile_picture_location`, `phone_number`, `location`, `intrest`, `hobby`
        try
        {
            SQLiteDatabase db=getWritableDatabase();
            ContentValues cv=new ContentValues();
            cv.put(USER_NO,userInfo.getUserNumber());
            cv.put(FIRST_NAME,userInfo.getFirstName());
            cv.put(LAST_NAME,userInfo.getLastName());
            cv.put(PASSWORD,userInfo.getPassword());
            cv.put(USERNAME,userInfo.getUserName());
            cv.put(VERIFIED,userInfo.getVerified());
            cv.put(INSTITUTION,userInfo.getInstitution());
            cv.put(LEVEL,userInfo.getLevel());
            cv.put(DBNAME,userInfo.getGender());
            cv.put(PROFILE_PIC_LOCATION,userInfo.getProfilepiclocation());
            cv.put(PHONE_NUMBER,Integer.parseInt(userInfo.getPhoneNumber()));
            cv.put(LOCATION,userInfo.getLocation());
            cv.put(INTEREST,userInfo.getInterest());
            cv.put(HOBBY,userInfo.getHobby());
            db.insert(USERS_TABLE,null,cv);
        }
        catch (Exception e)
        {
            //Log.d("errorfirst",e.toString());
        }
    }

    public ArrayList<QuestionsSet> getQuestionsSets(SQLiteDatabase db)
    {
        ArrayList<QuestionsSet> q=new ArrayList<>();
        Cursor c=db.rawQuery(
                "select * from "+QUESTIONS_SETS_LIST_TABLE+" ORDER BY Sn DESC",null
        );

        while(c.moveToNext())
        {
            String questionCode=c.getString(1);
            int sizeOfSet=c.getInt(2);
            QuestionsSet questionsSet=new QuestionsSet();
            questionsSet.setQuestionsCode(questionCode);
            questionsSet.setSizeOfSet(sizeOfSet);
            if(doesThisQuestionSetExist(db,questionCode))
                questionsSet.setDownloaded(true);
            q.add(questionsSet);
        }
        c.close();

        return  q;
    }
    public ArrayList<question> loadQuestions(String questionCode)
    {
        SQLiteDatabase db=getWritableDatabase();
        ArrayList<question> q=new ArrayList<>();
        Cursor c=db.rawQuery(
                "select * from "+QUESTION_TABLE+" where CODE=?",new String[]{questionCode}
        );
        int count=0;
        while(c.moveToNext())
        {
            int SN=c.getInt(0);
            String question=c.getString(2);
            String option1=c.getString(3);
            String option2=c.getString(4);
            String option3=c.getString(5);
            String option4=c.getString(6);
            int correctAns=c.getInt(7);
            //Log.d("values",question);
           com.susankya.schoolvalley.question question1=new question();
            question1.setCorrect_answer(correctAns);
            question1.setOption1(option1);
            question1.setOption2(option2);
            question1.setOption3(option3);
            question1.setOption4(option4);
            question1.setQuestion(question);
            question1.setQuestionNumber(count+1);
            q.add(question1);
            count++;
        }
        c.close();

        return  q;
    }

    public boolean doesThisQuestionSetExist(SQLiteDatabase db,String questionCode)
    {
        Cursor c=db.rawQuery(
                "select * from "+QUESTIONS_SETS_LIST_TABLE+" where CODE=?",new String[]{questionCode}
        );
        if(c.getCount()>0)
            return true;
        else return false;
    }
}