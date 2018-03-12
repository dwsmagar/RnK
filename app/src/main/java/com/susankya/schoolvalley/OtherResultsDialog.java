package com.susankya.schoolvalley;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONTokener;

/**
 * Created by ajay on 10/28/2015.
 */
public class OtherResultsDialog extends DialogFragment implements FragmentCodes {
private PhpConnect getResult;

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

    public int getClassValue(String s)
    {
        if(s.equals("Class 11"))
            return 11;
        else
            return 12;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.other_results,null);
        getDialog().getWindow().setTitle("Results list of all students");
        final Intent i=new Intent(getActivity(),ResultActivity.class);
       final   Button b=(Button)v.findViewById(R.id.allResult);
        final Button monthlyTestResult=(Button)v.findViewById(R.id.monthlyTestResult);
        Button objectiveResult=(Button)v.findViewById(R.id.objectiveResult);
        objectiveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            i.putExtra(ResultActivity.INDEX,2);
                 String link = HOST+"ExtractTheResult/ExtractTheResultOBJ.php?class="+11;
                getResult=new PhpConnect(link,"Loading",getActivity(),1);
                getResult.setListener(new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        int error=0;
                        JSONArray jsonArray=null;
                        try
                        {

                            jsonArray=(JSONArray) new JSONTokener(res).nextValue();
                        }
                        catch (Exception e)
                        {
                            error=1;
                        }
                        if(!jsonArray.isNull(0))
                        {
                            i.putExtra(ResultActivity.RESULT, res);
                            startActivityForResult(i, 0);
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Result not published yet",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }

                    }
                });
       }
        });
        final LinearLayout linearLayout=(LinearLayout)v.findViewById(R.id.select11_12);
        final LinearLayout linearLayoutMT=(LinearLayout)v.findViewById(R.id.select11_12MT);
        Button MT11=(Button)v.findViewById(R.id.MTResult11);
        Button MT12=(Button)v.findViewById(R.id.MTResult12);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                b.setVisibility(View.INVISIBLE);
            }
        });
        monthlyTestResult.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayoutMT.setVisibility(View.VISIBLE);
                        monthlyTestResult.setVisibility(View.INVISIBLE);
                    }
                }
        );

        View.OnClickListener listenerMT=new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b=(Button)v;
                int Class=getClassValue(b.getText().toString().trim());
              i.putExtra(ResultActivity.INDEX,1);
                i.putExtra(ResultActivity.CLASS,Class);
                String link = HOST+"ExtractTheResult/ExtractTheResultMT.php?class="+Class;
                getResult=new PhpConnect(link,"Loading",getActivity(),1);
            getResult.setListener(new PhpConnect.ConnectOnClickListener() {
                @Override
                public void onConnectListener(String res) {
                    int error=0;
                    JSONArray jsonArray=null;
                    try
                    {

                       jsonArray=(JSONArray) new JSONTokener(res).nextValue();
                    }
                    catch (Exception e)
                    {
                        error=1;
                    }
                    if(!jsonArray.isNull(0))
                    {
                        i.putExtra(ResultActivity.RESULT, res);
                        startActivityForResult(i, 0);
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Result not published yet",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            });
           }
        };

        MT11.setOnClickListener(listenerMT);
        MT12.setOnClickListener(listenerMT);
        View.OnClickListener listener=new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b=(Button)v;
                int Class=getClassValue(b.getText().toString().trim());
                i.putExtra(ResultActivity.INDEX,0);
                i.putExtra(ResultActivity.CLASS,Class);
                String link = HOST+"ExtractTheResult/ExtractTheResultModified.php?class="+Class;
                getResult=new PhpConnect(link,"Loading",getActivity(),1);
                getResult.setListener(new PhpConnect.ConnectOnClickListener() {
                    @Override
                    public void onConnectListener(String res) {
                        int error=0;
                        JSONArray jsonArray=null;
                        try
                        {

                            jsonArray=(JSONArray) new JSONTokener(res).nextValue();
                        }
                        catch (Exception e)
                        {
                            error=1;
                        }
                        if(!jsonArray.isNull(0))
                        {
                            i.putExtra(ResultActivity.RESULT, res);
                            startActivityForResult(i, 0);
                        }
                        else
                        {

                            Toast.makeText(getActivity(),"Result not published yet",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }

                    }
                });

           }
        };

        Button class11=(Button)v.findViewById(R.id.allResult11);
        Button class12=(Button)v.findViewById(R.id.allResult12);
        class12.setOnClickListener(listener);
        class11.setOnClickListener(listener);
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
                //Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
