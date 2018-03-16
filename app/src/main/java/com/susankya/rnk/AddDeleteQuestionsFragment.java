package com.susankya.rnk;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class AddDeleteQuestionsFragment extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String QUESTIONS_SETS_LIST_TABLE_1 = "NewQuestionCodeTAble";
    public static final String RESPONSEFROMNET = "responsefromnet";
    public static final String SECTION_NAME ="section_NAME";
    private   SQLiteHelper sqLiteHelper;
    private Menu menu;
    private  static SQLiteDatabase sqLiteDatabase;
    private String link=HOST+"Android Call/RetrieveQuestionCode.php";
    private static ArrayList<String> list;
    private adapt listAdapt;
    private String mParam1;
    private String filterString;
    private TextView Empty_List_view,header;
    private FloatingActionButton fab;
    private String mParam2,NO_OF_QUESTIONS="noOFquestion";
    boolean hasdownloaddQueations=false;
    ListView question_set_listview;
    ArrayList<QuestionsSet> questionsSets;
    ArrayList<QuestionsSet> tempquestionsSets;
   public static AddDeleteQuestionsFragment newInstance(String param1, String param2) {
        AddDeleteQuestionsFragment fragment = new AddDeleteQuestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddDeleteQuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        loadQuestionsSetFromPHP();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
       inflater.inflate(R.menu.search,menu);
        this.menu=menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                if(NavDrawerActivity.isShowingSearchEditText()) {
                    NavDrawerActivity.hideSearch();
                }
                else{
                    ((NavDrawerActivity)getActivity()).showSearch();

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
             sqLiteDatabase.execSQL("create table if not exists "+QUESTIONS_SETS_LIST_TABLE_1+" ("+C_SN+" integer primary key autoincrement, "+CODE+" varchar(50), "+NO_OF_QUESTIONS+" integer ); ");
            sqLiteDatabase.execSQL("create table if not exists "+TABLE_LIST_OF_CODE+" ("+C_SN+" integer primary key autoincrement, "+CODE+" varchar(50)); ");
            sqLiteDatabase.execSQL("create table if not exists "+QUESTION_TABLE+" ("+C_SN+" integer primary key autoincrement, "+CODE+" varchar(50), "+QUESTION+" varchar, "+
                    OPTION1+" varchar, "+OPTION2+" varchar, "+OPTION3+" varchar, "
                    +OPTION4+" varchar, "+CORRECT_OPTION+" integer); ");
        }
        catch (Exception e){
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
// 0x7FFF0035.


    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v= inflater.inflate(R.layout.fragment_add_delete_question, container, false);
        question_set_listview=(ListView) v.findViewById(R.id.questions_set_list);
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
        question_set_listview.setFocusable(true);
        NavDrawerActivity.getSearchEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isVisible()) {
                    filterString = s.toString();
                    tempquestionsSets = new ArrayList<QuestionsSet>();
                    for (QuestionsSet temp : questionsSets) {
                        if (temp.getQuestionsCode().toLowerCase().contains(filterString.toLowerCase()))
                            tempquestionsSets.add(temp);
                    }

                    listAdapt = new adapt(tempquestionsSets);
                    question_set_listview.setAdapter(listAdapt);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Empty_List_view=(TextView)v.findViewById(R.id.empty_list_TV);
       header=(TextView)v.findViewById(R.id.header);
    fab=(FloatingActionButton)v.findViewById(R.id.floating_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* FragmentManager fm = getFragmentManager();
                askQuestionCodeDialog dialog=new askQuestionCodeDialog();
                dialog.show(fm,"SHOWING");*/
                try {
                    FragmentManager fm = getFragmentManager();
                    savedQuestionCodeListDialog dialog = new savedQuestionCodeListDialog();
                    dialog.show(fm, "third button");
                }catch (Exception e){
                    //Log.d("TAG", "onClick: "+e.toString());
                }
            }
        });
        setHasOptionsMenu(true);
           hasdownloaddQueations=getActivity().getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE).getBoolean(HAS_DOWNLOADED_QUESTIONS,false);
          if(!hasdownloaddQueations) {
              loadQuestionsSetFromPHP();

          }else {
            String selectQuery = "SELECT * FROM " + QUESTIONS_SETS_LIST_TABLE_1;
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
              questionsSets = new ArrayList<>();
              if(cursor.getCount()>0) {
                  while (cursor.moveToNext())   {

        QuestionsSet q = new QuestionsSet();
        q.setQuestionsCode(cursor.getString(cursor.getColumnIndex(CODE)));
        q.setSizeOfSet(cursor.getInt(cursor.getColumnIndex(NO_OF_QUESTIONS)));
        questionsSets.add(q);
    }
}    listAdapt = new adapt(questionsSets);
     question_set_listview.setAdapter(listAdapt);
manageView(questionsSets);
          //  Toast.makeText(getActivity(),cursor.getCount()+"",Toast.LENGTH_SHORT).show();
        }

        return v;
    }


    private void loadQuestionsSetFromPHP()
    {
        questionsSets=new ArrayList<>();
        link=link.replace(" ","%20");
        int toShow=1;
        if(swipeRefreshLayout.isRefreshing())
            toShow=0;
        new PhpConnect(
                link,"Loading...",getActivity(),toShow,new String[]{"2568wwexve",Utilities.getDatabaseName(getActivity())},new String[]{"cmdxxx","dbName"}
        ).setListener(
                new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {

                      //  Toast.makeText(getActivity(), res, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jsonArray = (JSONArray) new JSONTokener(res).nextValue();
                            sqLiteDatabase.execSQL("delete from "+ QUESTIONS_SETS_LIST_TABLE_1);
                            if(jsonArray.length()==0){
                                Empty_List_view.setVisibility(View.VISIBLE);
                                Empty_List_view.setText("No Questions to Load");
                                header.setVisibility(View.GONE);
                                getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE,Context.MODE_PRIVATE).edit().putBoolean(HAS_DOWNLOADED_QUESTIONS,false).commit();
                            }
                            else
                            {
                                Empty_List_view.setVisibility(View.GONE);
                                getActivity().getSharedPreferences(PUBLIC_SHAREDPREFERENCE,Context.MODE_PRIVATE).edit().putBoolean(HAS_DOWNLOADED_QUESTIONS,true).commit();
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String code = jsonObject.getString("code");
                                int totalQuestions = jsonObject.getInt("totalQuestions");
                                QuestionsSet q = new QuestionsSet();
                                q.setSizeOfSet(totalQuestions);
                                q.setQuestionsCode(code);
                                ContentValues values = new ContentValues();
                                values.put(CODE,code);
                                values.put(NO_OF_QUESTIONS,totalQuestions);
                                //Log.d("TAG", "onConnectListener: "+sqLiteDatabase.insert(QUESTIONS_SETS_LIST_TABLE_1, null, values));;
                                questionsSets.add(q);
                            }
                           // Toast.makeText(getActivity(),cursor.getCount()+"",Toast.LENGTH_SHORT).show();
                            listAdapt = new adapt(questionsSets);
                            question_set_listview.setAdapter(listAdapt);
                            manageView(questionsSets);
                         //   showingDownloadSetList = true;

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.toString(),Toast.LENGTH_SHORT).show();
/*
                            questionsSets=new ArrayList<>();
                            listAdapt = new adapt(questionsSets);
                            questions_set_list.setAdapter(listAdapt);
                            showingDownloadSetList = true;
                            noSetsTV.setVisibility(View.VISIBLE);
                            noSetsTV.setText("Your institution has not uploaded any question sets.");
                            //Log.d("yaha xa", e.toString());*/
                        }
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }
        );
    }
    public  void downloadCode(){}

    private void manageView(ArrayList list){
        if(list.size()==0)
        {
            header.setVisibility(View.GONE);
            Empty_List_view.setVisibility(View.VISIBLE);
            Empty_List_view.setText("You have not added any question set. Click on the add button below to upload first question set.");
        }else {
            header.setVisibility(View.VISIBLE);
            Empty_List_view.setVisibility(View.GONE);
        }

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
            final ImageView v1=(ImageView)v.findViewById(R.id.download_questions_set);
            final TextViewPlus tvForFont=(TextViewPlus) v.findViewById(R.id.textForFont);
            final ProgressBar pb=(ProgressBar)v.findViewById(R.id.downloading_progress);
            final ImageView Popup=(ImageView)v.findViewById(R.id.popup);
            View.OnClickListener clickListener=new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            };
            View.OnClickListener clickListenerForMenu=new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popupMenu=new PopupMenu(getActivity(),Popup);
                    popupMenu.getMenuInflater().inflate(R.menu.delete_question_menu,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId()==R.id.delete_question_set){
                                String link=NEW_HOST+"deleting_questions_table.php";//databaseName
                                new PhpConnect(link,"Deleting"+q.getQuestionsCode()+"...",getActivity(),1,new String[]{CMDXXX,UtilitiesAdi.giveMeSN(getActivity(),Utilities.getDatabaseName(getActivity())),q.getQuestionsCode()},new String[]{"cmdxxx", "college_sn", "code"}).setListener(new PhpConnect.ConnectOnClickListener() {
                                    @Override
                                    public void onConnectListener(String res) {
                                        try {
                                            if (res.toString().equals("1")) {
                                                Toast.makeText(getActivity(), q.getQuestionsCode() + " deleted.", Toast.LENGTH_SHORT).show();
                                                sqLiteDatabase.delete(QUESTIONS_SETS_LIST_TABLE_1, CODE + " = ?", new String[] { q.getQuestionsCode()});
                                                questionsSets.remove(pos);
                                                notifyDataSetChanged();
                                                manageView(questionsSets);
                                            } else {
                                                Toast.makeText(getActivity(), "Unable to delete the question set " + q.getQuestionsCode()
                                                        , Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else if(item.getItemId()==R.id.view_question_set){
                                String getQuestionslink = HOST + "Android Call/getting_questions_table.php";
                                getQuestionslink = Utilities.encodeLinkSpace(getQuestionslink);
                                String dbname=Utilities.getDatabaseName(getActivity());
                                new PhpConnect(getQuestionslink,"Reading data...", getActivity(),1,
                                        new String[]{"2568wwexve",UtilitiesAdi.giveMeSN(getActivity(),dbname),dbname , questionsSets.get(pos).getQuestionsCode()},
                                        new String[]{"cmdxxx","college_sn", "dbName", "code"}).setListener(
                                        new PhpConnect.ConnectOnClickListener() {
                                            @Override
                                            public void onConnectListener(String res) {


                                                try {
                                                    Intent intent=new Intent(getActivity(),ViewQuestionActivity.class);
                                                    intent.putExtra(RESPONSEFROMNET,res);
                                                    intent.putExtra(SECTION_NAME,q.getQuestionsCode());
                                                    startActivity(intent);

                                                } catch (Exception e) {
                                                    //Log.d("errorQuestionsSet", e.toString());
                                                }


                                            }
                                        }
                                );
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            };
            Popup.setOnClickListener(clickListenerForMenu);
            v.setOnClickListener(clickListenerForMenu);

            v1.setVisibility(View.GONE);
tvForFont.setVisibility(View.VISIBLE);
            tvForFont.setText(q.getQuestionsCode().substring(0,1).toUpperCase());

            GradientDrawable  sd=(GradientDrawable) tvForFont.getBackground().mutate();
            sd.setColor(getResources().getColor(R.color.colorPrimary));
           tvForFont.setBackground(sd);
            t1.setText(q.getQuestionsCode());
            t2.setText(q.getSizeOfSet() + " questions");
            return v;
        }


    }
    public static class  savedQuestionCodeListDialog extends android.support.v4.app.DialogFragment {
        public static final String UNIT="unit";
        private ListView Lv;
      private   adapt adapt;
        private TextView header;
        private Button add_another_set;
        private EditText question_num,question_code;
        public static String EXTRA_TAG_INTEGER1="Integer1";
        public  static String EXTRA_TAG_INTEGER2="Integer2";
        private void setList(){
            try{
                list=new ArrayList();
                String[] columns=new String []{CODE};
                Cursor c=sqLiteDatabase.query(TABLE_LIST_OF_CODE,columns,null,null,null,null,null);
                //Toast.makeText(getActivity(),"reaches",Toast.LENGTH_SHORT).show();
                while(c.moveToNext()){
                    int i = c.getColumnIndex(CODE);
                    String code=c.getString(i);
                    list.add(code);
                    //    Toast.makeText(getActivity(),"done",Toast.LENGTH_SHORT).show();
                }
               adapt=new adapt(list);
                Lv.setAdapter(adapt);
                  manageHeader();
            }catch (Exception e){
                //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        private void manageHeader(){
            if (adapt.getCount()==0){
                header.setText("No Pending Questions");
                //dismiss();
                FragmentManager fm = getFragmentManager();
                askQuestionCodeDialog dialog=new askQuestionCodeDialog();
                dialog.setCancelable(false);
                dialog.show(fm,"SHOWING");
            }
        }
             @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SIS) {
            View v = inflater.inflate(R.layout.code_list_layout, null, false);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                 Lv=(ListView)v.findViewById(R.id.codeList);
                 add_another_set=(Button)v.findViewById(R.id.add_question_set_button);
                 add_another_set.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         FragmentManager fm = getFragmentManager();
                         askQuestionCodeDialog dialog=new askQuestionCodeDialog();
                         dialog.show(fm,"SHOWING");
                         dismiss();
                     }
                 });
                 header=(TextView)v.findViewById(R.id.header);
                 setList();
                     Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                             String code = parent.getAdapter().getItem(position).toString();
                             Intent intent = new Intent(getActivity(), ActivityForFragment.class);
                             intent.putExtra(ActivityForFragment.EXTRA_TAG_DATA3_1,0);
                             intent.putExtra(ActivityForFragment.EXTRA_TAG_DATA3_2, code);
                             intent.putExtra(ActivityForFragment.EXTRA_TAG_DATA3_3, true);
                             intent.putExtra(ActivityForFragment.EXTRA_TAG, 3);//index of this fragment arbitrary
                             startActivity(intent);
                             dismiss();
                         }
                     });
                 Lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                     @Override
                     public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                         return false;
                     }
                 });

            return v;
        }
        private class adapt extends ArrayAdapter<String> {
            private ArrayList<String> profile;

            public adapt(ArrayList<String> c) {
                super(getActivity(), 0, c);
                profile = c;
            }

            @Override
            public View getView(final int pos, View v, ViewGroup vg) {
                if (v == null) {
                    v = getActivity().getLayoutInflater().inflate(R.layout.code_list, null);
                }
               final String code=getItem(pos).toString();
                ImageView delete=(ImageView)v.findViewById(R.id.deletecode);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            sqLiteDatabase.delete(TABLE_LIST_OF_CODE,CODE+"= ? ",new String[]{code});
                            try {
                                sqLiteDatabase.delete(QUESTION_TABLE, CODE + "= ? ", new String[]{code});
                                Toast.makeText(getActivity(),code+" deleted",Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e){
                                //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                        }
                        setList();
                    }
                });
                TextView tv=(TextView)v.findViewById(R.id.code);
                tv.setText(getItem(pos)+"");
                return v;
            }

        }
    }

}
