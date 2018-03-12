package com.susankya.schoolvalley;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class ViewQuestionListDialog extends android.support.v4.app.DialogFragment {
    public static final String UNIT="unit";
    private EditText question_num,question_code;
    private Button ok,cancel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SIS) {
        View v = inflater.inflate(R.layout.view_question_list_dialog, null, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //  getDialog().getWindow().setTitle("Add Question:");
        question_code=(EditText)v.findViewById(R.id.ques_code);
        ok=(Button)v.findViewById(R.id.ok_button);
        cancel=(Button)v.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavDrawerActivity) getActivity()).onSectionAttached(((NavDrawerActivity)getActivity()).getPreviousIndex());
                ((NavDrawerActivity)getActivity()).setIndex(((NavDrawerActivity)getActivity()).getPreviousIndex());
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             NavDrawerActivity.setIsFromDialog(true);
                String code=question_code.getText().toString();
                if(code.isEmpty()) {
                    question_code.setError("Enter a valid questions code");

                }
                else {
                    if(connectToPHP())
                    {
                     try{
                        dismiss();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        Bundle args=new Bundle();
                        args.putString("code",code.trim());
                        //Log.d("codd",code);

                        Fragment fragment =new questionListFragment();
                        fragment.setArguments(args);
                         fm.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    }catch(Exception e){
                        //Toast.makeText(getActivity(), e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                  dismiss();

                }

            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(!NavDrawerActivity.isIsFromHome()) {
            ((NavDrawerActivity) getActivity()).onSectionAttached(((NavDrawerActivity) getActivity()).getPreviousIndex());
            ((NavDrawerActivity) getActivity()).setIndex(((NavDrawerActivity) getActivity()).getPreviousIndex());
        }}

    private boolean connectToPHP()
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
           return true;

        } else {
            Toast.makeText(getActivity().getApplicationContext().getApplicationContext(), "NO CONNECTION", Toast.LENGTH_SHORT).show();
            return false;

        }

    }
    public void onActivityCreated(Bundle SIS){
        super.onActivityCreated(SIS);
    }


}