package com.susankya.schoolvalley;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ResultActivity extends AppCompatActivity {
public static final String INDEX="index of fragment to load.";
    public static final String RESULT ="linkResult of php page to load.";
    public static final String NAME="Name";
    public static final String ROLL="Roll";
    public static final String ROLLVALUE="Rollvalue";
    private String res,name,colName,rollValue;
    public static final String CLASS="CLASS nae hola ni";
Toolbar toolbar;
    private EditText search;
    private int searchedRoll;
    private boolean clickedOnMenuItem=false;
public static ListView lv;
    public static ArrayList<Result> arrayList;

    public static void setListView(ListView listView,ArrayList<Result> r)
    {
        lv=listView;
        arrayList=r;

    }

    public void changeListViewPos()
    {
        Result r;

        if(search.getText().toString().trim().isEmpty())
        {
            r=null;
        }
        else
        {
            r=getResult(Integer.parseInt(search.getText().toString()));

            if(r!=null)
            {
                //lv.smoothScrollToPosition(r.getPos());
                lv.setSelection(r.getPos());
                //Log.d("xyz", r.getPos() + "");

            }
            else
            {
                Toast.makeText(ResultActivity.this, "Roll number not found.", Toast.LENGTH_SHORT).show();


            }
        }

       // ResultActivity.this.getSupportActionBar().setDisplayShowCustomEnabled(false);
        //ResultActivity.this.getSupportActionBar().setDisplayShowTitleEnabled(true);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);

    }
    public void changeActionBar()
    {
        clickedOnMenuItem=true;
        final ActionBar actionBar=getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbarview);
       search = (EditText) actionBar.getCustomView().findViewById(
                R.id.searchfield);
        if(search.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }


        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {


                if(actionId== EditorInfo.IME_ACTION_DONE)
                {

                    clickedOnMenuItem=false;
                    changeListViewPos();
                    return true;
                }

                return false;
            }
        });
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
      //  setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

      // getSupportActionBar().setDisplayShowHomeEnabled(true);
       if(getIntent()!=null){
            res =getIntent().getStringExtra(RESULT);
            name =getIntent().getStringExtra(NAME);
           colName=getIntent().getStringExtra(ROLL);
           rollValue=getIntent().getStringExtra(ROLLVALUE);

        }
        setTitle("Result for "+ colName.replace("_"," ")+" "+rollValue);
       final android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
        android.support.v4.app.Fragment fragment;
        fragment=  SingleResultFragment.newInstance(colName,rollValue,res);
        //setTitle("Result");
        fm.beginTransaction().replace(R.id.resultContainer, fragment)
                .commit();


    }


    public static Result getResult(int roll)
    {
        for(Result r:arrayList)
        {
            if(r.getRoll()==roll)
            {
                //Toast.makeText(ResultActivity.this,roll+"",Toast.LENGTH_LONG).show();
                //Log.d("abcd", roll + "");
                return r;
            }
        }
        return null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
   // getMenuInflater().inflate(R.menu.menu_search_result, menu);
       return true;
    }
    public static class SearchResultDialog extends DialogFragment implements FragmentCodes {


        private int roll;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return super.onCreateDialog(savedInstanceState);

        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            this.dismiss();
        }


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View v=inflater.inflate(R.layout.dialog_search_result,null);

            final EditText editTextRoll=(EditText)v.findViewById(R.id.search_result);
            Button searchButton=(Button)v.findViewById(R.id.search_button);
            searchButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!editTextRoll.getText().toString().trim().isEmpty())
                            {
                                 roll= Integer.parseInt(editTextRoll.getText().toString());
                                if(getResult(roll)!=null)
                                {
                                    lv.smoothScrollToPosition(getResult(roll).getPos());
                                    getDialog().cancel();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Roll number not found.", Toast.LENGTH_SHORT).show();
                                    editTextRoll.setText("");

                                }

                            }
                            else editTextRoll.setError("Enter a valid roll number");




                        }
                    }
            );
             return  v;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            getDialog().dismiss();
            //   Toast.makeText(getActivity(),""+resultCode,Toast.LENGTH_SHORT).show();
            if(resultCode== Activity.RESULT_OK){
                try {
                    //  Toast.makeText(getActivity(),"got intent",Toast.LENGTH_SHORT).show();
                    Bundle b = new Bundle();
                    b.putString("name", data.getStringExtra("name"));
                    b.putInt("roll", data.getIntExtra("roll", 1));
                    b.putInt("class", data.getIntExtra("class", 11));
                    b.putString("exam", "first");
                    b.putString("result", data.getStringExtra("result"));
                    b.putStringArray("values", data.getStringArrayExtra("values"));
                    Fragment fragment = new ViewResultFragment();
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                }
                catch (Exception e){
                    //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
