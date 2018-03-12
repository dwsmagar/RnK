package com.susankya.schoolvalley;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class HelpActivity extends AppCompatActivity implements FragmentCodes{
private ViewPager viewPager;
    private Button next,pre;
   private  SharedPreferences sp;
    private void manageButton(){
        if(viewPager.getCurrentItem()==0){
            pre.setClickable(false);
        pre.setText("");
        }
        else{
            pre.setClickable(true);
            pre.setText("Previous");

        }
        if(viewPager.getCurrentItem()==viewPager.getAdapter().getCount()-1){
            next.setClickable(false);
        }
        else
            next.setClickable(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle("Help");
    sp=getSharedPreferences(PUBLIC_SHAREDPREFERENCE, Context.MODE_PRIVATE);
    viewPager=(ViewPager)findViewById(R.id.viewPager);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return HelpFragment.newInstance(position + 1);
            }

            @Override
            public int getCount() {
                if(sp.getBoolean(HAS_VISITED,false))
                return 3;
                else
                    return 4;
            }


        });
       pre=(Button)findViewById(R.id.previous);
        next=(Button)findViewById(R.id.next);
        manageButton();
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.setClickable(true);
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                //  manageButton();
                next.setText ((viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1)
                        ? ""
                        : "Next");manageButton();
            }
        });      next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pre.setClickable(true);
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                manageButton();
if(next.getText().toString().equals("")){

   next.setClickable(false);
}
                next.setText ((viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1)
                        ? ""
                        : "Next");
            }
        });
    }


}
