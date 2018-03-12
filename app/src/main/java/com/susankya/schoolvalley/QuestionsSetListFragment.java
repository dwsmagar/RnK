package com.susankya.schoolvalley;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class QuestionsSetListFragment extends android.support.v4.app.Fragment implements FragmentCodes {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean showingDownloadSetList=false;

    private SQLiteDatabase sqLiteDatabase;
    private SQLiteHelper sqLiteHelper;

    private TextView noSetsTV;

    FloatingActionButton loadQuestionsSet;
    private Bitmap downloadedSetIcon;

    private ListView questions_set_list;
    ArrayList<QuestionsSet> questionsSets;
    adapt listAdapt;
    private String link=HOST+"Android Call/RetrieveQuestionCode.php";
    public static QuestionsSetListFragment newInstance(String param1, String param2) {
        QuestionsSetListFragment fragment = new QuestionsSetListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionsSetListFragment() {
        // Required empty public constructor
    }

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onResume() {
        super.onResume();
        //getActivity().setTitle("Questions");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sqLiteHelper=new SQLiteHelper(getActivity());
        sqLiteDatabase=sqLiteHelper.getWritableDatabase();

           try{
                sqLiteDatabase.execSQL("create table if not exists " + QUESTIONS_SETS_LIST_TABLE + " (" + C_SN + " integer primary key autoincrement, " + CODE + " varchar(50),SIZE int(6)); ");
                sqLiteDatabase.execSQL("create table if not exists "+TABLE_LIST_OF_CODE+" ("+C_SN+" integer primary key autoincrement, "+CODE+" varchar(50)); ");
                sqLiteDatabase.execSQL("create table if not exists "+QUESTION_TABLE+" ("+C_SN+" integer primary key autoincrement, "+CODE+" varchar(50), "+QUESTION+" varchar, "+
                        OPTION1+" varchar, "+OPTION2+" varchar, "+OPTION3+" varchar, "
                        +OPTION4+" varchar, "+CORRECT_OPTION+" integer); ");
            }
            catch (Exception e){
                //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            }
    }

    private void saveQuestion(String questionCode,ArrayList<question>mQuestions){
        try {
            sqLiteDatabase.delete(TABLE_LIST_OF_CODE, CODE + "= ? ", new String[]{questionCode});
            try {
                sqLiteDatabase.delete(QUESTION_TABLE, CODE + "= ? ", new String[]{questionCode});
            }
            catch (Exception e){
                //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        ContentValues cv1=new ContentValues();
        cv1.put(CODE, questionCode);
        cv1.put("SIZE",mQuestions.size());
        sqLiteDatabase.insert(QUESTIONS_SETS_LIST_TABLE, null, cv1);
        for (int i = 0; i < mQuestions.size(); i++) {
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
                    sqLiteDatabase.insert(QUESTION_TABLE, null, cv);
                }
                catch (Exception e){
                    //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void loadQuestionsSetFromPHP()
    {
        questionsSets=new ArrayList<>();
        link=link.replace(" ","%20");
        new PhpConnect(
                link,"Loading...",getActivity(),1,new String[]{"2568wwexve",Utilities.getDatabaseName(getActivity())},new String[]{"cmdxxx","dbName"}
        ).setListener(
                new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {

                       // Toast.makeText(getActivity(), res, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
                            if(jsonArray.length()==0){
                                noSetsTV.setVisibility(View.VISIBLE);
                                noSetsTV.setText("No Questions to Load");
                            }else
                            {
                                noSetsTV.setVisibility(View.GONE);
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String code = jsonObject.getString("code");
                                int totalQuestions = jsonObject.getInt("totalQuestions");
                                QuestionsSet q = new QuestionsSet();
                                q.setSizeOfSet(totalQuestions);
                                q.setQuestionsCode(code);
                                if (sqLiteHelper.doesThisQuestionSetExist(sqLiteDatabase, code))
                                    q.setDownloaded(true);
                                else q.setDownloaded(false);
                                questionsSets.add(q);


                            }
                            listAdapt = new adapt(questionsSets);
                            questions_set_list.setAdapter(listAdapt);

                            showingDownloadSetList = true;

                        } catch (Exception e) {

                            questionsSets=new ArrayList<>();
                            listAdapt = new adapt(questionsSets);
                            questions_set_list.setAdapter(listAdapt);
                            showingDownloadSetList = true;
                            noSetsTV.setVisibility(View.VISIBLE);
                            noSetsTV.setText("Your institution has not uploaded any question sets.");
                            //Log.d("yaha xa", e.toString());
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    private void getQuestionsSetfromSQLite()
    {
        questionsSets=new ArrayList<>();
        questionsSets=sqLiteHelper.getQuestionsSets(sqLiteDatabase);
        listAdapt=new adapt(questionsSets);
        questions_set_list.setAdapter(listAdapt);
        if(questionsSets.isEmpty())
        {
            noSetsTV.setVisibility(View.VISIBLE);
        }
        else
        {
            noSetsTV.setVisibility(View.GONE);
        }
    }
    private void startActivityForFragment(int position){

        Intent i=new Intent(getActivity(),ActivityForFragment.class);
        try {
            i.putExtra(CODE, questionsSets.get(position).getQuestionsCode());
        } catch (Exception e) {
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        i.putExtra(ActivityForFragment.EXTRA_TAG_DATA2,questionsSets.get(position).getQuestionsCode());
        i.putExtra(ActivityForFragment.EXTRA_TAG,2);
        startActivity(i);

    }

    void loadIt(View v)
    {
        if(Utilities.getBlocked(getActivity().getApplicationContext())==0)
        {
            if(showingDownloadSetList)
            {
                getQuestionsSetfromSQLite();
                loadQuestionsSet.setImageBitmap(
                        BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.floating_add_rgb)
                );
                showingDownloadSetList=false;
            }
            else
            {
                loadQuestionsSet.setImageBitmap(
                        BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.floating_done)
                );
                if(Utilities.isConnectionAvailable(getActivity())){
                    if(Utilities.getDatabaseName(getActivity()).equals("none"))
                    {
                        noSetsTV.setText("No institute selected");
                        InstituteNotSelected instituteNotchosenSialod=new InstituteNotSelected();
                        instituteNotchosenSialod.show(getFragmentManager(),"showing");
                    }else{
                        noSetsTV.setVisibility(View.GONE);
                        loadQuestionsSetFromPHP();
                    }
                }
                else
                {
                    questionsSets.clear();
                    listAdapt=new adapt(questionsSets);
                    noSetsTV.setVisibility(View.VISIBLE);
                    noSetsTV.setText("No Internet Connection");
                }
                showingDownloadSetList=true;

            }

        }
        else
            Snackbar.make(v,"You have been blocked by the admin.",Snackbar.LENGTH_LONG).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_questions_set_list, container, false);
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        swipeRefreshLayout.setRefreshing(true);
                        loadQuestionsSetFromPHP();
                    }
                }
        );
        downloadedSetIcon=BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.tick_sure);
        questions_set_list=(ListView)v.findViewById(R.id.questions_set_list);
      //  downloadQuestionsTV = (TextView) v.findViewById(R.id.download_question_TV);
        loadQuestionsSet=(FloatingActionButton)v.findViewById(R.id.floating_add_button);
        loadQuestionsSet.setVisibility(View.GONE);
        noSetsTV=(TextView)v.findViewById(R.id.empty_list_TV);




        questionsSets = new ArrayList<>();
        listAdapt = new adapt(questionsSets);
           getQuestionsSetfromSQLite();
        loadIt(v);
        questions_set_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        final int pos =position;
                       final  QuestionsSet questionsSet=questionsSets.get(position);
                        if(questionsSet.isDownloaded())
                        {
                            final SharedPreferences sp=getActivity().getSharedPreferences("Public", Context.MODE_PRIVATE);
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setTitle("Test");
                            builder1.setMessage("Would you like to take a test?");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            sp.edit().putBoolean(isTest, true).apply();

                                            startActivityForFragment(position);
                                        }
                                    });   builder1.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sp.edit().putBoolean(isTest, false).apply();
                                        startActivityForFragment(position);

                                    }
                                });
                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                        }




                    }
                }
        );




        return v;
    }

    private class adapt extends ArrayAdapter<QuestionsSet> {
        ArrayList<QuestionsSet> param;
        public adapt(ArrayList<QuestionsSet> c){
            super(getActivity(),0,c);
            param=c;
        }
        @Override
        public View getView(final int pos,View v,ViewGroup vg){
            v=getActivity().getLayoutInflater().inflate(R.layout.question_set_item,null);
            final QuestionsSet q=getItem(pos);
           final TextView t1=(TextView)v.findViewById(R.id.code);
            final TextView t2=(TextView)v.findViewById(R.id.num);
            ((ImageView)v.findViewById(R.id.popup)).setVisibility(View.GONE);
            final ImageView v1=(ImageView)v.findViewById(R.id.download_questions_set);
            final ProgressBar pb=(ProgressBar)v.findViewById(R.id.downloading_progress);
            if(q.isDownloaded())
            {
                v1.setClickable(false);
                v1.setImageBitmap(downloadedSetIcon);
            }
            v1.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(!q.isDownloaded())
                            {
                                String getQuestionslink = HOST + "Android Call/getting_questions_table.php";
                                getQuestionslink = Utilities.encodeLinkSpace(getQuestionslink);
                                v1.setVisibility(View.GONE);
                                pb.setVisibility(View.VISIBLE);
                                String dbname=Utilities.getDatabaseName(getActivity());
                                new PhpConnect(getQuestionslink, "", getActivity(), 0,
                                        new String[]{"2568wwexve",UtilitiesAdi.giveMeSN(getActivity(),dbname),dbname , questionsSets.get(pos).getQuestionsCode()},
                                        new String[]{"cmdxxx","college_sn", "dbName", "code"}).setListener(
                                        new PhpConnect.ConnectOnClickListener() {
                                            @Override
                                            public void onConnectListener(String res) {

                                                if (!res.equals("0")) {
                                                    v1.setVisibility(View.VISIBLE);
                                                    v1.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.tick_sure));
                                                    pb.setVisibility(View.GONE);
                                                }
                                                ArrayList<question> questions = new ArrayList<question>();

                                                try {
                                                    JSONArray array = (JSONArray) new JSONTokener(res)
                                                            .nextValue();
                                                    for (int i = 0; i < array.length(); i++) {

                                                        JSONObject jO = array.getJSONObject(i);
                                                         //Log.d("TAG", "onConnectListener: " +jO.toString());
                                                        question n = new question();
                                                        String code = jO.getString("questionCode");
                                                        String questionnum = Integer.toString(jO.getInt("questionNumber"));
                                                        n.setQuestion(jO.getString("question"));
                                                        n.setQuestionNumber(i + 1);
                                                        n.setQuestionCode(code);
                                                        n.setOption1(jO.getString("option1"));
                                                        n.setOption2(jO.getString("option2"));
                                                        n.setOption3(jO.getString("option3"));
                                                        n.setOption4(jO.getString("option4"));
                                                        n.setCorrect_answer(jO.getInt("correctOption"));
                                                        questions.add(n);

                                                    }
                                                    saveQuestion(questionsSets.get(pos).getQuestionsCode(), questions);
                                                    questionsSets.get(pos).setDownloaded(true);
                                                    notifyDataSetChanged();

                                                } catch (Exception e) {
                                                    //Log.d("errorQuestionsSet", e.toString());
                                                }


                                            }
                                        }
                                );
                            }


                        }
                    }
            );
            t1.setText(q.getQuestionsCode());
            t2.setText(q.getSizeOfSet() + " questions");


            return v;
        }

    }

}
