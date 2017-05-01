package com.ccs.news;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ccs.utils.Uris;
import com.ccs.model.ScuNoticesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DangH on 2017/4/28.
 */

public class ScuNoticeActivity extends BasicActivity {
    private int noticeType;
    private TextView textViewTitle;

    private ListView listViewNotices;
    private List<ScuNoticesBean> ListNotice;
    private int listNoticePageNow = 1;
    private AdapterNotices adapterNews;

    public int getContentViewId() {
        return R.layout.activity_notice;
    }

    public void doAfterCreate() {
        super.doAfterCreate();

        this.getParamsFromPrevious();

        this.setListView();
    }

    private void getParamsFromPrevious() {
        Intent intent = this.getIntent();

        noticeType = intent.getIntExtra("NoticeType", Uris.NoticeType.NEWS);

        textViewTitle = (TextView) this.findViewById(R.id.titletext);

        if (noticeType == Uris.NoticeType.NOTIFICATION) textViewTitle.setText("学院通知");
    }

    private RelativeLayout layoutViewFootProgressBar;
    private boolean isLoadFinish = false;

    private void setListView() {
        listViewNotices = (ListView) this.findViewById(R.id.listview);
        listNotices = new ArrayList<ScuNoticeBean>();
        adapterNews = new AdapterNotices(ScuNoticeActivity.this, listNotices);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layoutViewFootProgressBar = (RelativeLayout) inflater.inflate(R.layout.item_listview_foot_progressbar, null);

        listViewNotices.addFooterView(layoutViewFootProgressBar);
        listViewNotices.setAdapter(adapterNews);
        listViewNotices.removeFooterView(layoutViewFootProgressBar);

        listViewNotices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isLoadFinish) {
                    Intent intent = new Intent();

                    intent.setClass(ScuNoticeActivity.this, NoticeDetailActivity.class);
                    intent.putExtra("windowTitle", textViewTitle.getText().toString());
                    intent.putExtra("noticeUrl", listNotices.get(position).noticeContentLink);
                    startActivity(intent);
                }
            }
        });

        listViewNotices.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastItemIndex = view.getLastVisiblePosition();
                if (lastItemIndex + 1 == totalItemCount && isLoadFinish) {
                    isLoadFinish = false;
                    try {
                        addDataAtBackground();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        this.showProgressDialog(this, "请稍后", "正在加载数据...");
        this.addDataAtBackground();
    }

    private void addDataAtBackground() {
        listViewNotices.addFooterView(layoutViewFootProgressBar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<ScuNoticesBean> newNoticesList = NoticeTitleService.getNewsList(listNoticePageNow, noticeType);
                    if (newNoticesList != null && newNoticesList.size() > 0) {
                        listNotices.addAll(newNoticesList);
                        mHandler.sendEmptyMessage(1);
                    }else {
                        mHandler.sendEmptyMessage(2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    listNoticePageNow++;
                    adapterNews.notifyDataSetChanged();
                    isLoadFinish = true;
                    progressDialog.cancel();
                    break;
                case 2:
                    Toast.makeText(ScuNoticeActivity.this, "后面没有数据了", 1).show();
                    break;
            }
            listViewNotices.removeFooterView(layoutViewFootProgressBar);
        }
    };
}
