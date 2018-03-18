package com.susankya.rnk;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNoticeActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public static final String TITLE = "titleOfNotice", DESCRIPTION = "DescriptionOfNotice", NOTICE_NUM = "notice_num", ISEDIT = "isedit", CATEGORY="category";
    private String title = "", description = "", noticeNum = "0", category="";
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            title = getIntent().getStringExtra(TITLE);
            description = getIntent().getStringExtra(DESCRIPTION);
            noticeNum = getIntent().getStringExtra(NOTICE_NUM);
            isEdit = getIntent().getBooleanExtra(ISEDIT, false);
            category = getIntent().getStringExtra(CATEGORY);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportFragmentManager().beginTransaction().add(R.id.addNoticFrame, addNoticeFragment.newInstance(title, description, isEdit, noticeNum, category)).commit();

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(UtilitiesAdi.catColor(n.getCategory(), this.getResources()));
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}
