package com.susankya.schoolvalley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class AddNoticeActivity extends AppCompatActivity {
    Toolbar toolbar;
    public static final String TITLE = "titleOfNotice", DESCRIPTION = "DescriptionOfNotice", NOTICE_NUM = "notice_num", ISEDIT = "isedit";
    private String title = "", description = "", noticeNum = "0";
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        toolbar = findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        if (getIntent() != null) {
            title = getIntent().getStringExtra(TITLE);
            description = getIntent().getStringExtra(DESCRIPTION);
            noticeNum = getIntent().getStringExtra(NOTICE_NUM);
            isEdit = getIntent().getBooleanExtra(ISEDIT, false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportFragmentManager().beginTransaction().add(R.id.addNoticFrame, addNoticeFragment.newInstance(title, description, isEdit, noticeNum)).commit();
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
