package com.susankya.rnk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class  askQuestionCodeDialog extends android.support.v4.app.DialogFragment implements FragmentCodes {
    public static final String UNIT="unit";
    private EditText question_num,question_code;
    public static String EXTRA_TAG_INTEGER1="Integer1";
    public  static String EXTRA_TAG_INTEGER2="Integer2";
    private Button ok,cancel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SIS) {
        View v = inflater.inflate(R.layout.fragment_ask_question_code, null, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

      //  getDialog().getWindow().setTitle("Add Question:");
       question_code=(EditText)v.findViewById(R.id.ques_code);
        question_num=(EditText)v.findViewById(R.id.num_of_ques);
        ok=(Button)v.findViewById(R.id.ok_button);
        cancel=(Button)v.findViewById(R.id.cancel_button);
       cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=question_num.getText().toString();
                String code=question_code.getText().toString();
                if(question_code.getText().toString().isEmpty()||question_num.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=new Intent(getActivity(),ActivityForFragment.class);
                   intent.putExtra(ActivityForFragment.EXTRA_TAG_DATA3_1, Integer.parseInt(question_num.getText().toString()));
                    intent.putExtra(ActivityForFragment.EXTRA_TAG_DATA3_2,question_code.getText().toString().toUpperCase());
                    intent.putExtra(ActivityForFragment.EXTRA_TAG_DATA3_3,false);
                    intent.putExtra(ActivityForFragment.EXTRA_TAG,3);//index of this fragment arbitrary
                    startActivity(intent);
                     dismiss();
                }
            }
        });
        return v;
    }


}
