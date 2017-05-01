package com.ccs.news;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by DangH on 2017/4/28.
 */

public class NoticeDetailActivity extends BasicActivity {
    private TextView textViewTitle;
    private WebView webView;
    private ProgressBar webViewProgressbar;
    private String windowTitle = "";
    private String url = "";
    private String htmlString = "";

    public int getContentViewId() {
        return R.layout.activity_notice_detail;
    }

    public void doAfterCreate() {
        super.doAfterCreate();

        this.getParamsFromPrevious();

        textViewTitle = (TextView) this.findViewById(R.id.titletext);
        textViewTitle.setText(windowTitle);

        if (url != "")
            this.convertHtmlData();
    }

    private void getParamsFromPrevious() {
        Intent intent = this.getIntent();
        windowTitle = intent.getStringExtra("windowTitle");
        url = intent.getStringExtra("noticeUrl");
    }

    private void convertHtmlData() {
        this.showProgressDialog(this, "请稍后", "正在加载数据...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    htmlString = NoticeDetailService.getNewsDetailsHtml(url);
                    mHandler.sendMessage(mHandler.obtainMessage(22, htmlString));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            setWebViewContent();
        }
    };

    private void setWebViewContent() {
        webView = (WebView) findViewById(R.id.webView);
        webViewProgressbar = (ProgressBar) findViewById(R.id.webViewProgressbar);
        webViewProgressbar.setMax(100);

        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);
        settings.setDefaultTextEncodingName("utf-8");

        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.FAR;
        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        System.out.println("--------------------------------------\n");
        System.out.println("--------------------------------------"+screenDensity);
        System.out.println("--------------------------------------\n");
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        settings.setDefaultZoom(zoomDensity);

        webView.clearCache(true);

        webView.setWebViewClient(new WebViewClient(){
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                Toast.makeText(NoticeDetailActivity.this, "运行错误:" + description, Toast.LENGTH_SHORT).show();
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress) {
                webViewProgressbar.setProgress(newProgress);
                if (newProgress == 100) {
                    webViewProgressbar.setVisibility(View.GONE);
                    progressDialog.cancel();
                }
            }
        });

        webView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
    }
}
