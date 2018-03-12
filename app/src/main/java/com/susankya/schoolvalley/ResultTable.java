package com.susankya.schoolvalley;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultTable extends android.support.v4.app.Fragment implements FragmentCodes {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
private String nameOfColumn,resultString;
private String[] columnHeading;

    private static ViewGroup mContainerViewForRow,mContainerViewForResult;
    private  int noOfColumn;
    private int placeholderWidth,valueWidth;
    private ResultAdapter resultAdapter;
    private ResultAdapterAgain resultAdapterAgain;
   private ArrayList<String[]> listOfAllResult,temp;
    private String mParam1,rollColumn,rollnum;
    private EditText search;
    private int index;
    private EditText report;
    private int[] columnWidth;
    private ArrayList<result> results;
    private FloatingActionButton searchResult;
    private boolean clickedOnMenuItem=false;
    private  float[] columnWidthinPx;
private ListView resultList;
    public static ResultTable newInstance(String link,String rollColumn,String roll) {
        ResultTable fragment = new ResultTable();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, link);
        args.putString(ARG_PARAM2, rollColumn);
        args.putString(ARG_PARAM3, roll);
         fragment.setArguments(args);
        return fragment;
    }

    public ResultTable() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            rollColumn = getArguments().getString(ARG_PARAM2);
            rollnum = getArguments().getString(ARG_PARAM3);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
      //  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onResume() {
        super.onResume();
     //   getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


      View v= inflater.inflate(R.layout.fragment_result_table, container, false);
        View u= inflater.inflate(R.layout.horizontal_table_holder, container, false);
        mContainerViewForRow = (ViewGroup)u.findViewById(R.id.container);
        mContainerViewForResult = (ViewGroup)v.findViewById(R.id.container);
        resultList=(ListView)v.findViewById(R.id.list_result);
     // report= ((EditText)v.findViewById(R.id.text));
               //((EditText)v.findViewById(R.id.text)).setText(mParam1);
        searchResult=(FloatingActionButton)v.findViewById(R.id.floating_add_button);
        searchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResultDialog dialog=new SearchResultDialog();
                dialog.show(getFragmentManager(),"show");
            }
        });
        results=new ArrayList<>();
        try {
        JSONObject job= new JSONObject(mParam1);
        noOfColumn=job.getInt("no_of_columns");
        nameOfColumn=job.getString("columns");
        resultString=job.getString("datas");
            columnHeading=new String[noOfColumn];
            JSONArray columnHeaderArray=new JSONArray(nameOfColumn);
            JSONArray resultData=new JSONArray(resultString);
            listOfAllResult=new ArrayList<>();
            columnWidth=new int[noOfColumn];
            for (int i=noOfColumn-1;i>=0;i--){
                  columnHeading[i]=(String)columnHeaderArray.get(i);
                if(columnHeading[i].equals(rollColumn))
                    index=i;
                columnWidth[i]=columnHeading[i].length();
            }


      // Toast.makeText(getActivity(), "" + resultData.length(), Toast.LENGTH_LONG).show();
           for(int i=0;i<resultData.length();i++){
               JSONObject jobForResult=resultData.getJSONObject(i);
               String[] resultRow=new String[noOfColumn];
              for (int counter=0;counter<noOfColumn;counter++){
               resultRow[counter]=jobForResult.getString(columnHeading[counter]);
               }
               listOfAllResult.add(resultRow);
           }
            assighColumnWidth();
            makeRow(noOfColumn);
            temp=new ArrayList<>();
            mContainerViewForResult.addView(mContainerViewForRow, 0);
            for(String[] row:listOfAllResult){
                placeholderWidth=valueWidth=0;
                for(int i=0;i<noOfColumn;i++){
                    result r=new result();
                    r.setPlaceholder(columnHeading[i]);
                    r.setValue(row[i]);
                    results.add(r);
                    if (r.getPlaceHOlderSize()>placeholderWidth)
                        placeholderWidth=r.getPlaceHOlderSize();
                    if(r.getValueSize()>valueWidth)
                        valueWidth=r.getValueSize();
                }
            }
            resultAdapterAgain =new ResultAdapterAgain(results);
          resultList.setAdapter(resultAdapterAgain);
         }
        catch (Exception e)
        {
            //Toast.makeText(getActivity(), "here" + e.toString(), Toast.LENGTH_LONG).show();
        }
        return v;
    }

    public void changeListViewPos()
    {
 String s=search.getText().toString().trim();
        if(s.isEmpty())
        {
            return;
        }
        else
        {
int i=getPosition(s);
            if(i!=-1)
            {
resultList.setSelection(i);
                ((LinearLayout)resultList.getAdapter().getItem(i)).setBackgroundDrawable(getResources().getDrawable(R.drawable.black));
            }
            else
            {
                //Toast.makeText(getActivity(), "Roll number not found.", Toast.LENGTH_SHORT).show();
            }
        }

        getActivity().getActionBar().setDisplayShowCustomEnabled(false);
       getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);

    }
    public int getPosition(String s){
    int result=-1;
        for (int k=0;k<2;k++){
        for(int j=0;j<noOfColumn;j++){
        for(int i=0;i<listOfAllResult.size();i++){
            String[] str=listOfAllResult.get(i);
            if(k==0){
            if(str[j].trim().toLowerCase().equals(s.trim().toLowerCase())){
                result= i;
            break;
            }}else {
                if(str[j].trim().toLowerCase().contains(s.trim().toLowerCase())){
                    result= i;
                    break;
                }}

        }
        if(result!=-1)
        break;
        }
            if(result!=-1)
                break;
        }
        return  result;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_roll) {
            SearchResultDialog dialog=new SearchResultDialog();
            dialog.show(getFragmentManager(),"show");
/*
            if(clickedOnMenuItem)
            {
                changeListViewPos();
                clickedOnMenuItem=false;
            }
            else

                changeActionBar();*/

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
private class ResultAdapter extends ArrayAdapter<String[]> {
    private ViewGroup viewGroup;
    public ResultAdapter(ArrayList<String[]> c){
        super(getActivity(),0,c);
    }
    @Override
    public View getView(int pos,View v,ViewGroup vg){
        try {
            if (v == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.infinite_long_row, null);
            }

            String[] values=getItem(pos);

            viewGroup = (ViewGroup)v.findViewById(R.id.container);
        for (int i=0;i<noOfColumn;i++) {
         TextView tv=(TextView) viewGroup.getChildAt(i);
           tv.setVisibility(View.VISIBLE);
            tv.setText(values[i]);
            tv.setWidth((int)columnWidthinPx[i]);
        }
            return v;
        }
    catch (Exception e){
    //Toast.makeText(getActivity(), pos + e.toString(), Toast.LENGTH_SHORT).show();
    }
        return v;
}
}private class ResultAdapterAgain extends ArrayAdapter<result> {
    private ViewGroup viewGroup;
    public ResultAdapterAgain(ArrayList<result> c){
        super(getActivity(),0,c);
    }
    @Override
    public View getView(int pos,View v,ViewGroup vg){
        try {
            if (v == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.result_table_row, null);
            }
            float density =getActivity().getResources().getDisplayMetrics().density;
            result values=getItem(pos);
            TextView placeholder=(TextView) v.findViewById(R.id.placeholder);
            TextView value=(TextView)v.findViewById(R.id.value);
            placeholder.setText(values.getPlaceholder());
            value.setText(values.getValue());
            placeholder.setWidth((int)(placeholderWidth*15*density));
            value.setWidth((int)(valueWidth*15*density));
             return v;

        }
    catch (Exception e){
    //Toast.makeText(getActivity(), pos + e.toString(), Toast.LENGTH_SHORT).show();
    }
        return v;
}
}
    private void assighColumnWidth(){
        columnWidthinPx=new float[noOfColumn];
        float density =getActivity().getResources().getDisplayMetrics().density;
         //  Toast.makeText(getActivity(), density + "", Toast.LENGTH_SHORT).show();
             for(int i=0;i<listOfAllResult.size();i++){
            String[] columns=listOfAllResult.get(i);
            for (int j=0;j<noOfColumn;j++){
                if(columns[j].toString().length()>columnWidth[j])
                    columnWidth[j]=columns[j].toString().length();
         columnWidthinPx[j]=columnWidth[j]*density*15;
            }
        }
    }
    private void makeRow(int numOfColumns) {
        try{
        for(int i=noOfColumn-1;i>=0;i--){
            final View newView = (View) LayoutInflater.from(getActivity()).inflate(
                 R.layout.single_textview, mContainerViewForRow, false);
            TextView column=(TextView)newView.findViewById(R.id.textView);
            column.setText(columnHeading[i]);
          //  column.setWidth(columnWidth[i]*25);
            column.setWidth((int)columnWidthinPx[i]);
            mContainerViewForRow.addView(newView,0);
        }}
        catch (Exception e) {
         //   report.setText(e.toString());
            //Toast.makeText(getActivity(), 1 + "  " + e.toString(), Toast.LENGTH_LONG).show();
            }
    }
    public  class SearchResultDialog extends DialogFragment implements FragmentCodes {


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
          //  editTextRoll.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            Button searchButton=(Button)v.findViewById(R.id.search_button);
            searchButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!editTextRoll.getText().toString().trim().isEmpty())
                            {
                               int position = getPosition(editTextRoll.getText().toString());
                                if(position!=-1)
                                {
                                    resultList.setSelection(position);
                                    getDialog().cancel();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "No result found.", Toast.LENGTH_SHORT).show();
                                    editTextRoll.setText("");
                                    getDialog().cancel();

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
    class result{
        public result(){
            placeholder="new";
            value="new";
        }

        public String getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
public  int getPlaceHOlderSize(){
    return placeholder.length();
}
public  int getValueSize(){
    return value.length();
}
        private String placeholder,value;

    }

}
