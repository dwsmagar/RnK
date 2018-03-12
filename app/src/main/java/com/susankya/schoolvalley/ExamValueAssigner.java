package com.susankya.schoolvalley;


public class ExamValueAssigner {

    private int mDrawableId[],mClass,mRoll;
    private String mFaculty;
    private String mExam;
    private String subjectsList[];

    public int[] getmDrawableId() {
        return mDrawableId;
    }

    public String[] getSubjectsList()
    {
        return subjectsList;
    }
    public ExamValueAssigner(int roll,int CLASS,String term,String faculty)
    {

        mRoll=roll;
        mClass=CLASS;
        mExam=term;

        if(faculty.equals("sc"))
        {
           subjectsList=new String[]
                    {
                            "Physics","Chemistry","Maths",
                            "English","Bio/Comp","Nepali","Total",
                            "Average"
                    };
        }
        if(faculty.equals("com"))
        {

            subjectsList=new String[]
                    {
                            "Accounts","Economics","Business/Comp",
                            "English","BMat/Makt","Nepali","Total",
                            "Average"
                    };
        }

    }

    public int getmClass() {
        return mClass;
    }

    public void setmClass(int mClass) {
        this.mClass = mClass;
    }

    public String getmFaculty() {
        return mFaculty;
    }

    public void setmFaculty(String mFaculty) {
        this.mFaculty = mFaculty;
    }

    public String getmExam() {
        return mExam;
    }

    public void setmExam(String mExam) {
        this.mExam = mExam;
    }


}
