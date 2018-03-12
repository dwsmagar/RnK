package com.susankya.schoolvalley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


public class questionsListOffline extends android.support.v4.app.Fragment implements FragmentCodes {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private   JSONArray listOfCode;
private ListView list;
    private String questionCode;
private ArrayList<String> arrayListStr;
    private ArrayList<Integer> arrayListInt;
    private  ArrayList<String> jsonArrayListString;
    private String mParam1;
private int ListLayout;
    private adapt adapter;
    public static questionsListOffline newInstance(String param1, String param2) {
        questionsListOffline fragment = new questionsListOffline();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public questionsListOffline() {
        mParam1="a";

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
setHasOptionsMenu(true);
        this.setMenuVisibility(true);
          arrayListStr=new ArrayList();
        arrayListInt=new ArrayList();
        jsonArrayListString=new ArrayList();
        }
    @Override
    public void onDestroyOptionsMenu() {
        //      Toast.makeText(getActivity(),"CALLED",Toast.LENGTH_LONG).show();
        this.setMenuVisibility(false);
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onDestroyView() {
        onDestroyOptionsMenu();
        super.onDestroyView();
    }

    private void arrayMaker(){
       listOfCode=new JSONArray();
       listOfCode=load(questionListFragment.FILENAME);
       try {
           for (int i = 0; i < listOfCode.length(); i++) {
               String FileName=null;
               JSONObject jo = listOfCode.getJSONObject(i);
               FileName=jo.getString(questionListFragment.QUESTION_CODE);
               arrayListStr.add(FileName);
               JSONArray questionAnswerArray=load(FileName);
               arrayListInt.add(questionAnswerArray.length());
               jsonArrayListString.add(questionAnswerArray.toString());
           }
       }
       catch (Exception e){
           //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
       }
   }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_screen_slide,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                NavDrawerActivity.setIsFromHome(true);
                FragmentManager fm = getFragmentManager();
                ViewQuestionListDialog dialog=new ViewQuestionListDialog();
                dialog.show(fm,"SHOWING");
        break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startActivityForFragment(int position){

            Intent i=new Intent(getActivity(),ActivityForFragment.class);
        try {
            i.putExtra(CODE, listOfCode.getJSONObject(position).getString(questionListFragment.QUESTION_CODE).toString());
        } catch (JSONException e) {
            //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        i.putExtra(ActivityForFragment.EXTRA_TAG_DATA2,jsonArrayListString.get(position));
            i.putExtra(ActivityForFragment.EXTRA_TAG,2);
            startActivity(i);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setMenuVisibility(true);
  View v= inflater.inflate(R.layout.fragment_questions_list_offline, container, false);
   list=(ListView)v.findViewById(R.id.list);
        TextView tv=(TextView)v.findViewById(R.id.emptyView);
                list.setEmptyView(tv);
       // registerForContextMenu(list);
        arrayListStr.clear();
        arrayListInt.clear();
        jsonArrayListString.clear();
        arrayMaker();
        try {
            adapter = new adapt(arrayListStr);
            list.setAdapter(adapter);

        }
        catch (Exception e){
// Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos =position;
                final SharedPreferences sp=getActivity().getSharedPreferences("Public",Context.MODE_PRIVATE);
               AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Test");
                builder1.setMessage("Would you like to take a test?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sp.edit().putBoolean(isTest,true).apply();
                                startActivityForFragment(pos);
                            }
                        });   builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sp.edit().putBoolean(isTest,false).apply();
                                startActivityForFragment(pos);
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
return v;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        adapt adapter = (adapt)getListAdapter();
        switch (item.getItemId()) {
            case R.id.menu_item_delete_notice:{
                try {
                    JSONObject job=listOfCode.getJSONObject(position);
                    String FileName=job.getString(questionListFragment.QUESTION_CODE);
                    DeleteFromListFSaveQuestionCode(job);
                    getActivity().getApplicationContext().deleteFile(FileName);
                    arrayListStr.clear();
                    arrayListInt.clear();
                    jsonArrayListString.clear();
                    arrayMaker();
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e){

                }
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.delete_file_context, menu);
    }

    public Object getListAdapter() {
        return adapter;
    }

    private class adapt extends ArrayAdapter<String> {
        ArrayList<String> param;
        public adapt(ArrayList<String> c){
            super(getActivity(),0,c);
            param=c;
        }
        @Override
        public View getView(final int pos,View v,ViewGroup vg){
            if(v==null){
                v=getActivity().getLayoutInflater().inflate(R.layout.question_list_item_offline,null);
            }
            TextView t1=(TextView)v.findViewById(R.id.code);
            TextView t2=(TextView)v.findViewById(R.id.num);
            t1.setText(param.get(pos));
           t2.setText(Integer.toString(arrayListInt.get(pos)));
            ImageView delete=(ImageView)v.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setTitle("Delete");
                        builder1.setMessage("Are you sure you want to delete this file?");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        String FileName = null;
                                        try {
                                            JSONObject job = listOfCode.getJSONObject(pos);
                                            FileName = job.getString(questionListFragment.QUESTION_CODE);
                                            DeleteFromListFSaveQuestionCode(job);
                                            getActivity().getApplicationContext().deleteFile(FileName);
                                            arrayListStr.clear();
                                            arrayListInt.clear();
                                            jsonArrayListString.clear();
                                            arrayMaker();
                                            adapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            //Toast.makeText(getActivity(),"Could not delete file.",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });   builder1.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        return;
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    } catch (Exception e) {
//Toast.makeText(getActivity(),"Could not delete file.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return v;
        }

    }

    public boolean DeleteFromListFSaveQuestionCode(JSONObject job)
    {
        Writer writer = null;
        try {
            JSONArray JA= load(questionListFragment.FILENAME);
            JSONArray temp=new JSONArray();
            for(int i=0;i<JA.length();i++){
                if(JA.getJSONObject(i).toString().equals(job.toString()))
                {
                    continue;
                }
                else
                {
                    temp.put(JA.getJSONObject(i));
                }
            }
            OutputStream out = getActivity().getApplicationContext()
                    .openFileOutput(questionListFragment.FILENAME, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(temp.toString());
            Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT).show();
            return true;
        }
        catch(Exception e ){
           // Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            return false;
        }
        finally {
            if (writer != null)
                try {
                    writer.close();
                }
                catch (Exception e){
                    //Toast.makeText(getActivity(),"Error closing file",Toast.LENGTH_SHORT).show();
                }
        }}
    public JSONArray load(String mFilename)
    {
        String line=null;
   JSONArray array=new JSONArray();
        BufferedReader reader = null;
        try {
            // Open and read the file into a StringBuilder
            InputStream in = getActivity().getApplicationContext().openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
              array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

        }
        catch (Exception e) {
            //Toast.makeText(getActivity(), "Could not find file", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                }
                catch (Exception e){
                   // Toast.makeText(getActivity(), "Error closing file", Toast.LENGTH_SHORT).show();
                }
        }
        return array;
    }
 /*   @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        inflater.inflate(R.menu.ofline_result_list, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }*/
}
