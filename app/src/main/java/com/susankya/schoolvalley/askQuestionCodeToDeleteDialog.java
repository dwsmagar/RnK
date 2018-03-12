package com.susankya.schoolvalley;

import android.os.Bundle;
import android.view.LayoutInflater;
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

public class askQuestionCodeToDeleteDialog extends android.support.v4.app.DialogFragment implements FragmentCodes {
    public static final String UNIT="unit";
    private EditText question_num,question_code;
    private Button ok,cancel;
    public static String EXTRA="Extra Extra Extra";
    private ListView codeList ;
    private ArrayList<questionDetail> questionDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SIS) {
        View v = inflater.inflate(R.layout.question_code_delete_dialog, null, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        codeList=(ListView)v.findViewById(R.id.code_list);
        final TextView heading=(TextView)v.findViewById(R.id.heading);
        final ProgressBar pb=(ProgressBar)v.findViewById(R.id.progress);
        if(Utilities.isConnectionAvailable(getActivity()))
          pb.setVisibility(View.VISIBLE);
        String link=NEW_HOST+"RetrieveQuestionCode.php";//Utilities.getDatabaseName(getActivity())
        new PhpConnect(link,"Loading...",getActivity(),0,new String[]{CMDXXX,Utilities.getDatabaseName(getActivity())},new String[]{"cmdxxx","dbName"}).setListener(new PhpConnect.ConnectOnClickListener() {
            @Override
            public void onConnectListener(String res) {
                try {
                    pb.setVisibility(View.GONE);
                    questionDetails = new ArrayList<questionDetail>();
                    JSONArray JA = (JSONArray) new JSONTokener(res).nextValue();
                    for (int i = 0; i < JA.length(); i++) {
                        questionDetail QD = new questionDetail();
                        JSONObject job = JA.getJSONObject(i);
                        QD.setCode(job.getString("code"));
                        QD.setNumOfQues(job.getString("totalQuestions"));
                        questionDetails.add(QD);
                    }
                    adapt adapter = new adapt(questionDetails);
                    codeList.setAdapter(adapter);
                    if(questionDetails.size()==0){
                        heading.setText("No Questions to Laod");
                    }
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        });
        codeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                questionDetail qd = (questionDetail) parent.getAdapter().getItem(position);
               final String y=qd.getCode();
                if(y.length()!=0) {
                                       String link=NEW_HOST+"deleting_questions_table.php";//databaseName
                                     new PhpConnect(link,"Deleting...",getActivity(),1,new String[]{CMDXXX,Utilities.getDatabaseName(getActivity()),y},new String[]{"cmdxxx", "dbName", "code"}).setListener(new PhpConnect.ConnectOnClickListener() {
                                         @Override
                                         public void onConnectListener(String res) {
                                             try {
                                                 if (res.toString().equals("1")) {
                                                     Toast.makeText(getActivity(), y + " deleted.", Toast.LENGTH_SHORT).show();
                                                 } else {
                                                     Toast.makeText(getActivity(), "Unable to delete the question set " + y, Toast.LENGTH_SHORT).show();
                                                 }
                                             } catch (Exception e) {
                                                 //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                             }
                                           getDialog().dismiss();
                                         }
                    });
               /*     Intent i=new Intent(getActivity(),ActivityForFragment.class);
                    i.putExtra(ActivityForFragment.EXTRA_TAG_DATA4,y);
                    i.putExtra(ActivityForFragment.EXTRA_TAG,4);//arbitrary index for this fragment
                    startActivity(i);*/

                }
                else{
                    Toast.makeText(getActivity(), "Please enter valid data", Toast.LENGTH_SHORT).show();
                }
                //getDialog().dismiss();
            }
        });

        return v;
    }

    public void onActivityCreated(Bundle SIS){
        super.onActivityCreated(SIS);
    }

    private class adapt extends ArrayAdapter<questionDetail> {

        public adapt(ArrayList<questionDetail> c) {
            super(getActivity(), 0, c);
        }

        @Override
        public View getView(final int pos, View v, ViewGroup vg) {
            if (v == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.code_list, null);
            }
            questionDetail qd=getItem(pos);
            ((TextView)v.findViewById(R.id.numOfQuestion)).setText("Number of questions: "+qd.getNumOfQues()+"");
            final String code=qd.getCode().toString();
            ImageView delete=(ImageView)v.findViewById(R.id.deletecode);
            delete.setVisibility(View.INVISIBLE);
            TextView tv=(TextView)v.findViewById(R.id.code);
            tv.setText("Code/Topic: " +code+"");
            return v;
        }

    }
    public class questionDetail {
        private String code,numOfQues;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getNumOfQues() {
            return numOfQues;
        }

        public void setNumOfQues(String numOfQues) {
            this.numOfQues = numOfQues;
        }
    }
}