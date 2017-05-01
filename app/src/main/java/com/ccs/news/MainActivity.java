package com.ccs.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import com.ccs.utils.Uris.NoticeType;

public class MainActivity extends BasicActivity {
    private Button btnNews;
    private Button btnNotices;

    public int getContentViewId() {

        return R.layout.activity_main;
    }

    public void doAfterCreat() {

        this.findAndSetButtons();
    }

    private void findAndSetButtons() {
        btnNews = (Button) btnNews.findViewById(R.id.btn_news);
        btnNotices = (Button) btnNotices.findViewById(R.id.btn_notices);

        btnNews.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ScuNoticeActivity.class);
                intent.putExtra("NoticeType", NoticeType.NEWS);
                startActivity(intent);
            }
        });

        btnNotices.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ScuNoticeActivity.class);
                intent.putExtra("NoticeType", NoticeType.NOTICES);
                startActivity(intent);
            }
        });
    }
}
