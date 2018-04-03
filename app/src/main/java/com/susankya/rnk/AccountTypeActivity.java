package com.susankya.rnk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AccountTypeActivity extends AppCompatActivity implements FragmentCodes {

    private static final String SAVING_STATE_SLIDER_ANIMATION = "SliderAnimationSavingState";
    private boolean isSliderAnimation = false;
    private static SQLiteDatabase db;
    @BindView(R.id.stud)
    TextView stud;
    @BindView(R.id.receptionist)
    TextView reception;
    @BindView(R.id.subadmin)
    TextView sub_admin;
    @BindView(R.id.superadmin)
    TextView super_admin;
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
        String t = "<font color=\"#F79F1F\">WELCOME TO </font> <font color=\"#F79F1F\">R & K ASSOCIATES</font>";
        welcome.setText(Html.fromHtml(t));
        SharedPreferences pref = getApplicationContext().getSharedPreferences("account",MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("type","student");
                editor.commit();
                Intent i = new Intent(AccountTypeActivity.this, StudentInActivity.class);
                getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, MODE_PRIVATE).edit().putBoolean(IS_ADMIN, false).commit();
                startActivity(i);
                finish();
            }
        });

        reception.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("type","reception");
                editor.commit();
               Intent i = new Intent(AccountTypeActivity.this, AdminInActivity.class);
                getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, MODE_PRIVATE).edit().putBoolean(IS_ADMIN, true).commit();
                startActivity(i);
                finish();
            }
        });

        sub_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("type","sub-admin");
                editor.commit();
                Intent i = new Intent(AccountTypeActivity.this, AdminInActivity.class);
                getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, MODE_PRIVATE).edit().putBoolean(IS_ADMIN, true).commit();
                startActivity(i);
                finish();
            }
        });

        super_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("type","super-admin");
                editor.commit();
                Intent i = new Intent(AccountTypeActivity.this, AdminInActivity.class);
                getSharedPreferences(FragmentCodes.PUBLIC_SHAREDPREFERENCE, MODE_PRIVATE).edit().putBoolean(IS_ADMIN, true).commit();
                startActivity(i);
                finish();
            }
        });


        /*
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
        ); */
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
