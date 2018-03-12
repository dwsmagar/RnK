package com.susankya.schoolvalley;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AccountTypeActivity extends AppCompatActivity implements FragmentCodes {

    private static final String SAVING_STATE_SLIDER_ANIMATION = "SliderAnimationSavingState";
    private boolean isSliderAnimation = false;
    private static SQLiteDatabase db;
    @BindView(R.id.student)
    AppCompatRadioButton student;
    @BindView(R.id.teacher)
    AppCompatRadioButton teacher;
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.rootV)
    View v;
    @BindView(R.id.title_tv)
    TextView tv;
    @BindView(R.id.typechooser)
    RadioGroup group;
    @BindView(R.id.welcome_text)
    TextView welcome;
    boolean checkedOnce = false;
    int btnChecked;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_account);
        ButterKnife.bind(this);
        String t = "<font color=\"#333333\">WELCOME TO </font> <font color=\"#1CD36D\">NIMAS EDUCATION</font>";
        welcome.setText(Html.fromHtml(t));
        group.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (!checkedOnce) {
                            checkedOnce = true;
                            Animation fadeIn = new AlphaAnimation(0, 1);
                            fadeIn.setInterpolator(new AccelerateInterpolator()); //add this
                            fadeIn.setDuration(200);
                            AnimationSet animation = new AnimationSet(false); //change to false
                            animation.addAnimation(fadeIn);
                            nextBtn.setVisibility(View.VISIBLE);
                            nextBtn.startAnimation(animation);
                        }
                        btnChecked = checkedId;
                    }
                }
        );

        nextBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i;
                        if (btnChecked == R.id.student) {
                            i = new Intent(AccountTypeActivity.this, StudentInActivity.class);
                            getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, MODE_PRIVATE).edit().putBoolean(IS_ADMIN, false).commit();
                        } else {
                            i = new Intent(AccountTypeActivity.this, AdminInActivity.class);
                            getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, MODE_PRIVATE).edit().putBoolean(IS_ADMIN, true).commit();
                        }
                        startActivity(i);
                        finish();
                    }
                }
        );
    }

    public void onSaveInstanceState(Bundle outstate) {

        if (outstate != null) {
            outstate.putBoolean(SAVING_STATE_SLIDER_ANIMATION, isSliderAnimation);
        }
        super.onSaveInstanceState(outstate);
    }

    public void onRestoreInstanceState(Bundle inState) {
        if (inState != null) {
            isSliderAnimation = inState.getBoolean(SAVING_STATE_SLIDER_ANIMATION, false);
        }
        super.onRestoreInstanceState(inState);
    }
}
