package com.ccs.news;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.ccs.news.R;

import static com.ccs.news.R.attr.title;

/**
 * Created by DangH on 2017/4/28.
 */

public abstract class BasicActivity extends Activity {
    private RelativeLayout layoutGoback;
    public ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewId());
        doAfterCreate();
    }

    public abstract int getContentViewId();
    public void doAfterCreate(){
        layoutGoback = (RelativeLayout) this.findViewById(R.id.backtxt_btndiy);
        layoutGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    public void showProgressDialog(Context context, String message, String s) {
        progressDialog = new ProgressDialog(context, R.style.dialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIcon(android.R.drawable.ic_dialog_info);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public int getScreenWidth() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }
}
